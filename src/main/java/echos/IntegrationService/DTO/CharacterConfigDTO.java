package echos.IntegrationService.DTO;

import lombok.Data;

@Data
public class CharacterConfigDTO {
    private String characterName;       // 角色名称（供 Prompt 使用）
    private String backgroundStory;
    private String personalityTraits;
    private String languageStyle;
    private String relationWithUser;
}
