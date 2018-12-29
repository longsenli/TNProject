package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.model.mysql.MaterialRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/29 8:40
 */
@RestController
@RequestMapping("/api/material")
public class MaterialController {

    @Autowired
    private MaterialRecordMapper materialRecordMapper;

    @RequestMapping(value = "/getmaterialrecord")
    public TNPYResponse getMaterialRecord(String expendOrderID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<MaterialRecord> materialRecordList = materialRecordMapper.selectByExpendOrder(expendOrderID);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(materialRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    @RequestMapping(value = "/getusablematerial")
    public TNPYResponse getUsableMaterial(String plantID,String materialID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<MaterialRecord> materialRecordList = materialRecordMapper.selectUsableMaterial(plantID,materialID);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(materialRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    @RequestMapping(value = "/gainmaterialrecord")
    public TNPYResponse gainMaterialRecord(String materialIDListStr,String expendOrderID,String outputter ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<String> materialIDList = JSON.parseArray(materialIDListStr, String.class);
            System.out.println(materialIDList.toString());
            materialRecordMapper.updateGainMaterialRecord(materialIDList,expendOrderID,outputter,new Date());
            result.setStatus(1);
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
