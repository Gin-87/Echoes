package echos.UserService.Repository;

import echos.UserService.Entity.User;
import echos.UserService.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 根据手机号查询用户
    User findByPhone(String phone);

    // 根据邮箱查询用户
    User findByEmail(String email);

    // 根据状态查询所有用户
    List<User> findByStatus(UserStatus status);

    // 根据昵称模糊查询用户
    List<User> findByNicknameContaining(String nickname);

//    //基于手机号和密码的登录校验
//    @Query("SELECT u from User u where u.phone = :phone and u.password = :hashedPassword")
//    User findByPhoneAndHashedPassword(String phone, String hashedPassword);
//
//    //基于邮箱和密码的登录校验
//    @Query("SELECT u from User u where u.email = :email and u.password = :hashedPassword")
//    User findByEmailAndHashedPassword(String email, String hashedPassword);

    //联合查询
    @Query("SELECT u FROM User u " +
            "WHERE (:phone IS NULL OR u.phone LIKE CONCAT('%', :phone, '%')) " +
            "AND (:email IS NULL OR u.email LIKE CONCAT('%', :email, '%')) " +
            "AND (:nickname IS NULL OR u.nickname LIKE CONCAT('%', :nickname, '%')) " +
            "AND (:status IS NULL OR u.status = :status)")
    List<User> findUsersByCriteria(@Param("phone") String phone,
                                   @Param("email") String email,
                                   @Param("nickname") String nickname,
                                   @Param("status") UserStatus status);

    @Modifying
    @Query("DELETE FROM UserFavorite uf WHERE uf.userId = :userId")
    void deleteUserFavoritesByUserId(@Param("userId") Long userId);

    List<User> findByRoleId(Long roleId);

}
