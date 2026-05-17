package cn.iocoder.yudao.framework.common.util.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;
import java.util.concurrent.Executors;

/**
 * Cache tool class
 *
 * @author Yudao Source Code
 */
public class CacheUtils {

 /**
     * Maximum number of LoadingCache caches refreshed asynchronously
 *
     * @see <a href="">Local cache CacheUtils tool class suggestions</a>
 */
 private static final Integer CACHE_MAX_SIZE = 10000;

 /**
     * Construct a LoadingCache object that refreshes asynchronously
 *
     * Note: If your cache is related to ThreadLocal, either handle the passing of ThreadLocal yourself, or use the {@link #buildCache(Duration, CacheLoader)} method
 *
     * Or simply understand:
     * 1. For things related to "people", use the {@link #buildCache(Duration, CacheLoader)} method
     * 2. For things related to "global" and "system", use the current cache method.
 *
     * @param duration Expiration time
     * @param loader CacheLoader object
     * @return LoadingCache object
 */
 public static <K, V> LoadingCache<K, V> buildAsyncReloadingCache(Duration duration, CacheLoader<K, V> loader) {
 return CacheBuilder.newBuilder()
.maximumSize(CACHE_MAX_SIZE)
                // Only blocks the current data loading thread, other threads return the old value
.refreshAfterWrite(duration)
                // Fully asynchronous loading is achieved through asyncReloading, including loading threads blocked by refreshAfterWrite
                .build(CacheLoader.asyncReloading(loader, Executors.newCachedThreadPool())); // TODO Taro: You may want to think about whether to make it configurable in the future.
 }

 /**
     * Construct a LoadingCache object that is refreshed synchronously
 *
     * @param duration Expiration time
     * @param loader CacheLoader object
     * @return LoadingCache object
 */
 public static <K, V> LoadingCache<K, V> buildCache(Duration duration, CacheLoader<K, V> loader) {
 return CacheBuilder.newBuilder()
.maximumSize(CACHE_MAX_SIZE)
                // Only blocks the current data loading thread, other threads return the old value
.refreshAfterWrite(duration)
.build(loader);
 }

}
