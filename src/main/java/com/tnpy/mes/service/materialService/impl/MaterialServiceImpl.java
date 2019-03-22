package com.tnpy.mes.service.materialService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.customize.CustomMaterialRecord;
import com.tnpy.mes.model.mysql.*;
import com.tnpy.mes.service.materialService.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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
    @Autowired
    private BatteryStastisInventoryRecordMapper batteryStastisInventoryRecordMapper;
    @Autowired
    private WorkorderMapper workorderMapper;
    @Autowired
    private GrantMaterialRecordMapper grantMaterialRecordMapper;
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
            MaterialRecord materialRecord = materialRecordMapper.selectByPrimaryKey(materialIDList.get(0));
            TNPYResponse resultGrant = judgeZHGrantStatus(materialRecord.getSuborderid());
            if(resultGrant.getStatus() != StatusEnum.ResponseStatus.Success.getIndex())
            {
                return  resultGrant;
            }
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

            TNPYResponse resultGrant = judgeZHGrantStatus(materialRecord.getSuborderid());
            if(resultGrant.getStatus() != StatusEnum.ResponseStatus.Success.getIndex())
            {
                return  resultGrant;
            }

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
    TNPYResponse judgeZHGrantStatus(String orderSplitID)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            OrderSplit orderSplit = orderSplitMapper.selectByPrimaryKey(orderSplitID);
            String msgStr = "";
            if (orderSplit == null) {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("未找到工单信息！" + orderSplitID);
                return result;
            }
            Workorder workorder = workorderMapper.selectByPrimaryKey(orderSplit.getOrderid());
            if (workorder != null) {
                if (ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName().equals(workorder.getProcessid())) {
                    GrantMaterialRecord grantMaterialRecord = grantMaterialRecordMapper.selectByOrderID(orderSplit.getId());
                    if (grantMaterialRecord == null) {
                        result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                        result.setMessage("该工单未发料，不能够领用！" + orderSplit.getOrdersplitid());
                        return result;
                    }
                }
            } else {
                /*
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("未找到归属主工单信息！" + orderSplit.getOrderid());
                return result;*/
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        }
        catch (Exception ex)
        {
            result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
            result.setMessage("查找发料信息失败！" + orderSplitID);
            return result;
        }
    }
   public  TNPYResponse gainMaterialByQR(String qrCode,String expendOrderID,String outputter ){
        TNPYResponse result = new TNPYResponse();
        try
        {
            TNPYResponse resultGrant = judgeZHGrantStatus(qrCode);
            if(resultGrant.getStatus() != StatusEnum.ResponseStatus.Success.getIndex())
            {
                return  resultGrant;
            }
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
          //  List<CustomMaterialRecord> materialRecordList = materialRecordMapper.selectUsableMaterial(plantID,materialID,materialTBBatch);
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

    public  TNPYResponse getMaterialRecordBySubOrderID(String qrCode,String expendOrderID ){
        TNPYResponse result = new TNPYResponse();
        try
        {
            TNPYResponse resultGrant = judgeZHGrantStatus(qrCode);
            if(resultGrant.getStatus() != StatusEnum.ResponseStatus.Success.getIndex())
            {
                return  resultGrant;
            }
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
            List<CustomMaterialRecord> materialRecordList = materialRecordMapper.selectBySubOrderID(qrCode);

            result.setData(JSONObject.toJSON(materialRecordList).toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse orderOutputStatistics( String startTime,String endTime,String plantID,String processID,String lineID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String lineFilter = "";
            if(!"-1".equals(lineID))
            {
                lineFilter += " and lineID ='" + lineID + "' ";
            }
            List<Map<String, String>> orderOutputStatisticsList = materialRecordMapper.orderOutputStatistics( startTime, endTime, plantID, processID, lineFilter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderOutputStatisticsList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse orderRemnantProductStatistics( String startTime,String endTime,String plantID,String processID,String lineID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String lineFilter = "";
            if(!"-1".equals(lineID))
            {
                lineFilter += " and lineID ='" + lineID + "' ";
            }
            List<Map<String, String>> orderOutputStatisticsList = materialRecordMapper.orderRemnantProductStatistics( startTime, endTime, plantID, processID, lineFilter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderOutputStatisticsList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse batteryStatisInventory( String startTime,String endTime,String plantID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<BatteryStastisInventoryRecord> batteryStastisInventoryRecordList = batteryStastisInventoryRecordMapper.selectStatisInventoryByParam(plantID,startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(batteryStastisInventoryRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }


    //1 当天工单ID 2第二天工单ID 3 当天工单名称 4 第二天工单名称
    public TNPYResponse addGrantMaterialRecord( String orderSplitID,String operator ,int orderType)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            OrderSplit orderSplit = null;
            if(orderType < 3)
            {
                orderSplit = orderSplitMapper.selectByPrimaryKey(orderSplitID);
            }
            else
            {
                List<OrderSplit> orderSplitList =orderSplitMapper.selectByOrderSplitName(orderSplitID);
                if(orderSplitList == null || orderSplitList.size() < 1)
                {
                    result.setMessage("未获取到订单信息！" +orderSplitID );
                    return  result;
                }
                orderSplit = orderSplitList.get(0);
            }
            orderSplitID = orderSplit.getId();
            if(orderSplit ==null)
            {
                result.setMessage("未获取到订单信息！" +orderSplitID );
                return  result;
            }
            if(!(StatusEnum.WorkOrderStatus.finished.getIndex()+"").equals(orderSplit.getStatus()))
            {
                result.setMessage("该订单尚未入库！" +orderSplitID );
                return  result;
            }
            GrantMaterialRecord grantMaterialRecord = grantMaterialRecordMapper.selectByOrderID(orderSplitID);
            if(grantMaterialRecord != null)
            {
                result.setMessage("该订单已发料！" +orderSplitID );
                return  result;
            }


            Date date = new Date();//取时间

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if(orderType%2 == 0)
            {
                calendar.add(Calendar.DATE, 1);
            }
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果

            GrantMaterialRecord newGrantMaterialRecord = new GrantMaterialRecord();
            newGrantMaterialRecord.setBatterytype(orderSplit.getMaterialid());
            newGrantMaterialRecord.setGranttime(date);
            newGrantMaterialRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            newGrantMaterialRecord.setNumber(orderSplit.getProductionnum().intValue());
            newGrantMaterialRecord.setOperator(operator);
            newGrantMaterialRecord.setOrderid(orderSplitID);
            newGrantMaterialRecord.setOrdername(orderSplit.getOrdersplitid());
            newGrantMaterialRecord.setPlantid("1003");
            newGrantMaterialRecord.setProcessid("1007");
            newGrantMaterialRecord.setStatus("1");

            grantMaterialRecordMapper.insert(newGrantMaterialRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
       catch (Exception ex)
       {
           result.setMessage("发放失败！" + ex.getMessage());
           return  result;
       }
    }

    public TNPYResponse grantAndExpendStatistics(  String startTime,String endTime,String plantID,String processID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object,Object>> materialStatisInfo = materialRecordMapper.grantAndExpendStatistics(startTime,endTime,plantID,"1008","1007");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(materialStatisInfo).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getSecondaryMaterialInventoryStatistics( String plantID,String processID,String startTime,String endTime )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object,Object>> materialStatisInfo = materialRecordMapper.getSecondaryMaterialInventoryStatistics(startTime,endTime,plantID,processID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(materialStatisInfo).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getMaterialInventoryStatistics( String plantID,String processID,String startTime,String endTime )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object,Object>> materialStatisInfo = materialRecordMapper.getMaterialInventoryStatistics(startTime,endTime,plantID,processID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(materialStatisInfo).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getGrantMaterialRecord( String plantID,String processID,String startTime,String endTime )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object,Object>> materialStatisInfo = materialRecordMapper.getGrantMaterialRecord(startTime,endTime,plantID,processID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(materialStatisInfo).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
