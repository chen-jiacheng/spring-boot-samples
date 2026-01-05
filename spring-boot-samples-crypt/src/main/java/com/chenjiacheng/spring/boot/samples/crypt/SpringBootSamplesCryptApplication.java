package com.chenjiacheng.spring.boot.samples.crypt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by chenjiacheng on 2025/2/27 01:15
 *
 * @author chenjiacheng
 * @since 1.0.0
 */
@Slf4j
@SpringBootApplication
public class SpringBootSamplesCryptApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSamplesCryptApplication.class, args);
        log.info("SpringBootSamplesRedisApplication start success");
    }
}
