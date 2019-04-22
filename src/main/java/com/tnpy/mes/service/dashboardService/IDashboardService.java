package com.tnpy.mes.service.dashboardService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-04-22 10:27
 */
public interface IDashboardService {
    public TNPYResponse getDailyProduction(String plantID ,String processID,String queryTypeID,String startTime,String endTime);
}
