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
}
