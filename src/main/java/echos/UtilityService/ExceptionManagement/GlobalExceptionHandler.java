package echos.UtilityService.ExceptionManagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import echos.Common.ApiResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ApiResponse<Void> handleUserAlreadyExists(UserAlreadyExistsException e) {
        return ApiResponse.error(409, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleIllegalArgument(IllegalArgumentException e) {
        return ApiResponse.error(400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGeneral(Exception e) {
        return ApiResponse.error(500, "Internal server error: " + e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = String.format("Method %s not allowed. Supported methods are: %s", 
            ex.getMethod(), 
            String.join(", ", ex.getSupportedMethods()));
            
        return ApiResponse.error(HttpStatus.METHOD_NOT_ALLOWED.value(), message);
    }


}
