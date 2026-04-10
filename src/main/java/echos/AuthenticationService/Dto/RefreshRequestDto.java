package echos.AuthenticationService.Dto;

import lombok.Data;

@Data
public class RefreshRequestDto {
    private String refreshToken;
}