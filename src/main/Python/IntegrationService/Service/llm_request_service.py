"""
LLM 请求服务
- 策略模式：BaseLLMAdapter → GPTAdapter / DoubaoAdapter → LLMAdapterRegistry
- 指数退避重试（最多 3 次），4xx 客户端错误不重试
- 成功/失败均回调 Java /api/integration/callback
"""
import logging
import os
import time
from abc import ABC, abstractmethod
from typing import List, Dict, Optional

import requests
from requests.exceptions import HTTPError

from DataStructure.IntegrationConfigDTO import ModelConfigDTO

logger = logging.getLogger("echoes.llm")

JAVA_CALLBACK_URL = os.getenv(
    "JAVA_CALLBACK_URL", "http://localhost:8080/api/integration/callback"
)
MAX_RETRIES = 3
INITIAL_BACKOFF = 1  # 秒


# ─────────────────────────────────────────────
# 策略基类
# ─────────────────────────────────────────────
class BaseLLMAdapter(ABC):
    @abstractmethod
    def build_payload(self, model_config: ModelConfigDTO, messages: List[Dict]) -> dict:
        ...

    @abstractmethod
    def build_headers(self, model_config: ModelConfigDTO) -> dict:
        ...

    def extract_content(self, response_json: dict) -> str:
        """从响应中提取正文（OpenAI 兼容格式）"""
        try:
            return response_json["choices"][0]["message"]["content"]
        except (KeyError, IndexError):
            return ""


# ─────────────────────────────────────────────
# GPT 适配器
# ─────────────────────────────────────────────
class GPTAdapter(BaseLLMAdapter):
    def build_payload(self, model_config: ModelConfigDTO, messages: List[Dict]) -> dict:
        return {
            "model": model_config.model_name,
            "messages": messages,
            "max_completion_tokens": model_config.max_token or 1000,
        }

    def build_headers(self, model_config: ModelConfigDTO) -> dict:
        return {
            "Authorization": f"Bearer {model_config.model_key}",
            "Content-Type": "application/json",
        }


# ─────────────────────────────────────────────
# 豆包适配器
# ─────────────────────────────────────────────
class DoubaoAdapter(BaseLLMAdapter):
    def build_payload(self, model_config: ModelConfigDTO, messages: List[Dict]) -> dict:
        return {
            "model": model_config.model_entrance,
            "messages": messages,
            "max_tokens": model_config.max_token or 1000,
        }

    def build_headers(self, model_config: ModelConfigDTO) -> dict:
        return {
            "Authorization": f"Bearer {model_config.model_key}",
            "Content-Type": "application/json",
        }


# ─────────────────────────────────────────────
# 适配器注册表
# ─────────────────────────────────────────────
class LLMAdapterRegistry:
    _adapters: Dict[str, BaseLLMAdapter] = {
        "gpt": GPTAdapter(),
        "doubao": DoubaoAdapter(),
    }

    @classmethod
    def get(cls, model_code: str) -> BaseLLMAdapter:
        adapter = cls._adapters.get(model_code.lower())
        if adapter is None:
            raise ValueError(
                f"Unknown model_code: {model_code}. Supported: {list(cls._adapters.keys())}"
            )
        return adapter

    @classmethod
    def register(cls, model_code: str, adapter: BaseLLMAdapter):
        """支持运行时注册新适配器"""
        cls._adapters[model_code.lower()] = adapter


# ─────────────────────────────────────────────
# 回调 Java
# ─────────────────────────────────────────────
def _callback_java(task_id: int, content: str, success: bool):
    payload = {"task_id": task_id, "content": content, "success": success}
    try:
        resp = requests.post(JAVA_CALLBACK_URL, json=payload, timeout=10)
        resp.raise_for_status()
        logger.info("[Task %s] Callback to Java succeeded", task_id)
    except Exception as e:
        logger.error("[Task %s] Callback to Java failed: %s", task_id, str(e))


# ─────────────────────────────────────────────
# 主入口
# ─────────────────────────────────────────────
def call_language_model(
    model_config: ModelConfigDTO,
    messages: List[Dict[str, str]],
    task_id: Optional[int] = None,
    lang: Optional[str] = None,
) -> dict:
    """
    调用大语言模型，含指数退避重试。
    - 4xx 客户端错误（如 401 认证失败、400 参数错误）：立即终止，不重试
    - 5xx 服务端错误 / 网络异常：按指数退避重试，最多 MAX_RETRIES 次
    - 成功/最终失败均回调 Java
    """
    adapter = LLMAdapterRegistry.get(model_config.model_code)
    payload = adapter.build_payload(model_config, messages)
    headers = adapter.build_headers(model_config)

    last_error: Optional[str] = None

    for attempt in range(1, MAX_RETRIES + 1):
        try:
            logger.info("[Task %s] LLM call attempt %d/%d", task_id, attempt, MAX_RETRIES)
            response = requests.post(
                url=model_config.model_api_address,
                json=payload,
                headers=headers,
                timeout=60,
            )

            # ── 4xx：客户端错误，重试无意义，立即终止 ──
            if 400 <= response.status_code < 500:
                error_msg = (
                    f"Client error {response.status_code}: {response.text[:200]}"
                )
                logger.error(
                    "[Task %s] 4xx error, will NOT retry: %s", task_id, error_msg
                )
                if task_id is not None:
                    _callback_java(task_id, "", success=False)
                return {"error": error_msg}

            # ── 5xx 及其他：raise 让 except 捕获并重试 ──
            response.raise_for_status()

            result = response.json()
            content = adapter.extract_content(result)
            if task_id is not None:
                _callback_java(task_id, content, success=True)
            return result

        except HTTPError as e:
            # raise_for_status() 抛出的 5xx，走重试
            last_error = str(e)
            logger.warning("[Task %s] 5xx HTTPError attempt %d: %s", task_id, attempt, last_error)
        except requests.exceptions.RequestException as e:
            # 网络超时、连接失败等，走重试
            last_error = str(e)
            logger.warning("[Task %s] Network error attempt %d: %s", task_id, attempt, last_error)

        if attempt < MAX_RETRIES:
            backoff = INITIAL_BACKOFF * (2 ** (attempt - 1))
            logger.info("[Task %s] Retrying in %ds...", task_id, backoff)
            time.sleep(backoff)

    # 全部重试失败
    logger.error("[Task %s] All %d attempts failed. Last error: %s", task_id, MAX_RETRIES, last_error)
    if task_id is not None:
        _callback_java(task_id, "", success=False)

    return {"error": last_error}
