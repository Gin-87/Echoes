package echos.AuthorizationService.DTO;

import echos.AuthorizationService.Entity.MenuPermission;

public class MenuPermissionMapper {

    public static MenuDto toDto(MenuPermission permission) {
        MenuDto dto = new MenuDto();
        dto.setId(permission.getId());
        dto.setComponent(permission.getComponent());
        dto.setIcon(permission.getIcon());
        dto.setMenuName(permission.getMenuName());
        dto.setOrderNum(permission.getOrderNum());
        dto.setParentId(permission.getParentId());
        dto.setResource(permission.getResource());
        return dto;

    }
}
