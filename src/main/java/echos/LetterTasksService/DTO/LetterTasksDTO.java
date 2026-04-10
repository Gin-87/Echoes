package echos.LetterTasksService.DTO;

import echos.LetterTasksService.LetterStatus;
import lombok.Data;

@Data
public class LetterTasksDTO {
    private long id;
    private long characterId;
    private long userId;
    private LetterStatus status;
}
