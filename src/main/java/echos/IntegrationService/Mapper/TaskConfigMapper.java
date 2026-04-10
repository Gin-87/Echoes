package echos.IntegrationService.Mapper;

import echos.IntegrationService.DTO.TaskConfigDTO;
import echos.LetterTasksService.Entity.LetterTasksConfig;

public class TaskConfigMapper {

    public static TaskConfigDTO toDto(LetterTasksConfig config) {
        TaskConfigDTO dto = new TaskConfigDTO();
        dto.setPreferLanguage(config.getPreferredLanguage());
        dto.setUserSalutation(config.getUserSalutation());
        dto.setRelationshipWithUser(config.getUserCharacterRelationCustomized());
        return dto;
    }
}
