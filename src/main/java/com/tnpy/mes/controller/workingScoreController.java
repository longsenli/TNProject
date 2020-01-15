package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.workingScoreService.IWorkingScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2020-01-15 14:08
 */
@RestController
@RequestMapping("/api/workingScore")
public class workingScoreController {
    @Autowired
    private IWorkingScoreService workingScoreService ;

    @RequestMapping(value = "/getWorkingScoreDetailRecord")
    public TNPYResponse getWorkingScoreDetailRecord(String staffName,String startTime,String endTime)
    {
        return  workingScoreService.getWorkingScoreDetailRecord(staffName,startTime,endTime);
    }

    @RequestMapping(value = "/getWorkingScoreDailySummaryRecord")
    public TNPYResponse getWorkingScoreDailySummaryRecord(String staffName,String startTime,String endTime)
    {
        return  workingScoreService.getWorkingScoreDailySummaryRecord(staffName,startTime,endTime);
    }

    @RequestMapping(value = "/getWorkingScorePeriodSummaryRecord")
    public TNPYResponse getWorkingScorePeriodSummaryRecord(String staffName,String startTime,String endTime)
    {
        return  workingScoreService.getWorkingScorePeriodSummaryRecord(staffName,startTime,endTime);
    }
}
