package com.tnpy.mes.service.chargePackService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-05-06 14:39
 */
public interface IChargePackService {
    public TNPYResponse getChargingRackRecord(String plantID, String processID,String lineID,String locationID,String startTime,String endTime,String selectType) ;
    public TNPYResponse deteteChargingRackRecord(String id);
    public TNPYResponse changeChargingRackRecord( String jsonStr);
    public TNPYResponse pulloffChargingRackRecord( String jsonStr);

    public TNPYResponse getTidyBatteryRecord(String plantID, String processID,String lineID,String startTime,String endTime,String selectType);
    public TNPYResponse changeTidyBatteryRecord( String jsonStr);


    public TNPYResponse addPileTidyBatteryRecord( String jsonTidyRecord,String pileNum,String perPileMaterialNum,String storeLocation);
    public TNPYResponse getPileTidyBatteryRecord(String plantID, String processID,String lineID,String startTime,String endTime,String selectType);
    public TNPYResponse getPileRecordByPileID( String id);
    public TNPYResponse expendPileBatteryByPackage( String id,int packageNum,int totalNum);

    public TNPYResponse getBatteryGearLineInfo(String plantID, String startTime);
    public TNPYResponse getBatteryGearLineLocationInfo(String plantID,String lineID, String startTime);
    public TNPYResponse getBatteryGearRecordInfo(String plantID, String lineID,String workLocation,String altitude,String startTime);
}
