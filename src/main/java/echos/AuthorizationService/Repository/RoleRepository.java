package echos.AuthorizationService.Repository;


import echos.AuthorizationService.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // 根据角色名称查询角色
    Role findByRoleName(String roleName);
}
