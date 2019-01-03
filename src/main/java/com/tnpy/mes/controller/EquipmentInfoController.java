package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.equipmentInfoService.IEquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/24 8:57
 */
@RestController
@RequestMapping("/api/equipment")

public class EquipmentInfoController {

    @Autowired
    private IEquipmentInfoService equipmentInfoService ;



    @RequestMapping(value = "/getequipmentinfo")
    public TNPYResponse getEquipmentInfo(String typeID,String plantID) {
      return  equipmentInfoService.getEquipmentInfo(typeID, plantID);
    }


    @RequestMapping(value = "/deleteequipmentinfo")
    public TNPYResponse deleteEquipmentInfo( String equipID) {
        return  equipmentInfoService.deleteEquipmentInfo(equipID);
    }

    @RequestMapping(value = "/changeequipmentinfo")
    public TNPYResponse changeEquipmentInfo(@RequestBody String jsonStr) {

        return  equipmentInfoService.changeEquipmentInfo(jsonStr);
    }
}
