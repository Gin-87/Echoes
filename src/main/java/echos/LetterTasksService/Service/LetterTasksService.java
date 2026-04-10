package echos.LetterTasksService.Service;

import echos.CharacterService.CharacterStatus;
import echos.CharacterService.Service.CharacterService;
import echos.IntegrationService.Service.IntegrationService;
import echos.LetterTasksService.DTO.LetterTaskCreateUpdateDTO;
import echos.LetterTasksService.DTO.LetterTaskResponseDTO;
import echos.LetterTasksService.Entity.LetterTasks;
import echos.LetterTasksService.Entity.LetterTasksConfig;
import echos.LetterTasksService.FrequencyStatus;
import echos.LetterTasksService.LetterStatus;
import echos.LetterTasksService.Repository.LetterTasksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LetterTasksService {

    private static final Logger log = LoggerFactory.getLogger(LetterTasksService.class);

    @Autowired
    private LetterTasksRepository letterTasksRepository;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private LetterTasksConfigService letterTasksConfigService;

    @Lazy
    @Autowired
    private IntegrationService integrationService;

    public LetterTasks getById(Long id) {
        return letterTasksRepository.findById(id).orElse(null);
    }

    /** 通过用户ID查找信件任务 */
    public List<LetterTasks> getLetterTasksByUserId(long userId) {
        return letterTasksRepository.findByUserId(userId);
    }

    /** 通过用户ID和角色ID查找信件任务 */
    public List<LetterTasks> getLetterTasksByUserIdAndCharacterId(Long userId, Long characterId) throws IllegalAccessException {
        if (userId == null || (!userId.equals(characterService.getCharacterById(characterId).getOwner())
                && characterService.getCharacterById(characterId).getStatus().equals(CharacterStatus.PRIVATE))) {
            throw new IllegalAccessException("无权限查看相关信件");
        }
        return letterTasksRepository.findByUserIdAndCharacterId(userId, characterId);
    }

    /** 通过状态查找信件任务 */
    public List<LetterTasks> getLetterTasksByStatus(Long userId, LetterStatus status) {
        return letterTasksRepository.findByUserIdAndStatus(userId, status);
    }

    /**
     * 创建信件任务完整流程：
     * 1. 权限校验（角色是否对用户可见）
     * 2. 创建 LetterTasksConfig（保存用户偏好、频率等）
     * 3. 创建 LetterTasks（关联 config）
     * 4. 异步触发 AI 生成第一封信
     */
    @Transactional
    public LetterTaskResponseDTO createLetterTask(LetterTaskCreateUpdateDTO dto, Long userId) {
        // 1. 权限校验
        if (!characterService.isVisibleByUser(userId, dto.getCharacterId())) {
            throw new RuntimeException("Character is not visible to user");
        }

        // 2. 创建任务配置
        LetterTasksConfig config = new LetterTasksConfig();
        config.setEmail(dto.getTargetEmail());
        config.setSetting(dto.getTaskName() != null ? dto.getTaskName() : "");
        config.setUserSalutation(dto.getFinalAppellation());
        config.setUserCharacterRelationCustomized(dto.getRelationshipWithUser());
        config.setPreferredLanguage("CHN");  // 默认中文，可从用户偏好读取
        config.setFrequency(dto.getFrequency() != null
                ? FrequencyStatus.valueOf(dto.getFrequency().name())
                : FrequencyStatus.WEEKLY);
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());

        LetterTasksConfig savedConfig = letterTasksConfigService.createLetterTaskConfig(config);

        // 3. 创建信件任务
        LetterTasks task = new LetterTasks();
        task.setUserId(userId);
        task.setCharacterId(dto.getCharacterId());
        task.setTaskConfigId(savedConfig.getId());
        task.setStatus(LetterStatus.ENABLED);

        LetterTasks savedTask = letterTasksRepository.save(task);

        // 4. 异步触发 AI 生成第一封信
        integrationService.triggerLetterGeneration(savedTask.getId());
        log.info("[LetterTask] Created taskId={} for userId={}, triggered AI generation", savedTask.getId(), userId);

        // 5. 构造响应
        LetterTaskResponseDTO response = new LetterTaskResponseDTO();
        response.setId(savedTask.getId());
        response.setTaskName(dto.getTaskName());
        return response;
    }

    /** 更新信件任务 */
    public LetterTasks updateLetterTasks(LetterTasks letterTasks) {
        return letterTasksRepository.save(letterTasks);
    }

    /** 删除信件任务 */
    public void deleteLetterTasks(Long id) {
        letterTasksRepository.deleteById(id);
    }
}
