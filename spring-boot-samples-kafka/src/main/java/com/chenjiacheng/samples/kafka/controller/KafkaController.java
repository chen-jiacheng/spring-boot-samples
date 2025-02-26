package com.chenjiacheng.samples.kafka.controller;

import com.chenjiacheng.samples.kafka.service.UserTopicProducerService;
import com.chenjiacheng.samples.kafka.service.OrderTopicProducerService;
import com.chenjiacheng.samples.kafka.service.ProductTopicProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private UserTopicProducerService userTopicProducerService;

    @Autowired
    private OrderTopicProducerService orderTopicProducerService;

    @Autowired
    private ProductTopicProducerService productTopicProducerService;

    @GetMapping("/send/user")
    public String sendUserMessage(@RequestParam("message") String message) {
        userTopicProducerService.sendMessage(message);
        return "User message sent: " + message;
    }

    @GetMapping("/send/order")
    public String sendOrderMessage(@RequestParam("message") String message) {
        orderTopicProducerService.sendMessage(message);
        return "Order message sent: " + message;
    }

    @GetMapping("/send/product")
    public String sendProductMessage(@RequestParam("message") String message) {
        productTopicProducerService.sendMessage(message);
        return "Product message sent: " + message;
    }
}