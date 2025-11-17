package com.chenjiacheng.spring.boot.samples.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by chenjiacheng on 2025/2/27 01:15
 *
 * @author chenjiacheng
 * @since 1.0.0
 */
@Slf4j
@MapperScan("com.chenjiacheng.spring.boot.samples.mybatis.repository.mapper")
@SpringBootApplication
public class SpringBootSamplesMyBatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSamplesMyBatisApplication.class, args);
        log.info("SpringBootSamplesRedisApplication start success");
    }
}
