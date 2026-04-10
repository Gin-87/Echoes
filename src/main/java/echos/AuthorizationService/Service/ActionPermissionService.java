package echos.AuthorizationService.Service;


import echos.AuthorizationService.Entity.ActionPermission;
import echos.AuthorizationService.Repository.ActionPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActionPermissionService {

    @Autowired
    private ActionPermissionRepository actionPermissionRepository;

    /**
     * 获取所有操作权限
     */
    public List<ActionPermission> getAllActionPermissions() {
        return actionPermissionRepository.findAll();
    }

    /**
     * 根据 ID 获取操作权限
     */
    public ActionPermission getActionPermissionById(Long id) {
        Optional<ActionPermission> actionPermission = actionPermissionRepository.findById(id);
        return actionPermission.orElseThrow(() -> new IllegalArgumentException("ActionPermission not found with ID: " + id));
    }

    /**
     * 创建操作权限
     */
    @Transactional
    public ActionPermission createActionPermission(ActionPermission actionPermission) {
        return actionPermissionRepository.save(actionPermission);
    }

    /**
     * 更新操作权限
     */
    @Transactional
    public ActionPermission updateActionPermission(Long id, ActionPermission updatedActionPermission) {
        ActionPermission existingPermission = getActionPermissionById(id);
        existingPermission.setActionName(updatedActionPermission.getActionName());
        existingPermission.setResource(updatedActionPermission.getResource());
        return actionPermissionRepository.save(existingPermission);
    }

    /**
     * 删除操作权限
     */
    @Transactional
    public void deleteActionPermission(Long id) {
        if (!actionPermissionRepository.existsById(id)) {
            throw new IllegalArgumentException("ActionPermission not found with ID: " + id);
        }
        actionPermissionRepository.deleteById(id);
    }

    //根据resource获取操作权限ID
    public Long getActionPermissionIdByResource(String resource) {
        ActionPermission actionPermission = actionPermissionRepository.findByResource(resource);
        if (actionPermission == null) {
            return null;
        }
        return actionPermissionRepository.findByResource(resource).getId();
    }

}
