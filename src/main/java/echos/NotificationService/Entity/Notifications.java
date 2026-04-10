package echos.NotificationService.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 消息提醒实体类
 */
@Data
@Entity
@Table(name = "notifications")
public class Notifications {

    /** 主键ID，唯一 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 标题，唯一 */
    @Column(name = "title", length = 100, nullable = false, unique = true)
    private String title;

    /** 内容 */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /** 创建时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 创建者 */
    @Column(name = "creator", nullable = false)
    private long creator;

//    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Notifications> notifications = new HashSet<>();
}
