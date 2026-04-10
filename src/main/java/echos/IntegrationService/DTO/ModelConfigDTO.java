package echos.IntegrationService.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModelConfigDTO {
    private String modelCode;        // 标识：gpt / doubao
    private String modelApiAddress;  // 接口地址
    private String modelName;        // 用于 OpenAI
    private String modelEntrance;    // 用于豆包
    private String modelKey;         // API Key
    private Integer maxToken;
}
