package com.chenjiacheng.samples.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductTopicProducerService {

    @Autowired
    private KafkaTemplate<String, String> productKafkaTemplate;

    public void sendMessage(String message) {
        log.info("Product message sent: {}", message);
        productKafkaTemplate.sendDefault(message);
    }
}