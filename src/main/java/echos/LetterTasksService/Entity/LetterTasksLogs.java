package echos.LetterTasksService.Entity;

import echos.LetterTasksService.LogStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 信件任务日志表
 */
@Data
@Entity
@Table(name = "letter_task_logs")
public class LetterTasksLogs {

    /** 主键ID，唯一 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 任务ID */
    @Column(name = "task_id", nullable = false)
    private long taskId;

    /** 任务配置ID */
    @Column(name = "task_config_id", nullable = false)
    private long taskConfigId;

    /** 内容ID */
    @Column(name = "content_id", length = 25, nullable = false)
    private String contentId;

    /** 消息 */
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    /** 创建时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogStatus status;
}
