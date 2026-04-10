package echos.CharacterService.Repository;

import echos.CharacterService.Entity.CharacterConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterConfigRepository extends JpaRepository<CharacterConfig, Long> {
//
//    // 根据语言和性别查找角色配置
//    List<CharacterConfig> findByLanguageStyleAndGender(String language, String gender);
}
