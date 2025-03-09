package com.chenjiacheng.samples.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取锁
     *
     * @param lockKey   锁的键
     * @param requestId 唯一标识请求的ID
     * @param expireMs  锁的过期时间（毫秒）
     * @return 获取锁成功返回true，失败返回false
     */
    public boolean acquireLock(String lockKey, String requestId, long expireMs) {
        // 尝试获取锁
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId);
        if (result != null && result) {
            // 如果获取锁成功，设置过期时间
            redisTemplate.expire(lockKey, expireMs, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey   锁的键
     * @param requestId 唯一标识请求的ID
     * @return 释放锁成功返回true，失败返回false
     */
    public boolean releaseLock(String lockKey, String requestId) {
        // 使用 Lua 脚本确保删除操作的原子性
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = redisTemplate.execute(
                (RedisCallback<Object>) connection -> {
                    DefaultRedisScript<Object> redisScript = new DefaultRedisScript<>();
                    redisScript.setScriptText(script);
                    redisScript.setResultType(Object.class);
                    return connection.eval(redisScript.getScriptAsString().getBytes(), ReturnType.INTEGER, 1, lockKey.getBytes(), requestId.getBytes());
                }
        );
        return "1".equals(result.toString());
    }
}