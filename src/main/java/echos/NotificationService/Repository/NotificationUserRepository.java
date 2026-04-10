package echos.NotificationService.Repository;

import echos.NotificationService.Entity.NotificationUser;
import echos.NotificationService.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, Long> {

    Optional<NotificationUser> findById(Long id);

    List<NotificationUser> findByNotificationId(Long notificationId);

    List<NotificationUser> findByUserId(Long userId);

    List<NotificationUser> findByStatus(NotificationStatus status);

    @Query("SELECT n FROM NotificationUser n WHERE n.user = :user AND n.notification = :notification")
    List<NotificationUser> findByNotificationIdAndUserId(@Param("user") Long user, @Param("notification") Long notification);
}
