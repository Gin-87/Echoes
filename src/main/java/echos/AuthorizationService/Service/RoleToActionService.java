package echos.AuthorizationService.Service;


import echos.AuthorizationService.Entity.ActionPermission;
import echos.AuthorizationService.Entity.Role;
import echos.AuthorizationService.Entity.RoleToAction;
import echos.AuthorizationService.Repository.ActionPermissionRepository;
import echos.AuthorizationService.Repository.RoleRepository;
import echos.AuthorizationService.Repository.RoleToActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleToActionService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ActionPermissionRepository actionPermissionRepository;

    @Autowired
    private RoleToActionRepository roleToActionRepository;

    @Autowired
    private ActionPermissionService actionPermissionService;

    /**
     * 创建角色与操作权限的关联
     */
    @Transactional
    public RoleToAction createRoleActionAssociation(Long roleId, Long actionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        ActionPermission action = actionPermissionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action not found: " + actionId));

        RoleToAction roleToAction = new RoleToAction();
        roleToAction.setRole(role);
        roleToAction.setActionPermission(action);

        return roleToActionRepository.save(roleToAction);
    }

    /**
     * 查询角色关联的所有操作权限
     */
    public List<RoleToAction> getActionsByRoleId(Long roleId) {
        return roleToActionRepository.findByRoleId(roleId);
    }

    /**
     * 删除角色与操作权限的关联
     */
    @Transactional
    public void deleteRoleActionAssociation(Long roleId, Long actionId) {
        Optional<RoleToAction> roleToAction = roleToActionRepository.findByRoleId(roleId).stream()
                .filter(rta -> rta.getActionPermission().getId().equals(actionId))
                .findFirst();

        roleToAction.ifPresent(roleToActionRepository::delete);
    }

    //编辑角色与权限的关联
    @Transactional
    public RoleToAction updateRoleActionAssociation(Long roleId, Long actionId, Long roleToActionId) {
        RoleToAction roleToAction = new RoleToAction();
        roleToAction.setId(roleToActionId);
        roleToAction.setRole(roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)));
        roleToAction.setActionPermission(actionPermissionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action not found: " + actionId)));
        return roleToActionRepository.save(roleToAction);
    }



    //根据resource和角色ID判断是否有操作权限
    public boolean checkActionPermission(Long roleId, String resource){
        Long actionPermissionId = actionPermissionService.getActionPermissionIdByResource(resource);
        return roleToActionRepository.findByRoleIdAndActionPermissionId(roleId, actionPermissionId) != null;
    }


}
