package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.materialService.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/29 8:40
 */
@RestController
@RequestMapping("/api/material")
public class MaterialController {

    @Autowired
    private IMaterialService materialService ;

    @RequestMapping(value = "/getmaterialrecord")
    public TNPYResponse getMaterialRecord(String expendOrderID ) {
       return materialService.getMaterialRecord(expendOrderID);
    }

    @RequestMapping(value = "/getusablematerial")
    public TNPYResponse getUsableMaterial(String plantID,String materialID,String expendOrderID ) {
        return materialService.getUsableMaterial(plantID,materialID, expendOrderID);
    }

    @RequestMapping(value = "/gainmaterialrecord")
    public TNPYResponse gainMaterialRecord(String materialRecordIDListStr,String materialOrderID,String expendOrderID,String outputter ) {
        return materialService.gainMaterialRecord(materialRecordIDListStr,materialOrderID,expendOrderID,outputter);
    }

    @RequestMapping(value = "/gainpartmaterialrecord")
    public TNPYResponse gainPartMaterialRecord(String materialRecordID,String materialOrderID,String number,String expendOrderID,String outputter ) {
        return materialService.gainPartMaterialRecord(materialRecordID,materialOrderID,number,expendOrderID,outputter);
    }

    @RequestMapping(value = "/gainmaterialbyqr")
    public TNPYResponse gainMaterialByQR(String qrCode,String expendOrderID,String outputter ) {
        return materialService.gainMaterialByQR(qrCode,expendOrderID,outputter);
    }

    @RequestMapping(value = "/orderoutputstatistics")
    public TNPYResponse orderOutputStatistics( String startTime,String endTime,String plantID,String processID,String lineID ) {
        return  materialService.orderOutputStatistics( startTime, endTime, plantID, processID, lineID);
    }

    @RequestMapping(value = "/orderremnantproductstatistics")
    public TNPYResponse orderRemnantProductStatistics( String startTime,String endTime,String plantID,String processID,String lineID ) {
        return  materialService.orderRemnantProductStatistics( startTime, endTime, plantID, processID, lineID);
    }

    @RequestMapping(value = "/batterystatisinventory")
    public TNPYResponse batteryStatisInventory( String startTime,String endTime,String plantID ) {
        return  materialService.batteryStatisInventory( startTime, endTime, plantID);
    }
}
