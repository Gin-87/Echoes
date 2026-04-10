package echos.LetterTasksService.Controller;

import echos.AuthenticationService.Util.JwtUtil;
import echos.Common.ApiResponse;
import echos.LetterTasksService.DTO.LetterTaskCreateUpdateDTO;
import echos.LetterTasksService.DTO.LetterTaskResponseDTO;
import echos.LetterTasksService.DTO.LetterTasksDTO;
import echos.LetterTasksService.DTO.LetterTasksMapper;
import echos.LetterTasksService.Entity.LetterTasks;
import echos.LetterTasksService.LetterStatus;
import echos.LetterTasksService.Service.LetterTasksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/letter_tasks")
@Tag(name = "Letter Tasks API", description = "信件任务相关接口")
public class LetterTasksController {

    @Autowired
    private LetterTasksService letterTasksService;
    @Autowired
    private JwtUtil jwtUtil;

    public LetterTasksController(LetterTasksService letterTasksService) {this.letterTasksService = letterTasksService;}

    // TODO：userID
    @Operation(summary = "根据用户ID获取信件任务")
    @GetMapping("/getByUserId")
    public ApiResponse<List<LetterTasksDTO>> getLetterTasksByUserId(@RequestParam(name = "userId") Long userId) {
        try {
            List<LetterTasks> letterTasks = letterTasksService.getLetterTasksByUserId(userId);
            return ApiResponse.success(LetterTasksMapper.toDTO(letterTasks));
        } catch (Exception e) {
            return ApiResponse.error(500, "Letter tasks not found: " + e.getMessage());
        }
    }

    @Operation(summary = "根据用户ID和角色ID获取信件任务")
    @GetMapping("/getByUserIdAndCharacterId")
    public ApiResponse<List<LetterTasksDTO>> getLetterTasksByUserIdAndCharacterId(@RequestHeader(value = "Authorization") String userToken, @RequestParam(name = "characterId") Long characterId) {
        Long userId = null;
        if (userToken != null && !userToken.isEmpty()) {
            userId = jwtUtil.getUserIdFromToken(userToken);
        }
        try {
            List<LetterTasks> letterTasks = letterTasksService.getLetterTasksByUserIdAndCharacterId(userId, characterId);
            return ApiResponse.success(LetterTasksMapper.toDTO(letterTasks));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to retrieve letter tasks: " + e.getMessage());
        }
    }

    @Operation(summary = "根据状态获取信件任务", description = "需要登录后的用户ID调取对应状态信件")
    @GetMapping("/getByStatus")
    public ApiResponse<List<LetterTasksDTO>> getLetterTasksByStatus(@RequestParam(name = "userId") Long userId, @RequestParam(name = "status") LetterStatus status) {
        try {
            List<LetterTasks> list = letterTasksService.getLetterTasksByStatus(userId, status);
            return ApiResponse.success(LetterTasksMapper.toDTO(list));
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to retrieve letter tasks: " + e.getMessage());
        }
    }

//    // 创建信件任务
//    @PostMapping
//    public ResponseEntity<LetterTasksDTO> createLetterTasks(@RequestBody LetterTasksDTO letterTasksDTO) {
//        LetterTasks letterTask = LetterTasksMapper.toEntity(letterTasksDTO);
//        LetterTasks savedLetterTask = letterTasksService.createLetterTask(letterTask);
//        return ResponseEntity.ok(LetterTasksMapper.toDTO(savedLetterTask));
//    }


    //创建信件
    @Operation(summary = "创建信件任务的接口")
    @PostMapping("/create/letterTask")
    public ApiResponse<LetterTaskResponseDTO> createLetterTask(@RequestHeader(value = "Authorization") String userToken, @RequestBody LetterTaskCreateUpdateDTO letterTaskCreateUpdateDTO) {
        if (userToken.isEmpty()) {throw new IllegalArgumentException("用户ID不能为空");}
        try{
            Long userId = jwtUtil.getUserIdFromToken(userToken);
            return ApiResponse.success(letterTasksService.createLetterTask(letterTaskCreateUpdateDTO, userId));

        }catch (Exception e){
            return ApiResponse.error(400, "Failed to create letter task: " + e.getMessage());
        }


    }

    @Operation(summary = "更新信件任务")
    @PostMapping("/update")
    public ApiResponse<LetterTasksDTO> updateLetterTasks(@RequestBody LetterTasksDTO letterTasksDTO) {
        try {
            LetterTasks updated = letterTasksService.updateLetterTasks(LetterTasksMapper.toEntity(letterTasksDTO));
            return ApiResponse.success(LetterTasksMapper.toDTO(updated));
        } catch (Exception e) {
        return ApiResponse.error(400, "Failed to update letter task: " + e.getMessage());
        }
    }

    @Operation(summary = "删除信件任务")
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteLetterTasks(@RequestHeader(value = "Authorization") String userToken, @RequestParam(name = "letterTaskId") Long id) {
        if (userToken.isEmpty() || id == null) {
            throw new IllegalArgumentException("无效信件删除请求");
        }
        try {
            Long userId = jwtUtil.getUserIdFromToken(userToken);
            if (userId != letterTasksService.getById(id).getUserId()) {throw new IllegalAccessException("无删除操作权限");}
            letterTasksService.deleteLetterTasks(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(400, "Failed to delete letter task: " + e.getMessage());
        }
    }
}
