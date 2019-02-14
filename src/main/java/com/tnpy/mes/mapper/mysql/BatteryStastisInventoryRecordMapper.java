package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.BatteryStastisInventoryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface BatteryStastisInventoryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(BatteryStastisInventoryRecord record);

    int insertSelective(BatteryStastisInventoryRecord record);

    BatteryStastisInventoryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BatteryStastisInventoryRecord record);

    int updateByPrimaryKey(BatteryStastisInventoryRecord record);

    @Select("select currentStorage from tb_batterystastisinventoryrecord where plantID = #{plantID} and updateTime >= #{startTime}  and updateTime<= #{endTime} order by updateTime desc limit 1")
    String getSelectInventory(String plantID,String startTime,String endTime);

    @Select("select * from tb_batterystastisinventoryrecord where plantID = #{plantID} and updateTime >= #{startTime}  and updateTime<= #{endTime} order by updateTime desc ")
    List<BatteryStastisInventoryRecord> selectStatisInventoryByParam(String plantID, String startTime, String endTime);
}