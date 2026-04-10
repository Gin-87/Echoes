package echos.LetterTasksService.Repository;

import echos.LetterTasksService.Entity.LetterTasksConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LetterTasksConfigRepository extends JpaRepository<LetterTasksConfig, Long> {

}
