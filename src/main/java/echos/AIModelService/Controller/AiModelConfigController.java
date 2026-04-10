package echos.AIModelService.Controller;

import echos.AIModelService.Entity.AiModelConfig;
import echos.AIModelService.Service.AiModelConfigService;
import echos.SystemService.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-models")
@Tag(name = "AI模型配置", description = "管理AI模型配置的接口")
public class AiModelConfigController {

    @Autowired
    private AiModelConfigService service;

    @Operation(summary = "创建AI模型配置")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AiModelConfig>> create(@RequestBody AiModelConfig config) {

        //成功逻辑
        return ResponseEntity.ok(new ApiResponse<>(200, "Model created successfully", service.create(config)));
    }

    @Operation(summary = "更新AI模型配置")
    @PutMapping("/update/{id}")
    public ResponseEntity<AiModelConfig> update(@PathVariable Long id, @RequestBody AiModelConfig config) {
        return ResponseEntity.ok(service.update(id, config));
    }

    @Operation(summary = "获取所有AI模型配置")
    @GetMapping("/all")
    public ResponseEntity<List<AiModelConfig>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "根据ID获取AI模型配置")
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        AiModelConfig config = service.getById(id);
        if (config == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: 未找到对应模型配置");

         }
         return ResponseEntity.ok(config);
    }

    @Operation(summary = "根据ID删除AI模型配置")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "根据名称获取AI模型配置")
    @GetMapping("getByName/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {

        AiModelConfig config = service.getByModelName(name);
        if (config == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: 未找到对应模型配置");

        }
        return ResponseEntity.ok(config);
    }

}
