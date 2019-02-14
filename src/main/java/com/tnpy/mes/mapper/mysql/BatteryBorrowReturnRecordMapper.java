package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.BatteryBorrowReturnRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BatteryBorrowReturnRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(BatteryBorrowReturnRecord record);

    int insertSelective(BatteryBorrowReturnRecord record);

    BatteryBorrowReturnRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BatteryBorrowReturnRecord record);

    int updateByPrimaryKey(BatteryBorrowReturnRecord record);

    @Select("select sum(changeNum) from tb_batteryborrowreturnrecord where outPlantID = #{plantID}  and batteryType = #{batteryType} and updateTime >= #{startTime} and updateTime <= #{endTime}")
    String getLoanNum(String plantID,String startTime,String endTime,String batteryType);

    @Select("select sum(changeNum) from tb_batteryborrowreturnrecord where inPlantID = #{plantID}  and batteryType = #{batteryType}  and updateTime >= #{startTime} and updateTime <= #{endTime}")
    String getBorrowNum(String plantID,String startTime,String endTime,String batteryType);

}