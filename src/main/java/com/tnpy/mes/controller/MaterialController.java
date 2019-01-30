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
    public TNPYResponse getUsableMaterial(String plantID,String materialID ) {
        return materialService.getUsableMaterial(plantID,materialID);
    }

    @RequestMapping(value = "/gainmaterialrecord")
    public TNPYResponse gainMaterialRecord(String materialIDListStr,String expendOrderID,String outputter ) {
        return materialService.gainMaterialRecord(materialIDListStr,expendOrderID,outputter);
    }

    @RequestMapping(value = "/gainpartmaterialrecord")
    public TNPYResponse gainPartMaterialRecord(String materialID,String number,String expendOrderID,String outputter ) {
        return materialService.gainPartMaterialRecord(materialID,number,expendOrderID,outputter);
    }

    @RequestMapping(value = "/gainmaterialbyqr")
    public TNPYResponse gainMaterialByQR(String qrCode,String expendOrderID,String outputter ) {
        return materialService.gainMaterialByQR(qrCode,expendOrderID,outputter);
    }
}
