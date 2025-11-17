package com.chenjiacheng.spring.boot.samples.mybatis.repository.mapper;

import com.chenjiacheng.spring.boot.samples.mybatis.repository.model.User;
import org.apache.ibatis.annotations.Select;

/**
 * Created by chenjiacheng on 2025/11/17 23:44
 *
 * @author chenjiacheng
 * @since 1.0.0
 */
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User findUserById(Long id);

}
