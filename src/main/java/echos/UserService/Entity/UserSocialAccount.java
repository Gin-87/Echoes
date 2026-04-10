package echos.UserService.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_social_accounts")
public class UserSocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String platform;

    @Column(name = "social_id", nullable = false, length = 50)
    private String socialId;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @CreationTimestamp
    @Column(name = "linked_time", nullable = false)
    private LocalDateTime linkedTime;

    @Column(name = "access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "token_expiry_time", nullable = false)
    private LocalDateTime tokenExpiryTime;
}
