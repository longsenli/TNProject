package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ContentType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component

public interface ContentTypeMapper {
    int deleteByPrimaryKey(Integer type);

    int insert(ContentType record);

    int insertSelective(ContentType record);

    ContentType selectByPrimaryKey(Integer type);

    int updateByPrimaryKeySelective(ContentType record);

    int updateByPrimaryKey(ContentType record);

    @Select("select * from dict_contentType")
    List<ContentType> selectAllDict();
}