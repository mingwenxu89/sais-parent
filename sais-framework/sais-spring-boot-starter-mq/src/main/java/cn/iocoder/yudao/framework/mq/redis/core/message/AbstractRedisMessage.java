package cn.iocoder.yudao.framework.mq.redis.core.message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis message abstract base class
 *
 * @author Yudao Source Code
 */
@Data
public abstract class AbstractRedisMessage {

 /**
     * head
 */
 private Map<String, String> headers = new HashMap<>();

 public String getHeader(String key) {
 return headers.get(key);
 }

 public void addHeader(String key, String value) {
 headers.put(key, value);
 }

}
