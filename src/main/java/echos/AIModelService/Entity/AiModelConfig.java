package echos.AIModelService.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * AI模型配置实体类
 */
@Data
@Entity
@Table(name = "ai_model_configs")
public class AiModelConfig {

    /** 主键ID，自增 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 模型名称，唯一 */
    @Column(name = "model_name", length = 100, nullable = false, unique = true)
    private String modelName;

    /** API地址，唯一 */
    @Column(name = "api_address", nullable = false, unique = true, columnDefinition = "TEXT")
    private String apiAddress;

    /** API密钥，已加密 */
    @Column(name = "api_key", columnDefinition = "TEXT")
    private String apiKey;

    /** 参数配置，JSON格式 */
    @Column(name = "parameters", columnDefinition = "JSON")
    private String parameters;

    /** 创建时间 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 更新时间 */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    //最大输出token
    @Column
    private Integer MaxToken;

    //标识符，判断是豆包还是gpt
    @Column(nullable = false)
    private String code;



}
