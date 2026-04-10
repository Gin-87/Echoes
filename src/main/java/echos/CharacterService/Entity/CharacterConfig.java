package echos.CharacterService.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "character_config")
public class CharacterConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String backgroundStory;

    @Column(columnDefinition = "TEXT")
    private String userAppellation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String personalityTraits;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String languageStyle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String firstLetter;

}
