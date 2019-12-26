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
    public TNPYResponse cancelChargingRackRecord(String id);

    public TNPYResponse getTidyBatteryRecord(String plantID, String processID,String lineID,String startTime,String endTime,String selectType);
    public TNPYResponse changeTidyBatteryRecord( String jsonStr);


    public TNPYResponse addPileTidyBatteryRecord( String jsonTidyRecord,String pileNum,String perPileMaterialNum,String storeLocation);
    public TNPYResponse getPileTidyBatteryRecord(String plantID, String processID,String lineID,String startTime,String endTime,String selectType);
    public TNPYResponse getPileRecordByPileID( String id);
    public TNPYResponse expendPileBatteryByPackage( String packagepileid,String remainpackageNum,String packagetotalNum, String packageNum,String plantID,
    		String processID,String lineID,String userID,String username);
    public TNPYResponse finishPileTidyBatteryRecord( String id,String remainpileNum,String piletotalNum, String partpileNum,String plantID,
    		String processID,String lineID,String userID,String username);
    
    public TNPYResponse getBatteryGearLineInfo(String plantID, String startTime);
    public TNPYResponse getBatteryGearLineLocationInfo(String plantID,String lineID, String startTime);
    public TNPYResponse getBatteryGearRecordInfo(String plantID, String lineID,String workLocation,String altitude,String startTime);

    public TNPYResponse getBatteryInventoryRecord(String plantID,String startTime,String endTime);
    public TNPYResponse changeBatteryInventoryRecord( String jsonStr);

    public TNPYResponse getPackageRecordDetail(String plantID,String lineID,String startTime,String endTime);

    public TNPYResponse getPackageRecord(String plantID, String processID,String lineID,String startTime,String endTime);
}
