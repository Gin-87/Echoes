package echos.CharacterService.Repository;

import echos.CharacterService.CharacterStatus;
import echos.CharacterService.Entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {



    // 根据 code 查找角色（唯一）
    Character findByCode(String code);

    // 根据 userid 查找用户创建的所有角色
    List<Character> findByOwner(Long Owner);

    // 根据 status 查找角色(public/private）
    List<Character> findByStatus(CharacterStatus status);

    // 根据 name 或查 description 找角色
    List<Character> findByNameContainingOrDescriptionContaining(String name, String description);

    // 按状态获取指定用户创建的角色
    List<Character> findByStatusAndOwner(CharacterStatus status, Long Owner);

    @Modifying
    @Query("DELETE FROM UserFavorite uf WHERE uf.characterId = :characterId")
    void deleteUserFavoritesByCharacterId(@Param("characterId") Long characterId);




    @Query("SELECT c FROM Character c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Character> searchByKeyword(@Param("keyword") String keyword);






}
