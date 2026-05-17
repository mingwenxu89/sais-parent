package cn.iocoder.yudao.framework.tracer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * BizTracer configuration class
 *
 * @author Mashu
 */
@ConfigurationProperties("yudao.tracer")
@Data
public class TracerProperties {
}
