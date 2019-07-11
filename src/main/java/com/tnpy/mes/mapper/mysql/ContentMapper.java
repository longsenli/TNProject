package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.Content;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    @Select("select * from tb_content ${filter}")
    List<Content> selectContentByFilter(@Param("filter") String filter);
}