package echos.CharacterService.DTO;


import echos.CharacterService.CharacterStatus;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CharacterCreateUpdateDTO {

    private Long characterId;
    private String name;
    private String code;
    private String description;
    private String avatar_url;
    private CharacterStatus status;
    private Long configId;
    private String backgroundStory;
    private String userAppellation;
    private String personalityTraits;
    private String languageStyle;
    private String firstLetter;
    private String language;
    private String gender;
    private String token;

}
