package echos.NotificationService.Service;

import echos.NotificationService.Entity.Notifications;
import echos.NotificationService.NotificationStatus;
import echos.NotificationService.Repository.NotificationUserRepository;
import echos.NotificationService.Repository.NotificationsRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationsService {

    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    private NotificationUserRepository notificationUserRepository;

    public NotificationsService(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    // 通过keyword查找消息提醒
    public List<Notifications> searchByKeyword(String keyword) {
        List<Notifications> title = notificationsRepository.findByTitleContaining(keyword);
        List<Notifications> content = notificationsRepository.findByContentContaining(keyword);
        Set<Notifications> notifications = new HashSet<>(title);
        notifications.addAll(content);
        return new ArrayList<>(notifications);
    }

    // 创建消息提醒
    public Notifications createNotification(Notifications notification) {return notificationsRepository.save(notification);}

    // 更新消息提醒
    public Notifications updateNotification(Notifications notification) {return notificationsRepository.save(notification);}

    // 删除消息提醒
    public void deleteNotification(Long userId, Long id) throws IllegalAccessException {
        /* TODO: 判定用户角色确认操作权限 */
        if (true) throw new IllegalAccessException("无删除操作权限");
        notificationsRepository.deleteById(id);
    }

}
