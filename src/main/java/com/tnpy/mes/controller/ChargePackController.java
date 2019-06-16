package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.chargePackService.IChargePackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-05-06 14:37
 */
@RestController
@RequestMapping(value ="/api/chargepack")
public class ChargePackController {
    @Autowired
    private IChargePackService chargePackService;

    @RequestMapping(value = "/getchargingrackrecord")
    public TNPYResponse getChargingRackRecord(String plantID, String processID,String lineID,String locationID,String startTime,String endTime,String selectType) {
        return  chargePackService.getChargingRackRecord(plantID,processID,lineID, locationID, startTime, endTime,selectType);
    }

    @RequestMapping(value = "/detetechargingrackrecord")
    public TNPYResponse deteteChargingRackRecord(String id) {
        return  chargePackService.deteteChargingRackRecord(id);
    }

    @RequestMapping(value = "/changechargingrackrecord")
    public TNPYResponse changeChargingRackRecord(@RequestBody String jsonStr) {
        return  chargePackService.changeChargingRackRecord(jsonStr);
    }

    @RequestMapping(value = "/pulloffchargingrackrecord")
    public TNPYResponse pulloffChargingRackRecord(@RequestBody String jsonStr) {
        return  chargePackService.pulloffChargingRackRecord(jsonStr);
    }

    @RequestMapping(value = "/gettidybatteryrecord")
    public TNPYResponse getTidyBatteryRecord(String plantID, String processID,String lineID,String startTime,String endTime,String selectType) {
        return  chargePackService.getTidyBatteryRecord(plantID,processID,lineID, startTime, endTime,selectType);
    }

    @RequestMapping(value = "/changetidybatteryrecord")
    public TNPYResponse changeTidyBatteryRecord(@RequestBody String jsonStr) {
        return  chargePackService.changeTidyBatteryRecord(jsonStr);
    }


    @RequestMapping(value = "/getpilerecordbypileid")
    public TNPYResponse getPileRecordByPileID( String id) {
        return  chargePackService.getPileRecordByPileID(id);
    }

    @RequestMapping(value = "/expendpilebatterybypackage")
    public TNPYResponse expendPileBatteryByPackage( String id,int packageNum,int totalNum) {
        return  chargePackService.expendPileBatteryByPackage(id,packageNum,totalNum);
    }


    @RequestMapping(value = "/addpiletidybatteryrecord")
    public TNPYResponse addPileTidyBatteryRecord( String jsonTidyRecord,String pileNum,String perPileMaterialNum,String storeLocation) {
        return  chargePackService.addPileTidyBatteryRecord(jsonTidyRecord,pileNum,perPileMaterialNum,storeLocation);
    }

    @RequestMapping(value = "/getpiletidybatteryrecord")
    public TNPYResponse getPileTidyBatteryRecord(String plantID, String processID,String lineID,String startTime,String endTime,String selectType) {
        return  chargePackService.getPileTidyBatteryRecord(plantID,processID,lineID, startTime, endTime,selectType);
    }

    @RequestMapping(value = "/getbatterygearlineinfo")
    public TNPYResponse getBatteryGearLineInfo(String plantID, String startTime) {
        return  chargePackService.getBatteryGearLineInfo(plantID, startTime);
    }

    @RequestMapping(value = "/getbatterygearlinelocationinfo")
    public TNPYResponse getBatteryGearLineLocationInfo(String plantID,String lineID, String startTime) {
        return  chargePackService.getBatteryGearLineLocationInfo(plantID, lineID,startTime);
    }

    @RequestMapping(value = "/getbatterygearrecordinfo")
    public TNPYResponse getBatteryGearRecordInfo(String plantID, String lineID,String workLocation,String altitude,String startTime) {
        return  chargePackService.getBatteryGearRecordInfo(plantID,lineID,workLocation,altitude, startTime);
    }
}
