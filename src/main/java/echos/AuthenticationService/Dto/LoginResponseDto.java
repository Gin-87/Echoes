package echos.AuthenticationService.Dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;   // 登录成功生成的 JWT
    private String refreshToken; //刷新token
    private Long userId;    // 用户 ID
    private Long roleId;    // 角色 ID
}