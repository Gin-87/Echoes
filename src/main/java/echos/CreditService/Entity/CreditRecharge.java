package echos.CreditService.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "credit_recharges")
public class CreditRecharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "recharge_amount", nullable = false)
    private Integer rechargeAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "third_party_payment_id", length = 100)
    private String thirdPartyPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RechargeStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "stripe_response", columnDefinition = "TEXT")
    private String stripeResponse;


    @CreationTimestamp
    @Column(name = "recharge_time", nullable = false)
    private LocalDateTime rechargeTime;

    // PaymentMethod枚举
    public enum PaymentMethod {
        CREDIT_CARD, PAYPAL, STRIPE
    }

    // RechargeStatus枚举
    public enum RechargeStatus {
        PENDING, COMPLETED, FAILED
    }

    // PaymentStatus枚举
    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED
    }
}
