package echos.LetterTasksService.Repository;

import echos.LetterTasksService.Entity.LetterTasks;
import echos.LetterTasksService.LetterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LetterTasksRepository extends JpaRepository<LetterTasks, Long> {

    List<LetterTasks> findByUserId(Long userId);

    List<LetterTasks> findByStatus(LetterStatus status);

    List<LetterTasks> findByUserIdAndStatus(Long userId, LetterStatus status);

    @Query("SELECT l FROM LetterTasks l WHERE l.userId = :userId AND l.characterId = :characterId")
    List<LetterTasks> findByUserIdAndCharacterId(@Param("userId") Long userId, @Param("characterId") Long characterId);
}
