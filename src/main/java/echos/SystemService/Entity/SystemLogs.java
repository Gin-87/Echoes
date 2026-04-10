package echos.SystemService.Entity;

import echos.SystemService.SystemLogsStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统日志实体类
 */
@Data
@Entity
@Table(name = "system_logs")
public class SystemLogs {

    /** 主键ID，唯一 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 用户ID */
    @Column(name = "user_id", nullable = false)
    private long userId;

    /** 事件 */
    @Column(name = "event", nullable = false, columnDefinition = "TEXT")
    private String event;

    /** 请求链接 */
    @Column(name = "request_url", nullable = false, columnDefinition = "TEXT")
    private String requestUrl;

    @Column(name = "http_method",length = 50, nullable = false)
    private String httpMethod;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "response_status", nullable = false)
    private int responseStatus;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SystemLogsStatus status;
}
