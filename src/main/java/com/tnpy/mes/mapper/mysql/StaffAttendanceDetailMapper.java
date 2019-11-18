package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.StaffAttendanceDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface StaffAttendanceDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(StaffAttendanceDetail record);

    int insertSelective(StaffAttendanceDetail record);

    StaffAttendanceDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StaffAttendanceDetail record);

    int updateByPrimaryKey(StaffAttendanceDetail record);

    List<StaffAttendanceDetail> selectScanRecord(String staffID);

    @Select("select * from tb_staffattendancedetail ${filter} ")
    List<StaffAttendanceDetail> selectRecordByFilter(@Param("filter") String filter);

    @Update("update tb_staffattendancedetail set goTime = now() where goTime is null and staffID = #{staffID} and (lineID = #{qrCode} or worklocationID = #{qrCode} )")
    int updateStaffGoAttendanceInfo(String qrCode,String staffID);

    @Select("select id,plantID,processID,lineID,worklocationID,staffName,classType1,classType2,date_format(dayTime, '%Y-%m-%d')  as dayTime,DATE_FORMAT(comeTime,'%Y-%m-%d %H:%i:%s') as comeTime," +
            " DATE_FORMAT(goTime,'%Y-%m-%d %H:%i:%s') as goTime,verifierName, DATE_FORMAT(verifyTime,'%Y-%m-%d %H:%i:%s') as  verifyTime from tb_staffattendancedetail ${filter}  ")
    List<Map<Object,Object>> selectMapRecordByFilter(@Param("filter") String filter);

}