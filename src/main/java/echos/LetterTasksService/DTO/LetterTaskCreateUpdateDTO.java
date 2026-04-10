package echos.LetterTasksService.DTO;

import echos.LetterTasksService.Frequency;
import lombok.Data;

@Data
public class LetterTaskCreateUpdateDTO {
    private String taskName;
    private Long characterId;
    private Long ownerId;
    private String finalAppellation;
    private String targetEmail;
    private String relationshipWithUser;
    private Frequency frequency;

}
