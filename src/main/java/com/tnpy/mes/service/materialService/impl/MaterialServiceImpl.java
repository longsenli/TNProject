package com.tnpy.mes.service.materialService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.model.customize.CustomMaterialRecord;
import com.tnpy.mes.model.mysql.MaterialRecord;
import com.tnpy.mes.service.materialService.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
            List<CustomMaterialRecord> materialRecordList = materialRecordMapper.selectByExpendOrder(expendOrderID);
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
            List<CustomMaterialRecord> materialRecordList = materialRecordMapper.selectUsableMaterial(plantID,materialID);
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
            materialRecordMapper.updateGainMaterialRecord(materialIDList,expendOrderID,outputter,new Date(),StatusEnum.InOutStatus.Output.getIndex());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse gainPartMaterialRecord(String materialID,String number,String expendOrderID,String outputter )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            MaterialRecord materialRecord = materialRecordMapper.selectByPrimaryKey(materialID);
            MaterialRecord materialRecordCopy =  materialRecordMapper.selectByPrimaryKey(materialID);

            materialRecordCopy.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            materialRecordCopy.setNumber(materialRecord.getNumber() - Float.parseFloat(number) );
            materialRecord.setInorout(StatusEnum.InOutStatus.Output.getIndex());
            materialRecord.setOutputer(outputter);
            materialRecord.setOutputtime(new Date());
            materialRecord.setExpendorderid(expendOrderID);
            materialRecord.setNumber(Float.parseFloat(number) * 1.0);
            materialRecordMapper.updateByPrimaryKey(materialRecord);
            if(materialRecordCopy.getNumber() >0){
                materialRecordMapper.insert(materialRecordCopy);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
   public  TNPYResponse gainMaterialByQR(String qrCode,String expendOrderID,String outputter ){
        TNPYResponse result = new TNPYResponse();
        try
        {
            int count1 = materialRecordMapper.checkMaterialRecordUsed(qrCode,StatusEnum.InOutStatus.Input.getIndex());
            int count2 = materialRecordMapper.checkMaterialRelation(qrCode,expendOrderID);

            if(count1 < 1)
            {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("该物料已被领用！");
                return  result;
            }
            if(count2 < 1)
            {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("该工单不能够使用该物料！");
                return  result;
            }
            materialRecordMapper.updateGainMaterialByQR(qrCode,expendOrderID,outputter,new Date(),StatusEnum.InOutStatus.Output.getIndex());
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
