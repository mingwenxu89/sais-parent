package cn.iocoder.yudao.module.system.framework.captcha.config;

import cn.iocoder.yudao.module.system.framework.captcha.core.RedisCaptchaServiceImpl;
import com.anji.captcha.config.AjCaptchaAutoConfiguration;
import com.anji.captcha.properties.AjCaptchaProperties;
import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Captcha configuration class
 *
 * @author Yudao Source Code
 */
@Configuration(proxyBeanMethods = false)
@ImportAutoConfiguration(AjCaptchaAutoConfiguration.class) // Purpose: To solve the problem that the automatic configuration of aj-captcha for SpringBoot 3.X does not take effect
public class YudaoCaptchaConfiguration {

    @Bean(name = "AjCaptchaCacheService")
    @Primary
    public CaptchaCacheService captchaCacheService(AjCaptchaProperties config,
                                                   StringRedisTemplate stringRedisTemplate) {
        CaptchaCacheService captchaCacheService = CaptchaServiceFactory.getCache(config.getCacheType().name());
        if (captchaCacheService instanceof RedisCaptchaServiceImpl) {
            ((RedisCaptchaServiceImpl) captchaCacheService).setStringRedisTemplate(stringRedisTemplate);
        }
        return captchaCacheService;
    }

}
