package echos.NotificationService.Service;

import echos.NotificationService.Entity.NotificationUser;
import echos.NotificationService.NotificationStatus;
import echos.NotificationService.Repository.NotificationUserRepository;
import echos.NotificationService.Repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationUserService {

    @Autowired
    private NotificationUserRepository notificationUserRepository;

    @Autowired
    private NotificationsRepository notificationsRepository;

    public NotificationUserService(NotificationUserRepository notificationUserRepository, NotificationsRepository notificationsRepository) {
        this.notificationUserRepository = notificationUserRepository;
        this.notificationsRepository = notificationsRepository;
    }

    // 通过消息提醒ID查找消息提醒用户
    public List<NotificationUser> geNotificationUserByNotificationId(Long notificationId) {return notificationUserRepository.findByNotificationId(notificationId);}

    // 通过用户ID查找消息提醒用户
    public List<NotificationUser> getNotificationUserByUserId(Long userId) {return notificationUserRepository.findByUserId(userId);}

    // 通过状态查找消息提醒用户
    public List<NotificationUser> getNotificationUserByStatus(NotificationStatus status) {return notificationUserRepository.findByStatus(status);}

    // 创建消息提醒用户
    public NotificationUser createNotificationUser(NotificationUser notificationUser) {return notificationUserRepository.save(notificationUser);}

    // 更新消息提醒用户
    public NotificationUser updateNotificationUser(NotificationUser notificationUser) {return notificationUserRepository.save(notificationUser);}

    // 删除消息提醒用户
    public void deleteNotificationUser(Long id) {notificationUserRepository.deleteById(id);}

    public List<NotificationUser> getNotificationByNotificationIdAndUserId(Long userId, Long notificationId) {return notificationUserRepository.findByNotificationIdAndUserId(notificationId, userId);}

}
