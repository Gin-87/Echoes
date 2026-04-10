package echos.UserService.Controller;

import echos.AuthenticationService.Util.JwtUtil;
import echos.Common.ApiResponse;
import echos.UserService.DTO.UserDTO;
import echos.UserService.DTO.UserMapper;
import echos.UserService.Entity.User;
import echos.UserService.Service.UserService;
import echos.UserService.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "关于用户的操作")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    // 查询用户（支持多条件）
    @GetMapping("/search")
    @Operation(summary = "多条件查询用户")
    public ApiResponse<List<UserDTO>> searchUsers(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) UserStatus status) {
        try {
            List<User> users = userService.searchUsers(phone, email, nickname, status);
            return ApiResponse.success(UserMapper.toDTOList(users));
        } catch (Exception e) {
            return ApiResponse.error(500, "Failed to search users: " + e.getMessage());
        }
    }

    // 根据 ID 查询用户
    @GetMapping("/getById/{id}")
    @Operation(summary = "根据id精确查找用户")
    public ApiResponse<UserDTO> getUserById(@PathVariable(name = "id") Long id) {
        try {
            User user = userService.getUserById(id);
            return ApiResponse.success(UserMapper.toDTO(user));
        } catch (Exception e) {
            return ApiResponse.error(404, "User not found: " + e.getMessage());
        }
    }

    // 注册用户
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public ApiResponse<User> register(@RequestBody UserDTO userDTO) {
        try {
            // 添加日志
            log.info("Received registration request for: {}", userDTO.getEmail() != null ? userDTO.getEmail() : userDTO.getPhone());
            
            User user = userService.createUser(userDTO);
            return ApiResponse.success(user);
        } catch (Exception e) {
            log.error("Registration failed", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    // 更新用户
    @PostMapping("/update/{id}")
    @Operation(summary = "用于用户更新")
    public ApiResponse<UserDTO> updateUser(@PathVariable(name = "id") Long id, @RequestBody UserDTO userDTO) {
        try {
            User user = userService.updateUser(id, userDTO);
            return ApiResponse.success(UserMapper.toDTO(user));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to update user: " + e.getMessage());
        }
    }

    // 删除用户
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "用于用户删除")
    public ApiResponse<Void> deleteUser(@PathVariable(name = "id") Long id, @RequestHeader(value = "Authorization") String userToken) {
        Long roleId = jwtUtil.getRoleIdFromToken(userToken);
        try {
            userService.deleteUser(id, roleId);

            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to delete user: " + e.getMessage());
        }
    }

    // 加载后台管理页面用户列表
    @GetMapping("/admin/getAll")
    @Operation(summary = "根据id精确查找用户")
    public ApiResponse<List<UserDTO>> adminGetAll(@RequestHeader(value = "Authorization") String userToken) {
        try {
            System.out.println("userToken: " + userToken);
            Long roleId = jwtUtil.getRoleIdFromToken(userToken);
            System.out.println("roleId: " + roleId);
            return ApiResponse.success(userService.adminGetAllUsers(roleId));
        } catch (Exception e) {
            return ApiResponse.error(500, "查询用户失败" + e.getMessage());
        }
    }

}
