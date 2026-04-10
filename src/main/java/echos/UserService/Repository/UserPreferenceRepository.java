package echos.UserService.Repository;

import echos.UserService.Entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    // 根据 userId 查找用户偏好
    UserPreference findByUserId(Long userId);

    // 根据语言偏好查询用户偏好
    UserPreference findByLanguagePrefer(UserPreference languagePrefer);
}
