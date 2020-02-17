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


    @RequestMapping(value = "/addNewStaffBasicInfo")
    public TNPYResponse addNewStaffBasicInfo(@RequestBody String jsonStr) {
        return  epidemicManageService.addNewStaffBasicInfo(jsonStr);
    }

    @RequestMapping(value = "/getShelfBasicInfoRecord")
    public TNPYResponse getShelfBasicInfoRecord( String name) {
        return  epidemicManageService.getShelfBasicInfoRecord(name);
    }

    @RequestMapping(value = "/getStaffEpidemicBasicInfo")
    public TNPYResponse getStaffEpidemicBasicInfo( String identityNo) {
        return  epidemicManageService.getStaffEpidemicBasicInfo(identityNo);
    }

    @RequestMapping(value = "/getStaffEpidemicBasicInfoByName")
    public TNPYResponse getStaffEpidemicBasicInfoByName( String name) {
        return  epidemicManageService.getStaffEpidemicBasicInfoByName(name);
    }

    @RequestMapping(value = "/addStaffEpidemicBasicInfo")
    public TNPYResponse addStaffEpidemicBasicInfo(@RequestBody String jsonStr) {
        return  epidemicManageService.addStaffEpidemicBasicInfo(jsonStr);
    }

    @RequestMapping(value = "/deleteStaffEpidemicBasicInfo")
    public TNPYResponse deleteStaffEpidemicBasicInfo( String identityNO) {
        return  epidemicManageService.deleteStaffEpidemicBasicInfo(identityNO);
    }


    @RequestMapping(value = "/getStaffEpidemicBasicInfoByDepartment")
    public TNPYResponse getStaffEpidemicBasicInfoByDepartment( String department) {
        return  epidemicManageService.getStaffEpidemicBasicInfoByDepartment(department);
    }

    @RequestMapping(value = "/getStaffEpidemicBasicDepartmentInfo")
    public TNPYResponse getStaffEpidemicBasicDepartmentInfo( ) {
        return  epidemicManageService.getStaffEpidemicBasicDepartmentInfo();
    }

    @RequestMapping(value = "/addStaffTMPTRecord")
    public TNPYResponse addStaffTMPTRecord(@RequestBody String jsonStr) {
        return  epidemicManageService.addStaffTMPTRecord(jsonStr);
    }

    @RequestMapping(value = "/getStaffTMPTRecord")
    public TNPYResponse getStaffTMPTRecord( String name,String department,String startTime,String endTime,String tmptType) {
        return  epidemicManageService.getStaffTMPTRecord(name, department, startTime, endTime, tmptType);
    }


    @RequestMapping(value = "/getStaffLatestEpidemicTMPTRecord")
    public TNPYResponse getStaffLatestEpidemicTMPTRecord( String name) {
        return  epidemicManageService.getStaffLatestEpidemicTMPTRecord(name);
    }
}
