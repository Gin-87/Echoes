package echos.IntegrationService.Service;

import echos.AIModelService.Entity.AiModelConfig;
import echos.AIModelService.Service.AiModelConfigService;
import echos.CharacterService.Entity.Character;
import echos.CharacterService.Entity.CharacterConfig;
import echos.CharacterService.Service.CharacterConfigService;
import echos.CharacterService.Service.CharacterService;
import echos.IntegrationService.DTO.CharacterConfigDTO;
import echos.IntegrationService.DTO.IntegrationConfigDTO;
import echos.IntegrationService.DTO.ModelConfigDTO;
import echos.IntegrationService.DTO.TaskConfigDTO;
import echos.IntegrationService.Mapper.CharacterConfigMapper;
import echos.IntegrationService.Mapper.ModelMapper;
import echos.IntegrationService.Mapper.TaskConfigMapper;
import echos.LetterTasksService.Entity.LetterTasks;
import echos.LetterTasksService.Entity.LetterTasksConfig;
import echos.LetterTasksService.Entity.LetterTasksLogs;
import echos.LetterTasksService.LogStatus;
import echos.LetterTasksService.Service.LetterTasksConfigService;
import echos.LetterTasksService.Service.LetterTasksLogsService;
import echos.LetterTasksService.Service.LetterTasksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IntegrationService {

    private static final Logger log = LoggerFactory.getLogger(IntegrationService.class);

    @Autowired private LetterTasksService letterTasksService;
    @Autowired private LetterTasksConfigService letterTasksConfigService;
    @Autowired private LetterTasksLogsService letterTasksLogsService;
    @Autowired private CharacterService characterService;
    @Autowired private CharacterConfigService characterConfigService;
    @Autowired private AiModelConfigService aiModelConfigService;
    @Autowired private RestTemplate restTemplate;

    @Value("${python.service.url:http://localhost:8001}")
    private String pythonServiceUrl;

    /**
     * 基于任务ID，聚合所有配置数据，构造 IntegrationConfigDTO
     */
    public IntegrationConfigDTO getAllConfigByTaskId(Long taskId) {
        LetterTasks task = letterTasksService.getById(taskId);
        if (task == null) {
            throw new RuntimeException("LetterTask not found for id: " + taskId);
        }

        // 获取任务配置
        LetterTasksConfig taskConfig = letterTasksConfigService
                .getLetterTaskConfigById(task.getTaskConfigId())
                .orElseThrow(() -> new RuntimeException("LetterTasksConfig not found"));

        // 获取角色及其配置
        Character character = characterService.getCharacterById(task.getCharacterId());
        CharacterConfig characterConfig = characterConfigService.getCharacterConfigById(character.getConfigId());

        // 获取历史信件（日志表中 ACTIVE 状态的内容）
        List<LetterTasksLogs> logs = letterTasksLogsService.getLetterTasksLogsByTaskId(taskId);
        List<String> historyLetters = logs.stream()
                .filter(l -> l.getStatus() == LogStatus.ACTIVE)
                .map(LetterTasksLogs::getMessage)
                .collect(Collectors.toList());

        // 获取模型配置（取第一个可用，可按需扩展为按任务配置选择）
        AiModelConfig modelConfig = aiModelConfigService.getAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No AiModelConfig available"));

        // 组装 DTO
        IntegrationConfigDTO dto = new IntegrationConfigDTO();
        dto.setTaskId(taskId);

        TaskConfigDTO taskConfigDTO = TaskConfigMapper.toDto(taskConfig);
        dto.setTaskConfigDTO(taskConfigDTO);

        CharacterConfigDTO characterConfigDTO = CharacterConfigMapper.toDTO(characterConfig);
        characterConfigDTO.setCharacterName(character.getName());
        dto.setCharacterConfigDTO(characterConfigDTO);

        ModelConfigDTO modelConfigDTO = ModelMapper.toDto(modelConfig);
        modelConfigDTO.setModelApiAddress(modelConfig.getApiAddress());
        dto.setModelConfigDTO(modelConfigDTO);

        dto.setHasHistory(!historyLetters.isEmpty());
        dto.setHistoryLetters(historyLetters);

        return dto;
    }

    /**
     * 异步触发 Python 服务生成信件
     */
    @Async
    public void triggerLetterGeneration(Long taskId) {
        try {
            IntegrationConfigDTO payload = getAllConfigByTaskId(taskId);
            String url = pythonServiceUrl + "/api/receive-integration-config";
            log.info("[Integration] Triggering Python for taskId={}", taskId);
            restTemplate.postForEntity(url, payload, String.class);
            log.info("[Integration] Python triggered successfully for taskId={}", taskId);
        } catch (Exception e) {
            log.error("[Integration] Failed to trigger Python for taskId={}: {}", taskId, e.getMessage());
        }
    }

    /**
     * 接收 Python 回调，将生成的信件内容持久化到 LetterTasksLogs
     */
    public void handleCallback(Long taskId, String content, boolean success) {
        if (!success) {
            log.warn("[Integration] Failure callback for taskId={}", taskId);
            return;
        }
        LetterTasks task = letterTasksService.getById(taskId);
        if (task == null) {
            log.error("[Integration] Callback for unknown taskId={}", taskId);
            return;
        }
        LetterTasksLogs logEntry = new LetterTasksLogs();
        logEntry.setTaskId(taskId);
        logEntry.setTaskConfigId(task.getTaskConfigId());
        logEntry.setContentId(UUID.randomUUID().toString().replace("-", "").substring(0, 24));
        logEntry.setMessage(content);
        logEntry.setCreatedAt(LocalDateTime.now());
        logEntry.setStatus(LogStatus.ACTIVE);
        letterTasksLogsService.createLetterTasksLogs(logEntry);
        log.info("[Integration] Letter persisted for taskId={}", taskId);
    }
}
