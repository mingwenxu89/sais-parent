package cn.iocoder.yudao.framework.tenant.core.aop;

import cn.iocoder.yudao.framework.common.util.spring.SpringExpressionUtils;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Aspects that ignore multi-tenancy are implemented based on the {@link TenantIgnore} annotation and are used for some global logic.
 * For example, a scheduled task reads all data and processes it.
 * For another example, read all data and cache it.
 *
 * The implementation of the overall logic needs to be consistent with {@link TenantUtils#executeIgnore(Runnable)}
 *
 * @author Yudao Source Code
 */
@Aspect
@Slf4j
public class TenantIgnoreAspect {

 @Around("@annotation(tenantIgnore)")
 public Object around(ProceedingJoinPoint joinPoint, TenantIgnore tenantIgnore) throws Throwable {
 Boolean oldIgnore = TenantContextHolder.isIgnore();
 try {
            // Calculation conditions will be ignored only if they are met.
 Object enable = SpringExpressionUtils.parseExpression(tenantIgnore.enable());
 if (Boolean.TRUE.equals(enable)) {
 TenantContextHolder.setIgnore(true);
 }

            // execution logic
 return joinPoint.proceed();
 } finally {
 TenantContextHolder.setIgnore(oldIgnore);
 }
 }

}
