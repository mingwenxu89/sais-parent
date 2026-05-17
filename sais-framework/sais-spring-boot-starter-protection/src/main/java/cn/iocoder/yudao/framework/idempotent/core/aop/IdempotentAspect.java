package cn.iocoder.yudao.framework.idempotent.core.aop;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.idempotent.core.annotation.Idempotent;
import cn.iocoder.yudao.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import cn.iocoder.yudao.framework.idempotent.core.redis.IdempotentRedisDAO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Intercept methods declared with the {@link Idempotent} annotation to implement idempotent operations
 *
 * @author Yudao Source Code
 */
@Aspect
@Slf4j
public class IdempotentAspect {

 /**
     * IdempotentKeyResolver collection
 */
 private final Map<Class<? extends IdempotentKeyResolver>, IdempotentKeyResolver> keyResolvers;

 private final IdempotentRedisDAO idempotentRedisDAO;

 public IdempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
 this.keyResolvers = CollectionUtils.convertMap(keyResolvers, IdempotentKeyResolver::getClass);
 this.idempotentRedisDAO = idempotentRedisDAO;
 }

 @Around(value = "@annotation(idempotent)")
 public Object aroundPointCut(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        // Get IdempotentKeyResolver
 IdempotentKeyResolver keyResolver = keyResolvers.get(idempotent.keyResolver());
        Assert.notNull(keyResolver, "The corresponding IdempotentKeyResolver cannot be found");
        // Parse Key
 String key = keyResolver.resolver(joinPoint, idempotent);

        // 1. Lock Key
 boolean success = idempotentRedisDAO.setIfAbsent(key, idempotent.timeout(), idempotent.timeUnit());
        // Lock failed, throwing exception
 if (!success) {
            log.info("[aroundPointCut][There are duplicate requests for method ({}) parameters ({})]", joinPoint.getSignature().toString(), joinPoint.getArgs());
 throw new ServiceException(GlobalErrorCodeConstants.REPEATED_REQUESTS.getCode(), idempotent.message());
 }

        // 2. Execution logic
 try {
 return joinPoint.proceed();
 } catch (Throwable throwable) {
            // 3. In case of exception, delete Key
            // Refer to Meituan GTIS ideas: https://tech.meituan.com/2016/09/29/distributed-system-mutually-exclusive-idempotence-cerberus-gtis.html
 if (idempotent.deleteKeyWhenException()) {
 idempotentRedisDAO.delete(key);
 }
 throw throwable;
 }
 }

}
