package echos.CreditService.Repository;

import echos.CreditService.Entity.CreditRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRechargeRepository extends JpaRepository<CreditRecharge, Long> {

    /**
     * 根据 userId 查找所有的充值记录
     *
     * @param userId 用户ID
     * @return 用户的充值记录列表
     */
    List<CreditRecharge> findByUserId(Long userId);

    /**
     * 根据用户ID和状态查找充值记录
     *
     * @param userId 用户ID
     * @param status 充值状态
     * @return 充值记录列表
     */
    List<CreditRecharge> findByUserIdAndStatus(Long userId, CreditRecharge.RechargeStatus status);

    /**
     * 根据支付状态查找充值记录
     *
     * @param paymentStatus 支付状态
     * @return 充值记录列表
     */
    List<CreditRecharge> findByPaymentStatus(CreditRecharge.PaymentStatus paymentStatus);
}
