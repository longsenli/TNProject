package com.tnpy.mes.service.semifinishedBattery;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/12 10:12
 */
public interface ISemifinishedBatteryService {
    public TNPYResponse addScrapBattery(String jsonStr,int scrapNum);
    public TNPYResponse getScrapBatteryByline(String lineID,String plantID);
    public TNPYResponse deleteScrapBattery(String batteryID);

    public TNPYResponse addRepairBattery( String jsonStr,String type,int number);
    public TNPYResponse getRepairBatteryByline(String lineID,String plantID);
    public TNPYResponse deleteRepairBattery(String batteryID);

    public TNPYResponse addBorrowReturnRecord( String jsonStr);
    public TNPYResponse getBorrowReturnRecord(String outPlantID,String inPlantID,String startTime,String endTime,String batteryType);
    public TNPYResponse deleteBorrowReturnRecord(String id);

    public TNPYResponse getBatteryShipmentnumRecord(String plantID,String typeID);
    public TNPYResponse addBatteryShipmentnumRecord(String jsonStr);
    public TNPYResponse deleteBatteryShipmentnumRecord(String id);
}
