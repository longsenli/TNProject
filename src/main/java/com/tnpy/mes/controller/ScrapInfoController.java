package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.scrapInfoService.IScrapInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/29 9:06
 */
@RestController
@RequestMapping("/api/scrapinfo")
public class ScrapInfoController {

    @Autowired
    private IScrapInfoService scrapInfoService;
    @RequestMapping(value = "/getscrapinfo")
    public TNPYResponse getScrapInfo(String plantID , String processID,String lineID, String startTime, String endTime ) {
        return scrapInfoService.getScrapInfo(plantID,processID,lineID, startTime,endTime);
    }
    @RequestMapping(value = "/savescrapinfo")
    public TNPYResponse saveScrapInfo(@RequestBody String strJson ) {

        return scrapInfoService.saveScrapInfo(strJson);
    }

    @RequestMapping(value = "/getmaterialscrapinfo")
    public TNPYResponse getMaterialScrapInfo( String materialID, String orderID ) {
        return scrapInfoService.getMaterialScrapInfo(materialID,orderID);
    }
}