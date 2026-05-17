package cn.iocoder.yudao.framework.idempotent.core.keyresolver.impl;

import cn.hutool.crypto.SecureUtil;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import cn.iocoder.yudao.framework.idempotent.core.annotation.Idempotent;
import cn.iocoder.yudao.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import org.aspectj.lang.JoinPoint;

/**
 * The default (global level) idempotent Key parser uses method name + method parameters to assemble a Key
 *
 * In order to avoID the Key being too long, use MD5 for "compression"
 *
 * @author Yudao Source Code
 */
public class DefaultIdempotentKeyResolver implements IdempotentKeyResolver {

 @Override
 public String resolver(JoinPoint joinPoint, Idempotent idempotent) {
 String methodName = joinPoint.getSignature().toString();
 String argsStr = StrUtils.joinMethodArgs(joinPoint);
 return SecureUtil.md5(methodName + argsStr);
 }

}
