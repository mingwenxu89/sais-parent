package cn.iocoder.yudao.framework.signature.core.aop;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.signature.core.annotation.ApiSignature;
import cn.iocoder.yudao.framework.signature.core.redis.ApiSignatureRedisDAO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;

/**
 * Intercept methods declared with {@link APISignature} annotation to implement signatures
 *
 * @author Zhougang
 */
@Aspect
@Slf4j
@AllArgsConstructor
public class ApiSignatureAspect {

 private final ApiSignatureRedisDAO signatureRedisDAO;

 @Before("@annotation(signature)")
 public void beforePointCut(JoinPoint joinPoint, ApiSignature signature) {
        // 1. Verification passes and ends directly
 if (verifySignature(signature, Objects.requireNonNull(ServletUtils.getRequest()))) {
 return;
 }

        // 2. The verification fails and an exception is thrown.
        log.error("[beforePointCut][Method {} parameter ({}) signature failed]", joinPoint.getSignature().toString(),
 joinPoint.getArgs());
 throw new ServiceException(BAD_REQUEST.getCode(),
 StrUtil.blankToDefault(signature.message(), BAD_REQUEST.getMsg()));
 }

 public boolean verifySignature(ApiSignature signature, HttpServletRequest request) {
        // 1.1 Verification Header
 if (!verifyHeaders(signature, request)) {
 return false;
 }
        // 1.2 Verify whether the appID can obtain the corresponding appSecret
 String appId = request.getHeader(signature.appId());
 String appSecret = signatureRedisDAO.getAppSecret(appId);
        Assert.notNull(appSecret, "[appId({})] The corresponding appSecret cannot be found", appId);

        // 2. Verify signature [Important! 】
        String clientSignature = request.getHeader(signature.sign()); // client signature
        String serverSignatureString = buildSignatureString(signature, request, appSecret); // Server signature string
        String serverSignature = DigestUtil.sha256Hex(serverSignatureString); // Server signature
 if (ObjUtil.notEqual(clientSignature, serverSignature)) {
 return false;
 }

        // 3. Record the nonce in the cache to prevent reuse (Key point 2: Here you need to set ttl to a value x 2 that allows timestamp time difference)
 String nonce = request.getHeader(signature.nonce());
 if (BooleanUtil.isFalse(signatureRedisDAO.setNonce(appId, nonce, signature.timeout() * 2, signature.timeUnit()))) {
 String timestamp = request.getHeader(signature.timestamp());
            log.info("[verifySignature][appId({}) timestamp({}) nonce({}) sign({}) There is a duplicate request]", appId, timestamp, nonce, clientSignature);
            throw new ServiceException(GlobalErrorCodeConstants.REPEATED_REQUESTS.getCode(), "There are duplicate requests");
 }
 return true;
 }

 /**
     * Verify request header signature parameters
 * <p>
     * 1. Is appID empty?
     * 2. Whether timestamp is empty and whether the request has timed out. The default is 10 minutes.
     * 3. Whether the nonce is empty, whether the random number is more than 10 digits, and whether it has been accessed within the specified time.
     * 4. Is sign empty?
 *
 * @param signature signature
 * @param request request
     * @return Whether to verify that the Header passes
 */
 private boolean verifyHeaders(ApiSignature signature, HttpServletRequest request) {
        // 1. Non-empty verification
 String appId = request.getHeader(signature.appId());
 if (StrUtil.isBlank(appId)) {
 return false;
 }
 String timestamp = request.getHeader(signature.timestamp());
 if (StrUtil.isBlank(timestamp)) {
 return false;
 }
 String nonce = request.getHeader(signature.nonce());
 if (StrUtil.length(nonce) < 10) {
 return false;
 }
 String sign = request.getHeader(signature.sign());
 if (StrUtil.isBlank(sign)) {
 return false;
 }

        // 2. Check whether timestamp exceeds the allowed range (Key point 1: the absolute value needs to be taken here)
 long expireTime = signature.timeUnit().toMillis(signature.timeout());
 long requestTimestamp = Long.parseLong(timestamp);
 long timestampDisparity = Math.abs(System.currentTimeMillis() - requestTimestamp);
 if (timestampDisparity > expireTime) {
 return false;
 }

        // 3. Check whether the nonce exists. It exists and can only be used once.
 return signatureRedisDAO.getNonce(appId, nonce) == null;
 }

 /**
     * Build signature string
 * <p>
     * The format is = request parameters + request body + request header + key
 *
 * @param signature signature
 * @param request request
 * @param appSecret appSecret
     * @return Signature string
 */
 private String buildSignatureString(ApiSignature signature, HttpServletRequest request, String appSecret) {
        SortedMap<String, String> parameterMap = getRequestParameterMap(request); // Request header
        SortedMap<String, String> headerMap = getRequestHeaderMap(signature, request); // Request parameters
        String requestBody = StrUtil.nullToDefault(ServletUtils.getBody(request), ""); // Request body
 return MapUtil.join(parameterMap, "&", "=")
 + requestBody
 + MapUtil.join(headerMap, "&", "=")
 + appSecret;
 }

 /**
     * Get the request header signature parameter Map
 *
     * @param request ask
     * @param signature Signature annotation
 * @return signature params
 */
 private static SortedMap<String, String> getRequestHeaderMap(ApiSignature signature, HttpServletRequest request) {
 SortedMap<String, String> sortedMap = new TreeMap<>();
 sortedMap.put(signature.appId(), request.getHeader(signature.appId()));
 sortedMap.put(signature.timestamp(), request.getHeader(signature.timestamp()));
 sortedMap.put(signature.nonce(), request.getHeader(signature.nonce()));
 return sortedMap;
 }

 /**
     * Get request parameters Map
 *
     * @param request ask
 * @return queryParams
 */
 private static SortedMap<String, String> getRequestParameterMap(HttpServletRequest request) {
 SortedMap<String, String> sortedMap = new TreeMap<>();
 for (Map.Entry<String, String[]> entry: request.getParameterMap().entrySet()) {
 sortedMap.put(entry.getKey(), entry.getValue()[0]);
 }
 return sortedMap;
 }

}