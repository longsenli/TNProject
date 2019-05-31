package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PlanProductionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    @Update(" update tb_planproductionrecord a inner join ( select materialID,sum(number) as totalNumber,inputPlantID from tb_materialrecord where inputTime > #{month} group by inputPlantID,materialID ) b " +
            "  on a.plantID = b.inputPlantID and a.materialID = b.materialID  set a.realProduction =ifnull( b.totalNumber,0),a.accomplishmentRatio = format(ifnull( b.totalNumber,0)/a.planProduction * 100,2) where planMonth >=  #{month} ")
    int updateFinishRate(String month);
}