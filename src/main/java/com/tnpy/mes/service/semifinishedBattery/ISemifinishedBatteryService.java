package com.tnpy.mes.service.semifinishedBattery;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/12 10:12
 */
public interface ISemifinishedBatteryService {
    public TNPYResponse addScrapBattery(String jsonStr);
    public TNPYResponse getScrapBatteryByline(String lineID);
    public TNPYResponse deleteScrapBattery(String batteryID);

    public TNPYResponse addRepairBattery( String jsonStr,String type);
    public TNPYResponse getRepairBatteryByline(String lineID);
    public TNPYResponse deleteRepairBattery(String batteryID);
}
