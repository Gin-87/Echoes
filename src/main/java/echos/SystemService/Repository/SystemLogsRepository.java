package echos.SystemService.Repository;

import echos.SystemService.Entity.SystemLogs;
import echos.SystemService.SystemLogsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface SystemLogsRepository extends JpaRepository<SystemLogs, Long> {

    Optional<SystemLogs> findById(Long id);

    List<SystemLogs> findByUserId(Long userId);

    List<SystemLogs> findByEvent(String event);

    List<SystemLogs> findByMessageContaining(String keyword);

    @Query("SELECT s FROM SystemLogs s WHERE s.userId = :userId AND s.status = :status")
    List<SystemLogs> findByUserIdAndEvent(@Param("userId") Long userId, @Param("status") SystemLogsStatus status);
}
