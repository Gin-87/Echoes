package echos.DialogService.Controller;


import echos.DialogService.Entity.Dialog;
import echos.DialogService.Service.DialogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 对话记录控制器
 */
@RestController
@RequestMapping("/api/dialogs")
@Tag(name = "Dialog Controller", description = "管理对话记录的接口")
public class DialogController {

    @Autowired
    private DialogService dialogService;
    /**
     * 保存新的对话记录
     */
    @Operation(summary = "保存新的对话记录", description = "保存一条新的对话记录")
    @PostMapping("/save")
    public ResponseEntity<Dialog> saveDialog(
            @RequestParam Long taskId,
            @RequestParam Boolean isUser,
            @RequestParam String content) {
        Dialog dialog = dialogService.saveDialog(taskId, isUser, content);
        return ResponseEntity.ok(dialog);
    }

    /**
     * 根据ID查询对话记录
     */
    @Operation(summary = "获取单条对话记录", description = "根据对话ID查询记录")
    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<Dialog>> getDialogById(@PathVariable("id") Long id) {
        Optional<Dialog> dialog = dialogService.getDialogById(id);
        if (dialog.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No dialogs found for dialog ID " + id);
        }
        return ResponseEntity.ok(dialogService.getDialogById(id));
    }

    /**
     * 查询某任务下的所有对话记录
     */
    @Operation(summary = "获取任务下的对话列表", description = "根据任务ID获取所有对话记录")
    @GetMapping("/list/by-task/{taskId}")
    public ResponseEntity<List<Dialog>> getDialogsByTaskId(@PathVariable("taskId") Long taskId) {
        List<Dialog> dialogs = dialogService.getDialogsByTaskId(taskId);
        if (dialogs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No dialogs found for task ID " + taskId);
        }
        return ResponseEntity.ok(dialogService.getDialogsByTaskId(taskId));
    }

    /**
     * 查询某任务下的所有对话记录的内容
     */
    @Operation(summary = "获取任务下的对话内容列表", description = "根据任务ID获取所有对话内容")
    @GetMapping("/content/list/by-task/{taskId}")
    public ResponseEntity<List<String>> getDialogContentsByTaskId(@PathVariable("taskId") Long taskId) {
        List<Dialog> dialogs = dialogService.getDialogsByTaskId(taskId);
        if (dialogs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No dialogs found for task ID " + taskId);
        }
        return ResponseEntity.ok(dialogService.getDialogsByTaskId(taskId).stream().map(Dialog::getContent).toList());
    }


    /**
     * 删除单条对话记录
     */
    @Operation(summary = "删除单条对话记录", description = "根据对话ID删除记录")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDialog(@PathVariable("id") Long id) {
        dialogService.deleteDialog(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除某任务下的所有对话记录
     */
    @Operation(summary = "删除任务下的所有对话记录", description = "根据任务ID删除全部对话记录")
    @DeleteMapping("/delete/by-task/{taskId}")
    public ResponseEntity<Void> deleteDialogsByTaskId(@PathVariable("taskId") Long taskId) {
        dialogService.deleteDialogsByTaskId(taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取任务下的最近N条对话记录
     */
    @Operation(summary = "获取最近的对话记录", description = "根据任务ID获取最近N条对话记录")
    @GetMapping("/recent/by-task/{taskId}")
    public ResponseEntity<List<Dialog>> getRecentDialogsByTaskId(
            @PathVariable("taskId") Long taskId,
            @RequestParam("limit") int limit) {
        return ResponseEntity.ok(dialogService.getRecentDialogsByTaskId(taskId, limit));
    }

    /**
     * 分页获取所有对话记录
     */
    @Operation(summary = "分页获取对话记录", description = "分页获取所有对话记录")
    @GetMapping("/list")
    public ResponseEntity<List<Dialog>> getDialogsWithPagination(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return ResponseEntity.ok(dialogService.getDialogsWithPagination(page, size));
    }
}
