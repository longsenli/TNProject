package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.wageManageService.IWageManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-09-04 9:40
 */
@RestController
@RequestMapping("/api/wage")
public class WageController {
    @Autowired
    private IWageManageService wageManageService ;

    @RequestMapping(value = "/getProductionWageDetail")
    public TNPYResponse getProductionWageDetail(String plantID,String processID,String staffName,String startTime,String endTime )
    {
        return  wageManageService.getProductionWageDetail(plantID,processID,staffName,startTime,endTime);
    }

    @RequestMapping(value = "/getRewardingPunishmentDetail")
    public TNPYResponse getRewardingPunishmentDetail(String plantID, String processID, String staffName, String startTime, String endTime)
    {
        return  wageManageService.getRewardingPunishmentDetail(plantID,processID,staffName,startTime,endTime);
    }

    @RequestMapping(value = "/changeRewardingPunishmentDetail")
    public TNPYResponse changeRewardingPunishmentDetail(@RequestBody String jsonStr ) {
        return  wageManageService.changeRewardingPunishmentDetail(jsonStr);
    }

    @RequestMapping(value = "/deleteRewardingPunishmentDetail")
    public TNPYResponse deleteRewardingPunishmentDetail( String recordID ) {
        return  wageManageService.deleteRewardingPunishmentDetail(recordID);
    }


    @RequestMapping(value = "/getPaystubDetail")
    public TNPYResponse getPaystubDetail(String plantID, String processID, String staffName, String startTime, String endTime)
    {
        return  wageManageService.getPaystubDetail(plantID,processID,staffName,startTime,endTime);
    }

    @RequestMapping(value = "/changePaystubDetail")
    public TNPYResponse changePaystubDetail(@RequestBody String jsonStr ) {
        return  wageManageService.changePaystubDetail(jsonStr);
    }

    @RequestMapping(value = "/deletePaystubDetail")
    public TNPYResponse deletePaystubDetail( String recordID ) {
        return  wageManageService.deletePaystubDetail(recordID);
    }
}
