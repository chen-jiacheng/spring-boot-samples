package com.chenjiacheng.samples.redis.controller;

import com.chenjiacheng.samples.redis.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("/process")
    public String processBusinessLogic(@RequestParam String lockKey) {
        businessService.processBusinessLogic(lockKey);
        return "业务逻辑处理完成";
    }
}