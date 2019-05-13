package com.tnpy.mes.service.tidyBatteryService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-05-06 14:39
 */
public interface ITidyBatteryService {
    public TNPYResponse getTidyBatteryRecord(String plantID, String processID) ;
    public TNPYResponse deteteTidyBatteryRecord(String id);
    public TNPYResponse changeTidyBatteryRecord(String jsonStr);
}
