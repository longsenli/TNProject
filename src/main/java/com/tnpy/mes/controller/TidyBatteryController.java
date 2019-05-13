package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.tidyBatteryService.ITidyBatteryService;
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
@RequestMapping(value ="/api/tidybattery")
public class TidyBatteryController {
    @Autowired
    private ITidyBatteryService tidyBatteryService;

    @RequestMapping(value = "/gettidybatteryrecord")
    public TNPYResponse getTidyBatteryRecord(String plantID, String processID) {
        return  tidyBatteryService.getTidyBatteryRecord(plantID,processID);
    }

    @RequestMapping(value = "/detetetidybatteryRecord")
    public TNPYResponse deteteTidyBatteryRecord(String id) {
        return  tidyBatteryService.deteteTidyBatteryRecord(id);
    }

    @RequestMapping(value = "/changetidybatteryrecord")
    public TNPYResponse changeTidyBatteryRecord(@RequestBody String jsonStr) {
        return  tidyBatteryService.changeTidyBatteryRecord(jsonStr);
    }

}
