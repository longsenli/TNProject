package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WorkShiftRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    @Insert("Insert into  tb_workshiftrecord (id,plantID,workShift,dayNight,ABShift,\n" +
            "processID,lineID,staffID,staffName,timeMeasure,status) (select \n" +
            "uuid(),a.plantID,'${dateStr}',case b.ABShift when '${YBTeam}' then '夜班' else '白班' end ,b.ABShift,a.processID,a.lineID,a.staffID,a.staffName,1,1\n" +
            "from tb_workshift a left join tb_shiftteam b on a.shiftTeamID = b.id where a.status='1' )")
    int automaticScheduling(@Param("YBTeam") String YBTeam,@Param("dateStr")  String dateStr);

    @Select("select ABShift from tb_workshiftrecord where workShift = #{dateStr} limit 1")
    String getYBTeam(String dateStr);
}