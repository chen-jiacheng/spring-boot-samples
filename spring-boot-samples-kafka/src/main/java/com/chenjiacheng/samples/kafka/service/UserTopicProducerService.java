package com.chenjiacheng.samples.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserTopicProducerService {

    @Autowired
    private KafkaTemplate<String, String> userKafkaTemplate;

    public void sendMessage(String message) {
        userKafkaTemplate.send("user", message);
    }
}