package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.BatteryRepairRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface BatteryRepairRecordMapper {
    int deleteByPrimaryKey(String batteryid);

    int insert(BatteryRepairRecord record);

    int insertSelective(BatteryRepairRecord record);

    BatteryRepairRecord selectByPrimaryKey(String batteryid);

    int updateByPrimaryKeySelective(BatteryRepairRecord record);

    int updateByPrimaryKey(BatteryRepairRecord record);

    @Select("select * from tb_batteryrepairrecord ${filter}")
    List<BatteryRepairRecord> selectByFilter(@Param("filter") String filter);

    @Select("select count(1) from tb_batteryrepairrecord where plantID = #{plantID}  and batteryType = #{batteryType} and repairTime >= #{startTime} and repairTime <= #{endTime} and status = #{status}")
    String getRepairNum(String plantID,String startTime,String endTime,String status,String batteryType);

}