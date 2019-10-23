package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.plateWeighService.IPlateWeighService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-09-03 9:50
 */

@RestController
@RequestMapping("/api/plateweigh")
public class PlateWeighController {
    @Autowired
    private IPlateWeighService plateWeighService ;

    @RequestMapping(value = "/getPlateWeighRecord")
    public TNPYResponse getPlateWeighRecord(String plantID, @RequestParam(defaultValue = "-1") String balanceID,String staffName, String materialName, String startTime, String endTime ) {
        return  plateWeighService.getPlateWeighRecord( plantID,balanceID, staffName,materialName, startTime, endTime );
    }

    @RequestMapping(value = "/getPlateWeighBasicData")
    public TNPYResponse getPlateWeighBasicData(String plantID) {
        return  plateWeighService.getPlateWeighBasicData( plantID );
    }


    @RequestMapping(value = "/getRealtimeRecord")
    public TNPYResponse getRealtimeRecord(String plantID, String staffName, String materialName,@RequestParam(defaultValue = "-1") String balanceID ) {
        return  plateWeighService.getRealtimeRecord( plantID, staffName,materialName ,balanceID);
    }

    @RequestMapping(value = "/getQualifiedRateInfo")
    public TNPYResponse getQualifiedRateInfo(String plantID, String balanceID,String staffName, String materialName,String weighQualifyRange, String startTime, String endTime) {
        return  plateWeighService.getQualifiedRateInfo( plantID,balanceID, staffName,materialName,weighQualifyRange, startTime, endTime );
    }
}
