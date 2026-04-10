package echos.AuthorizationService.Service;

import echos.AuthorizationService.Entity.Role;
import echos.AuthorizationService.Repository.RoleRepository;
import echos.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    /**
     * 获取所有角色
     * @return List<Role>
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * 根据 ID 获取角色
     * @param roleId 角色 ID
     * @return Role
     */
    public Role getRoleById(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        return role.orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
    }

    /**
     * 创建新角色
     * @param role 角色对象
     * @return Role
     */
    @Transactional
    public Role createRole(Role role) {
        if (roleRepository.findByRoleName(role.getRoleName()) != null) {
            throw new IllegalStateException("Role name already exists: " + role.getRoleName());
        }
        return roleRepository.save(role);
    }

    /**
     * 更新角色
     * @param roleId 角色 ID
     * @param updatedRole 更新后的角色对象
     * @return Role
     */
    @Transactional
    public Role updateRole(Long roleId, Role updatedRole) {
        Role existingRole = getRoleById(roleId);
        existingRole.setRoleName(updatedRole.getRoleName());
        return roleRepository.save(existingRole);
    }

    /**
     * 删除角色
     * @param roleId 角色 ID
     */
    @Transactional
    public void deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role not found with ID: " + roleId);
        }
        roleRepository.deleteById(roleId);
    }


    //一个服务类，主要用来判断管理员操作
    public boolean isAdmin(Long roleId) {
        return getRoleById(roleId).getRoleName().equalsIgnoreCase("ADMIN");
    }

    //确认是否是需求的角色
    public void checkUserRole(Long roleId, String roleName) {
        if(!getRoleById(roleId).getRoleName().equalsIgnoreCase(roleName)) {
            throw new IllegalArgumentException("需要角色 " + roleName + "！");
        }
    }

    //查询管理员的roleId
    public Long getAdminRoleId(){
        if (roleRepository.findByRoleName("admin") == null) {
            return roleRepository.findByRoleName("ADMIN").getId();

        }
        return roleRepository.findByRoleName("admin").getId();

    }
}
