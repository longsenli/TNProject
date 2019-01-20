package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WorkShift;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WorkShiftMapper {
    int deleteByPrimaryKey(String id);

    int insert(WorkShift record);

    int insertSelective(WorkShift record);

    WorkShift selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WorkShift record);

    int updateByPrimaryKey(WorkShift record);

    @Select("select * from tb_workShift where plantID = #{plantID} and shiftTeamID = #{shiftTeamID}")
    List<WorkShift> getWorkShiftDetail(String plantID,String shiftTeamID );
}