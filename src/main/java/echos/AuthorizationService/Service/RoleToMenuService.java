package echos.AuthorizationService.Service;


import echos.AuthenticationService.Util.JwtUtil;
import echos.AuthorizationService.Entity.MenuPermission;
import echos.AuthorizationService.Entity.Role;
import echos.AuthorizationService.Entity.RoleToMenu;
import echos.AuthorizationService.Repository.MenuPermissionRepository;
import echos.AuthorizationService.Repository.RoleRepository;
import echos.AuthorizationService.Repository.RoleToMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleToMenuService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    @Autowired
    private RoleToMenuRepository roleToMenuRepository;

    @Autowired
    private MenuPermissionService menuPermissionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 创建角色与菜单的关联
     */
    public RoleToMenu createRoleMenuAssociation(Long roleId, Long menuPermissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        MenuPermission menu = menuPermissionRepository.findById(menuPermissionId)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + menuPermissionId));

        RoleToMenu roleToMenu = new RoleToMenu();
        roleToMenu.setRole(role);
        roleToMenu.setMenuPermission(menu);

        return roleToMenuRepository.save(roleToMenu);
    }

    /**
     * 查询角色关联的所有菜单
     */
    public List<RoleToMenu> getMenusByRoleId(Long roleId) {
        return roleToMenuRepository.findByRoleId(roleId);
    }


    //查找角色关联的所有菜单id
    public List<Long> getMenusIdsByRoleId(Long roleId) {
        List<RoleToMenu> roleToMenuList = roleToMenuRepository.findByRoleId(roleId);
        List<Long> menuIds = new ArrayList<>();
        for (RoleToMenu roleToMenu : roleToMenuList) {
            menuIds.add(roleToMenu.getMenuPermission().getId());
        }
        return menuIds;
    }


    //编辑角色和菜单权限的关联
    public RoleToMenu updateRoleMenuAssociation(Long roleId, Long menuPermissionId, Long roleToMenuId) {
        RoleToMenu roleToMenu = new RoleToMenu();
        roleToMenu.setId(roleToMenuId);
        roleToMenu.setRole(roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)));
        roleToMenu.setMenuPermission(menuPermissionRepository.findById(menuPermissionId)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + menuPermissionId)));

        return roleToMenuRepository.save(roleToMenu);
    }

    /**
     * 删除角色与菜单的关联
     */
    public void deleteRoleMenuAssociation(Long roleId, Long menuId) {
        RoleToMenu roleToMenu = roleToMenuRepository.findByRoleIdAndMenuPermissionId(roleId, menuId);
        roleToMenuRepository.delete(roleToMenu);
    }


    //通过路径判断是否有菜单访问权限
    public boolean checkMenuPermission(Long roleId, String resource) {
        Long menuPermissionId = menuPermissionService.getMenuPermissionIdByResource(resource);
        return roleToMenuRepository.findByRoleIdAndMenuPermissionId(roleId, menuPermissionId) != null;
    }


    //通过token获取有权限访问的MenuID列表
    public List<Long> getMenuPermissionsByToken(String token) {
        Long userRoleId = null;


        if (token == null || token.isEmpty()) {
             userRoleId = (3L);
        }
        else{
             userRoleId = jwtUtil.getRoleIdFromToken(token);
        }

        return getMenusIdsByRoleId(userRoleId);
    }



}
