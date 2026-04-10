package echos.AuthenticationService.Dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String identifier;     // 手机号或邮箱
    private String password;    // RSA加密后的密码
}