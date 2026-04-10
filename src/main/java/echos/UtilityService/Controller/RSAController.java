package echos.UtilityService.Controller;

import echos.UtilityService.PasswordUtil;
import echos.UtilityService.RSADecryptUtil;
import echos.UtilityService.RSAKeyUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Base64;

@RestController
@RequestMapping("/rsa-service/")
@Tag(name = " RSA API", description = "关于前端非对称加密的操作")
public class RSAController {

    // 提供 RSA 公钥给前端
    @Operation(summary = "提供 RSA 公钥给前端")
    @GetMapping("/rsa/public-key")
    public String getPublicKey() {
        // 直接使用 RSAKeyUtil.getPublicKey()，它已经返回了 Base64 编码的字符串
        return RSAKeyUtil.getPublicKey();
    }

    // 解密接口
    @GetMapping("/rsa/decrypt")
    @Operation(summary = "rsa解密")
    public String decrypt(@RequestParam("encryptedPassword") String encryptedPassword) throws GeneralSecurityException, UnsupportedEncodingException {
        return RSADecryptUtil.decrypt(encryptedPassword);
    }

//    // 哈希加密接口
//    @GetMapping("/rsa/hashEncrypt")
//    public String hashEncrypt(@RequestParam("Password") String Password) throws GeneralSecurityException, UnsupportedEncodingException {
//        return PasswordUtil.encode(Password);
//    }
}
