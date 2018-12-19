package com.tnpy.dao;

import com.tnpy.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    void addUser(User user);
    void updataUser(User user);
    @Select({"select * from user where userid = #{id}"})
    User findByUserId(int id);
    @Select({"select * from user where name = #{name}"})
    User findByUserName(String name);
}
