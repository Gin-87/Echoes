package echos.NotificationService.Repository;

import echos.NotificationService.Entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findByTitleContaining(String keyword);

    List<Notifications> findByContentContaining(String keyword);

}
