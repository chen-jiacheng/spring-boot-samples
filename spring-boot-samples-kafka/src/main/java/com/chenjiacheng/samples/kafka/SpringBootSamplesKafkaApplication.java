package com.chenjiacheng.samples.kafka;

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
// @SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
@SpringBootApplication
public class SpringBootSamplesKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSamplesKafkaApplication.class, args);
        log.info("SpringBootSamplesKafkaApplication start success");
    }
}
