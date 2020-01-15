package com.tnpy.mes.service.workingScoreService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2020-01-15 14:12
 */
public interface IWorkingScoreService {
    public TNPYResponse getWorkingScoreDetailRecord(String staffName,String startTime,String endTime);
    public TNPYResponse getWorkingScoreDailySummaryRecord(String staffName,String startTime,String endTime);
    public TNPYResponse getWorkingScorePeriodSummaryRecord(String staffName,String startTime,String endTime);
}
