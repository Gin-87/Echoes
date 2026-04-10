package echos.AuthorizationService.Entity;




import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "role_to_menu")
public class RoleToMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false) // 关联 Role
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_permission_id", nullable = false) // 关联 MenuPermission
    private MenuPermission menuPermission;
}

