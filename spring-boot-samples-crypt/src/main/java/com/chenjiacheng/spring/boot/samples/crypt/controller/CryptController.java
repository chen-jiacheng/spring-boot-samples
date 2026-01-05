package com.chenjiacheng.spring.boot.samples.crypt.controller;

import com.chenjiacheng.spring.boot.samples.crypt.util.SM4Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptController {


    @GetMapping("/encrypt")
    public String encrypt(@RequestParam String data) {
        return SM4Utils.encrypt(data);
    }

    @GetMapping("/decrypt")
    public String decrypt(@RequestParam String data) {
        return SM4Utils.decrypt(data);
    }
}