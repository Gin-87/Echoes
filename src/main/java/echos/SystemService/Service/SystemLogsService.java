package echos.SystemService.Service;

import echos.SystemService.Entity.SystemLogs;
import echos.SystemService.Repository.SystemLogsRepository;
import echos.SystemService.SystemLogsStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemLogsService {

    @Autowired
    private SystemLogsRepository systemLogsRepository;

    public SystemLogsService(SystemLogsRepository systemLogsRepository) {
        this.systemLogsRepository = systemLogsRepository;
    }

    // 通过ID查找系统日志
    public Optional<SystemLogs> getSystemLogsById(Long id) {return systemLogsRepository.findById(id);}

    // 通过用户ID查找系统日志
    public List<SystemLogs> getSystemLogsByUserId(Long userId) {return systemLogsRepository.findByUserId(userId);}

    // 通过事件查找系统日志
    public List<SystemLogs> getSystemLogsByEvent(String event) {return systemLogsRepository.findByEvent(event);}

    // 通过用户ID和状态查找系统日志
    public List<SystemLogs> searchSystemLogs(Long userId, SystemLogsStatus status) {return systemLogsRepository.findByUserIdAndEvent(userId, status);}

    // 通过keyword查找系统日志
    public List<SystemLogs> searchByMessage(String keyword) {return systemLogsRepository.findByMessageContaining(keyword);}

    // 创建系统日志
    public SystemLogs createSystemLogs(SystemLogs systemLogs) {return systemLogsRepository.save(systemLogs);}

    // 更新系统日志
    public SystemLogs updateSystemLogs(SystemLogs systemLogs) {return systemLogsRepository.save(systemLogs);}

    // 删除系统日志
    public void deleteSystemLogs(Long id) {systemLogsRepository.deleteById(id);}
}
