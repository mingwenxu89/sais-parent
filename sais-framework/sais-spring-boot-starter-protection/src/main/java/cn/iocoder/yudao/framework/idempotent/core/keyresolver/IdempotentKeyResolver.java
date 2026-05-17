package cn.iocoder.yudao.framework.idempotent.core.keyresolver;

import cn.iocoder.yudao.framework.idempotent.core.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;

/**
 * Idempotent Key parser interface
 *
 * @author Yudao Source Code
 */
public interface IdempotentKeyResolver {

 /**
     * Parse a Key
 *
     * @param idempotent Idempotent annotation
     * @param joinPoint AOP aspects
 * @return Key
 */
 String resolver(JoinPoint joinPoint, Idempotent idempotent);

}
