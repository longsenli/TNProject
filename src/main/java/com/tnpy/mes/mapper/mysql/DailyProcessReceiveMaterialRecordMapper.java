package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProcessReceiveMaterialRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DailyProcessReceiveMaterialRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProcessReceiveMaterialRecord record);

    int insertSelective(DailyProcessReceiveMaterialRecord record);

    DailyProcessReceiveMaterialRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProcessReceiveMaterialRecord record);

    int updateByPrimaryKey(DailyProcessReceiveMaterialRecord record);
}