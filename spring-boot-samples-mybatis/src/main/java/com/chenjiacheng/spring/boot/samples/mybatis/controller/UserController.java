package com.chenjiacheng.spring.boot.samples.mybatis.controller;

import com.alibaba.fastjson.JSON;
import com.chenjiacheng.spring.boot.samples.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenjiacheng on 2025/11/17 23:42
 *
 * @author chenjiacheng
 * @since 1.0.0
 */
@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/findUserById",method = RequestMethod.GET)
    public String findUserById(@RequestParam("id") Long id) {
        return JSON.toJSONString(userService.findUserById(id));
    }

}
