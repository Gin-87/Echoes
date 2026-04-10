package echos.CharacterService.Service;

import echos.AuthenticationService.Util.JwtUtil;
import echos.CharacterService.DTO.CharacterCreateUpdateDTO;
import echos.CharacterService.DTO.CharacterDTO;
import echos.CharacterService.DTO.CharacterMapper;

import echos.CharacterService.Entity.CharacterConfig;
import echos.CharacterService.Repository.CharacterConfigRepository;
import echos.UserService.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import echos.CharacterService.Repository.CharacterRepository;
import echos.CharacterService.Entity.Character;
import org.springframework.beans.factory.annotation.Autowired;
import echos.CharacterService.CharacterStatus;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private CharacterConfigRepository characterConfigRepository;

    @Lazy
    @Autowired
    private UserFavoriteService userFavoriteService;

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

//    public CharacterService(CharacterRepository characterRepository, CharacterConfigRepository characterConfigRepository) {
//        this.characterRepository = characterRepository;
//        this.characterConfigRepository = characterConfigRepository;
//    }

    // 根据 characterId 查找character
    //     public Character getCharacterById(Long id) {
    //         return characterRepository.findById(id);
    //     }
    public Character getCharacterById(Long id) {
        if (characterRepository.existsById(id)) {
            return characterRepository.findById(id).get();
        }
        throw new EntityNotFoundException("Character with id " + id + " not found");
    }

    // 根据 code 查找character
    public Character getCharacterByCode(String code) {
        return characterRepository.findByCode(code);
    }

    // 根据 OwnerId 查找用户创建的所有角色
    public List<CharacterDTO> getCharactersByOwnerId(Long userId) {
        List<Character>characters = characterRepository.findByOwner(userId);

        List<CharacterDTO> dtos = CharacterMapper.toDTOList(characters);

        return processDTOList(dtos,userId);
    }


    // 根据 OwnerId 查找用户创建的所有角色 返回character列表
    public List<Character> getOriginCharactersByOwnerId(Long userId) {


        return characterRepository.findByOwner(userId);
    }


    // 根据 status 查找角色
    public List<Character> getCharactersByStatus(CharacterStatus status) {
        return characterRepository.findByStatus(status);
    }

    // 查找所有状态合法的角色
    public List<Character> getAllValidCharacters() {
        return characterRepository.findByStatus(CharacterStatus.PUBLIC);
    }

    // 根据状态和owner联合查找角色
    public List<Character> getCharactersByStatusAndOwner(CharacterStatus status, Long userId) {
        return characterRepository.findByStatusAndOwner(status, userId);
    }



//    // 根据 name 或 description keyword 查找角色
//    public List<Character> searchCharacters(Long userId, String keyword) {
//        String[] keywords = keyword.split(";；");
//        List<Character> characters = new ArrayList<>();
//        for (String k : keywords) {
//            characters.addAll(characterRepository.findByNameContainingOrDescriptionContaining(k, k));
//        }
//        return characters;
//    }

    //搜索框查询，获取所有符合关键词且可见的角色
    public List<CharacterDTO> searchCharactersByKeyword(Long userId, String keyword) {
        // 使用正则表达式分割，支持英文和中文分号

        String[] keywords = keyword.split("[;；]");


        Set<CharacterDTO> characterSet = new HashSet<>();
        for (String k : keywords) {
            System.out.println("k"+k);
            k = k.trim();  // 去掉空格

            System.out.println("k"+k);

            if (!k.isEmpty()) {
                try {
                    List<Character> results = characterRepository.findByNameContainingOrDescriptionContaining(k, k);
                    System.out.println("成功搜出结果：" + results.size() + " 条");
                    List<CharacterDTO> dtos = CharacterMapper.toDTOList(results);
                    characterSet.addAll(dtos);
                } catch (Exception e) {
                    System.err.println("查询出错：" + e.getMessage());
                    e.printStackTrace();  // 打印完整的异常栈
                }

            }
        }

        return processDTOList(filterByUserId(characterSet,userId), userId);  // 返回去重后的列表
    }




