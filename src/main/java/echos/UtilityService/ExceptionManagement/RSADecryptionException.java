package echos.UtilityService.ExceptionManagement;

public class RSADecryptionException extends RuntimeException {
    public RSADecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
