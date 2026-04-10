package echos.NotificationService.Controller;

import echos.AuthenticationService.Util.JwtUtil;
import echos.Common.ApiResponse;
import echos.NotificationService.Entity.Notifications;
import echos.NotificationService.Service.NotificationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "notification API", description = "消息提醒相关接口")
public class NotificationsController {

    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "根据keyword获取消息提醒")
    @GetMapping("/searchByKeyword")
    public ApiResponse<List<Notifications>> searchByKeyword(@RequestParam(name = "keyword") String keyword) {
        try {
            return ApiResponse.success(notificationsService.searchByKeyword(keyword));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to retrieve notifications: " + e.getMessage());
        }
    }

    @Operation(summary = "创建消息提醒")
    @PostMapping("/create")
    public ApiResponse<Notifications> createNotification(@RequestBody Notifications notifications) {
        try {
            return ApiResponse.success(notificationsService.createNotification(notifications));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to create notification: " + e.getMessage());
        }
    }

    @Operation(summary = "更新消息提醒")
    @PostMapping("/update")
    public ApiResponse<Notifications> updateNotification(@RequestBody Notifications notifications) {
        try {
        return ApiResponse.success(notificationsService.updateNotification(notifications));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to update notification: " + e.getMessage());
        }
    }

    @Operation(summary = "删除消息提醒")
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteNotification(@RequestHeader(value = "Authorization") String userToken, @RequestParam(name = "id") Long id) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(userToken);
            notificationsService.deleteNotification(userId, id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to delete notification: " + e.getMessage());
        }
    }
}
