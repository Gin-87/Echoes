package echos.NotificationService.Entity;

import echos.NotificationService.NotificationStatus;
import echos.UserService.Entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息提醒用户实体类
 */
@Data
@Entity
@Table(name = "notification_user")
public class NotificationUser {

    /** 主键ID，唯一 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 消息提醒ID */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", insertable = false, updatable = false, nullable = false)
    private Notifications notification;

    /** 用户ID */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
    private User user;

    /** 推送时间 */
    @Column(name = "deliver_time", nullable = false)
    private LocalDateTime deliverTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

}
