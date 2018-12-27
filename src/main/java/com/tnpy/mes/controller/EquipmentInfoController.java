package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.EquipmentInfoMapper;
import com.tnpy.mes.mapper.mysql.EquipmentTypeMapper;
import com.tnpy.mes.model.mysql.EquipmentInfo;
import com.tnpy.mes.model.mysql.EquipmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.UUID;

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

    @RequestMapping(value = "/changeequipmentinfo")
    public TNPYResponse changeEquipmentInfo(@RequestBody String jsonStr) {

        EquipmentInfo equipmentInfo=(EquipmentInfo) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), EquipmentInfo.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(equipmentInfo.getId()))
            {
                equipmentInfo.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                equipmentInfoMapper.insertSelective(equipmentInfo);
            }
            else
            {
                equipmentInfoMapper.updateByPrimaryKey(equipmentInfo);
            }
            result.setStatus(1);
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }
}
