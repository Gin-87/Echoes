package echos.CreditService.Entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_credit_record")
public class UserCreditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING) // 使用Enum类型存储changeType
    @Column(name = "change_type", nullable = false)
    private ChangeType changeType;

    @Column(name = "change_amount", nullable = false)
    private Integer changeAmount;

    @Column(name = "previous_balance", nullable = false)
    private Integer previousBalance;

    @Column(name = "new_balance", nullable = false)
    private Integer newBalance;

    @Column(name = "description", length = 500)
    private String description;


    @CreationTimestamp
    @Column(name = "change_time", nullable = false)
    private LocalDateTime changeTime;

    // ChangeType枚举类
    public enum ChangeType {
        INCREASE, DECREASE
    }
}
