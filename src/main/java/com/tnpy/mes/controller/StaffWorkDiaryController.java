package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.staffWorkDiaryService.IStaffWorkDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-11-17 11:46
 */
@RestController
@RequestMapping("/api/staffWorkDiary")
public class StaffWorkDiaryController {
    @Autowired
    private IStaffWorkDiaryService staffWorkDiaryService  ;

    @RequestMapping(value = "/getStaffAttendanceInfo")
    public TNPYResponse getStaffAttendanceInfo(String plantID,String processID,String lineID,String classType,String staffID,String startTime,String endTime)
    {
        return  staffWorkDiaryService.getStaffAttendanceInfo(plantID, processID, lineID,classType, staffID, startTime, endTime);
    }

    @RequestMapping(value = "/insertStaffComeAttendanceInfo")
    public TNPYResponse insertStaffComeAttendanceInfo(String qrCode,String staffID,String staffName,String classType1,String classType2,String dayTime,String workContent,@RequestParam(defaultValue = "-1") String teamType)  // 1 上机扫码，  2 下机扫码
    {
        return  staffWorkDiaryService.insertStaffComeAttendanceInfo(qrCode, staffID,staffName,classType1,classType2,dayTime,workContent,teamType);
    }
    @RequestMapping(value = "/insertStaffGoAttendanceInfo")
    public TNPYResponse insertStaffGoAttendanceInfo(String qrCode,String staffID)  // 1 上机扫码，  2 下机扫码
    {
        return  staffWorkDiaryService.insertStaffGoAttendanceInfo(qrCode, staffID);
    }
    @RequestMapping(value = "/deleteStaffAttendanceInfo")
    public TNPYResponse deleteStaffAttendanceInfo(String id)
    {
        return  staffWorkDiaryService.deleteStaffAttendanceInfo(id);
    }
    @RequestMapping(value = "/confirmStaffAttendanceInfo")
    public TNPYResponse confirmStaffAttendanceInfo(String staffID,String staffName,String recordID)
    {
        return  staffWorkDiaryService.confirmStaffAttendanceInfo(staffID,staffName,recordID);
    }

    @RequestMapping(value = "/getTMPProductionWageInfo")
    public TNPYResponse getTMPProductionWageInfo(String plantID,String processID,String lineID,String dayString,String classType)
    {
        return  staffWorkDiaryService.getTMPProductionWageInfo(plantID,processID,lineID,dayString,classType);
    }

    @RequestMapping(value = "/getFinalProductionWageInfo")
    public TNPYResponse getFinalProductionWageInfo(String plantID,String processID,String dayString,String classType)
    {
        return  staffWorkDiaryService.getFinalProductionWageInfo(plantID,processID,dayString,classType);
    }


    @RequestMapping(value = "/confirmProductionWageInfo")
    public TNPYResponse confirmProductionWageInfo( String recordJsonString,String verifierID,String verifierName)
    {
        return  staffWorkDiaryService.confirmProductionWageInfo(recordJsonString,verifierID,verifierName);
    }

    @RequestMapping(value = "/deleteConfirmProductionWageRecord")
    public TNPYResponse deleteConfirmProductionWageRecord(String plantID,String processID,String dayString,String classType)
    {
        return  staffWorkDiaryService.deleteConfirmProductionWageRecord(plantID,processID,dayString,classType);
    }

    @RequestMapping(value = "/getLocationQRInfo")
    public TNPYResponse getLocationQRInfo(String QRCode)
    {
        return  staffWorkDiaryService.getLocationQRInfo(QRCode);
    }

    @RequestMapping(value = "/getShelfWageDetail")
    public TNPYResponse getShelfWageDetail(String staffID,String startTime,String endTime)
    {
        return  staffWorkDiaryService.getShelfWageDetail(staffID,startTime,endTime);
    }

    @RequestMapping(value = "/getShelfDailyTMPDetail")
    public TNPYResponse getShelfDailyTMPDetail(String staffID,String dayTime)
    {
        return  staffWorkDiaryService.getShelfDailyTMPDetail(staffID,dayTime);
    }

    @RequestMapping(value = "/getTMPDailyProductionDetailRecord")
    public TNPYResponse getTMPDailyProductionDetailRecord(String plantID,String processID,String dayTime,String classType)
    {
        return  staffWorkDiaryService.getTMPDailyProductionDetailRecord(plantID,processID,dayTime,classType);
    }
    @RequestMapping(value = "/getTMPDailyProductionSummaryRecord")
    public TNPYResponse getTMPDailyProductionSummaryRecord(String plantID,String processID,String dayTime,String classType)
    {
        return  staffWorkDiaryService.getTMPDailyProductionSummaryRecord(plantID,processID,dayTime,classType);
    }

    @RequestMapping(value = "/saveDailyLineProductionDetailRecord")
    public TNPYResponse saveDailyLineProductionDetailRecord(@RequestBody String jsonStr)
    {
        return  staffWorkDiaryService.saveDailyLineProductionDetailRecord(jsonStr);
    }

    @RequestMapping(value = "/saveDailyProcessProductionDetailRecord")
    public TNPYResponse saveDailyProcessProductionDetailRecord(@RequestBody String jsonStr)
    {
        return  staffWorkDiaryService.saveDailyProcessProductionDetailRecord(jsonStr);
    }

    @RequestMapping(value = "/getDailyLineProductionDetailRecord")
    public TNPYResponse getDailyLineProductionDetailRecord(String plantID,String processID,String dayTime,String classType)
    {
        return  staffWorkDiaryService.getDailyLineProductionDetailRecord(plantID,processID,dayTime,classType);
    }

    @RequestMapping(value = "/getDailyProcessProductionDetailRecord")
    public TNPYResponse getDailyProcessProductionDetailRecord(String plantID,String processID,String dayTime,String classType)
    {
        return  staffWorkDiaryService.getDailyProcessProductionDetailRecord(plantID,processID,dayTime,classType);
    }

    @RequestMapping(value = "/getStaffAttendanceSummary")
    public TNPYResponse getStaffAttendanceSummary(String plantID,String processID,String lineID,String startTime,String endTime,String teamType)
    {
        return  staffWorkDiaryService.getStaffAttendanceSummary(plantID,processID,lineID,startTime,endTime,teamType);
    }
}


