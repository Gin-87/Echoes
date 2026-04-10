package echos.AuthorizationService.Entity;



import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Set;

@Data
@Entity
@Table(name = "menu_permissions")
public class MenuPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "resource", nullable = false, unique = true)
    private String resource;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "icon")
    private String icon;

    @Column(name = "component")
    private String component;

    // 反向映射到 RoleToMenu
    @OneToMany(mappedBy = "menuPermission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RoleToMenu> roleToMenus;

    @CreationTimestamp

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
}
