package echos.CreditService.Entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "credit")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;


    @CreationTimestamp
    @Column(name = "total_credits", nullable = false)
    private Integer totalCredits;


    @UpdateTimestamp
    @Column(name = "last_update_time", nullable = false)
    private LocalDateTime lastUpdateTime;

}
