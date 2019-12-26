package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProductionSummaryLine;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Mapper
@Component
public interface DailyProductionSummaryLineMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProductionSummaryLine record);

    int insertSelective(DailyProductionSummaryLine record);

    DailyProductionSummaryLine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProductionSummaryLine record);

    int updateByPrimaryKey(DailyProductionSummaryLine record);

    @Insert(" insert into tb_dailyproductionsummaryline (id,plantID,processID,lineID,materialID,materialName,production,classType1,dayTime,status)\n" +
            "select uuid(),inputPlantID,inputProcessID,inputLineID,materialID,materialNameInfo,sum(number),#{classType},#{dayString},'1' from tb_materialrecord where orderID like ${orderID} and status = '1'\n" +
            " and inputLineID is not null and inputLineID != '-1'  group by inputLineID, materialNameInfo")
    int insertDailyProductionSummaryLine(@Param("orderID") String orderID, String classType,String dayString);

    @Insert(" insert into tb_dailyproductionsummaryline (id,plantID,processID,lineID,materialID,materialName,production,classType1,dayTime,status)\n" +
            "select uuid(),plantID,'1015',lineID,materialID,materialName,count(1) ,#{classType},#{dayString},'1' from tb_plasticusedrecord where usedOrderID like ${orderID}  group by lineID,materialName\n")
    int insertDailyProductionSummaryLineZHQD(@Param("orderID") String orderID, String classType,String dayString);


    @Insert("insert into tb_dailyproductionsummaryline (id,plantID,processID,lineID,materialID,materialName,production,extd1,classType1,dayTime,status)\n" +
            "   select uuid(),plantID,processID,lineID,materialID,materialName,sum(productionNumber) ,materialType ,#{classType},#{dayString},'1' \n" +
            "   from tb_chargingrackrecord where status = '1' and putonDate = #{dayString} group by lineID,materialID,materialType ")
    int insertDailyProductionSummaryLineCD(@Param("orderID") String orderID, String classType,String dayString);

    @Insert(" insert into tb_dailyproductionsummaryworklocation (id,plantID,processID,lineID,workLocation,materialID,materialName,production,classType1,dayTime,status)\n" +
            "select uuid(),inputPlantID,inputProcessID,inputLineID,inputWorkLocationID,materialID,materialNameInfo,sum(number),#{classType},#{dayString},'1' " +
            " from tb_materialrecord where orderID like ${orderID} and status = '1'\n" +
            " and inputWorkLocationID is not null and inputWorkLocationID != '-1' and inputProcessID in ('1011')  group by inputWorkLocationID,materialNameInfo")
    int insertDailyProductionSummaryWorklocation(@Param("orderID") String orderID, String classType,String dayString);

    @Insert("insert into tb_dailyproductionsummaryworklocation (id,plantID,processID,lineID,workLocation,materialID,materialName,production,classType1,dayTime,status)\n" +
            "select uuid(),plantID,'1015',lineID,workLocation,materialID,materialName,count(1) ,#{classType},#{dayString},'1'  from tb_plasticusedrecord \n" +
            "where usedOrderID like ${orderID} group by workLocation,materialName")
    int insertDailyProductionSummaryWorklocationZHQD(@Param("orderID") String orderID, String classType,String dayString);


    @Insert("insert into tb_dailyproductionsummaryprocess (id,plantID,processID,materialID,materialName,production,extd1,classType1,dayTime,status)\n" +
            "  select uuid(),plantID,processID,materialID,materialName,sum(productionNumber) ,materialType,#{classType},#{dayString},'1' \n" +
            "   from tb_chargingrackrecord where status = '1' and putonDate = #{dayString} group by plantID,materialID,materialType ")
    int insertDailyProductionSummaryProcessCD(@Param("orderID") String orderID, String classType,String dayString);

    @Insert("insert into tb_dailyproductionsummaryprocess (id,plantID,processID,materialID,materialName,production,classType1,dayTime,status)\n" +
            " select uuid(),inputPlantID,inputProcessID,materialID,materialNameInfo,sum(number) ,#{classType},#{dayString},'1' \n" +
            " from tb_materialrecord where orderID like ${orderID} and status = '1'\n" +
            " and inputProcessID is not null and inputProcessID != '-1'   group by inputPlantID,inputProcessID, materialNameInfo")
    int insertDailyProductionSummaryProcess(@Param("orderID") String orderID, String classType,String dayString);

    @Insert(" insert into tb_dailyproductionsummaryprocess (id,plantID,processID,materialID,materialName,production,classType1,dayTime,status)\n" +
            "select uuid(),plantID,'1015',materialID,materialName,count(1) ,#{classType},#{dayString},'1' from tb_plasticusedrecord where usedOrderID like ${orderID}  group by materialName")
    int insertDailyProductionSummaryProcessZHQD(@Param("orderID") String orderID, String classType,String dayString);

    @Select("(select  '' as plantID,'' as processID,lineID,workLocation,materialName,sum(production) as production, '' as classType1,'总计' as dayTime from tb_dailyproductionsummaryworklocation ${filter} group by workLocation,materialName  limit 500)" +
            " union all " +
            " (select  plantID,processID,lineID,workLocation,materialName,production,classType1,DATE_FORMAT(dayTime,'%Y-%m-%d') as dayTime from tb_dailyproductionsummaryworklocation ${filter} order by dayTime,classType1,workLocation limit 500)")
    List<Map<Object, Object>> selectDailyProductionSummaryWorklocation(@Param("filter") String filter);

    @Select("(select  '' as plantID,'' as processID,'' as lineID,materialName,sum(production) as production, '' as classType1,'总计' as dayTime from tb_dailyproductionsummaryline ${filter} group by lineID,materialName  limit 500)" +
            " union all " +
            " (select  plantID,processID,lineID,materialName,production,classType1,DATE_FORMAT(dayTime,'%Y-%m-%d') as dayTime from tb_dailyproductionsummaryline ${filter} order by dayTime,classType1,lineID limit 500)")
    List<Map<Object, Object>> selectDailyProductionSummaryLine(@Param("filter") String filter);

    @Select("(select  '' as plantID,'' as processID,materialName,sum(production) as production, '' as classType1,'总计' as dayTime from tb_dailyproductionsummaryline ${filter} group by processID,materialName  limit 500 )" +
            " union all " +
            " (select  plantID,processID,materialName,production,classType1,DATE_FORMAT(dayTime,'%Y-%m-%d') as dayTime from tb_dailyproductionsummaryline ${filter} order by dayTime,classType1,processID limit 500)")
    List<Map<Object, Object>> selectDailyProductionSummaryProcess(@Param("filter") String filter);

    @Select(" ${filter} ")
    List<Map<Object, Object>> selectDailyProductionSummary(@Param("filter") String filter);
}