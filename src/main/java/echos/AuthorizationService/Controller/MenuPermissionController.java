package echos.AuthorizationService.Controller;

import echos.AuthenticationService.Util.JwtUtil;
import echos.AuthorizationService.DTO.MenuDto;
import echos.AuthorizationService.DTO.MenuPermissionMapper;
import echos.AuthorizationService.Entity.MenuPermission;
import echos.AuthorizationService.Service.MenuPermissionService;
import echos.AuthorizationService.Service.RoleToMenuService;
import echos.Common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
@Tag(name = "MenuPermission API", description = "基于角色的菜单权限接口")
public class MenuPermissionController {

    private static final Logger log = LoggerFactory.getLogger(MenuPermissionController.class);

    @Autowired
    private RoleToMenuService roleToMenuService;

    @Autowired
    private MenuPermissionService menuPermissionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取用户的菜单权限
     */
    @GetMapping("/menu")
    @Operation(summary = "获取当前用户的菜单权限")
    public ApiResponse<List<MenuDto>> getUserMenus(@RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Getting menu permissions for auth header: {}", authHeader);
            System.out.println("进入了接口内部");

            // 处理 Bearer token
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ApiResponse.error(401, "Invalid authorization header");
            }

            // 从 token 中获取角色 ID
            Long roleId = jwtUtil.getRoleIdFromToken(authHeader);
            log.info("Role ID extracted from token: {}", roleId);

            // 获取该角色可以访问的菜单 ID 列表
            List<Long> menuIds = roleToMenuService.getMenusIdsByRoleId(roleId);
            log.info("Found {} menu IDs for role {}", menuIds.size(), roleId);

            // 获取菜单详细信息并转换成 DTO 列表
            List<MenuDto> menuDtos = menuIds.stream()
                    .map(id -> menuPermissionService.getMenuPermissionById(id))
                    .filter(menu -> menu != null)  // 过滤掉可能的 null 值
                    .map(MenuPermissionMapper::toDto)  // 转换成 MenuDto
                    .collect(Collectors.toList());

            log.info("Retrieved {} menu DTOs", menuDtos.size());

            return ApiResponse.success(menuDtos);
        } catch (Exception e) {
            log.error("Error while fetching user menus: ", e);
            return ApiResponse.error(500, "Internal server error: " + e.getMessage());
        }
    }
}
