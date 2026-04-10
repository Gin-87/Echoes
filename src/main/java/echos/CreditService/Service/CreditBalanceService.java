package echos.CreditService.Service;

import echos.CreditService.Entity.Credit;
import echos.CreditService.Entity.UserCreditRecord;
import echos.CreditService.Repository.CreditRepository;
import echos.CreditService.Repository.UserCreditRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditBalanceService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private UserCreditRecordRepository userCreditRecordRepository;

    /**
     * 获取用户当前积分余额
     */
    public Integer getUserBalance(Long userId) {
        Credit credit = creditRepository.findByUserId(userId);
        if (credit == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        return credit.getTotalCredits();
    }

    /**
     * 更新用户积分余额，并记录到 UserCreditRecord
     * Important: changeAmount 参数区分正负
     */
    @Transactional
    public void updateUserBalance(Long userId, Integer changeAmount, String description) {
        // 获取当前用户余额
        Credit credit = creditRepository.findByUserId(userId);
        if (credit == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // 计算新的余额
        Integer currentBalance = credit.getTotalCredits();
        Integer newBalance = currentBalance + changeAmount;

        if (newBalance < 0) {
            throw new IllegalStateException("Insufficient balance for user with ID: " + userId);
        }


        // 更新余额
        credit.setTotalCredits(newBalance);
        credit.setLastUpdateTime(LocalDateTime.now());
        creditRepository.save(credit);

        UserCreditRecord.ChangeType changeType = null;

        //判断变动类型
        if (changeAmount > 0) {
            changeType = UserCreditRecord.ChangeType.INCREASE;
        }
        else{
            changeType = UserCreditRecord.ChangeType.DECREASE;
        }

        // 添加变动记录
        UserCreditRecord record = new UserCreditRecord();
        record.setUserId(userId);
        record.setChangeType(changeType);
        record.setChangeAmount(changeAmount);
        record.setPreviousBalance(currentBalance);
        record.setNewBalance(newBalance);
        record.setDescription(description);
        userCreditRecordRepository.save(record);
    }

    //初始化用户余额
    @Transactional
    public void initialUserCredit(Long userId) {
        Credit credit = new Credit();
        credit.setUserId(userId);
        credit.setTotalCredits(0);
        creditRepository.save(credit);
    }

    /**
     * 查询用户的积分变动记录
     */
    public List<UserCreditRecord> getUserCreditRecords(Long userId, int page, int size) {
        return userCreditRecordRepository.findByUserId(userId);
    }

    /**
     * 检查用户积分是否充足
     */
    public boolean checkBalanceSufficient(Long userId, Integer requiredAmount) {
        Integer currentBalance = getUserBalance(userId);
        return currentBalance >= requiredAmount;
    }



}
