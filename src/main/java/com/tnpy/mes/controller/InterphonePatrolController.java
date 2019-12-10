package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.interphonePatrolService.IInterphonePatrolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-12-09 8:31
 */
@RestController
@RequestMapping("/api/interphonePatrol")
public class InterphonePatrolController {
    @Autowired
    IInterphonePatrolService interphonePatrolService;

    @RequestMapping(value = "/getInterphonePatrolLocationInfo")
    public TNPYResponse getInterphonePatrolLocationInfo(String plantID,String processID) {

        return  interphonePatrolService.getInterphonePatrolLocationInfo(plantID,processID);
    }

    @RequestMapping(value = "/addInterphonePatrolRecord")
    public TNPYResponse addInterphonePatrolRecord( String recordJson) {

        return  interphonePatrolService.addInterphonePatrolRecord(recordJson);
    }

    @RequestMapping(value = "/deleteInterphonePatrolRecord")
    public TNPYResponse deleteInterphonePatrolRecord(String id) {

        return  interphonePatrolService.deleteInterphonePatrolRecord(id);
    }

    @RequestMapping(value = "/getInterphonePatrolRecordDetail")  // 实际中是查询某一天全部的信息
    public TNPYResponse getInterphonePatrolRecordDetail(String plantID,String startTime,String endTime) {

        return  interphonePatrolService.getInterphonePatrolRecordDetail(plantID,startTime,endTime);
    }

    @RequestMapping(value = "/getInterphonePatrolRecordReport")// 实际中是查询某一天全部的信息
    public TNPYResponse getInterphonePatrolRecordReport(String plantID,String startTime,String endTime) {

        return  interphonePatrolService.getInterphonePatrolRecordReport(plantID,startTime,endTime);
    }
}
