from pydantic import BaseModel
from typing import List, Optional


class TaskConfigDTO(BaseModel):
    user_salutation: str
    relationship_with_user: str
    prefer_language: str          # CHN / ENG


class CharacterConfigDTO(BaseModel):
    character_name: str
    background_story: str
    personality_traits: str
    language_style: str
    relation_with_user: Optional[str] = None


class ModelConfigDTO(BaseModel):
    model_code: str               # gpt / doubao
    model_api_address: str
    model_key: str
    model_name: Optional[str] = None
    model_entrance: Optional[str] = None
    max_token: Optional[int] = None


class IntegrationConfigDTO(BaseModel):
    task_id: int
    task_config_dto: TaskConfigDTO
    character_config_dto: CharacterConfigDTO
    model_config_dto: ModelConfigDTO
    has_history: bool
    history_letters: List[str]
