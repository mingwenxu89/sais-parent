package cn.iocoder.yudao.framework.idempotent.core.keyresolver.impl;

import cn.hutool.crypto.SecureUtil;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import cn.iocoder.yudao.framework.idempotent.core.annotation.Idempotent;
import cn.iocoder.yudao.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import org.aspectj.lang.JoinPoint;

/**
 * User-level idempotent Key parser, using method name + method parameters + userID + userType, assembled into a Key
 *
 * In order to avoID the Key being too long, use MD5 for "compression"
 *
 * @author Yudao Source Code
 */
public class UserIdempotentKeyResolver implements IdempotentKeyResolver {

 @Override
 public String resolver(JoinPoint joinPoint, Idempotent idempotent) {
 String methodName = joinPoint.getSignature().toString();
 String argsStr = StrUtils.joinMethodArgs(joinPoint);
 Long userId = WebFrameworkUtils.getLoginUserId();
 Integer userType = WebFrameworkUtils.getLoginUserType();
 return SecureUtil.md5(methodName + argsStr + userId + userType);
 }

}
