package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.epidemicManageService.IEpidemicManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2020-02-06 8:25
 */
@RestController
@RequestMapping(value ="/api/EpidemicManage")
public class EpidemicManageController {


        @Autowired
        private IEpidemicManageService epidemicManageService ;

        @RequestMapping(value = "/addShelfBehaviorRecord")
        public TNPYResponse addShelfBehaviorRecord(@RequestBody String jsonStr) {
            return  epidemicManageService.addShelfBehaviorRecord(jsonStr);
        }
    @RequestMapping(value = "/getShelfFilloutEpidemicRecord")
    public TNPYResponse getShelfFilloutEpidemicRecord( String identityID) {
        return  epidemicManageService.getShelfFilloutEpidemicRecord(identityID);
    }
}
