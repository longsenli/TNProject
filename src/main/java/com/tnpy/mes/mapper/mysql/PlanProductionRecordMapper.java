package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PlanProductionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PlanProductionRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PlanProductionRecord record);

    int insertSelective(PlanProductionRecord record);

    PlanProductionRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PlanProductionRecord record);

    int updateByPrimaryKey(PlanProductionRecord record);

    @Select("select * from tb_planproductionrecord  ${filter}")
    List<PlanProductionRecord> getPlanProductionRecordByFilter(@Param("filter") String filter );

    @Select("select sum(planDailyProduction)/2 as num from tb_planproductionrecord where plantID = #{plantID} and processID = #{processID} and planMonth = #{planMonth}")
    String getplanNumber(String plantID,String processID,String planMonth);
}