package echos.CharacterService.Repository;

import echos.CharacterService.Entity.UserFavorite;
import echos.CharacterService.Entity.UserFavoriteId;
import echos.UserService.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, UserFavoriteId> {

    //根据用户ID，获取当前用户收藏的角色列表
    public List<UserFavorite> findByUserId(Long userId);

    //根据角色id，查询所有的收藏关系
    public List<UserFavorite> findByCharacterId(Long CharacterId);

    //根据角色ID和用户ID查询
    public UserFavorite findByUserIdAndCharacterId(Long userId, Long characterId);

    void deleteByCharacterId(Long characterId);

}
