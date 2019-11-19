package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.staffWorkDiaryService.IStaffWorkDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public TNPYResponse insertStaffComeAttendanceInfo(String qrCode,String staffID,String staffName,String classType1,String classType2,String dayTime)  // 1 上机扫码，  2 下机扫码
    {
        return  staffWorkDiaryService.insertStaffComeAttendanceInfo(qrCode, staffID,staffName,classType1,classType2,dayTime);
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
}
