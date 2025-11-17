package com.chenjiacheng.spring.boot.samples.mybatis.service;

import com.chenjiacheng.spring.boot.samples.mybatis.repository.model.User;

/**
 * Created by chenjiacheng on 2025/11/17 23:43
 *
 * @author chenjiacheng
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 根据id查询用户
     *
     * @param id id
     * @return 用户
     */
    User findUserById(Long id);
}
