package echos.UserService.Controller;

import echos.AuthenticationService.Util.JwtUtil;
import echos.Common.ApiResponse;
import echos.UserService.DTO.UserPreferenceDTO;
import echos.UserService.Entity.UserPreference;
import echos.UserService.Service.UserPreferenceService;
import echos.UserService.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-prefer")
@Tag(name = "User prefer API", description = "关于用户偏好的操作")
public class UserPreferenceController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserPreferenceService userPreferenceService;

    @PostMapping("/update")
    @Operation(summary = "用户偏好变更")
    public ApiResponse<UserPreference> updateUserPrefer(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserPreferenceDTO	 request) {
        try {
            // 从 token 中提取 userId
            Long userId = jwtUtil.getUserIdFromToken(authHeader);
            
            // 调用 service 来更新语言
            System.out.println(userId);
            System.out.println(request);
            System.out.println(request.getSelectedLanguage());
            UserPreference updatedPreference = userPreferenceService.updateUserPreference(userId, request.getSelectedLanguage());
            
            return ApiResponse.success(updatedPreference);
        } catch (Exception e) {
            return ApiResponse.error(500, "Failed to update user preference: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    @Operation(summary = "获取用户偏好设置")
    public ApiResponse<UserPreference> getUserPreference(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null) {
                return ApiResponse.success(new UserPreference());

            }            Long userId = jwtUtil.getUserIdFromToken(authHeader);
            UserPreference preference = userPreferenceService.getUserPreferenceByUserId(userId);
            return ApiResponse.success(preference);
        } catch (Exception e) {
            return ApiResponse.error(500, "Failed to get user preference: " + e.getMessage());
        }
    }
}
