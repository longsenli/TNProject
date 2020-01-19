package com.tnpy.mes.mapper.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component

public interface DashboardMapper {
    @Select("${sqlStr}")
    List<Map<Object, Object>> getDailyProduction(@Param("sqlStr") String sqlStr);
    
    @Select("${sqlStr}")
    List<LinkedHashMap<Object, Object>> queryDef(@Param("sqlStr") String sqlStr);
    
    @Select("${sql}")
    List<Map<Object, Object>> getNowInDryingKilnjz(@Param("sql") String sql);

    @Select("${sql}")
    List<Map<Object, Object>> getInventoryInfo(@Param("sql") String sql);

//
//    @Select("select * from ( \n" +
//            "select  staffName,staffID, case when ANumber < BNumber  then 'B' else 'A' end as classType from (\n" +
//            "select staffName,staffID,sum(if(extd2 ='A',1,0)) as ANumber,sum(if(extd2 ='B',1,0)) as BNumber from tb_staffattendancedetail  \n" +
//            "where plantID = #{plantID} and processID = #{processID} and dayTime >= #{startTime} and dayTime <= #{endTime}   and verifyTime is not null group by staffName,staffID ) a \n" +
//            ") b left join  \n" +
//            "(select materialID,materialName,shelfProduction,staffID,dayTime,verifierName  from tb_dailyproductionandwagedetail \n" +
//            "where dayTime >= #{startTime} and dayTime <= #{endTime} and  plantID = #{plantID} and processID = #{processID} ) c on b.staffID = c.staffID   order by classType, CONVERT(b.staffName using gbk),c.dayTime,materialName\n")
//    List<Map<Object, Object>> getStaffInfoDetail( String plantID,String processID,String startTime,String endTime);


    @Select(  "select  verifierName, case when ANumber < BNumber  then 'B' else 'A' end as classType,case when ANumber < BNumber  then BNumber else ANumber end as classNumber from (\n" +
            "select verifierName,sum(if(extd2 ='A',1,0)) as ANumber,sum(if(extd2 ='B',1,0)) as BNumber from tb_staffattendancedetail  \n" +
            "where plantID = #{plantID} and processID = #{processID} and dayTime >= #{startTime} and dayTime <= #{endTime} and verifyTime is not null group by verifierName ) a" )
    List<Map<Object, Object>> getClassTypeDetail( String plantID,String processID,String startTime,String endTime);


    @Select(  " select * from ( select  staffName,staffID, case when ANumber < BNumber  then 'B' else 'A' end as classType from ( \n" +
            "select staffName,staffID,sum(if(extd2 ='A',1,0)) as ANumber,sum(if(extd2 ='B',1,0)) as BNumber from tb_staffattendancedetail  \n" +
            "where plantID = #{plantID} and processID = #{processID} and dayTime >= #{startTime} and dayTime <= #{endTime}   " +
            " and verifyTime is not null group by staffName,staffID   ) a ) b order by  order by classType,CONVERT(staffName using gbk ) " )
    List<Map<Object, Object>> getStaffInfoDetail( String plantID,String processID,String startTime,String endTime);

    @Select(    "select staffName,staffID,classType1,DATE_FORMAT(dayTime,   '%Y-%m-%d') as dayTime,case when classType2 = '全班' then 1 else 0.5 end as duration from tb_staffattendancedetail  \n" +
            "where plantID = #{plantID} and processID = #{processID} and dayTime >= #{startTime} and dayTime <= #{endTime}   " +
            " and verifyTime is not null  order by CONVERT(staffName using gbk ) ,dayTime " )
    List<Map<Object, Object>> getStaffAttendanceDetailRecord( String plantID,String processID,String startTime,String endTime);

    @Select(   "select materialID,materialName,shelfProduction,univalence,staffID,DATE_FORMAT(dayTime,   '%Y-%m-%d') as dayTime,verifierName  from tb_dailyproductionandwagedetail \n" +
            "where dayTime >= #{startTime} and dayTime <= #{endTime} and  plantID = #{plantID} and processID = #{processID}   order by CONVERT(staffName using gbk),dayTime,materialName\n")
    List<Map<Object, Object>> staffProductionDetailRecord( String plantID,String processID,String startTime,String endTime);
}