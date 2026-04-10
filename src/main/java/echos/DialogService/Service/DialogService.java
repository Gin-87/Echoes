package echos.DialogService.Service;



import echos.DialogService.Entity.Dialog;
import echos.DialogService.Repository.DialogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 对话记录服务类
 */
@Service
public class DialogService {

    @Autowired
    private DialogRepository dialogRepository;

    /**
     * 保存新的对话记录
     * @param taskId 任务ID
     * @param isUser 是否为用户发送
     * @param content 对话内容
     * @return 保存后的对话记录
     */
    @Transactional
    public Dialog saveDialog(Long taskId, Boolean isUser, String content) {
        Dialog dialog = new Dialog();
        dialog.setTaskId(taskId);
        dialog.setIsUser(isUser);
        dialog.setContent(content);
        dialog.setCreatedAt(LocalDateTime.now());
        return dialogRepository.save(dialog);
    }

    /**
     * 根据ID查询对话记录
     * @param id 对话记录ID
     * @return 对话记录
     */
    @Transactional(readOnly = true)
    public Optional<Dialog> getDialogById(Long id) {
        return dialogRepository.findById(id);
    }

    /**
     * 查询某个任务下的所有对话记录（按生成时间升序）
     * @param taskId 任务ID
     * @return 对话记录列表
     */
    @Transactional(readOnly = true)
    public List<Dialog> getDialogsByTaskId(Long taskId) {
        return dialogRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
    }

    /**
     * 删除某条对话记录
     * @param id 对话记录ID
     */
    @Transactional
    public void deleteDialog(Long id) {
        dialogRepository.deleteById(id);
    }

    /**
     * 删除某任务下的所有对话记录
     * @param taskId 任务ID
     */
    @Transactional
    public void deleteDialogsByTaskId(Long taskId) {
        List<Dialog> dialogs = dialogRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
        dialogRepository.deleteAll(dialogs);
    }

    /**
     * 查询某任务下的最近N条对话记录（按生成时间倒序）
     * @param taskId 任务ID
     * @param limit 限制条数
     * @return 最近的对话记录列表
     */
    @Transactional(readOnly = true)
    public List<Dialog> getRecentDialogsByTaskId(Long taskId, int limit) {
        List<Dialog> dialogs = dialogRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
        return dialogs.stream()
                      .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                      .limit(limit)
                      .toList();
    }

    /**
     * 查询所有对话记录（按生成时间升序），支持分页z
     * @param page 当前页码（从0开始）
     * @param size 每页条数
     * @return 分页后的对话记录列表
     */
    @Transactional(readOnly = true)
    public List<Dialog> getDialogsWithPagination(int page, int size) {
        return dialogRepository.findAll()
                .stream()
                .skip(page * size)
                .limit(size)
                .toList();
    }
}
