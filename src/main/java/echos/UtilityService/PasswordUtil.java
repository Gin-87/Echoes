package echos.UtilityService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 对密码进行哈希加密
    public static String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // 验证密码是否匹配
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
