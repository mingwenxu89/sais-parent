package cn.iocoder.yudao.framework.tenant.core.service;

import cn.iocoder.yudao.framework.common.biz.system.tenant.TenantCommonApi;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.cache.CacheUtils;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.List;

/**
 * Tenant framework Service implementation class
 *
 * @author Yudao Source Code
 */
@RequiredArgsConstructor
public class TenantFrameworkServiceImpl implements TenantFrameworkService {

 private static final ServiceException SERVICE_EXCEPTION_NULL = new ServiceException();

 private final TenantCommonApi tenantApi;

 /**
     * Caching for {@link #getTenantIds()}
 */
 private final LoadingCache<Object, List<Long>> getTenantIdsCache = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // Expiration time 1 minute
 new CacheLoader<Object, List<Long>>() {

 @Override
 public List<Long> load(Object key) {
 return tenantApi.getTenantIdList();
 }

 });

 /**
     * Caching for {@link #validTenant(Long)}
 */
 private final LoadingCache<Long, ServiceException> validTenantCache = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // Expiration time 1 minute
 new CacheLoader<Long, ServiceException>() {

 @Override
 public ServiceException load(Long id) {
 try {
 tenantApi.validateTenant(id);
 return SERVICE_EXCEPTION_NULL;
 } catch (ServiceException ex) {
 return ex;
 }
 }

 });

 @Override
 @SneakyThrows
 public List<Long> getTenantIds() {
 return getTenantIdsCache.get(Boolean.TRUE);
 }

 @Override
 public void validTenant(Long id) {
 ServiceException serviceException = validTenantCache.getUnchecked(id);
 if (serviceException != SERVICE_EXCEPTION_NULL) {
 throw serviceException;
 }
 }

}
