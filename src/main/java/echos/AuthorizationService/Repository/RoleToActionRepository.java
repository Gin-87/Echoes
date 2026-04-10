package echos.AuthorizationService.Repository;


import echos.AuthorizationService.Entity.RoleToAction;
import echos.AuthorizationService.Entity.RoleToMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleToActionRepository extends JpaRepository<RoleToAction, Long> {
    // 根据角色 ID 查询关联的操作
    List<RoleToAction> findByRoleId(Long roleId);

    // 根据操作权限ID 查询角色
    List<RoleToAction> findByActionPermissionId(Long actionId);


    // 根据操作权限ID和角色ID查询是否存在
    RoleToAction findByRoleIdAndActionPermissionId(Long roleId, Long actionPermissionId);
}
