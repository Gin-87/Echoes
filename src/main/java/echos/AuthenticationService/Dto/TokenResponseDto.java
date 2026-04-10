package echos.AuthenticationService.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponseDto {
    private String accessToken;
}

