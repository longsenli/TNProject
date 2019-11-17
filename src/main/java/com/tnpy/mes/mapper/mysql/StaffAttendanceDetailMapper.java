package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.StaffAttendanceDetail;

public interface StaffAttendanceDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(StaffAttendanceDetail record);

    int insertSelective(StaffAttendanceDetail record);

    StaffAttendanceDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StaffAttendanceDetail record);

    int updateByPrimaryKey(StaffAttendanceDetail record);
}