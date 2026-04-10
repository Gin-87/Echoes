package echos.NotificationService.Controller;

import echos.AuthenticationService.Util.JwtUtil;
import echos.AuthorizationService.Service.RoleService;
import echos.Common.ApiResponse;
import echos.NotificationService.Entity.NotificationUser;
import echos.NotificationService.NotificationStatus;
import echos.NotificationService.Service.NotificationUserService;
import echos.UserService.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notification_user")
@Tag(name = "notification-user API", description = "消息提醒-用户相关接口")
public class NotificationUserController {

    @Autowired
    private NotificationUserService notificationUserService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RoleService roleService;

    // TODO: CHECK USER PARAMETER
    @Operation(summary = "根据用户ID获取消息提醒用户")
    @GetMapping("/getByUser")
    public ApiResponse<List<NotificationUser>> getNotificationUserByUserId(@RequestParam(name = "userId") Long userId) {
        try {
            return ApiResponse.success(notificationUserService.getNotificationUserByUserId(userId));
        } catch (Exception e) {
            return ApiResponse.error(404, "Notification user not found: " + e.getMessage());
        }
    }

    @Operation(summary = "根据状态获取消息提醒用户")
    @GetMapping("/getByStatus")
    public ApiResponse<List<NotificationUser>> getNotificationUserByStatus(@RequestHeader(value = "Authorization") String userToken,
                                                                           @RequestParam(name = "status") NotificationStatus status) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(userToken);
            // TODO: IMPLEMENT AUTHENTICATION LOGIC FOR STATUS SEARCH
            return ApiResponse.success(notificationUserService.getNotificationUserByStatus(status));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to retrieve notification user: " + e.getMessage());
        }
    }

    @Operation(summary = "根据消息提醒ID和用户ID获取消息提醒用户")
    @GetMapping("/search")
    public ApiResponse<List<NotificationUser>> searchNotificationUser(@RequestParam(name = "userId") Long userId,
                                                                         @RequestParam(name = "notificationId") Long notificationId) {
        try {
        return ApiResponse.success(notificationUserService.getNotificationByNotificationIdAndUserId(userId, notificationId));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to retrieve notification user: " + e.getMessage());
        }
    }

    @Operation(summary = "创建消息提醒用户")
    @PostMapping("/create")
    public ApiResponse<NotificationUser> addNotificationUser(@RequestHeader(value = "Authorization") String userToken,
                                                             @RequestBody NotificationUser notificationUser) {
        //确认是否是管理员操作
        Long roleId = jwtUtil.getRoleIdFromToken(userToken);
        roleService.checkUserRole(roleId, "admin");
        try {
            return ApiResponse.success(notificationUserService.createNotificationUser(notificationUser));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to add notification user: " + e.getMessage());
        }
    }

    @Operation(summary = "更新消息提醒用户")
    @PostMapping("/update")
    public ApiResponse<NotificationUser> updateNotificationUser(@RequestBody NotificationUser notificationUser) {
        try {
            return ApiResponse.success(notificationUserService.updateNotificationUser(notificationUser));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to update notification user: " + e.getMessage());
        }
    }

    @Operation(summary = "删除消息提醒用户", description = "通过ID删除消息提醒用户")
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteNotificationUser(@RequestHeader(value = "Authorization") String userToken,
                                                       @RequestParam(name = "id") Long id) {
        //确认是否是管理员操作
        Long roleId = jwtUtil.getRoleIdFromToken(userToken);
        roleService.checkUserRole(roleId, "admin");
        try {
            notificationUserService.deleteNotificationUser(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to delete notification user: " + e.getMessage());
        }
    }
}
