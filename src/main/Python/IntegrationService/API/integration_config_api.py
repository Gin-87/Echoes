import logging

from fastapi import APIRouter, BackgroundTasks, HTTPException
from fastapi.responses import JSONResponse

from DataStructure.IntegrationConfigDTO import IntegrationConfigDTO
from Service.prompt_service import generate_messages
from Service.llm_request_service import call_language_model

logger = logging.getLogger("echoes.api")
router = APIRouter()


def _run_llm_task(data: IntegrationConfigDTO):
    """后台执行：生成 Prompt → 调用 LLM → 回调 Java"""
    try:
        logger.info("[Task %s] Starting LLM generation", data.task_id)
        messages = generate_messages(data)
        call_language_model(
            model_config=data.model_config_dto,
            messages=messages,
            task_id=data.task_id,
            lang=data.task_config_dto.prefer_language,
        )
        logger.info("[Task %s] LLM generation completed", data.task_id)
    except Exception as e:
        logger.error("[Task %s] LLM generation failed: %s", data.task_id, str(e))


@router.post("/receive-integration-config", status_code=202)
async def receive_integration_config(
    data: IntegrationConfigDTO,
    background_tasks: BackgroundTasks,
):
    """
    Java 后端推送任务配置 → 立即返回 202 → 后台异步调用 LLM
    """
    logger.info("[Task %s] Received integration config, queuing background task", data.task_id)
    background_tasks.add_task(_run_llm_task, data)
    return JSONResponse(
        status_code=202,
        content={"message": "Task accepted", "task_id": data.task_id},
    )


@router.get("/health")
async def health():
    return {"status": "ok"}
