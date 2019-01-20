package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WorkShiftRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WorkShiftRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(WorkShiftRecord record);

    int insertSelective(WorkShiftRecord record);

    WorkShiftRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WorkShiftRecord record);

    int updateByPrimaryKey(WorkShiftRecord record);

    @Select("select * from tb_workShiftRecord where plantID= #{plantID} and processID = #{processID} and workShift = #{workShift} and dayNight=#{dayNight}")
    List<WorkShiftRecord> getWorkShiftRecord(String plantID,String processID,String workShift,String dayNight );
}