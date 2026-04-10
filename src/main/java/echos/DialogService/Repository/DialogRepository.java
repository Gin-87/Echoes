package echos.DialogService.Repository;


import echos.DialogService.Entity.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 对话记录仓库类
 */
@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long> {

    /**
     * 根据任务ID查询对话记录（按生成时间排序）
     * @param taskId 任务ID
     * @return 对话记录列表
     */
    List<Dialog> findByTaskIdOrderByCreatedAtDesc(Long taskId);
}
