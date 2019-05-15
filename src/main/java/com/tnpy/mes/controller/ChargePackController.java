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
}
