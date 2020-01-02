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

    //plantID,processID,lineID,materialID,materialName,productionNumber,usedMaterialID,usedMaterialName,usedNumber,scrapNumber,weightNumber,classType,teamType,dayTime
    @Select("select c.*,d.scrapNumber,d.weightNumber ,#{classType} as classType ,#{teamType} as teamType,#{dayTime} as dayTime from (\n" +
            "select a.plantID,a.processID,a.lineID,a.worklocationID,a.materialID,a.materialName,a.number as productionNumber, usedMaterialID,usedMaterialName, usedNumber  from \n" +
            "(\n" +
            "SELECT inputPlantID as plantID,inputProcessID as processID ,inputLineID as lineID ,inputWorkLocationID as worklocationID,materialID,materialNameInfo as materialName,sum(number)   as number\n" +
            "FROM tb_materialrecord where inputPlantID =#{plantID} and inputProcessID=  #{processID} and orderID like ${orderString} and status = '1' group by inputPlantID,inputProcessID,inputWorkLocationID,materialID\n" +
            ") a left join \n" +
            " ( SELECT outputPlantID as plantID,outputProcessID as processID,outputLineID as lineID,materialID as usedMaterialID,materialNameInfo as usedMaterialName,sum(number)   as usedNumber\n" +
            "FROM tb_materialrecord where outputPlantID = #{plantID} and outputProcessID=  #{processID} and expendOrderID like ${orderString}  group by outputPlantID,outputProcessID,outputLineID,materialID )\n" +
            "b on a.plantID = b.plantID and a.processID = b.processID and a.lineID = b.lineID\n" +
            ") c left join \n" +
            "( SELECT plantID,processID,lineID,materialID,materialName,sum(value) as scrapNumber ,sum(weight) as weightNumber\n" +
            "FROM tb_materialscraprecord where plantID = #{plantID} and processID= #{processID} and productDay = #{dayTime} and classType =#{classType}   and status = '1' group by plantID,processID,lineID,materialID )\n" +
            "d on  c.plantID = d.plantID and c.processID = d.processID and c.lineID = d.lineID and c.usedMaterialID = d.materialID order by c.lineID,c.worklocationID,c.materialID")
    List<Map<Object,Object>> getJZTMPDailyProductionDetailRecord(String plantID, String processID, @Param("orderString") String orderString, String dayTime, String classType,String teamType);

    @Select( " select plantID,#{processID} as processID,lineID,workLocation as worklocationID ,materialID,materialName,count(1) as productionNumber  ,'' as usedMaterialID, '' as usedMaterialName,0 as usedNumber, " +
            " 0 as scrapNumber,0 as weightNumber ,#{classType} as classType ,#{teamType} as teamType,#{dayTime} as dayTime " +
            " from tb_plasticusedrecord where  plantID = #{plantID} and  usedOrderID like ${orderString} and status ='1' group by workLocation,materialName order by workLocation")
    List<Map<Object,Object>> getZHQDTMPDailyProductionDetailRecord(String plantID, String processID, @Param("orderString") String orderString, String dayTime, String classType,String teamType);

    @Select( " select a.*,b.usedMaterialID,b.usedMaterialName,b.usedNumber,b.scrapNumber,b.usedNumberTransition1 ,#{classType} as classType ,#{teamType} as teamType,#{dayTime} as dayTime  from (            \n" +
            "select plantID,#{processID},lineID,materialID,materialName,sum(productionNumber) as productionNumber,materialType as productionTransition1 from tb_chargingrackrecord \n" +
            "where plantID = #{plantID} and putonDate = #{dayTime} and status  = '1' group by lineID,materialID,materialType\n" +
            ") a left join (\n" +
            "select lineID,materialID as usedMaterialID ,materialName as usedMaterialName,sum(realNumber) as usedNumber,sum(repairNumber) as scrapNumber,materialType as usedNumberTransition1 from tb_chargingrackrecord \n" +
            "where plantID = #{plantID} and pulloffDate >#{startTime} and pulloffDate < #{endTime} and status  = '1' group by lineID,materialID,materialType ) b on a.lineID = b.lineID  order by a.lineID,a.materialName,b.usedMaterialName ")
    List<Map<Object,Object>> getCDTMPDailyProductionDetailRecord(String plantID, String processID, String startTime,String endTime, String dayTime, String classType,String teamType);


    //plantID,processID,lineID,materialID,materialName,productionNumber,usedMaterialID,usedMaterialName,usedNumber,scrapNumber,weightNumber,classType,teamType,dayTime
