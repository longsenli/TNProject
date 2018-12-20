package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DocType;

public interface DocTypeMapper {
    int deleteByPrimaryKey(String name);

    int insert(DocType record);

    int insertSelective(DocType record);

    DocType selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(DocType record);

    int updateByPrimaryKey(DocType record);
}