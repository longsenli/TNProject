package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DataProvenanceRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DataProvenanceRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(DataProvenanceRelation record);

    int insertSelective(DataProvenanceRelation record);

    DataProvenanceRelation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DataProvenanceRelation record);

    int updateByPrimaryKey(DataProvenanceRelation record);
}