package cn.iocoder.yudao.framework.tenant.config;

import cn.iocoder.yudao.framework.common.biz.system.tenant.TenantCommonApi;
import cn.iocoder.yudao.framework.common.enums.WebFilterOrderEnum;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.framework.redis.config.YudaoCacheProperties;
import cn.iocoder.yudao.framework.security.core.service.SecurityFrameworkService;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnoreAspect;
import cn.iocoder.yudao.framework.tenant.core.db.TenantDatabaseInterceptor;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJobAspect;
import cn.iocoder.yudao.framework.tenant.core.mq.rabbitmq.TenantRabbitMQInitializer;
import cn.iocoder.yudao.framework.tenant.core.mq.redis.TenantRedisMessageInterceptor;
import cn.iocoder.yudao.framework.tenant.core.mq.rocketmq.TenantRocketMQInitializer;
import cn.iocoder.yudao.framework.tenant.core.redis.TenantRedisCacheManager;
import cn.iocoder.yudao.framework.tenant.core.security.TenantSecurityWebFilter;
import cn.iocoder.yudao.framework.tenant.core.service.TenantFrameworkService;
import cn.iocoder.yudao.framework.tenant.core.service.TenantFrameworkServiceImpl;
import cn.iocoder.yudao.framework.tenant.core.web.TenantContextWebFilter;
import cn.iocoder.yudao.framework.tenant.core.web.TenantVisitContextInterceptor;
import cn.iocoder.yudao.framework.web.config.WebProperties;
import cn.iocoder.yudao.framework.web.core.handler.GlobalExceptionHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

@AutoConfiguration
@ConditionalOnProperty(prefix = "yudao.tenant", value = "enable", matchIfMissing = true) // Allow multi-tenancy to be disabled using yudao.tenant.enable=false
@EnableConfigurationProperties(TenantProperties.class)
public class YudaoTenantAutoConfiguration {

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    public TenantFrameworkService tenantFrameworkService(TenantCommonApi tenantApi) {
        return new TenantFrameworkServiceImpl(tenantApi);
    }

    // ========== AOP ==========

    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect() {
        return new TenantIgnoreAspect();
    }

    // ========== DB ==========

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties properties,
                                                                 MybatisPlusInterceptor interceptor) {
        TenantLineInnerInterceptor inner = new TenantLineInnerInterceptor(new TenantDatabaseInterceptor(properties));
        // Add to interceptor
        // It needs to be added first, mainly in front of the paging plug-in. This is the rule of MyBatis Plus
        MyBatisUtils.addInterceptor(interceptor, inner, 0);
        return inner;
    }

    // ========== WEB ==========

    @Bean
    public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
        FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantContextWebFilter());
        registrationBean.setOrder(WebFilterOrderEnum.TENANT_CONTEXT_FILTER);
        return registrationBean;
    }

    @Bean
    public TenantVisitContextInterceptor tenantVisitContextInterceptor(TenantProperties tenantProperties,
                                                                       SecurityFrameworkService securityFrameworkService) {
        return new TenantVisitContextInterceptor(tenantProperties, securityFrameworkService);
    }

    @Bean
    public WebMvcConfigurer tenantWebMvcConfigurer(TenantProperties tenantProperties,
                                                   TenantVisitContextInterceptor tenantVisitContextInterceptor) {
        return new WebMvcConfigurer() {

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(tenantVisitContextInterceptor)
                        .excludePathPatterns(tenantProperties.getIgnoreVisitUrls().toArray(new String[0]));
            }
        };
    }

    // ========== Security ==========

    @Bean
    public FilterRegistrationBean<TenantSecurityWebFilter> tenantSecurityWebFilter(TenantProperties tenantProperties,
                                                                                   WebProperties webProperties,
                                                                                   GlobalExceptionHandler globalExceptionHandler,
                                                                                   TenantFrameworkService tenantFrameworkService) {
        FilterRegistrationBean<TenantSecurityWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantSecurityWebFilter(webProperties, tenantProperties, getTenantIgnoreUrls(),
                globalExceptionHandler, tenantFrameworkService));
        registrationBean.setOrder(WebFilterOrderEnum.TENANT_SECURITY_FILTER);
        return registrationBean;
    }

    /**
     * If there is {@link TenantIgnore} annotation on the Controller API, it will be added to the URL collection of ignored tenants.
     *
     * @return Ignore tenant's URL collection
     */
    private Set<String> getTenantIgnoreUrls() {
        Set<String> ignoreUrls = new HashSet<>();
        // Get the HandlerMethod collection corresponding to the API
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // Get the API annotated with @TenantIgnore
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(TenantIgnore.class) // method level
                && !handlerMethod.getBeanType().isAnnotationPresent(TenantIgnore.class)) { // API level
                continue;
            }
            // Add to ignored URLs
            if (entry.getKey().getPatternsCondition() != null) {
                ignoreUrls.addAll(entry.getKey().getPatternsCondition().getPatterns());
            }
            if (entry.getKey().getPathPatternsCondition() != null) {
                ignoreUrls.addAll(
                        convertList(entry.getKey().getPathPatternsCondition().getPatterns(), PathPattern::getPatternString));
            }
        }
        return ignoreUrls;
    }

    // ========== MQ ==========

    @Bean
    public TenantRedisMessageInterceptor tenantRedisMessageInterceptor() {
        return new TenantRedisMessageInterceptor();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.amqp.rabbit.core.RabbitTemplate")
    public TenantRabbitMQInitializer tenantRabbitMQInitializer() {
        return new TenantRabbitMQInitializer();
    }

    @Bean
    @ConditionalOnClass(name = "org.apache.rocketmq.spring.core.RocketMQTemplate")
    public TenantRocketMQInitializer tenantRocketMQInitializer() {
        return new TenantRocketMQInitializer();
    }

    // ========== Job ==========

    @Bean
    public TenantJobAspect tenantJobAspect(TenantFrameworkService tenantFrameworkService) {
        return new TenantJobAspect(tenantFrameworkService);
    }

    // ========== Redis ==========

    @Bean
    @Primary // When introducing a tenant, tenantRedisCacheManager is the main Bean
    public RedisCacheManager tenantRedisCacheManager(RedisTemplate<String, Object> redisTemplate,
                                                     RedisCacheConfiguration redisCacheConfiguration,
                                                     YudaoCacheProperties yudaoCacheProperties,
                                                     TenantProperties tenantProperties) {
        // Create RedisCacheWriter object
        RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory,
                BatchStrategies.scan(yudaoCacheProperties.getRedisScanBatchSize()));
        // Create TenantRedisCacheManager object
        return new TenantRedisCacheManager(cacheWriter, redisCacheConfiguration, tenantProperties.getIgnoreCaches());
    }

}
