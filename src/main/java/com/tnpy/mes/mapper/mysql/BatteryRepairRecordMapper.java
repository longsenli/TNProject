package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.BatteryRepairRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BatteryRepairRecordMapper {
    int deleteByPrimaryKey(String batteryid);

    int insert(BatteryRepairRecord record);

    int insertSelective(BatteryRepairRecord record);

    BatteryRepairRecord selectByPrimaryKey(String batteryid);

    int updateByPrimaryKeySelective(BatteryRepairRecord record);

    int updateByPrimaryKey(BatteryRepairRecord record);
}