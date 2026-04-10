package echos.UserService.Repository;

import echos.UserService.Entity.UserSocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSocialAccountRepository extends JpaRepository<UserSocialAccount, Long> {

    // 根据 userId 查询所有绑定的第三方账号
    List<UserSocialAccount> findByUserId(Long userId);

    // 根据 userId 和平台名称查询第三方账号
    UserSocialAccount findByUserIdAndPlatform(Long userId, String platform);

    // 根据 socialId 和平台名称查询第三方账号
    UserSocialAccount findBySocialIdAndPlatform(String socialId, String platform);
}
