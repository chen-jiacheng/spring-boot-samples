package com.chenjiacheng.samples.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderTopicProducerService {

    @Autowired
    private KafkaTemplate<String, String> orderKafkaTemplate;

    public void sendMessage(String message) {
        orderKafkaTemplate.send("order", message);
    }
}