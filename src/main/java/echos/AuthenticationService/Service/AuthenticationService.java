package echos.AuthenticationService.Service;


import echos.AuthenticationService.Dto.LoginRequestDto;
import echos.AuthenticationService.Dto.LoginResponseDto;
import echos.AuthenticationService.Dto.RefreshRequestDto;
import echos.AuthenticationService.Dto.TokenResponseDto;

public interface AuthenticationService {

    /**
     * 用户登录
     *
     * @param loginRequestDto 包含用户登录信息（如 email 或 phone 和密码）
     * @return 包含 JWT 和用户基本信息的响应
     */
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT 字符串
     * @return 如果 Token 有效返回 true，否则返回 false
     */
    boolean validateToken(String token);

    /**
     * 从 Token 中提取用户 ID
     *
     * @param token JWT 字符串
     * @return 用户 ID
     */
    Long getUserIdFromToken(String token);

    /**
     * 从 Token 中提取角色 ID
     *
     * @param token JWT 字符串
     * @return 角色 ID
     */
    Long getRoleIdFromToken(String token);

    /**
     * 从 Token 中提取角色 ID
     *
     * @param refreshRequestDto refreshToken字符串
     * @return 新的accessToken
     */
    TokenResponseDto refreshAccessToken(RefreshRequestDto refreshRequestDto);

}
