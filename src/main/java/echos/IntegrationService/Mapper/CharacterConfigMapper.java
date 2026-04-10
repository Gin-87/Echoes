package echos.IntegrationService.Mapper;

import echos.CharacterService.Entity.CharacterConfig;
import echos.IntegrationService.DTO.CharacterConfigDTO;

public class CharacterConfigMapper {

    public static CharacterConfigDTO toDTO(CharacterConfig entity) {
        CharacterConfigDTO dto = new CharacterConfigDTO();
        dto.setLanguageStyle(entity.getLanguageStyle());
        dto.setBackgroundStory(entity.getBackgroundStory());
        dto.setPersonalityTraits(entity.getPersonalityTraits());
        dto.setRelationWithUser(entity.getUserAppellation());
        // characterName 由调用方从 Character 实体填入
        return dto;
    }
}