//    public List<Character> filterCharacters(String language, String gender) {
//        List<CharacterConfig> characterConfigs = characterConfigRepository.findByLanguageStyleAndGender(language, gender);
//        List<Character> characters = new ArrayList<>();
//        for (CharacterConfig characterConfig : characterConfigs) {
//            characters.add(characterRepository.findByConfigId(characterConfig.getId()));
//        }
//        return characters;
//    }


    @Transactional
    // 创建角色
    public Character createCharacter(Character character) {
        return characterRepository.save(character);
    }


    @Transactional
    // 更新角色
    public Character updateCharacter(Character character) {
        return characterRepository.save(character);
    }


    @Transactional
    // 删除角色（通过 ID）
    public void deleteCharacter(Long id) {
        try {
            // 获取角色信息
            Character character = characterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Character not found with id: " + id));
            Long configId = character.getConfigId();

            // 使用 JPQL 删除收藏记录
            characterRepository.deleteUserFavoritesByCharacterId(id);

            // 删除角色
            characterRepository.delete(character);


            // 删除配置
            if (configId != null && characterConfigRepository.existsById(configId)) {
                characterConfigRepository.deleteById(configId);
            }
        } catch (Exception e) {
            System.err.println("删除失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("删除角色失败: " + e.getMessage(), e);
        }
    }




    //角色详情查询
    public CharacterCreateUpdateDTO getCharacterDetail(Long id) {
        Character character = characterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Character with id " + id + " not found"));

        CharacterConfig config = characterConfigRepository.findById(character.getConfigId()).orElseThrow(() -> new EntityNotFoundException("Config with id " + character.getConfigId() + " not found"));

        return CharacterMapper.characterAndConfigToDto(character, config);

    }




    // 获取个人可见的所有角色
    public List<CharacterDTO> getAllVisibleCharactersByUserId(Long userId) {

        //查询公开角色
        List<Character> publicCharacters = getAllValidCharacters();

        //查询个人拥有的私有角色
        List<Character> privateOwnedCharacters = getCharactersByStatusAndOwner(CharacterStatus.PRIVATE, userId);

        //拼接角色列表
        List<Character> characters = Stream.concat(publicCharacters.stream(), privateOwnedCharacters.stream())
                .collect(Collectors.toList());

        List<CharacterDTO> characterDTOs = CharacterMapper.toDTOList(characters);


//        //根据userID，标识每个角色是否被当前用户收藏
//        for (CharacterDTO characterDTO : characterDTOs) {
//            characterDTO.setIsFavorite(userFavoriteService.existsByUserIdAndCharacterId(userId, characterDTO.getId()));
//            characterDTO.setOwner_name(userService.getUserById(characterDTO.getOwner()).getNickname());
//            characterDTO.setNum_of_favorites(userFavoriteService.getNumOfFavoritesByCharacterId(characterDTO.getId()));
//        }

        return processDTOList(characterDTOs, userId);

    }

    //获取游客可见的角色
    public List<CharacterDTO> getAllPublicCharacter() {

        System.out.println("进入了查找公开角色的方法");

        //查询公开角色
        List<Character> publicCharacters = getAllValidCharacters();


        List<CharacterDTO> characterDTOs = CharacterMapper.toDTOList(publicCharacters);


        //根据userID，标识每个角色是否被当前用户收藏
        for (CharacterDTO characterDTO : characterDTOs) {
            characterDTO.setIsFavorite(false);
            characterDTO.setOwner_name(userService.getUserById(characterDTO.getOwner()).getNickname());
            characterDTO.setNum_of_favorites(userFavoriteService.getNumOfFavoritesByCharacterId(characterDTO.getId()));
        }

        return characterDTOs;

    }

    //管理后台查询所有角色
    public List<Character> getAllAdmin() {
        return characterRepository.findAll();
    }




    public void checkVisible(Long characterId, String userToken) {
        //角色查询权限鉴权
        Character character = getCharacterById(characterId);
        if (userToken == null || userToken.isEmpty()) {
            if (!character.getStatus().equals(CharacterStatus.PUBLIC)) {
                throw new RuntimeException("Invalid user");
            }
        }
        else{
            Long userId = jwtUtil.getUserIdFromToken(userToken);
            if (character.getStatus().equals(CharacterStatus.PRIVATE) && !Objects.equals(character.getOwner(), userId)){
                throw new RuntimeException("Invalid user");
            }
        }
    }


    //一个过滤方法，用于从根据用户情况过滤查询结构
    private List<CharacterDTO> filterByUserId( Set<CharacterDTO> characters, Long userId) {
        if (characters.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<CharacterDTO> characterList = new ArrayList<>(characters);

        //如果是游客，只返回public的角色
        if (userId == null) {
            for (CharacterDTO character : characterList) {
                if (character.getStatus() == CharacterStatus.PRIVATE) {
                    characters.remove(character);
                }

            }
            return new ArrayList<>(characters);
        }
        //如果已经登录，返回所有的公开角色和个人的私有角色
        else {
            for (CharacterDTO character : characterList) {
                if (character.getStatus() == CharacterStatus.PRIVATE && !Objects.equals(character.getOwner(), userId)) {
                    characters.remove(character);
                }
            }
            return new ArrayList<>(characters);
        }

    }

    //将转换后的DTO使用依赖处理 批量处理
    private List<CharacterDTO> processDTOList(List<CharacterDTO> characterDTOs, Long userId) {
        for (CharacterDTO characterDTO : characterDTOs) {
            //根据userID，标识每个角色是否被当前用户收藏

            characterDTO.setIsFavorite(userFavoriteService.existsByUserIdAndCharacterId(userId, characterDTO.getId()));

            //标记所有者名称
            System.out.println("标记所有者");
            System.out.println(userService.getUserById(characterDTO.getOwner()).getNickname());
            characterDTO.setOwner_name(userService.getUserById(characterDTO.getOwner()).getNickname());

            characterDTO.setNum_of_favorites(userFavoriteService.getNumOfFavoritesByCharacterId(characterDTO.getId()));
        }

        return characterDTOs;
    }


    //更改角色的所有人
    public void updateOwner(Character character, Long ownerId) {
        character.setOwner(ownerId);
        characterRepository.save(character);
    }

    //检查用户是否有权限看到该角色
    public boolean isVisibleByUser(Long userId, Long characterId) {
        Character character = getCharacterById(characterId);
        // 角色是 PUBLIC 则所有人可见
        if (character.getStatus() == CharacterStatus.PUBLIC) {
            return true;
        }
        // 角色是 PRIVATE 则只有 owner 可见
        return character.getOwner().equals(userId);
    }



}
