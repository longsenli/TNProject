package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProductionDetailRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DailyProductionDetailRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProductionDetailRecord record);

    int insertSelective(DailyProductionDetailRecord record);

    DailyProductionDetailRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProductionDetailRecord record);

    int updateByPrimaryKey(DailyProductionDetailRecord record);
    @Select("SELECT distinct units FROM tb_workorder where plantID = #{plantID} and processID = #{processID} and  id like  ${orderString} and units is not null limit 1;")
    String getTeamType(String plantID, String processID,@Param("orderString") String orderString);

    //plantID,processID,lineID,materialID,materialName,productionNumber,usedMaterialID,usedMaterialName,usedNumber,scrapNumber,weightNumber,classType,teamType,dayTime
    @Select("select c.*,d.scrapNumber,d.weightNumber ,#{classType} as classType ,#{teamType} as teamType,#{dayTime} as dayTime from (\n" +
            "select a.plantID,a.processID,a.lineID,a.materialID,a.materialName,a.number as productionNumber, usedMaterialID,usedMaterialName, usedNumber  from \n" +
            "(\n" +
            "SELECT inputPlantID as plantID,inputProcessID as processID ,inputLineID as lineID ,materialID,materialNameInfo as materialName,sum(number)   as number\n" +
            "FROM tb_materialrecord where inputPlantID =#{plantID} and inputProcessID=  #{processID} and orderID like ${orderString} and status = '1' group by inputPlantID,inputProcessID,inputLineID,materialID\n" +
            ") a left join \n" +
            " ( SELECT outputPlantID as plantID,outputProcessID as processID,outputLineID as lineID,materialID as usedMaterialID,materialNameInfo as usedMaterialName,sum(number)   as usedNumber\n" +
            "FROM tb_materialrecord where outputPlantID = #{plantID} and outputProcessID=  #{processID} and expendOrderID like ${orderString}  group by outputPlantID,outputProcessID,outputLineID,materialID )\n" +
            "b on a.plantID = b.plantID and a.processID = b.processID and a.lineID = b.lineID\n" +
            ") c left join \n" +
            "( SELECT plantID,processID,lineID,materialID,materialName,sum(value) as scrapNumber ,sum(weight) as weightNumber\n" +
            "FROM tb_materialscraprecord where plantID = #{plantID} and processID= #{processID} and productDay = #{dayTime} and classType =#{classType}   and status = '1' group by plantID,processID,lineID,materialID )\n" +
            "d on  c.plantID = d.plantID and c.processID = d.processID and c.lineID = d.lineID and c.usedMaterialID = d.materialID order by c.lineID,c.materialID")
    List<Map<Object,Object>> getTMPDailyProductionDetailRecord(String plantID, String processID, @Param("orderString") String orderString, String dayTime, String classType,String teamType);


    @Select("select  plantID,processID,materialID,materialName,sum(productionNumber) as productionNumber,sum(planDailyProduction) as planDailyProduction ,sum(ratioFinish) as ratioFinish,sum(lastInventory) as lastInventory\n" +
            " from ( ( select a.plantID,a.processID,a.materialID,a.materialName,a.productionNumber,b.planDailyProduction,ROUND(a.productionNumber * 100 /ifnull(b.planDailyProduction,1),2) as ratioFinish ,0 as lastInventory from (  \n" +
            "  SELECT inputPlantID as plantID,inputProcessID as processID  ,materialID,materialNameInfo as materialName,sum(number)   as productionNumber  \n" +
            "         FROM tb_materialrecord  where inputPlantID = #{plantID} and inputProcessID=  #{processID} and orderID like ${orderString} and status = '1' group by inputPlantID,inputProcessID,materialID \n" +
            " ) a left join ( select materialID,ROUND(planDailyProduction/2 ) as planDailyProduction from tb_planproductionrecord where  plantID =  #{plantID} and processID = #{processID} and planMonth = #{dayString} ) \n" +
            "    b on a.materialID  = b.materialID order by materialName )\n" +
            "    union all\n" +
            "   ( select plantID,processID,productionMaterialID,productionMaterialName,0 as productionNumber,0 as planDailyProduction,0 as ratioFinish,lastInventory  from tb_dailyprocessproductiondetailrecord\n" +
            "    where plantID = #{plantID} and processID =  #{processID}  and dayTime = #{lastDay} and classType = #{lastClassType} and  productionMaterialID is not null )\n" +
            "    )  a group by plantID,processID,materialID order by materialName ")
    List<Map<Object,Object>> getTMPDailyProductionSummaryRecord(String plantID, String processID,@Param("orderString") String orderString,String dayString,String lastDay,String lastClassType);

    @Select("SELECT outputPlantID as plantID,outputProcessID as processID,materialID as usedMaterialID,materialNameInfo as usedMaterialName,sum(number)   as usedNumber \n" +
            " FROM tb_materialrecord where outputPlantID = #{plantID} and outputProcessID=  #{processID} and expendOrderID like ${orderString}  group by outputPlantID,outputProcessID,materialID order by materialNameInfo  ")
    List<Map<Object,Object>> getTMPDailyUsedInfoSummaryRecord(String plantID, String processID,@Param("orderString") String orderString);

    @Select(" SELECT plantID,processID,materialID,materialName,sum(value) as scrapNumber ,sum(weight) as weightNumber  \n" +
            " FROM tb_materialscraprecord where plantID = #{plantID} and processID= #{processID} and productDay = #{dayTime} and classType =#{classType}   and status = '1' group by plantID,processID,materialID order by materialName  ")
    List<Map<Object,Object>> getTMPDailyScrapInfoSummaryRecord(String plantID, String processID,String dayTime,String classType);

    @Select("select a.*,b.name as materialName from ( SELECT batteryType as materialID,acceptPlantID as plantID,processID,sum(number) as recieveNumber FROM tb_grantmaterialrecord where acceptPlantID = #{plantID} and " +
            " processID = #{processID} and grantTime > #{startTime} and grantTime < #{endTime} group by batteryType ) a left join sys_material b on a.materialID = b.id  order by materialName ")
    List<Map<Object,Object>> getTMPDailyRecieveInfoSummaryRecord(String plantID, String processID,String startTime,String endTime);

    @Select("select a.*,b.name as materialName  from (   SELECT batteryType as materialID, plantID,processID,sum(number) as grantNumber  FROM tb_grantmaterialrecord where plantID = #{plantID} and processID = #{processID} " +
            " and grantTime > #{startTime} and grantTime < #{endTime} group by batteryType ) a left join sys_material b on a.materialID = b.id  order by materialName ")
    List<Map<Object,Object>> getTMPDailyGrantInfoSummaryRecord(String plantID, String processID,String startTime,String endTime);

    @Select( "(select count(1) as attendanceNumber from tb_staffattendancedetail where plantID = #{plantID} and processID =#{processID}  and dayTime =#{dayTime} and classType1 = #{classType} and status ='1' )\n" +
            "union all\n" +
            "(select count(1) as totalLine from sys_productionline where  plantID = #{plantID} and processID = #{processID} and status = '1' ) \n" +
            "union all\n" +
            "( select count(distinct lineID ) as runningLine from tb_staffattendancedetail where plantID = #{plantID} and processID =#{processID} and dayTime =#{dayTime} and classType1 = #{classType} and status ='1' )\n ")
    List<Integer> getTMPDailyAttendanceSummaryRecord(String plantID, String processID,String dayTime,String classType);



}