package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DocType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component

public interface DocTypeMapper {
    int deleteByPrimaryKey(String name);

    int insert(DocType record);

    int insertSelective(DocType record);

    DocType selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(DocType record);

    int updateByPrimaryKey(DocType record);

    @Select("select * from dict_docType")
    List<DocType> selectAllDict();
}