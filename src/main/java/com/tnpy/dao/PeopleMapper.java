package com.tnpy.dao;

import com.tnpy.pojo.People;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PeopleMapper {
    int insert(People record);

    int insertSelective(People record);
    @Select("select * from people ")
    List<People> allPeople();

    @Select("select name from people where id  = '1'")
    String Name();

    String getName(String id);
}