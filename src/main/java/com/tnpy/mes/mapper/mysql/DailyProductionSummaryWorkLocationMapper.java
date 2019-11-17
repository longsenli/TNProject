package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProductionSummaryWorkLocation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DailyProductionSummaryWorkLocationMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProductionSummaryWorkLocation record);

    int insertSelective(DailyProductionSummaryWorkLocation record);

    DailyProductionSummaryWorkLocation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProductionSummaryWorkLocation record);

    int updateByPrimaryKey(DailyProductionSummaryWorkLocation record);
}