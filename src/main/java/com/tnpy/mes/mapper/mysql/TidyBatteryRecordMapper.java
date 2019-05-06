package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TidyBatteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TidyBatteryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(TidyBatteryRecord record);

    int insertSelective(TidyBatteryRecord record);

    TidyBatteryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TidyBatteryRecord record);

    int updateByPrimaryKey(TidyBatteryRecord record);
}