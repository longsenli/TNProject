package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialRelation;

public interface MaterialRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialRelation record);

    int insertSelective(MaterialRelation record);

    MaterialRelation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialRelation record);

    int updateByPrimaryKey(MaterialRelation record);
}