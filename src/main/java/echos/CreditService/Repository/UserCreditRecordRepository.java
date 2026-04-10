package echos.CreditService.Repository;

import echos.CreditService.Entity.UserCreditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCreditRecordRepository extends JpaRepository<UserCreditRecord, Long> {

    /**
     * 根据 userId 查找用户的所有积分变动记录
     *
     * @param userId 用户ID
     * @return 积分变动记录列表
     */
    List<UserCreditRecord> findByUserId(Long userId);

    /**
     * 根据 userId 和变动类型查找记录
     *
     * @param userId     用户ID
     * @param changeType 变动类型（INCREASE/DECREASE）
     * @return 积分变动记录列表
     */
    List<UserCreditRecord> findByUserIdAndChangeType(Long userId, UserCreditRecord.ChangeType changeType);

    /**
     * 根据变动类型查找记录
     *
     * @param changeType 变动类型（INCREASE/DECREASE）
     * @return 积分变动记录列表
     */

    List<UserCreditRecord> findByChangeType(UserCreditRecord.ChangeType changeType);

}
