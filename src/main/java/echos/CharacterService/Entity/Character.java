package echos.CharacterService.Entity;

import echos.CharacterService.CharacterGender;
import echos.CharacterService.CharacterStatus;
import echos.UserService.UserLanguage;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "characters")

public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String avatar_url;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CharacterStatus status;

    @Column(name = "owner", nullable = false)
    private Long owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CharacterGender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserLanguage language;


    @Column(nullable = false, unique = true, length = 25)
    private Long configId;

    //添加映射关系，实现角色被用户收藏
    @OneToMany(mappedBy = "character", cascade = CascadeType.REMOVE)
    private Set<UserFavorite> favoriteByUsers = new HashSet<>();

}
