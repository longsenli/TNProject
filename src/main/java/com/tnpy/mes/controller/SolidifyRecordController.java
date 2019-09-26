package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.solidifyRecord.ISolidifyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/15 13:16
 */
@RestController
@RequestMapping("/api/solidifyrecord")
public class SolidifyRecordController {
    @Autowired
    private ISolidifyRecordService solidifyRecordService ;


    @RequestMapping(value = "/selectbyroom")
    public TNPYResponse getSolidifyRecordByRoom(String plantID,String roomID)
    {
        return  solidifyRecordService.getSolidifyRecordByRoom(plantID,roomID);
    }
    @RequestMapping(value = "/addsolidifyrecord")
    public TNPYResponse addSolidifyRecord(String id,String status,String recorder,String roomID)
    {
        return  solidifyRecordService.addSolidifyRecord(id,status,recorder, roomID);
    }

    @RequestMapping(value = "/getsolidifyrecordbyparam")
    public TNPYResponse getSolidifyRecordByParam(String plantID,String roomID,String solidifyStepID,String startTime,String endTime)
    {
        return  solidifyRecordService.getSolidifyRecordByParam(plantID,roomID,solidifyStepID,startTime,endTime);
    }

    @RequestMapping(value = "/getinsolidifyroombyparam")
    public TNPYResponse getInSolidifyRoomByParam(String plantID,String roomID)
    {
        return  solidifyRecordService.getInSolidifyRoomByParam(plantID,roomID);
    }

    @RequestMapping(value = "/getInSolidifyRoomByParamNew")
    public TNPYResponse getInSolidifyRoomByParamNew(String plantID,String roomID,String status)
    {
        return  solidifyRecordService.getInSolidifyRoomByParamNew(plantID,roomID,status);
    }

    @RequestMapping(value = "/putinsolidifyroom")
    public TNPYResponse putinSolidifyRoom(String roomID,String orderIDList,String operatorName,String roomName)
    {
        return  solidifyRecordService.putinSolidifyRoom(roomID,orderIDList,operatorName,roomName);
    }

    @RequestMapping(value = "/changesolidifystatus")
    public TNPYResponse changeSolidifyStatus(String roomID,String orderIDList,String operatorName,String status)
    {
        return  solidifyRecordService.changeSolidifyStatus(roomID,orderIDList,operatorName,status);
    }

    @RequestMapping(value = "/changeAllSolidifyStatusAuto")
    public TNPYResponse changeAllSolidifyStatusAuto(String roomID,String operatorName)
    {
        return  solidifyRecordService.changeAllSolidifyStatusAuto(roomID,operatorName);
    }

    @RequestMapping(value = "/uninputSolidifyRoom")
    public TNPYResponse uninputSolidifyRoom(String plantID,String startTime,String endTime)
    {
        return solidifyRecordService.uninputSolidifyRoom(plantID,startTime,endTime);
    }
}

