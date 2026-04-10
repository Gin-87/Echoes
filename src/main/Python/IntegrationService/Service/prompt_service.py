"""
Prompt 构造服务：基于 IntegrationConfigDTO 生成 LLM messages 列表
"""
from typing import List, Dict

from DataStructure.IntegrationConfigDTO import IntegrationConfigDTO
from Service.system_templates_service import SystemMessageGenerator


def generate_messages(integration_config: IntegrationConfigDTO) -> List[Dict[str, str]]:
    """
    根据 IntegrationConfigDTO 生成大语言模型的 messages 参数
    """
    task_config = integration_config.task_config_dto
    character_config = integration_config.character_config_dto

    generator = SystemMessageGenerator(
        lang=task_config.prefer_language,
        has_history=integration_config.has_history,
    )

    system_message = generator.generate(
        character_name=character_config.character_name,
        background_story=character_config.background_story,
        personality_traits=character_config.personality_traits,
        language_style=character_config.language_style,
        user_salutation=task_config.user_salutation,
        relationship_with_user=task_config.relationship_with_user,
    )

    messages = [{"role": "system", "content": system_message}]

    # 历史信件交替作为 user/assistant 轮次
    if integration_config.has_history:
        for index, letter in enumerate(integration_config.history_letters):
            role = "user" if index % 2 == 0 else "assistant"
            messages.append({"role": role, "content": letter})

    return messages
