package echos.CharacterService.DTO;


import echos.CharacterService.CharacterStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CharacterManageListDTO {

    private Long id;

    private String code;

    private String name;

    private String creator;

    private Long creatorId;

    private LocalDateTime creationDateTime;

    private CharacterStatus status;

}
