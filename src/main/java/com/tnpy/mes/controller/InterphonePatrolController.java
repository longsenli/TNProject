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
}
