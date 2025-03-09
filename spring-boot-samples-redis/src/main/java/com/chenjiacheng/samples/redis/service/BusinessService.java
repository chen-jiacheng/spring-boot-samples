package com.chenjiacheng.samples.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class BusinessService {

    @Autowired
    private RedisLockService redisLockService;

    public void processBusinessLogic(String lockKey) {
        String requestId = UUID.randomUUID().toString();
        boolean locked = false;
        try {
            // 尝试获取锁，超时时间设为30秒
            locked = redisLockService.acquireLock(lockKey, requestId, 30000);
            if (locked) {
                // 执行业务逻辑
                System.out.println("执行业务逻辑...");
                // 模拟业务处理时间
                TimeUnit.SECONDS.sleep(3);
            } else {
                // 获取锁失败
                System.out.println("获取锁失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            if (locked) {
                redisLockService.releaseLock(lockKey, requestId);
                System.out.println("释放锁");
            }
        }
    }
}