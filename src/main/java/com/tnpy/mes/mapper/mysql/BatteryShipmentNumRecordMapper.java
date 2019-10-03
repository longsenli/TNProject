package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.BatteryShipmentNumRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BatteryShipmentNumRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(BatteryShipmentNumRecord record);

    int insertSelective(BatteryShipmentNumRecord record);

    BatteryShipmentNumRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BatteryShipmentNumRecord record);

    int updateByPrimaryKey(BatteryShipmentNumRecord record);

    @Select("select * from tb_batteryShipmentNumRecord ${filter}")
    List<BatteryShipmentNumRecord> selectByFilter(@Param("filter") String filter);

}