//    @Select("select a.*,b.usedMaterialID, b.usedMaterialName,b.usedNumber, 0 as scrapNumber,0 as weightNumber ,#{classType} as classType ,#{teamType} as teamType,#{dayTime} as dayTime  from \n" +
//            " ( select plantID,#{processID},solidifyRoomID as lineID,materialID,materialName,sum(productionNum) as productionNumber \n" +
//            "from tb_solidifyrecord where plantID = #{plantID} and  endtime3 > #{startTime} and endtime3 <  #{endTime} group by solidifyRoomID,materialName )\n" +
//            "a left join \n" +
//            "( select plantID,#{processID},solidifyRoomID as lineID,materialID as usedMaterialID, materialName as usedMaterialName, sum(productionNum) as usedNumber \n" +
//            "from tb_solidifyrecord where plantID = #{plantID} and  starttime1 >#{startTime} and starttime1 < #{endTime} group by solidifyRoomID,materialName  \n" +
//            ") b on a.lineID = b.lineID order by a.lineID")
//    List<Map<Object,Object>> getGHSTMPDailyProductionDetailRecord(String plantID, String processID, String startTime,String endTime, String dayTime, String classType,String teamType);

    @Select("  select plantID,#{processID} as processID,lineID,materialID,materialName,sum(productionNumber) as productionNumber,usedMaterialID,usedMaterialName,sum(usedNumber) as usedNumber, " +
            " 0 as scrapNumber,0 as weightNumber ,#{classType} as classType ,#{teamType} as teamType,#{dayTime} as dayTime from (\n" +
            "          (  select plantID, solidifyRoomID as lineID,materialID,materialName,sum(productionNum) as productionNumber ,'-' as usedMaterialID,'' as usedMaterialName, 0 as usedNumber\n" +
            "            from tb_solidifyrecord where plantID = #{plantID} and  endtime3 > #{startTime}  and endtime3 < #{endTime} group by solidifyRoomID,materialName )\n" +
            "            union all\n" +
            "           ( select plantID, solidifyRoomID as lineID,'-' as materialID,'' as materialName ,0  as productionNumber,materialID as usedMaterialID, materialName as usedMaterialName, sum(productionNum) as usedNumber  \n" +
            "            from tb_solidifyrecord where plantID = #{plantID} and  starttime1 >#{startTime} and starttime1 < #{endTime} group by solidifyRoomID,materialName order by materialName limit 200)\n" +
            "            ) a group by lineID,materialID,usedMaterialID order by lineID,materialID desc")
    List<Map<Object,Object>> getGHSTMPDailyProductionDetailRecord(String plantID, String processID, String startTime,String endTime, String dayTime, String classType,String teamType);


    @Select("select  plantID,processID,materialID,materialName,sum(productionNumber) as productionNumber,sum(planDailyProduction) as planDailyProduction ,sum(ratioFinish) as ratioFinish,sum(lastInventory) as lastInventory\n" +
            " from ( ( select a.plantID,a.processID,a.materialID,a.materialName,a.productionNumber,b.planDailyProduction,ROUND(a.productionNumber * 100 /ifnull(b.planDailyProduction,1),2) as ratioFinish ,0 as lastInventory from (  \n" +
            " select plantID, #{processID} as processID,materialID,materialName,count(1) as productionNumber from tb_plasticusedrecord where plantID = #{plantID}" +
            " and usedOrderID    like ${orderString} and status ='1' group by materialName" +
            " ) a left join ( select materialID,ROUND(planDailyProduction/2 ) as planDailyProduction from tb_planproductionrecord where  plantID =  #{plantID} and processID = #{processID} and planMonth = #{dayString} ) \n" +
            "    b on a.materialID  = b.materialID order by materialName )\n" +
            "    union all\n" +
            "   ( select plantID,processID,productionMaterialID,productionMaterialName,0 as productionNumber,0 as planDailyProduction,0 as ratioFinish,ifnull(currentInventory,0) as lastInventory  from tb_dailyprocessproductiondetailrecord\n" +
            "    where plantID = #{plantID} and processID =  #{processID}  and dayTime = #{lastDay} and classType = #{lastClassType} and  productionMaterialID is not null )\n" +
            "    )  a group by plantID,processID,materialID order by materialName ")
    List<Map<Object,Object>> getZHQDTMPDailyProductionSummaryRecord(String plantID, String processID,@Param("orderString") String orderString,String dayString,String lastDay,String lastClassType);

    @Select("select  plantID,processID,materialID,materialName,ifnull(sum(productionNumber),0) as productionNumber,ifnull(sum(planDailyProduction),0) as planDailyProduction ,sum(ratioFinish) as ratioFinish,ifnull(sum(lastInventory),0) as lastInventory\n" +
            " from ( ( select a.plantID,a.processID,a.materialID,a.materialName,a.productionNumber,b.planDailyProduction,ROUND(a.productionNumber * 100 /ifnull(b.planDailyProduction,1),2) as ratioFinish ,0 as lastInventory from (  \n" +
            " select plantID,#{processID}as processID,materialID,materialName,sum(productionNumber) as productionNumber from tb_chargingrackrecord \n" +
            "where plantID =#{plantID} and putonDate = #{dayString} and status  = '1' group by materialName" +
            " ) a left join ( select materialID,ROUND(planDailyProduction/2 ) as planDailyProduction from tb_planproductionrecord where  plantID =  #{plantID} and processID = #{processID} and planMonth = #{dayString} ) \n" +
            "    b on a.materialID  = b.materialID order by materialName )\n" +
            "    union all\n" +
            "   ( select plantID,processID,productionMaterialID,productionMaterialName,0 as productionNumber,0 as planDailyProduction,0 as ratioFinish,ifnull(currentInventory,0) as lastInventory  from tb_dailyprocessproductiondetailrecord\n" +
            "    where plantID = #{plantID} and processID =  #{processID}  and dayTime = #{lastDay} and classType = #{lastClassType} and  productionMaterialID is not null )\n" +
            "    )  a group by plantID,processID,materialID order by materialName ")
    List<Map<Object,Object>> getCDTMPDailyProductionSummaryRecord(String plantID, String processID,@Param("orderString") String orderString,String dayString,String lastDay,String lastClassType);


    @Select("select  plantID,processID,materialID,materialName,ifnull(sum(productionNumber),0) as productionNumber,sum(planDailyProduction) as planDailyProduction ,sum(ratioFinish) as ratioFinish,ifnull(sum(lastInventory),0) as lastInventory\n" +
            " from ( ( select a.plantID,a.processID,a.materialID,a.materialName,a.productionNumber,b.planDailyProduction,ROUND(a.productionNumber * 100 /ifnull(b.planDailyProduction,1),2) as ratioFinish ,0 as lastInventory from (  \n" +
            "  SELECT inputPlantID as plantID,inputProcessID as processID  ,materialID,materialNameInfo as materialName,sum(number)   as productionNumber  \n" +
            "         FROM tb_materialrecord  where inputPlantID = #{plantID} and inputProcessID=  #{processID} and orderID like ${orderString} and status = '1' group by inputPlantID,inputProcessID,materialID \n" +
            " ) a left join ( select materialID,ROUND(planDailyProduction/2 ) as planDailyProduction from tb_planproductionrecord where  plantID =  #{plantID} and processID = #{processID} and planMonth = #{dayString} ) \n" +
            "    b on a.materialID  = b.materialID order by materialName )\n" +
            "    union all\n" +
            "   ( select plantID,processID,productionMaterialID,productionMaterialName,0 as productionNumber,0 as planDailyProduction,0 as ratioFinish,ifnull(currentInventory,0) as lastInventory  from tb_dailyprocessproductiondetailrecord\n" +
            "    where plantID = #{plantID} and processID =  #{processID}  and dayTime = #{lastDay} and classType = #{lastClassType} and  productionMaterialID is not null )\n" +
            "    )  a group by plantID,processID,materialID order by materialName ")
    List<Map<Object,Object>> getTMPDailyProductionSummaryRecord(String plantID, String processID,@Param("orderString") String orderString,String dayString,String lastDay,String lastClassType);

    @Select("SELECT outputPlantID as plantID,outputProcessID as processID,materialID,materialNameInfo as materialName,sum(number)   as usedNumber \n" +
            " FROM tb_materialrecord where outputPlantID = #{plantID} and outputProcessID=  #{processID} and expendOrderID like ${orderString}  group by outputPlantID,outputProcessID,materialID order by materialNameInfo  ")
    List<Map<Object,Object>> getTMPDailyUsedInfoSummaryRecord(String plantID, String processID,@Param("orderString") String orderString);

    @Select(" select plantID,processID,materialID,materialName,sum(materialNumber) as scrapNumber ,sum(if(inputPlantID='1001',materialNumber,0)) as scrapNumberTransition1, \n" +
            "sum(if(inputPlantID='1002',materialNumber,0)) as scrapNumberTransition2, sum(if(inputPlantID='1003',materialNumber,0)) as scrapNumberTransition3,0 as weightNumber\n" +
            "from tb_unqualifiedmaterialreturn where inputPlantID = #{plantID} and inputProcessID = #{processID} and status = '1' and returnTime > #{startTime} and  returnTime < #{endTime} group by materialID ")
    List<Map<Object,Object>> getJZTMPDailyScrapInfoSummaryRecord(String plantID, String processID,String startTime,String endTime);

    @Select(" select plantID,processID,materialID,materialName,sum(materialNumber) as scrapNumber ,sum(if(inputPlantID='1001',materialNumber,0)) as scrapNumberTransition1, \n" +
            "sum(if(inputPlantID='1002',materialNumber,0)) as scrapNumberTransition2, sum(if(inputPlantID='1003',materialNumber,0)) as scrapNumberTransition3,0 as weightNumber\n" +
            "from tb_unqualifiedmaterialreturn where plantID = #{plantID} and processID = #{processID} and status = '1' and returnTime > #{startTime} and  returnTime < #{endTime} group by materialID ")
    List<Map<Object,Object>> getTBTMPReturnMaterialSummaryRecord(String plantID, String processID,String startTime,String endTime);

    @Select(" SELECT plantID,processID,materialID,materialName,sum(value) as scrapNumber ,sum(weight) as weightNumber ,0 as scrapNumberTransition1,0 as scrapNumberTransition2,0 as scrapNumberTransition3 \n" +
            " FROM tb_materialscraprecord where plantID = #{plantID} and processID= #{processID} and productDay = #{dayTime} and classType =#{classType}   and status = '1' group by plantID,processID,materialID order by materialName  ")
    List<Map<Object,Object>> getTMPDailyScrapInfoSummaryRecord(String plantID, String processID,String dayTime,String classType);

    @Select("select a.*,b.name as materialName from ( SELECT batteryType as materialID,acceptPlantID as plantID,processID,sum(number) as receiveNumber, " +
            " sum(if(plantID='1001',number,0)) as receiveMaterialNumber1,sum(if(plantID='1002' ,number,0)) as receiveMaterialNumber2,sum(if(plantID='1003' ,number,0)) as receiveMaterialNumber3 " +
            " FROM tb_grantmaterialrecord where acceptPlantID = #{plantID} and " +
            " processID = #{processID} and grantTime > #{startTime} and grantTime < #{endTime} group by batteryType ) a left join sys_material b on a.materialID = b.id  order by materialName ")
    List<Map<Object,Object>> getTMPDailyRecieveInfoSummaryRecord(String plantID, String processID,String startTime,String endTime);

    @Select("select a.*,b.name as materialName  from (   SELECT batteryType as materialID, plantID,processID,sum(number) as grantNumber," +
            " sum(if(acceptPlantID='1001' ,number,0)) as grantNumberTransition1,sum(if(acceptPlantID='1002' ,number,0)) as grantNumberTransition2, " +
            "sum(if(acceptPlantID='1003' ,number,0)) as grantNumberTransition3 FROM tb_grantmaterialrecord where plantID = #{plantID} and processID = #{processID} " +
            " and grantTime > #{startTime} and grantTime < #{endTime} group by batteryType ) a left join sys_material b on a.materialID = b.id  order by materialName ")
    List<Map<Object,Object>> getTMPDailyGrantInfoSummaryRecord(String plantID, String processID,String startTime,String endTime);

    @Select(" select plantID,#{processID} as processID,materialID ,materialName ,sum(realNumber + repairNumber) as grantNumber from tb_chargingrackrecord \n" +
            "where plantID = #{plantID} and pulloffDate > #{startTime} and pulloffDate < #{endTime} and status  = '1' group by materialName order by  materialName")
    List<Map<Object,Object>> getCDTMPDailyGrantInfoSummaryRecord(String plantID, String processID,String startTime,String endTime);

    @Select( "select ifnull(currentInventory,0) as currentInventory ,receiveMaterialID as materialID,receiveMaterialName as materialName from tb_dailyprocessproductiondetailrecord where plantID = #{plantID}" +
            " and processID = #{processID} and dayTime = #{dayTime} and classType = #{lastClassType} and receiveMaterialID is not null  \n" )
    List<Map<Object,Object>> getTMPTBOnlineMterialSummaryRecord(String plantID, String processID,String dayTime,String lastClassType);

    @Select( "select m.*,n.name as materialName from ( select materialID,sum(reinputNum) as reinputNum,sum(newReturn) as newReturn,sum(onlineNum) as onlineNum from ( \n" +
            "(SELECT materialID, sum(if(status ='3',materialNum,0) ) as reinputNum, sum(if(status ='3', 0 ,materialNum)) as newReturn ,0 as onlineNum FROM tb_onlinematerialrecord\n" +
            " where updateTime > #{startTime}  and updateTime < #{endTime} and plantID = #{plantID} and processID = #{processID}   group by materialID)\n" +
            "union all\n" +
            "( SELECT materialID, 0 as reinputNum, 0 as newReturn,sum( materialNum ) as onlineNum FROM tb_onlinematerialrecord\n" +
            " where  status= '1' and plantID = #{plantID} and processID = #{processID} group by materialID ) ) a group by materialID ) m left join sys_material n on m.materialID = n.id " )
    List<Map<Object,Object>> getTMPZPXTOnlineMterialSummaryRecord(String plantID, String processID,String startTime,String endTime);

    @Select( "(select count(1) as attendanceNumber from tb_staffattendancedetail where plantID = #{plantID} and processID =#{processID}  and dayTime =#{dayTime} and classType1 = #{classType} and status ='1' )\n" +
            "union all\n" +
            "(select count(1) as totalLine from sys_productionline where  plantID = #{plantID} and processID = #{processID} and status = '1' ) \n" +
            "union all\n" +
            "( select count(distinct lineID ) as runningLine from tb_staffattendancedetail where plantID = #{plantID} and processID =#{processID} and dayTime =#{dayTime} and classType1 = #{classType} and status ='1' )\n ")
    List<Integer> getTMPDailyAttendanceSummaryRecord(String plantID, String processID,String dayTime,String classType);


    @Select( "(select count(1) as attendanceNumber from tb_staffattendancedetail where plantID = #{plantID} and processID =#{processID}  and dayTime =#{dayTime} and classType1 = #{classType} and status ='1' )\n" +
            "union all\n" +
            "(select count(1) as totalLine from tb_worklocation where  plantID = #{plantID} and processID = #{processID} and status = '1' ) \n" +
            "union all\n" +
            "( select count(distinct worklocationID ) as runningLine from tb_staffattendancedetail where plantID = #{plantID} and processID =#{processID} and dayTime =#{dayTime} and classType1 = #{classType} and status ='1' )\n ")
    List<Integer> getGWTMPDailyAttendanceSummaryRecord(String plantID, String processID,String dayTime,String classType);


    @Select(  "select   #{plantID} as plantID, #{processID} as processID, #{classType} as classType, #{dayTime} as dayTime,materialID as productionMaterialID,materialName as productionMaterialName ," +
            " sum(remainNumber) as receiveMaterialNumber1,sum(remainTS) as receiveMaterialNumber2,sum(totalInNumber) as productionNumber,sum(inTS) as productionTransition1,sum(totalOutNumber) as grantNumber,sum(outTS) as grantNumberTransition1 from (\n" +
            "( SELECT materialID,materialName,sum(productionNum) as remainNumber,count(1) as remainTS,0 as totalInNumber, 0 as inTS,0 as totalOutNumber,0 as outTS  \n" +
            "FROM tb_solidifyrecord where plantID = #{plantID} and status < '9' group by materialID,materialName) union all\n" +
            "(SELECT materialID,materialName,0 as remainNumber,0 as remainTS, sum(productionNum) as totalInNumber,count(1) as inTS ,0 as totalOutNumber,0 as outTS \n" +
            " FROM tb_solidifyrecord where plantID = #{plantID} and  starttime1> #{startTime} and  starttime1 < #{endTime} group by materialID,materialName) union all\n" +
            "(SELECT materialID,materialName,0 as remainNumber,0 as remainTS,0 as totalInNumber, 0 as inTS,sum(productionNum) as totalOutNumber,count(1) as outTS \n" +
            "FROM tb_solidifyrecord where plantID =#{plantID} and  endtime3> #{startTime} \n" +
            "and  endtime3 <  #{endTime} group by materialID,materialName)  ) a group by materialID,materialName")
    List<Map<Object, Object>>  getSolidifyTMPDailyProductionSummaryRecord(String plantID,String processID,String classType,String dayTime,  String startTime,String endTime);
}
