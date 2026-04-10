package echos.AIModelService.Repository;


import echos.AIModelService.Entity.AiModelConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * AI模型配置仓库类
 */
@Repository
public interface AiModelConfigRepository extends JpaRepository<AiModelConfig, Long> {

    /**
     * 根据模型名称查找配置
     * @param modelName 模型名称
     * @return AI模型配置
     */
    AiModelConfig findByModelName(String modelName);

    @Query("SELECT a FROM AiModelConfig a WHERE a.id = :id")
    AiModelConfig getByCustomId(@Param("id") Long id);


    /**
     * 根据API地址查找配置
     * @param apiAddress API地址
     * @return AI模型配置
     */
    AiModelConfig findByApiAddress(String apiAddress);
}
