package echos.AuthorizationService.Entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Set;

@Data
@Entity
@Table(name = "action_permissions")
public class ActionPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action_name", nullable = false, length = 100)
    private String actionName;

    @Column(name = "resource", nullable = false, length = 100)
    private String resource;

    // 反向映射到 RoleToAction
    @OneToMany(mappedBy = "actionPermission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RoleToAction> roleToActions;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
}
