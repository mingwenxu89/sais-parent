package cn.iocoder.yudao.framework.lock4j.core;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.baomidou.lock.LockFailureStrategy;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Customize the lock acquisition failure strategy and throw {@link ServiceException} exception
 */
@Slf4j
public class DefaultLockFailureStrategy implements LockFailureStrategy {

    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        log.debug("[onLockFailure][Thread:{} failed to acquire lock, key:{} failed to acquire lock:{}]", Thread.currentThread().getName(), key, arguments);
        throw new ServiceException(GlobalErrorCodeConstants.LOCKED);
    }
}
