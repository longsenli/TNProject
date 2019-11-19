package com.tnpy.mes.service.staffWorkDiaryService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-11-17 11:47
 */
public interface IStaffWorkDiaryService {
    public TNPYResponse getStaffAttendanceInfo(String plantID, String processID, String lineID,String classType, String staffID, String startTime, String endTime);
    public TNPYResponse insertStaffComeAttendanceInfo(String qrCode,String staffID,String staffName,String classType1,String classType2,String dayTime);
    public TNPYResponse insertStaffGoAttendanceInfo(String qrCode,String staffID);
    public TNPYResponse deleteStaffAttendanceInfo(String id);
    public TNPYResponse confirmStaffAttendanceInfo(String staffID,String staffName,String recordID);
}
