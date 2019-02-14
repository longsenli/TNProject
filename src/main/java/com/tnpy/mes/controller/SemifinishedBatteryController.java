package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.semifinishedBattery.ISemifinishedBatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/12 10:06
 */
@RestController
@RequestMapping("/api/semifinishedbattery")
public class SemifinishedBatteryController {

    @Autowired
    private ISemifinishedBatteryService  semifinishedBatteryService;

    @RequestMapping(value = "/addscrapbattery")
    public TNPYResponse addScrapBattery(@RequestBody String jsonStr)
    {
        return  semifinishedBatteryService.addScrapBattery(jsonStr);
    }

    @RequestMapping(value = "/getscrapbatterybyline")
    public TNPYResponse getScrapBatteryByline(String lineID,String plantID)
    {
        return  semifinishedBatteryService.getScrapBatteryByline(lineID, plantID);
    }

    @RequestMapping(value = "/deletescrapbattery")
    public TNPYResponse deleteScrapBattery(String batteryID)
    {
        return  semifinishedBatteryService.deleteScrapBattery(batteryID);
    }

    @RequestMapping(value = "/addrepairbattery")
    public TNPYResponse addRepairBattery( String jsonStr,String type)
    {
        return  semifinishedBatteryService.addRepairBattery(jsonStr,type);
    }

    @RequestMapping(value = "/getrepairbatterybyline")
    public TNPYResponse getRepairBatteryByline(String lineID,String plantID)
    {
        return  semifinishedBatteryService.getRepairBatteryByline(lineID,plantID);
    }

    @RequestMapping(value = "/deleterepairbattery")
    public TNPYResponse deleteRepairBattery(String batteryID)
    {
        return  semifinishedBatteryService.deleteRepairBattery(batteryID);
    }
}
