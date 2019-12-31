package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyLineProductionDetailRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
@Component
public interface DailyLineProductionDetailRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyLineProductionDetailRecord record);

    int insertSelective(DailyLineProductionDetailRecord record);

    DailyLineProductionDetailRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyLineProductionDetailRecord record);

    int updateByPrimaryKey(DailyLineProductionDetailRecord record);


    @Select("select count(1) from tb_dailylineproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType =#{classType}")
    int selectConfirmNumber(String plantID,String processID,String dayTime,String classType );

    @Select("select * from tb_dailylineproductiondetailrecord where plantID = #{plantID} and processID = #{processID} and dayTime =#{dayTime} and classType =#{classType} order by materialName")
    List<Map<Object, Object>> getDailyLineProductionDetailRecord(String plantID, String processID, String dayTime, String classType);

}