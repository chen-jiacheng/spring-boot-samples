package com.chenjiacheng.samples.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // String 操作
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Hash 操作
    public void putHash(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object getHash(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    // List 操作
    public void leftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    // Set 操作
    public void addSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // SortedSet 操作
    public void addSortedSet(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public Set<Object> rangeSortedSet(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}