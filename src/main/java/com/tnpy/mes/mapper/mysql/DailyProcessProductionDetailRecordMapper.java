package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProcessProductionDetailRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
@Component
public interface DailyProcessProductionDetailRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProcessProductionDetailRecord record);

    int insertSelective(DailyProcessProductionDetailRecord record);

    DailyProcessProductionDetailRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProcessProductionDetailRecord record);

    int updateByPrimaryKey(DailyProcessProductionDetailRecord record);


    @Select("select count(1) from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType =#{classType}")
    int selectConfirmNumber(String plantID,String processID,String dayTime,String classType );


    @Select("select * from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType =#{classType} order by productionMaterialName")
    List<Map<Object, Object>> getDailyProcessProductionDetailRecord(String plantID, String processID, String dayTime, String classType);

    @Select("select * from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType =#{classType} order by reveiveType asc, productionMaterialName")
    List<Map<Object, Object>> getBBDailyProcessProductionDetailRecord(String plantID, String processID, String dayTime, String classType);


    @Select("select productionMaterialName,productionMaterialID,sum(productionNumber) as productionNumber,sum(planDailyProduction) as planDailyProduction,\n" +
            " sum(if(classType = '夜班',currentInventory,0)) as currentInventory,round(ifnull(sum(productionNumber) /sum(planDailyProduction),1) *100,2) as ratioFinish,\n" +
            " sum(if(classType = '白班',lastInventory,0)) as lastInventory, sum(if(classType = '夜班',inventoryTransition1,0)) as inventoryTransition1,\n" +
            " sum(receiveNumber) as receiveNumber, sum(receiveMaterialNumber1) as receiveMaterialNumber1, sum(receiveMaterialNumber2) as receiveMaterialNumber2, sum(receiveMaterialNumber3) as receiveMaterialNumber3,\n" +
            "  sum(scrapNumber) as scrapNumber, sum(scrapNumberTransition1) as scrapNumberTransition1, sum(scrapNumberTransition2) as scrapNumberTransition2, sum(scrapNumberTransition3) as scrapNumberTransition3,\n" +
            "   sum(grantNumber) as grantNumber, sum(grantNumberTransition1) as grantNumberTransition1, sum(grantNumberTransition2) as grantNumberTransition2, sum(grantNumberTransition3) as grantNumberTransition3,dayTime\n" +
            "from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID =#{processID} and dayTime = #{dayTime}  group by productionMaterialID order by productionMaterialName")
            List<Map<Object, Object>> getJZDailyProcessProductionAllDayRecord(String plantID, String processID, String dayTime, String classType);


    @Select("select productionMaterialName,productionMaterialID,sum(productionNumber) as productionNumber,sum(productionTransition1) as productionTransition1 ,sum(planDailyProduction) as planDailyProduction,\n" +
            "round(ifnull(sum(productionNumber) /sum(planDailyProduction),1) *100,2) as ratioFinish\n" +
            " from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime = #{dayTime} and productionMaterialName is not null  \n" +
            " group by productionMaterialID order by productionMaterialName  ")
    List<Map<Object, Object>> getTBDailyProductionAllDayRecord(String plantID, String processID, String dayTime, String classType);


    @Select("select receiveMaterialID,receiveMaterialName,\n" +
            " sum(if(classType = '夜班',currentInventory,0)) as currentInventory,\n" +
            " sum(if(classType = '白班',lastInventory,0)) as lastInventory, sum(if(classType = '夜班',inventoryTransition1,0)) as inventoryTransition1,\n" +
            " sum(receiveNumber) as receiveNumber, sum(receiveMaterialNumber1) as receiveMaterialNumber1, sum(receiveMaterialNumber2) as receiveMaterialNumber2, sum(receiveMaterialNumber3) as receiveMaterialNumber3,\n" +
            "  sum(scrapNumber) as scrapNumber, sum(scrapNumberTransition1) as scrapNumberTransition1, sum(scrapNumberTransition2) as scrapNumberTransition2, sum(scrapNumberTransition3) as scrapNumberTransition3,\n" +
            " sum(usedNumber) as usedNumber,sum(weightNumber) as weightNumber " +
            "from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID =#{processID} and dayTime = #{dayTime} and receiveMaterialName is not null " +
            " group by receiveMaterialID order by receiveMaterialName")
    List<Map<Object, Object>> getTBDailyUsedInfoAllDayRecord(String plantID, String processID, String dayTime, String classType);


    @Select("select productionMaterialName,productionMaterialID,sum(productionNumber) as productionNumber,sum(scrapNumber) as scrapNumber,sum(weightNumber) as weightNumber ,sum(planDailyProduction) as planDailyProduction,\n" +
            "round(ifnull(sum(productionNumber) /sum(planDailyProduction),1) *100,2) as ratioFinish\n" +
            " from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime = #{dayTime} and productionMaterialName is not null  \n" +
            " group by productionMaterialID order by productionMaterialName  ")
    List<Map<Object, Object>> getFBDailyProductionAllDayRecord(String plantID, String processID, String dayTime, String classType);

    @Select("select receiveMaterialID,receiveMaterialName,\n" +
            " sum(if(classType = '夜班',usedNumberTransition1,0)) as usedNumberTransition1,\n" +
            " sum(if(classType = '白班',reveiveType,0)) as reveiveType, sum(if(classType = '夜班',usedNumberTransition2,0)) as usedNumberTransition2,\n" +
            " sum(receiveNumber) as receiveNumber, sum(receiveMaterialNumber1) as receiveMaterialNumber1, sum(receiveMaterialNumber2) as receiveMaterialNumber2, sum(receiveMaterialNumber3) as receiveMaterialNumber3,\n" +
            " sum(usedNumber) as usedNumber  " +
            "from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID =#{processID} and dayTime = #{dayTime} and receiveMaterialName is not null " +
            " group by receiveMaterialID order by receiveMaterialName")
    List<Map<Object, Object>> getFBDailyUsedInfoAllDayRecord(String plantID, String processID, String dayTime, String classType);

    @Select("select productionMaterialName,productionMaterialID,sum(productionNumber) as productionNumber ,sum(planDailyProduction) as planDailyProduction,\n" +
            " sum(if(classType = '夜班',lastInventory,0)) as lastInventory,\n" +
            " sum(if(classType = '白班',currentInventory,0)) as currentInventory, sum(if(classType = '夜班',inventoryTransition1,0)) as inventoryTransition1,\n" +
            " round(ifnull(sum(productionNumber) /sum(planDailyProduction),1) *100,2) as ratioFinish ,\n" +
            "   sum(grantNumber) as grantNumber, sum(grantNumberTransition1) as grantNumberTransition1, sum(grantNumberTransition2) as grantNumberTransition2, sum(grantNumberTransition3) as grantNumberTransition3 \n" +
            " from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime = #{dayTime} and reveiveType is null and productionMaterialName is not null  \n" +
            " group by productionMaterialID order by productionMaterialName  ")
    List<Map<Object, Object>> getBBDailyProductionAllDayRecord(String plantID, String processID, String dayTime, String classType);

    @Select("select receiveMaterialID,receiveMaterialName,\n" +
           " sum(usedNumber) as usedNumber ,sum(scrapNumber) as scrapNumber,sum(weightNumber) as weightNumber   " +
            "from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime = #{dayTime} and reveiveType is null and receiveMaterialName is not null " +
            " group by receiveMaterialID order by receiveMaterialName")
    List<Map<Object, Object>> getBBDailyUsedInfoAllDayRecord(String plantID, String processID, String dayTime, String classType);

    @Select("select productionMaterialID,productionMaterialName,receiveMaterialID,receiveMaterialName,\n" +
            " sum(usedNumber) as usedNumber    " +
            "from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime = #{dayTime} and reveiveType = 'BBYLMX' and receiveMaterialName is not null " +
            " group by productionMaterialName,receiveMaterialID order by productionMaterialName,receiveMaterialName")
    List<Map<Object, Object>> getBBDailyUsedToInputAllDayRecord(String plantID, String processID, String dayTime, String classType);
}