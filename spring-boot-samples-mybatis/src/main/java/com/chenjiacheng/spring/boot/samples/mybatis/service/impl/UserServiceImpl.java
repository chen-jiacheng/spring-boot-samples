package com.chenjiacheng.spring.boot.samples.mybatis.service.impl;

import com.chenjiacheng.spring.boot.samples.mybatis.repository.mapper.UserMapper;
import com.chenjiacheng.spring.boot.samples.mybatis.repository.model.User;
import com.chenjiacheng.spring.boot.samples.mybatis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenjiacheng on 2025/11/17 23:43
 *
 * @author chenjiacheng
 * @since 1.0.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(Long id) {
        return userMapper.findUserById( id);
    }
}
