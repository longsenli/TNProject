package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.InterphonePatrolLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface InterphonePatrolLocationMapper {
    int deleteByPrimaryKey(String id);

    int insert(InterphonePatrolLocation record);

    int insertSelective(InterphonePatrolLocation record);

    InterphonePatrolLocation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(InterphonePatrolLocation record);

    int updateByPrimaryKey(InterphonePatrolLocation record);

    @Select(" select id,name,plantID,processID from tb_interphonepatrollocation ${filter} ")
    List<Map<Object,Object>> selectLocationInfoDetail(@Param("filter") String filter);
}