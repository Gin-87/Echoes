package echos.LetterTasksService.Entity;

import echos.LetterTasksService.FrequencyStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 信件任务配置实体类
 */
@Data
@Entity
@Table(name = "letter_tasks_config")
public class LetterTasksConfig {

    /** 主键ID，唯一 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 邮箱 */
    @Column(name = "email", length = 100)
    private String email;

    /** 设置 */
    @Column(name = "setting", nullable = false, columnDefinition = "TEXT")
    private String setting;


    //对用户的称呼
    @Column(name = "user_salutation")
    private String userSalutation;

    //自定义背景故事
    @Column(columnDefinition = "TEXT")
    private String userCharacterRelationCustomized;

    /** 创建时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 更新时间 */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    //语言标识

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FrequencyStatus frequency;
}
