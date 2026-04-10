package echos.SystemService.Controller;

import echos.SystemService.Entity.SystemLogs;
import echos.SystemService.Service.SystemLogsService;
import echos.SystemService.SystemLogsStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/system_logs")
@Tag(name = "System Logs API", description = "系统日志相关接口")
public class SystemLogsController {

    @Autowired
    private SystemLogsService systemLogsService;

    @Operation(summary = "根据ID获取系统日志")
    @GetMapping("/getById")
    public ResponseEntity<Optional<SystemLogs>> getSystemLogsById(Long id) {return ResponseEntity.ok(systemLogsService.getSystemLogsById(id));}

    // 根据用户ID获取系统日志
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SystemLogs>> getSystemLogsByUserId(@PathVariable Long userId) {return ResponseEntity.ok(systemLogsService.getSystemLogsByUserId(userId));}

    // 根据事件获取系统日志
    @GetMapping("/event/{event}")
    public ResponseEntity<List<SystemLogs>> getSystemLogsByEvent(@PathVariable String event) {return ResponseEntity.ok(systemLogsService.getSystemLogsByEvent(event));}

    // 根据用户ID和状态获取系统日志
    @GetMapping("/search/{conditions}")
    public ResponseEntity<List<SystemLogs>> getSystemLogsByUserIdAndStatus(@PathVariable Long userId, @RequestParam SystemLogsStatus status) {return ResponseEntity.ok(systemLogsService.searchSystemLogs(userId, status));}

    // 根据keyword获取系统日志
    @GetMapping("/{message}")
    public ResponseEntity<List<SystemLogs>> searchSystemLogsByKeyword(@PathVariable String keyword) {return ResponseEntity.ok(systemLogsService.searchByMessage(keyword));}

    // 创建系统日志
    @PostMapping
    public ResponseEntity<SystemLogs> createSystemLogs(@RequestBody SystemLogs systemLogs) {return ResponseEntity.ok(systemLogsService.createSystemLogs(systemLogs));}

    // 更新系统日志
    @PostMapping("/{id}")
    public ResponseEntity<SystemLogs> updateSystemLogs(@RequestBody SystemLogs systemLogs) {return ResponseEntity.ok(systemLogsService.updateSystemLogs(systemLogs));}

    // 删除系统日志
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemLogs(@PathVariable Long id) {
        systemLogsService.deleteSystemLogs(id);
        return ResponseEntity.noContent().build();
    }
}
