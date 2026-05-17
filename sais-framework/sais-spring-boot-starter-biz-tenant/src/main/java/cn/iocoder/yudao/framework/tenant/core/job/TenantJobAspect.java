package cn.iocoder.yudao.framework.tenant.core.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.tenant.core.service.TenantFrameworkService;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multi-tenant JobHandler AOP
 * When the task is executed, the logic of the Job will be executed one by one according to the tenant.
 *
 * Note that the idempotence of JobHandler needs to be ensured. Because when a job fails to execute due to a tenant and is retried, the tenant that was successfully executed before will also be executed again.
 *
 * @author Yudao Source Code
 */
@Aspect
@RequiredArgsConstructor
@Slf4j
public class TenantJobAspect {

 private final TenantFrameworkService tenantFrameworkService;

 @Around("@annotation(tenantJob)")
 public String around(ProceedingJoinPoint joinPoint, TenantJob tenantJob) {
        // Get tenant list
 List<Long> tenantIds = tenantFrameworkService.getTenantIds();
 if (CollUtil.isEmpty(tenantIds)) {
 return null;
 }

        // Execute Job one by one tenant
 Map<Long, String> results = new ConcurrentHashMap<>();
 tenantIds.parallelStream().forEach(tenantId -> {
            // TODO Taro: First implement parallelism through parallel; 1) Multiple tenants, one execution log; 2) Abnormal situations
 TenantUtils.execute(tenantId, () -> {
 try {
 Object result = joinPoint.proceed();
 results.put(tenantId, StrUtil.toStringOrEmpty(result));
 } catch (Throwable e) {
                    log.error("[execute][Tenant({}) An exception occurred while executing Job", tenantId, e);
 results.put(tenantId, ExceptionUtil.getRootCauseMessage(e));
 }
 });
 });
 return JsonUtils.toJsonString(results);
 }

}
