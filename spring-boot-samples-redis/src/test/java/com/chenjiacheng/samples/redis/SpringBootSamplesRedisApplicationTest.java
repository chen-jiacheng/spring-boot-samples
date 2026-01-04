package com.chenjiacheng.samples.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
/**
 * SpringBootSamplesRedisApplicationTest
 * 
 * @author chenjiacheng
 * @since 2026/1/5 00:30
 */
@SpringBootTest
public class SpringBootSamplesRedisApplicationTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void contextLoadTest() {
        assertNotNull(ctx);
    }
}