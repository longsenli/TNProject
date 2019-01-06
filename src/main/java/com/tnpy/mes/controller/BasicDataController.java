package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.basicDataService.IBasicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/27 15:46
 */
@RestController
@RequestMapping("/api/basicdata")
public class BasicDataController {
    @Autowired
    private IBasicDataService basicDataService;

    @RequestMapping(value = "/getindustrialplant")
    public TNPYResponse getIndustrialPlant() {
       return  basicDataService.getIndustrialPlant();
    }

    @RequestMapping(value = "/getproductionprocess")
    public TNPYResponse getProductionProcess() {
        return  basicDataService.getProductionProcess();
    }

    @RequestMapping(value = "/getproductionline")
    public TNPYResponse getProductionLine(String plantID,String processID) {
        return  basicDataService.getProductionLine(plantID,processID);
    }


    @RequestMapping(value = "/getmaterial")
    public TNPYResponse getMaterial() {
        return  basicDataService.getMaterial();
    }

    @RequestMapping(value = "/getequipmenttype")
    public TNPYResponse getEquipmentType() {

        return  basicDataService.getEquipmentType();
    }

    @RequestMapping(value = "/getmaterialbyprocess")
    public TNPYResponse getMaterialByProcess(String processID) {

        return  basicDataService.getOutMaterialByProcess(processID);
    }

}
