package com.chenjiacheng.samples.redis.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * StringRedisTemplateHandler
 *
 * @author chenjiacheng
 * @since 2026/1/5 00:28
 */
@Component
public class StringRedisTemplateHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置值
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除值
     *
     * @param key 键
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 设置值并设置过期时间
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param unit     时间单位
     */
    public void setExpire(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }
}
