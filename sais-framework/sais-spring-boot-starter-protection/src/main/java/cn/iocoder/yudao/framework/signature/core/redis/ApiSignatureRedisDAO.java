package cn.iocoder.yudao.framework.signature.core.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * HTTP API Signature Redis DAO
 *
 * @author Zhougang
 */
@AllArgsConstructor
public class ApiSignatureRedisDAO {

 private final StringRedisTemplate stringRedisTemplate;

 /**
     * Verification random number
 * <p>
     * KEY format: signature_nonce:%s // The parameter is a random number
     * VALUE format: String
     * Expiration time: not fixed
 */
 private static final String SIGNATURE_NONCE = "api_signature_nonce:%s:%s";

 /**
     * Signing key
 * <p>
     * HASH structure
     * KEY format: %s //The parameter is appid
     * VALUE format: String
     * Expiration time: never expires (preloaded to Redis)
 */
 private static final String SIGNATURE_APPID = "api_signature_app";

    // ========== Random number for signature verification ==========

 public String getNonce(String appId, String nonce) {
 return stringRedisTemplate.opsForValue().get(formatNonceKey(appId, nonce));
 }

 public Boolean setNonce(String appId, String nonce, int time, TimeUnit timeUnit) {
 return stringRedisTemplate.opsForValue().setIfAbsent(formatNonceKey(appId, nonce), "", time, timeUnit);
 }

 private static String formatNonceKey(String appId, String nonce) {
 return String.format(SIGNATURE_NONCE, appId, nonce);
 }

    // ========== Signing Key ==========

 public String getAppSecret(String appId) {
 return (String) stringRedisTemplate.opsForHash().get(SIGNATURE_APPID, appId);
 }

}
