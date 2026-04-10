package echos.AuthorizationService.Repository;


import echos.AuthorizationService.Entity.RoleToMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleToMenuRepository extends JpaRepository<RoleToMenu, Long> {
    // 根据角色 ID 查询关联的菜单
    List<RoleToMenu> findByRoleId(Long roleId);

    // 根据菜单权限ID 查询角色
    List<RoleToMenu> findByMenuPermissionId(Long menuId);

    // 根据菜单权限ID和角色ID查询是否存在
    RoleToMenu findByRoleIdAndMenuPermissionId(Long roleId, Long menuId);

}
