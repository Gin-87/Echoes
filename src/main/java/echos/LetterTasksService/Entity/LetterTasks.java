package echos.LetterTasksService.Entity;

import echos.LetterTasksService.LetterStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 信件任务实体类
 */
@Data
@Entity
@Table(name = "letter_tasks")
public class LetterTasks {

    /** 主键ID，唯一 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 角色ID */
    @Column(name = "character_id", nullable = false)
    private long characterId;

    /** 用户ID */
    @Column(name = "user_id", nullable = false)
    private long userId;

    /** 任务配置ID，唯一 */
    @Column(name = "task_config_id", nullable = false, unique = true)
    private long taskConfigId;

    /** 创建时间 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 更新时间 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LetterStatus status;
}
