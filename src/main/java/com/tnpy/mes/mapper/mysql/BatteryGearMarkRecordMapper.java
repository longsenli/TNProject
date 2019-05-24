package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.BatteryGearMarkRecord;

public interface BatteryGearMarkRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(BatteryGearMarkRecord record);

    int insertSelective(BatteryGearMarkRecord record);

    BatteryGearMarkRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BatteryGearMarkRecord record);

    int updateByPrimaryKey(BatteryGearMarkRecord record);
}