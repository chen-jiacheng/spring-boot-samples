package com.chenjiacheng.samples.redis.controller;

import com.chenjiacheng.samples.redis.service.RedisService;
import com.chenjiacheng.samples.redis.service.StringRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisService stringRedisService;

    @PostMapping("/set")
    public String set(@RequestParam String key, @RequestParam String value) {
        redisService.set(key, value);
        return "Value set successfully";
    }

    @GetMapping("/get")
    public Object get(@RequestParam String key) {
        return redisService.get(key);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        redisService.delete(key);
        return "Value deleted successfully";
    }

    @PostMapping("/set-string")
    public String setString(@RequestParam String key, @RequestParam String value) {
        stringRedisService.set(key, value);
        return "String value set successfully";
    }

    @GetMapping("/get-string")
    public String getString(@RequestParam String key) {
        return stringRedisService.get(key);
    }

    @DeleteMapping("/delete-string")
    public String deleteString(@RequestParam String key) {
        stringRedisService.delete(key);
        return "String value deleted successfully";
    }
}