package echos.AuthenticationService.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Component
public class JwtUtil {

    private final long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000; // 7 天


    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 签名密钥

    public String generateToken(Long userId, Long roleId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 使用 userId 作为主体
                .claim("roleId", roleId) // 添加 roleId 信息到 Payload
                .setIssuedAt(new Date(System.currentTimeMillis())) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token 有效期 1 小时
                .signWith(secretKey) // 使用密钥签名
                .compact();
    }

    //支持token刷新机制
    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey)
                .compact();
    }


    //验证token
    public Claims parseToken(String token) {
        String pureToken = token.replace("Bearer ", "").trim(); // 去除 Bearer 前缀

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(pureToken)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid Token: " + e.getMessage(), e);
        }
    }



    //提取userid
    public Long getUserIdFromToken(String token) {
        String pureToken = token.replace("Bearer ", "").trim(); // 去除 Bearer 前缀

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(pureToken)
                .getBody();

        return Long.parseLong(claims.getSubject()); // 主体是 userId
    }

    //提取角色id
    public Long getRoleIdFromToken(String token) {

        if(token == null) return 3L;
        String pureToken = token.replace("Bearer ", "").trim(); // 去除 Bearer 前缀

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(pureToken)
                .getBody();

        return claims.get("roleId", Long.class); // 提取 roleId
    }


    //获取签发时间
    public LocalDateTime getIssuedAtLocalDateTime(String token) {
        String pureToken = token.replace("Bearer ", "").trim(); // 去除 Bearer 前缀

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey) // 替换为你的签名密钥
                .build()
                .parseClaimsJws(pureToken)
                .getBody();

        // 将签发时间转换为 LocalDateTime
        return claims.getIssuedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    //获取过期时间
    public LocalDateTime getExpirationLocalDateTime(String token) {
        String pureToken = token.replace("Bearer ", "").trim();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey) // 替换为你的签名密钥
                .build()
                .parseClaimsJws(pureToken)
                .getBody();

        // 将过期时间转换为 LocalDateTime
        return claims.getExpiration().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }


    public boolean isTokenExpired(String token) {
        String pureToken = token.replace("Bearer ", "").trim(); // 去除 Bearer 前缀

        LocalDateTime expirationTime = getExpirationLocalDateTime(pureToken);

        // 比较过期时间和当前时间
        return expirationTime.isBefore(LocalDateTime.now());
    }


}


