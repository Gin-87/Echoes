package echos.CreditService.Repository;

import echos.CreditService.Entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

    /**
     * 根据 userId 查找用户的积分记录
     *
     * @param userId 用户ID
     * @return Credit 实体
     */
    Credit findByUserId(Long userId);

}
