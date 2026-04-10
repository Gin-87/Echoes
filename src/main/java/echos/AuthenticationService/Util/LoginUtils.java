package echos.AuthenticationService.Util;

import java.util.regex.Pattern;

public class LoginUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$"); // 支持国际格式的手机号

    public static boolean isEmail(String identifier) {
        return EMAIL_PATTERN.matcher(identifier).matches();
    }

    public static boolean isPhone(String identifier) {
        return PHONE_PATTERN.matcher(identifier).matches();
    }
}
