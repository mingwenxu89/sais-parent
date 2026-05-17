package cn.iocoder.yudao.framework.lock4j.core;

/**
 * Lock4j Redis Key enumeration class
 *
 * @author Yudao Source Code
 */
public interface Lock4jRedisKeyConstants {

    /**
     * Distributed lock
     *
     * KEY format: lock4j:%s // Parameters come from DefaultLockKeyBuilder class
     * VALUE data format: HASH // RLock.class: Redisson's Lock lock, using Hash data structure
     * Expiration time: not fixed
     */
    String LOCK4J = "lock4j:%s";

}
