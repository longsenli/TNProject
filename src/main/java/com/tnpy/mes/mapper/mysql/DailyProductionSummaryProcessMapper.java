package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProductionSummaryProcess;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DailyProductionSummaryProcessMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProductionSummaryProcess record);

    int insertSelective(DailyProductionSummaryProcess record);

    DailyProductionSummaryProcess selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProductionSummaryProcess record);

    int updateByPrimaryKey(DailyProductionSummaryProcess record);
}