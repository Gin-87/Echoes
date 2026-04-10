package echos.AuthorizationService.Repository;



import echos.AuthorizationService.Entity.MenuPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuPermissionRepository extends JpaRepository<MenuPermission, Long> {
    // 根据菜单名称查找菜单权限
    MenuPermission findByMenuName(String menuName);

    // 根据访问路径查找菜单权限
    MenuPermission findByResource(String resource);


}

