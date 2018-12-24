package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.EquipmentParaRecordMapper;
import com.tnpy.mes.mapper.mysql.ParameterInfoMapper;
import com.tnpy.mes.model.mysql.EquipmentParaRecord;
import com.tnpy.mes.model.mysql.ParameterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/24 8:57
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentParamController {
    @Autowired
    private ParameterInfoMapper parameterInfoMapper;
@Autowired
private EquipmentParaRecordMapper equipmentParaRecordMapper;
    @RequestMapping(value = "/getequipmentparam")
    public TNPYResponse getEquipmentParam(String equipmentTypeID) {

        TNPYResponse result = new TNPYResponse();
        try {
            List<ParameterInfo> parameterInfoList = parameterInfoMapper.selectByEquipType(equipmentTypeID);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    @RequestMapping(value = "/saveequipmentparam")
    public TNPYResponse saveEquipmentParam(@RequestBody String json) {
        System.out.println(json);

        TNPYResponse result = new TNPYResponse();
        try {
            List<EquipmentParaRecord> equipmentParaRecordList = JSON.parseArray(json, EquipmentParaRecord.class);
            for(int i =0;i<equipmentParaRecordList.size();i++)
            {
                equipmentParaRecordList.get(i).setRecordtime(new Date());
                equipmentParaRecordMapper.insertSelective(equipmentParaRecordList.get(i));
            }
            result.setStatus(1);
            result.setData("");
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
