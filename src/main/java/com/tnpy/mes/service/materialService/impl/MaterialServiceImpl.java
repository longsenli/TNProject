package com.tnpy.mes.service.materialService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.model.mysql.MaterialRecord;
import com.tnpy.mes.service.materialService.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:32
 */
@Service("materialService")
public class MaterialServiceImpl implements IMaterialService {

    @Autowired
    private MaterialRecordMapper materialRecordMapper;

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

    public TNPYResponse gainMaterialRecord(String materialIDListStr, String expendOrderID, String outputter ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<String> materialIDList = JSON.parseArray(materialIDListStr, String.class);
            System.out.println(materialIDList.toString());
            materialRecordMapper.updateGainMaterialRecord(materialIDList,expendOrderID,outputter,new Date());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
