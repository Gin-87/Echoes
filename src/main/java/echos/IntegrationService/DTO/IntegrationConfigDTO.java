package echos.IntegrationService.DTO;

import lombok.Data;
import java.util.List;

@Data
public class IntegrationConfigDTO {
    private long taskId;
    private TaskConfigDTO taskConfigDTO;
    private CharacterConfigDTO characterConfigDTO;
    private ModelConfigDTO modelConfigDTO;
    private boolean hasHistory;
    private List<String> historyLetters;
}
