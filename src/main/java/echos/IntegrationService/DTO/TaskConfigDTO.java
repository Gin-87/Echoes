package echos.IntegrationService.DTO;

import lombok.Data;

@Data
public class TaskConfigDTO {
    private String userSalutation;
    private String relationshipWithUser;
    private String preferLanguage;   // 统一字段名（Python 侧为 lang）
}
