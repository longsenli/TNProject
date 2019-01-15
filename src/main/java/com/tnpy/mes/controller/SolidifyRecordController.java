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
}
