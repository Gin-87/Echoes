package echos.UserService.DTO;



import echos.UserService.UserStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String phone;
    private String email;
    private String nickname;
    private String password;
    private Long role_id;
    private String role_name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UserStatus status;

}
