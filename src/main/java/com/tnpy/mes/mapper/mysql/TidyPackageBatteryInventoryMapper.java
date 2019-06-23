package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TidyPackageBatteryInventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TidyPackageBatteryInventoryMapper {
    int deleteByPrimaryKey(String id);

    int insert(TidyPackageBatteryInventory record);

    int insertSelective(TidyPackageBatteryInventory record);

    TidyPackageBatteryInventory selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TidyPackageBatteryInventory record);

    int updateByPrimaryKey(TidyPackageBatteryInventory record);

    @Select("select * from tb_tidypackagebatteryinventory ${filter}")
    List<TidyPackageBatteryInventory> selectByFilter(@Param("filter") String filter);
}