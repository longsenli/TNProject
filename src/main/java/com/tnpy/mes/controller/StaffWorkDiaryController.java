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
    public TNPYResponse getStaffAttendanceInfo(String plantID,String processID,String lineID,String staffID,String startTime,String endTime)
    {
        return  staffWorkDiaryService.getStaffAttendanceInfo(plantID, processID, lineID, staffID, startTime, endTime);
    }

}
