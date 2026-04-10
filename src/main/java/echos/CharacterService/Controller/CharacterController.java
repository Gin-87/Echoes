package echos.CharacterService.Controller;

import echos.AuthenticationService.Util.JwtUtil;
import echos.AuthorizationService.Service.RoleService;
import echos.CharacterService.CharacterStatus;
import echos.CharacterService.DTO.CharacterCreateUpdateDTO;
import echos.CharacterService.DTO.CharacterDTO;
import echos.CharacterService.DTO.CharacterManageListDTO;
import echos.CharacterService.DTO.CharacterMapper;
import echos.CharacterService.Entity.CharacterConfig;
import echos.CharacterService.Service.CharacterConfigService;
import echos.CharacterService.Service.UserFavoriteService;
import echos.Common.ApiResponse;
import echos.UserService.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import echos.CharacterService.Entity.Character;
import echos.CharacterService.Service.CharacterService;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

@RestController
@RequestMapping("/api/characters")

public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private UserFavoriteService userFavoriteService;

    @Autowired
    private CharacterConfigService characterConfigService;


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    // 根据角色 ID 获取角色
    @GetMapping("/getById")
    public ApiResponse<Character> getCharById(@RequestParam(name = "characterId") Long id) {
        try {
            Character character = characterService.getCharacterById(id);
            return ApiResponse.success(character);
        } catch (Exception e) {
            return ApiResponse.error(404, "Character not found: " + e.getMessage());
        }
    }

    // 根据 code 获取角色
    @GetMapping("/getByCode")
    public ApiResponse<CharacterDTO> getCharacterByCode(@RequestParam(name = "code") String code) {
        try {
            Character character = characterService.getCharacterByCode(code);
            return ApiResponse.success(CharacterMapper.toDTO(character));
        } catch (Exception e) {
            return ApiResponse.error(404, "Character not found: " + e.getMessage());
        }
    }

//    // 根据用户 ID 获取用户创建的所有角色
//    @GetMapping("/user/{userId}")
//    public ApiResponse<List<CharacterDTO>> getCharactersByUserId(@PathVariable Long userId) {
//        try {
//            List<Character> characters = characterService.getCharactersByUserId(userId);
//            return ApiResponse.success(CharacterMapper.toDTOList(characters));
//        } catch (Exception e) {
//            return ApiResponse.error(400, "Character search failed: " + e.getMessage());
//        }
//    }

    @Operation(summary = "根据角色状态（public/private）获取角色")
    @GetMapping("/status")
    // Get: localhost:3000/status/Public
    public ApiResponse<List<CharacterDTO>> getCharactersByStatus(@RequestParam(name = "status") CharacterStatus status) {
        try {
            List<Character> characters = characterService.getCharactersByStatus(status);
            return ApiResponse.success(CharacterMapper.toDTOList(characters));
        } catch (Exception e) {
            return ApiResponse.error(500, "Character search failed: " + e.getMessage());
        }

    }

    @Operation(summary = "根据 name 或 description 查找角色")
    @GetMapping("/search/ByKeyword")
    public ApiResponse<List<CharacterDTO>> searchCharacters(@RequestHeader(value = "Authorization", required = false) String userToken, @RequestParam(name = "keyword") String keyword) {
        //查找角色
        System.out.println(keyword);
        Long userId = null;
        if (userToken != null && !userToken.isEmpty()) {
            userId = jwtUtil.getUserIdFromToken(userToken);
        }


        try {
            List<CharacterDTO> characters = characterService.searchCharactersByKeyword(userId,keyword);
            return ApiResponse.success(characters);
        } catch (Exception e) {
            return ApiResponse.error(500, "Character search failed: " + e.getMessage());
        }
    }

