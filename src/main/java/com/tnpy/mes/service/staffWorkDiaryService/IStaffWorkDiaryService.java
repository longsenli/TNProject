package com.tnpy.mes.service.staffWorkDiaryService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-11-17 11:47
 */
public interface IStaffWorkDiaryService {
    public TNPYResponse getStaffAttendanceInfo(String plantID, String processID, String lineID,String classType, String staffID, String startTime, String endTime);
    public TNPYResponse insertStaffComeAttendanceInfo(String qrCode,String staffID,String staffName,String classType1,String classType2,String dayTime,String workContent, String teamType);
    public TNPYResponse insertStaffGoAttendanceInfo(String qrCode,String staffID);
    public TNPYResponse deleteStaffAttendanceInfo(String id);
    public TNPYResponse confirmStaffAttendanceInfo(String staffID,String staffName,String recordID);

    public TNPYResponse getTMPProductionWageInfo(String plantID,String processID,String lineID,String dayString,String classType);
    public TNPYResponse confirmProductionWageInfo( String recordJsonString,String verifierID,String verifierName);
    public TNPYResponse getFinalProductionWageInfo(String plantID,String processID,String dayString,String classType);
    public TNPYResponse deleteConfirmProductionWageRecord(String plantID,String processID,String dayString,String classType);

    public TNPYResponse getLocationQRInfo(String QRCode);

    public TNPYResponse getShelfWageDetail(String staffID,String startTime,String endTime);
    public TNPYResponse getShelfDailyTMPDetail(String staffID,String dayTime);

    public TNPYResponse getTMPDailyProductionDetailRecord(String plantID,String processID,String dayTime,String classType);
    public TNPYResponse getTMPDailyProductionSummaryRecord(String plantID,String processID,String dayTime,String classType);

    public TNPYResponse saveDailyLineProductionDetailRecord( String jsonStr);
    public TNPYResponse saveDailyProcessProductionDetailRecord( String jsonStr);

    public TNPYResponse getDailyLineProductionDetailRecord(String plantID,String processID,String dayTime,String classType);
    public TNPYResponse getDailyProcessProductionDetailRecord(String plantID,String processID,String dayTime,String classType);

    public TNPYResponse getStaffAttendanceSummary(String plantID,String processID,String lineID,String startTime,String endTime,String teamType);
}
