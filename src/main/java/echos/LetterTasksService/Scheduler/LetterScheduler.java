package echos.LetterTasksService.Scheduler;

import echos.IntegrationService.Service.IntegrationService;
import echos.LetterTasksService.Entity.LetterTasks;
import echos.LetterTasksService.Entity.LetterTasksConfig;
import echos.LetterTasksService.FrequencyStatus;
import echos.LetterTasksService.LetterStatus;
import echos.LetterTasksService.Repository.LetterTasksRepository;
import echos.LetterTasksService.Service.LetterTasksConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时调度器：按照任务配置的频率（WEEKLY / MONTHLY 等），定时触发 AI 续写信件
 */
@Component
public class LetterScheduler {

    private static final Logger log = LoggerFactory.getLogger(LetterScheduler.class);

    @Autowired
    private LetterTasksRepository letterTasksRepository;

    @Autowired
    private LetterTasksConfigService letterTasksConfigService;

    @Autowired
    private IntegrationService integrationService;

    /**
     * 每天凌晨 2:00 执行：检查所有 ENABLED 任务，根据频率判断是否应该触发今天
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void scheduleDailyCheck() {
        log.info("[Scheduler] Daily letter check started at {}", LocalDateTime.now());

        List<LetterTasks> enabledTasks = letterTasksRepository.findByStatus(LetterStatus.ENABLED);
        log.info("[Scheduler] Found {} ENABLED tasks", enabledTasks.size());

        for (LetterTasks task : enabledTasks) {
            try {
                letterTasksConfigService.getLetterTaskConfigById(task.getTaskConfigId())
                        .ifPresent(config -> {
                            if (shouldTriggerToday(config)) {
                                log.info("[Scheduler] Triggering AI for taskId={}, frequency={}",
                                        task.getId(), config.getFrequency());
                                integrationService.triggerLetterGeneration(task.getId());
                            }
                        });
            } catch (Exception e) {
                log.error("[Scheduler] Error processing taskId={}: {}", task.getId(), e.getMessage());
            }
        }

        log.info("[Scheduler] Daily letter check completed");
    }

    /**
     * 根据频率配置判断今天是否应触发
     */
    private boolean shouldTriggerToday(LetterTasksConfig config) {
        FrequencyStatus freq = config.getFrequency();
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();
        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue(); // 1=Mon, 7=Sun

        return switch (freq) {
            case WEEKLY      -> dayOfWeek == 1;              // 每周一
            case HALFWEEKLY  -> dayOfWeek == 1 || dayOfWeek == 4; // 每周一、四
            case MONTHLY     -> dayOfMonth == 1;             // 每月1日
            case HALFMONTHLY -> dayOfMonth == 1 || dayOfMonth == 15; // 每月1日、15日
            case RANDOM      -> Math.random() < 0.3;         // 每天30%概率
        };
    }
}
