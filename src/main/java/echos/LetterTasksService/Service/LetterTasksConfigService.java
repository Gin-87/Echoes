package echos.LetterTasksService.Service;

import echos.LetterTasksService.Entity.LetterTasksConfig;
import echos.LetterTasksService.Repository.LetterTasksConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LetterTasksConfigService {

    @Autowired
    private LetterTasksConfigRepository letterTasksConfigRepository;

    public LetterTasksConfigService(LetterTasksConfigRepository letterTasksConfigRepository) {
        this.letterTasksConfigRepository = letterTasksConfigRepository;
    }

    // 通过ID查找信件任务配置
    public Optional<LetterTasksConfig> getLetterTaskConfigById(Long id) {
        return letterTasksConfigRepository.findById(id);
    }

    // 创建信件任务配置
    public LetterTasksConfig createLetterTaskConfig(LetterTasksConfig letterTasksConfig) {return letterTasksConfigRepository.save(letterTasksConfig);}

    // 更新信件任务配置
    public LetterTasksConfig updateLetterTaskConfig(LetterTasksConfig letterTasksConfig) {
        return letterTasksConfigRepository.save(letterTasksConfig);
    }

    // 删除信件任务配置
    public void deleteLetterTaskConfig(Long id) {
        letterTasksConfigRepository.deleteById(id);
    }
}
