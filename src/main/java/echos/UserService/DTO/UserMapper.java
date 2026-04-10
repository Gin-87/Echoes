package echos.UserService.DTO;

import echos.UserService.Entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    // 将实体类转换为 DTO
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setStatus(user.getStatus());
        dto.setRole_id(user.getRoleId());
        return dto;
    }

    // 将 DTO 转换为实体类
    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setStatus(dto.getStatus());
        user.setPassword(dto.getPassword());
        user.setRoleId(dto.getRole_id());
        return user;
    }

    // 批量转换实体类为 DTO
    public static List<UserDTO> toDTOList(List<User> users) {
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }
}
