package com.tnpy.mes.mapper.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component

public interface DashboardMapper {
    @Select("${sqlStr}")
    List<Map<Object, Object>> getDailyProduction(@Param("sqlStr") String sqlStr);
    
    @Select("${sqlStr}")
    List<LinkedHashMap<Object, Object>> queryDef(@Param("sqlStr") String sqlStr);
    
    @Select("${sql}")
    List<Map<Object, Object>> getNowInDryingKilnjz(@Param("sql") String sql);

    @Select("${sql}")
    List<Map<Object, Object>> getInventoryInfo(@Param("sql") String sql);
}