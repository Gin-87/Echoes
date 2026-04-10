package echos.IntegrationService.Controller;

import echos.IntegrationService.DTO.IntegrationConfigDTO;
import echos.IntegrationService.Service.IntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Integration Controller：为 Python AI 服务提供数据，并接收 Python 回调
 */
@RestController
@RequestMapping("/api/integration")
@Tag(name = "Integration Controller", description = "用于 AI 集成的接口")
public class IntegrationController {

    @Autowired
    private IntegrationService integrationService;

    /**
     * Python 服务主动拉取：根据任务ID获取所有配置参数
     */
    @Operation(summary = "根据任务ID获取所有配置参数", description = "Python AI 服务调用，拉取任务+角色+模型配置")
    @GetMapping("/find/{task_id}")
    public ResponseEntity<IntegrationConfigDTO> getConfigByTaskId(@PathVariable("task_id") Long taskId) {
        IntegrationConfigDTO dto = integrationService.getAllConfigByTaskId(taskId);
        return ResponseEntity.ok(dto);
    }

    /**
     * Python 服务回调：将生成的信件内容持久化
     * Body: { "task_id": 1, "content": "...", "success": true }
     */
    @Operation(summary = "接收 Python AI 回调", description = "Python 生成信件后回写内容，持久化到 LetterTasksLogs")
    @PostMapping("/callback")
    public ResponseEntity<Map<String, Object>> callback(@RequestBody Map<String, Object> body) {
        Long taskId = Long.valueOf(body.get("task_id").toString());
        String content = (String) body.get("content");
        boolean success = Boolean.parseBoolean(body.getOrDefault("success", "true").toString());

        integrationService.handleCallback(taskId, content, success);
        return ResponseEntity.ok(Map.of("status", "ok", "task_id", taskId));
    }
}
