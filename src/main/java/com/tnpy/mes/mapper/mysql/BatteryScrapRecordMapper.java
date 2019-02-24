package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.BatteryScrapRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface BatteryScrapRecordMapper {
    int deleteByPrimaryKey(String batteryid);

    int insert(BatteryScrapRecord record);

    int insertSelective(BatteryScrapRecord record);

    BatteryScrapRecord selectByPrimaryKey(String batteryid);

    int updateByPrimaryKeySelective(BatteryScrapRecord record);

    int updateByPrimaryKey(BatteryScrapRecord record);

    @Select("select * from tb_batteryScrapRecord ${filter}")
    List<BatteryScrapRecord> selectByFilter(@Param("filter") String filter);

    @Select("select count(1) from tb_batteryscraprecord where plantID = #{plantID} and scrapTime >= #{startTime} and scrapTime <= #{endTime} and batteryType = #{batteryType} and status ='1'")
    String getScrapNum(String plantID,String startTime,String endTime,String batteryType);

    int insertManyScrapRecord(@Param("batteryScrapRecordList") List<BatteryScrapRecord> batteryScrapRecordList);
}