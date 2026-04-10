package echos.CharacterService.Service;

import echos.CharacterService.Entity.Character;
import echos.CharacterService.Entity.UserFavorite;
import echos.CharacterService.Repository.UserFavoriteRepository;
import echos.UserService.Entity.User;
import echos.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserFavoriteService {

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;


    //判断是否存在收藏关系
    public boolean existsByUserIdAndCharacterId(Long userId, Long characterId) {
        return userFavoriteRepository.findByUserIdAndCharacterId(userId, characterId) != null;
    }

    //根据用户ID查询所有收藏的角色
    public List<Character> getFavoritesByUserId(Long userId) {
        return Optional.ofNullable(userFavoriteRepository.findByUserId(userId))
                .orElse(Collections.emptyList())  // 如果返回 null，替换为空列表
                .stream()
                .map(UserFavorite::getCharacter)
                .collect(Collectors.toList());
    }

    // 创建收藏关系
    public boolean addFavorite(Long userId, Long characterId) {
        // 如果已存在收藏关系，则不重复添加
        if (existsByUserIdAndCharacterId(userId, characterId)) {
            throw new RuntimeException("User favorite already exists");
        }

        // 创建收藏对象
        User user = userService.getUserById(userId);
        Character character = characterService.getCharacterById(characterId);
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setUserId(userId);
        userFavorite.setUser(user);
        userFavorite.setCharacterId(characterId);
        userFavorite.setCharacter(character);
        userFavoriteRepository.save(userFavorite);

        return true;
    }

    // 取消收藏关系
    public boolean removeFavorite(Long userId, Long characterId) {
        // 检查是否存在收藏关系
        Optional<UserFavorite> existingFavorite = Optional.ofNullable(
                userFavoriteRepository.findByUserIdAndCharacterId(userId, characterId));

        if (existingFavorite.isPresent()) {
            userFavoriteRepository.delete(existingFavorite.get());
            return true;
        }

        return false; // 如果不存在收藏关系，则返回 false
    }

    //查询某一角色的所有收藏关系
    public List<UserFavorite> getFavoriteByCharacterId(Long characterId) {
        return userFavoriteRepository.findByCharacterId(characterId);
    }


    //查询某一角色的收藏量
    public Integer getNumOfFavoritesByCharacterId(Long CharacterId) {
        Integer count = 0;
        for (UserFavorite userFavorite : userFavoriteRepository.findByCharacterId(CharacterId)) {
            count++;
        }
        return count;
    }

    @Transactional
    public void removeAllFavoritesByCharacterId(Long characterId) {
        userFavoriteRepository.deleteByCharacterId(characterId);
    }
}