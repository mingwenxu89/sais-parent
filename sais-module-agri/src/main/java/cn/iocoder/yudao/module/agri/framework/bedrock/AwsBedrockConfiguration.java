package cn.iocoder.yudao.module.agri.framework.bedrock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

import jakarta.annotation.Resource;
import java.time.Duration;

/**
 * Creates the BedrockRuntimeClient bean when AWS credentials are configured.
 * Mirrors the @ConditionalOnProperty guard used by AwsIotConfiguration.
 */
@Configuration
@ConditionalOnProperty(prefix = "yudao.agri.bedrock", name = "access-key-id")
@Slf4j
public class AwsBedrockConfiguration {

    @Resource
    private AwsBedrockProperties properties;

    @Bean(destroyMethod = "close")
    public BedrockRuntimeClient bedrockRuntimeClient() {
        log.info("[AwsBedrock] Creating BedrockRuntimeClient: region={}, model={}",
                properties.getRegion(), properties.getModelId());
        return BedrockRuntimeClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                properties.getAccessKeyId(),
                                properties.getSecretAccessKey())))
                .region(Region.of(properties.getRegion()))
                .overrideConfiguration(c -> c.apiCallTimeout(Duration.ofMillis(properties.getTimeoutMs())))
                .build();
    }

}
