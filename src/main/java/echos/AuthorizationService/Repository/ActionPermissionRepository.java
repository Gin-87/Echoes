package echos.AuthorizationService.Repository;


import echos.AuthorizationService.Entity.ActionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionPermissionRepository extends JpaRepository<ActionPermission, Long> {

    // 根据操作名称查询权限
    ActionPermission findByActionName(String actionName);


    //根据resource查询操作权限
    ActionPermission findByResource(String resource);



}
