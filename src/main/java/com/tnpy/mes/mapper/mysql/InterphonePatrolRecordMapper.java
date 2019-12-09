package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.InterphonePatrolRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface InterphonePatrolRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(InterphonePatrolRecord record);

    int insertSelective(InterphonePatrolRecord record);

    InterphonePatrolRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(InterphonePatrolRecord record);

    int updateByPrimaryKey(InterphonePatrolRecord record);
}