package echos.IntegrationService.Mapper;

import echos.AIModelService.Entity.AiModelConfig;
import echos.IntegrationService.DTO.ModelConfigDTO;

public class ModelMapper {

    public static ModelConfigDTO toDto(AiModelConfig model) {
        ModelConfigDTO dto = new ModelConfigDTO();
        dto.setModelName(model.getModelName());
        dto.setModelCode(model.getCode());
        dto.setModelKey(model.getApiKey());
        dto.setModelEntrance(model.getModelName());   // 豆包用
        dto.setModelApiAddress(model.getApiAddress());
        dto.setMaxToken(model.getMaxToken());
        return dto;
    }
}
