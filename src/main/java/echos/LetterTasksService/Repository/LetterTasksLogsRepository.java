package echos.LetterTasksService.Repository;

import echos.LetterTasksService.Entity.LetterTasksLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LetterTasksLogsRepository extends JpaRepository<LetterTasksLogs, Long> {

    List<LetterTasksLogs> findByTaskId(Long taskId);

    List<LetterTasksLogs> findByMessageContaining(String keyword);
}
