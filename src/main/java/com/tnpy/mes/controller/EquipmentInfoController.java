package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.EquipmentInfoMapper;
import com.tnpy.mes.mapper.mysql.EquipmentTypeMapper;
import com.tnpy.mes.model.mysql.EquipmentInfo;
import com.tnpy.mes.model.mysql.EquipmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/24 8:57
 */
@RestController
@RequestMapping("/api/equipment")

public class EquipmentInfoController {
    @Autowired
    private EquipmentTypeMapper equipmentTypeMapper;
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;

    @RequestMapping(value = "/getequipmenttype")
    public TNPYResponse getEquipmentType() {

        TNPYResponse result = new TNPYResponse();
        try
        {
            List<EquipmentType> equipmentTypeList = equipmentTypeMapper.selectAllType();
            result.setStatus(1);
            result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }

    }

    @RequestMapping(value = "/getequipmentinfo")
    public TNPYResponse getEquipmentInfo(String typeID) {
        TNPYResponse result = new TNPYResponse();
       try
       {
           List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectByType(typeID);
           result.setStatus(1);
           result.setData(JSONObject.toJSONString(equipmentInfoList, SerializerFeature.WriteMapNullValue).toString());
           return  result;
       }
       catch (Exception ex)
       {
           result.setMessage("查询出错！" + ex.getMessage());
           return  result;
       }


    }
}
