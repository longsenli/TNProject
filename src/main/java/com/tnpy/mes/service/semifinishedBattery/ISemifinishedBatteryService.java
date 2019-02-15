package com.tnpy.mes.service.semifinishedBattery;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/12 10:12
 */
public interface ISemifinishedBatteryService {
    public TNPYResponse addScrapBattery(String jsonStr);
    public TNPYResponse getScrapBatteryByline(String lineID,String plantID);
    public TNPYResponse deleteScrapBattery(String batteryID);

    public TNPYResponse addRepairBattery( String jsonStr,String type);
    public TNPYResponse getRepairBatteryByline(String lineID,String plantID);
    public TNPYResponse deleteRepairBattery(String batteryID);

    public TNPYResponse addBorrowReturnRecord( String jsonStr);
    public TNPYResponse getBorrowReturnRecord(String outPlantID,String inPlantID,String startTime,String endTime,String batteryType);
    public TNPYResponse deleteBorrowReturnRecord(String id);
}
