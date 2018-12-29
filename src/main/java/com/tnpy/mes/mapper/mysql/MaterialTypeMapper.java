package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialType;

public interface MaterialTypeMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialType record);

    int insertSelective(MaterialType record);

    MaterialType selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialType record);

    int updateByPrimaryKey(MaterialType record);
}