package com.chenjiacheng.spring.boot.samples.mybatis.repository.model;

import lombok.Data;

/**
 * Created by chenjiacheng on 2025/11/17 23:45
 *
 * @author chenjiacheng
 * @since 1.0.0
 */
@Data
public class User {

    private Long id;

    private String createTime;

    private String updateTime;

    private String username;

    private String password;

    private String email;

    private String status;

    private String deleteFlag;

}
