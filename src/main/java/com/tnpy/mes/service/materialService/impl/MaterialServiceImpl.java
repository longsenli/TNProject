package com.tnpy.mes.service.materialService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.BatchrelationcontrolMapper;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.mapper.mysql.OrderSplitMapper;
import com.tnpy.mes.model.customize.CustomMaterialRecord;
import com.tnpy.mes.model.mysql.Batchrelationcontrol;
import com.tnpy.mes.model.mysql.MaterialRecord;
import com.tnpy.mes.model.mysql.OrderSplit;
import com.tnpy.mes.service.materialService.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    @Autowired
    private BatchrelationcontrolMapper batchrelationcontrolMapper;
    @Autowired
    private OrderSplitMapper orderSplitMapper;
    public TNPYResponse getMaterialRecord(String expendOrderID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<CustomMaterialRecord> materialRecordList = materialRecordMapper.selectByExpendOrder(expendOrderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(materialRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getUsableMaterial(String plantID,String materialID,String expendOrderID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String materialTBBatch = batchrelationcontrolMapper.selectTBBatchByOrderID(expendOrderID);
            if(StringUtils.isEmpty(materialTBBatch))
                materialTBBatch = null;
            List<CustomMaterialRecord> materialRecordList = materialRecordMapper.selectUsableMaterial(plantID,materialID,materialTBBatch);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(materialRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    //materialOrderID 工单号 如
    public TNPYResponse judgeAvailable(String materialOrderID, String expendOrderID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String materialTBBatch = batchrelationcontrolMapper.selectTBBatchByOrderID(materialOrderID);
            String expendTBBatch = batchrelationcontrolMapper.selectTBBatchByOrderID(expendOrderID);
            if(StringUtils.isEmpty(expendTBBatch))
            {
                Batchrelationcontrol batchrelationcontrol = new Batchrelationcontrol();
                batchrelationcontrol.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                batchrelationcontrol.setRelationtime(new Date());
                batchrelationcontrol.setRelationorderid(expendOrderID);
                batchrelationcontrol.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                batchrelationcontrol.setTbbatch(materialTBBatch);
                batchrelationcontrolMapper.insert(batchrelationcontrol);
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                return  result;
            }
            if(expendTBBatch.equals(materialTBBatch))
            {
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            }
            else
            {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("当前工单所用涂板批次号为：" + expendTBBatch + "所投物料所用涂板批次号为：" + materialTBBatch);
            }
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询物料是否可用出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse gainMaterialRecord(String materialRecordIDListStr,String materialOrderID, String expendOrderID, String outputter ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            TNPYResponse materialUseable = judgeAvailable(materialOrderID,expendOrderID);
            if(materialUseable.getStatus() != StatusEnum.ResponseStatus.Success.getIndex() )
            {
                return materialUseable;
            }
            List<String> materialIDList = JSON.parseArray(materialRecordIDListStr, String.class);
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
    public TNPYResponse gainPartMaterialRecord(String materialRecordID,String materialOrderID,String number,String expendOrderID,String outputter )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            TNPYResponse materialUseable = judgeAvailable(materialOrderID,expendOrderID);
            if(materialUseable.getStatus() != StatusEnum.ResponseStatus.Success.getIndex() )
            {
                return materialUseable;
            }
            MaterialRecord materialRecord = materialRecordMapper.selectByPrimaryKey(materialRecordID);
            MaterialRecord materialRecordCopy =  materialRecordMapper.selectByPrimaryKey(materialRecordID);

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
            OrderSplit orderSplit = orderSplitMapper.selectByPrimaryKey(qrCode);
            String msgStr = "";
            if(orderSplit != null)
            {
                msgStr = "工单批次码： " + orderSplit.getOrdersplitid();
            }
            else
            {
                msgStr = "该批次码未找到，二维码数据为：" +  qrCode;
            }
            int count1 = materialRecordMapper.checkMaterialRecordUsed(qrCode,StatusEnum.InOutStatus.Input.getIndex());
            int count2 = materialRecordMapper.checkMaterialRelation(qrCode,expendOrderID);

            if(count1 < 1)
            {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage(msgStr + "， 该批次码不存在或已被领用！");
                return  result;
            }
            if(count2 < 1)
            {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("该工单不能够使用该物料！");
                return  result;
            }


            TNPYResponse materialUseable = judgeAvailable(orderSplit.getOrderid(),expendOrderID);
            if(materialUseable.getStatus() != StatusEnum.ResponseStatus.Success.getIndex() )
            {
                return materialUseable;
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
