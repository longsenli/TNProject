package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PileBatteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface PileBatteryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PileBatteryRecord record);

    int insertSelective(PileBatteryRecord record);

    PileBatteryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PileBatteryRecord record);

    int updateByPrimaryKey(PileBatteryRecord record);
}