package echos.AuthenticationService.Controller;

import echos.AuthenticationService.Dto.LoginRequestDto;
import echos.AuthenticationService.Dto.LoginResponseDto;
import echos.AuthenticationService.Dto.RefreshRequestDto;
import echos.AuthenticationService.Dto.TokenResponseDto;
import echos.AuthenticationService.Service.AuthenticationService;
import echos.Common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "用户认证相关接口")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    /**
     * 用户登录接口
     *
     * @param loginRequest 用户登录信息
     * @return 包含 Access Token 和 Refresh Token 的响应
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            LoginResponseDto loginResponse = authService.login(loginRequest);
            return ApiResponse.success(loginResponse);
        } catch (Exception e) {
            return ApiResponse.error(401, "Login failed: " + e.getMessage());
        }
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT 字符串
     * @return true 表示有效，false 表示无效
     */
    @Operation(summary = "令牌有效性校验")
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    /**
     * 刷新 Access Token 接口
     *
     * @param refreshRequest 包含 Refresh Token 的请求
     * @return 新的 Access Token 响应
     */
    @Operation(summary = "刷新令牌")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshAccessToken(@RequestBody RefreshRequestDto refreshRequest) {
        TokenResponseDto tokenResponse = authService.refreshAccessToken(refreshRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
