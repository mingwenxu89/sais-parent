package cn.iocoder.yudao.framework.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Cache configuration items
 *
 * @author Wanwan
 */
@ConfigurationProperties("yudao.cache")
@Data
@Validated
public class YudaoCacheProperties {

 /**
     * {@link #RedisScanBatchSize} default value
 */
 private static final Integer REDIS_SCAN_BATCH_SIZE_DEFAULT = 30;

 /**
     * Redis scan returns the number at one time
 */
 private Integer redisScanBatchSize = REDIS_SCAN_BATCH_SIZE_DEFAULT;

}
