package echos.LetterTasksService.Service;

import echos.LetterTasksService.Entity.LetterTasksLogs;
import echos.LetterTasksService.Repository.LetterTasksLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LetterTasksLogsService {

    @Autowired
    private LetterTasksLogsRepository letterTasksLogsRepository;

    /** 通过ID查找日志 */
    public Optional<LetterTasksLogs> getLetterTasksLogById(Long id) {
        return letterTasksLogsRepository.findById(id);
    }

    /** 通过任务ID查找所有日志（按创建时间排序） */
    public List<LetterTasksLogs> getLetterTasksLogsByTaskId(Long taskId) {
        return letterTasksLogsRepository.findByTaskId(taskId);
    }

    /** 关键词搜索日志 */
    public List<LetterTasksLogs> searchLetterTasksLogs(String keyword) {
        return letterTasksLogsRepository.findByMessageContaining(keyword);
    }

    /** 创建日志 */
    public LetterTasksLogs createLetterTasksLogs(LetterTasksLogs letterTasksLogs) {
        return letterTasksLogsRepository.save(letterTasksLogs);
    }

    /** 更新日志 */
    public LetterTasksLogs updateLetterTasksLogs(LetterTasksLogs letterTasksLogs) {
        return letterTasksLogsRepository.save(letterTasksLogs);
    }

    /** 删除日志 */
    public void deleteLetterTasksLogs(Long id) {
        letterTasksLogsRepository.deleteById(id);
    }
}
