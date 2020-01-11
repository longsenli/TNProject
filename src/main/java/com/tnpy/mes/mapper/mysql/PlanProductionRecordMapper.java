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
            "  on a.plantID = b.inputPlantID and a.materialID = b.materialID  set a.realProduction =ifnull( b.totalNumber,0),a.accomplishmentRatio = round(ifnull( b.totalNumber,0)/a.planProduction * 100,2) where planMonth =  #{month} ")
    int updateFinishRate(String month);

    @Update(" insert into tb_planproductionrecord (id,materialID,materialName,plantID,processID,planProduction,planDailyProduction,planMonth,operator,status)\n" +
            "select uuid(),a.materialID,a.materialName,a.plantID,a.processID,a.planProduction ,CEILING(a.planProduction/ ${dayNumber}) as planProductionDaily,#{dayString},'管理员','2' from \n" +
            "(select materialID,materialName,plantID,processID,planProduction from tb_planproductionrecord where planMonth = #{monthString} ) a left join \n" +
            "(select materialID,materialName,plantID,processID,planProduction from tb_planproductionrecord where planMonth = #{dayString}) b \n" +
            "on a.materialID = b.materialID and a.plantID = b.plantID and a.processID = b.processID  and b.planProduction is null \n")
    int insertDailyPlanProduction(String monthString,String dayString,@Param("dayNumber") String dayNumber);

    @Update(" update tb_planproductionrecord a left join ( select materialID,sum(number) as totalNumber,inputPlantID,inputProcessID from tb_materialrecord where orderID like ${orderDay} group by inputPlantID,inputProcessID,materialID ) b \n" +
            "  on a.plantID = b.inputPlantID and a.materialID = b.materialID and a.processID = b.inputProcessID  set a.realProduction =ifnull( b.totalNumber,0),a.accomplishmentRatio = round(ifnull( b.totalNumber,0)/a.planDailyProduction * 100,2) where planMonth =  #{dayString}")
    int updateDailyRate(String dayString,@Param("orderDay") String orderDay);
}