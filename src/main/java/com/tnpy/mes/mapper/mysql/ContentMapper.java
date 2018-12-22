package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.Content;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component

public interface ContentMapper {
    int deleteByPrimaryKey(String id);

    int insert(Content record);

    int insertSelective(Content record);

    Content selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKey(Content record);

    List<Content> selectByFilter(@Param("filter") String filter);
}