package com.chenjiacheng.samples.redis.handler;

import com.chenjiacheng.samples.redis.SpringBootSamplesRedisApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * StringRedisTemplateHandlerTest
 *
 * @author chenjiacheng
 * @since 2026/1/5 00:32
 */
@Slf4j
public class HashRedisTemplateHandlerTest extends SpringBootSamplesRedisApplicationTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String REDIS_KEY = "test:hash:redis:template";

    @Test
    public void put() {
        String prefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        stringRedisTemplate.opsForHash().put(REDIS_KEY,"chenjiacheng-"+prefix,"He's chenjiacheng.1");
        stringRedisTemplate.opsForHash().put(REDIS_KEY,"chenjiacheng-"+prefix,"He's chenjiacheng.2");
    }

    @Test
    public void putAll() {
        List<String> keys = Arrays.asList("ORIGIN_KEY_1", "ORIGIN_KEY_2", "ORIGIN_KEY_3");
        for (String key : keys) {
            stringRedisTemplate.opsForHash().put(REDIS_KEY, key, "origin value-" + key);
        }

        Map<String,String> map = new HashMap<>();
        String key0 = keys.get(0);
        map.put(key0, "modify value-" + key0);

        stringRedisTemplate.opsForHash().putAll(REDIS_KEY, map);
    }

    @Test
    public void get() {
        Object data = stringRedisTemplate.opsForHash().get(REDIS_KEY, "chenjiacheng");
        log.info("data: {}", data);
    }

    @Test
    public void delete() {
        Long deleted = stringRedisTemplate.opsForHash().delete(REDIS_KEY, "chenjiacheng-20260105004720");
        log.info("deleted: {}", deleted);
    }


}