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


    @Select("select * from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType =#{classType}")
    List<Map<Object, Object>> getDailyProcessProductionDetailRecord(String plantID, String processID, String dayTime, String classType);


    @Select("select * from tb_dailyprocessproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} ")
    List<Map<Object, Object>> getDailyProcessProductionDetailRecord(String plantID, String processID, String dayTime, String classType);
}