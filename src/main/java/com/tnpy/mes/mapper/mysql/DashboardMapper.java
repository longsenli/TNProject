package com.tnpy.mes.mapper.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component

public interface DashboardMapper {
    @Select("${sqlStr}")
    List<Map<Object, Object>> getDailyProduction(@Param("sqlStr") String sqlStr);
}