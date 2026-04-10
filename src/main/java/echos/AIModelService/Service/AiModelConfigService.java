package echos.AIModelService.Service;

import echos.AIModelService.Entity.AiModelConfig;
import echos.AIModelService.Repository.AiModelConfigRepository;
import echos.UtilityService.RSADecryptUtil;
import echos.UtilityService.RSAEncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AiModelConfigService {

    @Autowired
    private AiModelConfigRepository repository;



    //创建新的AI模型
    @Transactional
    public AiModelConfig create(AiModelConfig config) {
        if (config.getId() != null) {
            throw new IllegalArgumentException("创建时ID必须为空");
        }
        config.setApiKey(RSAEncryptUtil.encrypt(config.getApiKey()));
        return repository.save(config);
    }


    //更新模型
    @Transactional
    public AiModelConfig update(Long id, AiModelConfig config) {
        AiModelConfig existing = repository.getByCustomId(id);
        if (existing == null) {
            throw new RuntimeException("ID does not exist");
        }
        existing.setModelName(config.getModelName());
        existing.setApiAddress(config.getApiAddress());
        existing.setApiKey(config.getApiKey());
        existing.setParameters(config.getParameters());
        return repository.save(existing);
    }

    //查找所有模型
    @Transactional(readOnly = true)
    public List<AiModelConfig> getAll() {
        return repository.findAll();
    }


    //根据ID查找
    @Transactional(readOnly = true)
    public AiModelConfig getById(Long id) {
        AiModelConfig config = repository.getByCustomId(id);
        if (config == null) {
            return null;
        }

        config.setApiKey(RSADecryptUtil.decrypt(config.getApiKey()));
        return config;
    }

    //根据name查找
    @Transactional
    public AiModelConfig getByModelName(String modelName) {
        AiModelConfig config = repository.findByModelName(modelName);

        if (config == null) {
            return null;
        }
        config.setApiKey(RSADecryptUtil.decrypt(config.getApiKey()));

        return config;
    }

    //根据ID删除
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

