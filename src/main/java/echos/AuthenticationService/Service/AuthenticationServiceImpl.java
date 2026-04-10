package echos.AuthenticationService.Service;

import echos.AuthenticationService.Dto.LoginRequestDto;
import echos.AuthenticationService.Dto.LoginResponseDto;
import echos.AuthenticationService.Dto.RefreshRequestDto;
import echos.AuthenticationService.Dto.TokenResponseDto;
import echos.AuthenticationService.Util.JwtUtil;
import echos.AuthenticationService.Util.LoginUtils;
import echos.UserService.Entity.User;
import echos.UserService.Repository.UserRepository;
import echos.UserService.Service.UserService;
import echos.UtilityService.RSADecryptUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // 添加参数验证
        if (loginRequestDto.getIdentifier() == null || loginRequestDto.getPassword() == null) {
            throw new IllegalArgumentException("Identifier and password cannot be null");
        }

        String loginId = loginRequestDto.getIdentifier();

        if(LoginUtils.isEmail(loginId) || loginId.equals("admin")){
            if (userService.userLoginCheckByEmail(loginId,loginRequestDto.getPassword())){
                User user = userService.getUserByEmail(loginId);
                String jwt = jwtUtil.generateToken(user.getId(),user.getRoleId());
                String RefreshToken = jwtUtil.generateRefreshToken(user.getId());
                return new LoginResponseDto(jwt,RefreshToken, user.getId(),user.getRoleId());
            }else{
                throw new BadCredentialsException("Invalid email or password");
            }

        } else if (LoginUtils.isPhone(loginId)) {

            if (userService.userLoginCheckByPhone(loginId, loginRequestDto.getPassword())) {
                //登录接口判断




                User user = userService.getUserByPhone(loginId);
                String jwt = jwtUtil.generateToken(user.getId(), user.getRoleId());
                String RefreshToken = jwtUtil.generateRefreshToken(user.getId());
                return new LoginResponseDto(jwt,RefreshToken, user.getId(), user.getRoleId());
            }else{
                throw new BadCredentialsException("Invalid phone or password");
            }
        }
        else{
            throw new BadCredentialsException("Invalid email or phone number");
        }
    }


    // accessToken 刷新
    @Override
    public TokenResponseDto refreshAccessToken(RefreshRequestDto refreshRequestDto) {
        String refreshToken = refreshRequestDto.getRefreshToken();

        // 验证 Refresh Token
        Claims claims = jwtUtil.parseToken(refreshToken);


        Long userId = Long.parseLong(claims.getSubject());

        // 验证用户是否存在
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BadCredentialsException("Invalid refresh token");
        }


        // 生成新的 Access Token
        String newAccessToken = jwtUtil.generateToken(userId, user.getRoleId());

        return new TokenResponseDto(newAccessToken);
    }

    //token有效校验
    @Override
    public boolean validateToken(String token) {
        return jwtUtil.isTokenExpired(token);
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return jwtUtil.getUserIdFromToken(token);
    }

    @Override
    public Long getRoleIdFromToken(String token) {
        return jwtUtil.getRoleIdFromToken(token);
    }
}