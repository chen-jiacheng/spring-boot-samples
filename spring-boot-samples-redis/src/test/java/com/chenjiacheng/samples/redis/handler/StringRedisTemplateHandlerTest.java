package com.chenjiacheng.samples.redis.handler;

import com.chenjiacheng.samples.redis.SpringBootSamplesRedisApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StringRedisTemplateHandlerTest
 *
 * @author chenjiacheng
 * @since 2026/1/5 00:32
 */
@Slf4j
public class StringRedisTemplateHandlerTest extends SpringBootSamplesRedisApplicationTest {

    @Autowired
    private StringRedisTemplateHandler stringRedisTemplateHandler;

    private String REDIS_KEY = "test:string:redis:template";

    @Test
    public void set() {
        stringRedisTemplateHandler.set(REDIS_KEY, "hello world");
    }

    @Test
    public void get() {
        String data = stringRedisTemplateHandler.get(REDIS_KEY);
        log.info("data: {}", data);
    }

    @Test
    public void delete() {
        stringRedisTemplateHandler.delete(REDIS_KEY);

    }

    @Test
    public void setExpire() {
        stringRedisTemplateHandler.setExpire(REDIS_KEY, "hello world", 10, TimeUnit.SECONDS);
    }
}