//    // 筛选角色（支持多条件）
//    @GetMapping("/")
//    public ApiResponse<List<CharacterDTO>> filterCharacters(
//            @RequestParam(required = false) String language,
//            @RequestParam(required = false) String gender) {
//        try {
//            List<Character> characters = characterService.filterCharacters(language, gender);
//            return ApiResponse.success(CharacterMapper.toDTOList(characters));
//        } catch (Exception e) {
//            return ApiResponse.error(400, "Failed to retrieve characters: " + e.getMessage());
//        }
//    }

    @Operation(summary = "新增角色")
    @PostMapping("/create")
    public ApiResponse<CharacterDTO> createCharacter(@RequestHeader(value = "Authorization") String userToken,
                                                     @RequestBody CharacterCreateUpdateDTO dto) {
        try {

            //确认是否是管理员操作
            Long roleId = jwtUtil.getRoleIdFromToken(userToken);
            roleService.checkUserRole(roleId, "admin");

            //创建角色
            Character character = CharacterMapper.ComplexDtoToCharacter(dto);

            Long owner = jwtUtil.getUserIdFromToken(dto.getToken());

            character.setOwner(owner);

            //创建角色配置
            CharacterConfig config = CharacterMapper.ComplexDtoToConfig(dto);

            //创建角色
            CharacterConfig savedConfig = characterConfigService.createCharacterConfig(config);

            character.setConfigId(savedConfig.getId());


            return ApiResponse.success(CharacterMapper.toDTO(characterService.createCharacter(character)));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to create character: " + e.getMessage());
        }
    }

    // 更新角色(未完工）
    @Operation(summary = "更新角色")
    @PostMapping("/update")
    public ApiResponse<CharacterDTO> updateCharacter(@RequestParam(name = "characterId") Long id,
                                                     @RequestBody CharacterDTO characterDTO) {
        try {
            Character character = CharacterMapper.toEntity(characterDTO);
            character.setId(id); // 确保 ID 一致
            Character updatedCharacter = characterService.updateCharacter(character);
            return ApiResponse.success(CharacterMapper.toDTO(updatedCharacter));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to update character: " + e.getMessage());
        }
    }

    @Operation(summary = "删除角色", description = "通过ID删除角色")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteCharacter(@RequestParam(name = "id") Long id) {
        try {
            System.out.println("接收到删除请求，角色ID: " + id);
            characterService.deleteCharacter(id);
            return ApiResponse.success(null);
        } catch (EntityNotFoundException e) {
            System.err.println("删除失败 - 未找到角色: " + e.getMessage());
            return ApiResponse.error(404, "Character not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("删除失败 - 其他错误: " + e.getMessage());
            e.printStackTrace();
            return ApiResponse.error(500, "Failed to delete character: " + e.getMessage());
        }
    }

    //查询所有可被当前用户发现的角色
    @GetMapping("/getAll")
    @Operation(summary = "查询所有可被发现的角色，包含收藏标识符")
    public ApiResponse<List<CharacterDTO>> getAllCharactersByUserId(@RequestHeader(value = "Authorization", required = false) String userToken) {
        try {

            if (userToken == null || userToken.isEmpty()) {
                return ApiResponse.success(characterService.getAllPublicCharacter());
            }
            //提取userId
            Long userId = jwtUtil.getUserIdFromToken(userToken);
            return ApiResponse.success(characterService.getAllVisibleCharactersByUserId(userId));
        } catch (Exception e) {
            return ApiResponse.error(500, "Failed to retrieve characters from user: " + e.getMessage());
        }
    }


    //查询所有当前用户创建的角色
    @GetMapping("/getMy")
    @Operation(summary = "查询当前用户创建的角色，包含收藏标识符")
    public ApiResponse<List<CharacterDTO>> getMyCharactersByUserId(@RequestHeader(value = "Authorization") String userToken) {
        try {

            if (userToken == null || userToken.isEmpty()) {
                throw new RuntimeException("Invalid user");
            }
            //提取userId
            Long userId = jwtUtil.getUserIdFromToken(userToken);
            return ApiResponse.success(characterService.getCharactersByOwnerId(userId));
        } catch (Exception e) {
            return ApiResponse.error(500, "Failed to retrieve characters from user: " + e.getMessage());
        }
    }



    // Guest Access
    @GetMapping("/getPublic")
    @Operation(summary = "查询所有公共角色")
    public ApiResponse<List<CharacterDTO>> getPublicCharacters() {
        try {
            List<Character> characters = characterService.getCharactersByStatus(CharacterStatus.PUBLIC);
            return ApiResponse.success(CharacterMapper.toDTOList(characters));
        } catch (Exception e) {
            return ApiResponse.error(500, "Failed to retrieve characters: " + e.getMessage());
        }
    }


    // Admin Access
    @GetMapping("/admin/getAll")
    @Operation(summary = "管理员级别角色查询")
    public ApiResponse<List<CharacterManageListDTO>> getAllCharactersByAdminUserId(@RequestHeader(value = "Authorization") String userToken) {
        try {

            //校验是否是管理员角色
            Long roleId = jwtUtil.getRoleIdFromToken(userToken);
            if (roleId != 2L) {
                return ApiResponse.error(403, "无权限");
            }

            List<Character> characters = characterService.getAllAdmin();

            List<CharacterManageListDTO> listDTOS =  CharacterMapper.toAdminDTOList(characters);
            for (CharacterManageListDTO dto : listDTOS) {
                dto.setCreator(userService.getUserById(dto.getCreatorId()).getNickname());
            }

            return ApiResponse.success(listDTOS);


        } catch (Exception e) {
            return ApiResponse.error(500, "Failed to retrieve characters from user: " + e.getMessage());
        }
    }


    //根据角色id查询角色详情
    @GetMapping("/getDetail/{characterId}")
    @Operation(summary = "角色详情查询")
    public ApiResponse<CharacterCreateUpdateDTO> getCharacterDetail(@PathVariable(name = "characterId") Long characterId,  @RequestHeader(value = "Authorization", required = false) String userToken) {

        //角色查询权限鉴权
        characterService.checkVisible(characterId, userToken);

        try{
            return ApiResponse.success(characterService.getCharacterDetail(characterId));

        }
        catch(Exception e){
            return ApiResponse.error(500, "Failed to retrieve character detail: " + e.getMessage());
        }
    }
}