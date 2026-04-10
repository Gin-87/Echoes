package echos.AuthorizationService.Service;

import echos.AuthorizationService.Entity.MenuPermission;
import echos.AuthorizationService.Repository.MenuPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MenuPermissionService {

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    /**
     * 获取所有菜单权限
     */
    public List<MenuPermission> getAllMenuPermissions() {
        return menuPermissionRepository.findAll();
    }

    /**
     * 根据 ID 获取菜单权限
     */
    public MenuPermission getMenuPermissionById(Long id) {
        Optional<MenuPermission> menuPermission = menuPermissionRepository.findById(id);
        return menuPermission.orElseThrow(() -> new IllegalArgumentException("MenuPermission not found with ID: " + id));
    }

    /**
     * 创建菜单权限
     */
    @Transactional
    public MenuPermission createMenuPermission(MenuPermission menuPermission) {
        return menuPermissionRepository.save(menuPermission);
    }

    /**
     * 更新菜单权限
     */
    @Transactional
    public MenuPermission updateMenuPermission(Long id, MenuPermission updatedMenuPermission) {
        MenuPermission existingPermission = getMenuPermissionById(id);
        existingPermission.setMenuName(updatedMenuPermission.getMenuName());
        existingPermission.setResource(updatedMenuPermission.getResource());
        return menuPermissionRepository.save(existingPermission);
    }

    /**
     * 删除菜单权限
     */
    @Transactional
    public void deleteMenuPermission(Long id) {
        if (!menuPermissionRepository.existsById(id)) {
            throw new IllegalArgumentException("MenuPermission not found with ID: " + id);
        }
        menuPermissionRepository.deleteById(id);
    }

    //根据resource查询菜单权限ID
    public Long getMenuPermissionIdByResource(String resource) {
        MenuPermission menuPermission = menuPermissionRepository.findByResource(resource);
        if (menuPermission == null) {
            return null;
        }
        return menuPermission.getId();
    }
}
