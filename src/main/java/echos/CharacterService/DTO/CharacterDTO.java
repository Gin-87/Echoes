package echos.CharacterService.DTO;

import echos.CharacterService.CharacterStatus;
import lombok.Data;

@Data
public class CharacterDTO {

    private Long id;
    private String name;
    private String code;
    private String description;
    private String avatar_url;
    private CharacterStatus status;
    private Long owner;
    private String owner_name;
    private Boolean isFavorite;
    private Integer num_of_favorites;

}