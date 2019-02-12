package com.tnpy.mes.service.workOrderService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.customize.CustomWorkOrderRecord;
import com.tnpy.mes.model.mysql.*;
import com.tnpy.mes.service.workOrderService.IWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:38
 */
@Service("workOrderService")
public class WorkOrderServiceImpl implements IWorkOrderService {
    @Autowired
    private WorkorderMapper workOrderMapper;

    @Autowired
    private OrderSplitMapper orderSplitMapper;

    @Autowired
    private MaterialRecordMapper materialRecordMapper;
    @Autowired
    private SolidifyRecordMapper solidifyRecordMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private  BatchrelationcontrolMapper batchrelationcontrolMapper;

    public TNPYResponse getWorkOrder( ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Workorder> workOrderList = workOrderMapper.selectAll();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getWorkOrderByLineID( String lineID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Workorder> workOrderList = workOrderMapper.selectByFilter(" where lineID = '" + lineID + "' and status < 4  order by  scheduledStartTime desc ");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getWorkOrderByParam(String plantID,String processID,String lineID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = "where  plantID ='" + plantID + "' and processID = '" + processID + "' ";
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            List<Workorder> workOrderList = workOrderMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getCustomWorkOrderByParam(String plantID,String processID,String lineID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = "where  plantID ='" + plantID + "' and processID = '" + processID + "' ";
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            filter += " order by createTime desc limit 1000 ";
            List<CustomWorkOrderRecord> workOrderList = workOrderMapper.selectCustomResultByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    private  String getOrderNumber(int number,int length)
    {
        String numStr = String.valueOf(number);
        String tmp = "";
        for(int i =0;i<length - numStr.length();i++)
        {
            tmp += "0";
        }
        return  tmp+numStr;
    }

    public TNPYResponse changeWorkOrder( String jsonStr ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            Workorder workorder=(Workorder) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), Workorder.class);

            if(StringUtils.isEmpty(workorder.getId()))
            {
                workorder.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                workorder.setStatus(StatusEnum.WorkOrderStatus.ordered.getIndex() + "");
                workorder.setCreatetime(new Date());
                workOrderMapper.insertSelective(workorder);

                List<OrderSplit> orderSplitList = new ArrayList<>();
                for(int i =0 ;i<workorder.getBatchnum();i++) {
                    OrderSplit orderSplit = new OrderSplit();
                    orderSplit.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                    orderSplit.setOrderid(workorder.getId());
                    orderSplit.setStatus(StatusEnum.WorkOrderStatus.ordered.getIndex() + "");
                    orderSplit.setMaterialid(workorder.getMaterialid());
                    orderSplit.setOrdersplitid(workorder.getOrderid() + getOrderNumber(i + 1,3));
                    orderSplit.setProductionnum(workorder.getTotalproduction()/workorder.getBatchnum() *1.0);
                    orderSplitList.add(orderSplit);
                }
                orderSplitMapper.insertManyOrder(orderSplitList,workorder.getId());
            }
            else
            {
                workOrderMapper.updateByPrimaryKey(workorder);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("修改出错！" + ex.getMessage());
            return  result;
        }
    }


    public TNPYResponse getOrderSplit(String orderID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<OrderSplit > orderSplitList = orderSplitMapper.selectByOrderID(orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }


    public TNPYResponse getOrderSplitAfterMap(String orderID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<OrderSplit > orderSplitList = orderSplitMapper.selectAfterMapByOrderID(orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse finishOrderSplit( String jsonStr ,String name) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            OrderSplit orderSplit=(OrderSplit) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), OrderSplit.class);
            TNPYResponse judgeResult = judgeEnoughMaterial(orderSplit.getMaterialid(),orderSplit.getOrderid(),orderSplit.getProductionnum());
            if(judgeResult.getStatus() != StatusEnum.ResponseStatus.Success.getIndex())
            {
                return  judgeResult;
            }
            orderSplit.setStatus(StatusEnum.WorkOrderStatus.finished.getIndex() + "");
            orderSplitMapper.updateByPrimaryKeySelective(orderSplit);
            MaterialRecord materialRecord = new MaterialRecord();
            materialRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            materialRecord.setInorout(1);
            materialRecord.setMaterialid(orderSplit.getMaterialid());
            materialRecord.setNumber(orderSplit.getProductionnum());
            materialRecord.setOrderid(orderSplit.getOrderid());
            materialRecord.setSuborderid(orderSplit.getId());
            materialRecord.setInputtime(new Date());
            materialRecord.setInputer(name);
            materialRecordMapper.insert(materialRecord);
            boolean blTB = ConfigParamEnum.BasicParamEnum.TBProcessID.getName().equals(workOrderMapper.getProcessIDByOrder(orderSplit.getOrderid()));
            try
            {
                if(blTB)
                {
                    SolidifyRecord solidifyRecord = new SolidifyRecord();
                    solidifyRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                    solidifyRecord.setMaterialid(orderSplit.getMaterialid());
                    solidifyRecord.setOrderid(orderSplit.getOrderid());
                    solidifyRecord.setOrdersplitid(orderSplit.getId());
                    solidifyRecord.setOrdersplitname(orderSplit.getOrdersplitid());
                    solidifyRecordMapper.insertSelective(solidifyRecord);
                }
            }catch (Exception ex)
            {
                result.setMessage(result.getMessage() + " " +ex.getMessage() );
            }

            try
            {
                if(blTB)
                {
                    String batchID = batchrelationcontrolMapper.selectTBBatchByOrderID(orderSplit.getOrderid());
                    if(org.springframework.util.StringUtils.isEmpty(batchID) || "null".equals(batchID.trim()) || batchID.length() < 6)
                    {
                        Batchrelationcontrol batchrelationcontrol = new Batchrelationcontrol();
                        batchrelationcontrol.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                        batchrelationcontrol.setRelationorderid(orderSplit.getOrderid());
                        batchrelationcontrol.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                        batchrelationcontrol.setRelationtime(new Date());
                        String batch = orderSplit.getOrdersplitid().substring(0,orderSplit.getOrdersplitid().length() - 13)
                                + orderSplit.getOrdersplitid().substring(orderSplit.getOrdersplitid().length() - 11,orderSplit.getOrdersplitid().length() - 3);
                        batchrelationcontrol.setTbbatch(batch);
                        batchrelationcontrolMapper.insert(batchrelationcontrol);
                    }
                }
            }catch (Exception ex)
            {
                result.setMessage(result.getMessage() + " " +ex.getMessage() );
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

    private TNPYResponse judgeEnoughMaterial(String outMaterial,String finishOrderID,double production)
    {
        TNPYResponse result = new TNPYResponse();
        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
        boolean bl = true;
        try
        {
            List<Map<String, String>> outMaterialProportion = materialMapper.selectProportionalityByOut(outMaterial);
            List<Map<String, String>> inputRecord = materialRecordMapper.selectBatchChargingByOrder(finishOrderID);
            String productionStr = materialRecordMapper.getProductionByOrderID(finishOrderID);
            Double productionALl = 0.0;
            try
            {
                productionALl = Double.parseDouble(productionStr);
            }catch (Exception ex)
            {
                productionALl = 0.0;
            }
            productionALl = productionALl + production;
            Map<String, Double> outMaterialProportionMap = new HashMap<String, Double>();
            Map<String, Double> inputRecordMap = new HashMap<String, Double>();

            String typeID= "";
            double number=-1;
            for (Map<String, String> proportionMap : outMaterialProportion)
            {
                typeID = null;
                number = -1;
                for (Map.Entry<String, String> entry : proportionMap.entrySet()) {
                    if (org.springframework.util.StringUtils.isEmpty(entry.getValue()))
                        break;
                    if("typeID".equals(entry.getKey()))
                    {
                        typeID = entry.getValue();
                    }
                    if("proportionality".equals(entry.getKey()) && entry.getValue().split(":").length ==2)
                    {
                        number =Double.valueOf(entry.getValue().split(":")[0]) /Double.valueOf(entry.getValue().split(":")[1])  ;
                    }
                }
                if(number > -1)
                {
                    outMaterialProportionMap.put(typeID,number);
                }
            }

            for (Map<String, String> inputMap : inputRecord)
            {
                typeID = null;
                number = -1;
                for (Map.Entry<String, String> entry : inputMap.entrySet()) {
                    if("typeID".equals(entry.getKey()))
                    {
                        typeID = entry.getValue();
                    }
                    if("sum".equals(entry.getKey()))
                    {
                        number = Double.valueOf(entry.getValue());
                    }
                }
                if(number > -1)
                {
                    inputRecordMap.put(typeID,number);
                }
            }

            for (Map.Entry<String, Double> entry : outMaterialProportionMap.entrySet()) {
               if(!inputRecordMap.containsKey(entry.getKey()))
               {
                   result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                   result.setMessage(materialTypeMapper.getTypeNameByID(entry.getKey()) + "投料不足，已投："
                           + "0" + "，至少需要投入：" +  entry.getValue() * productionALl );
                   bl = false;
                   break;
               }
               if(inputRecordMap.get(entry.getKey()) + 1.0 < entry.getValue() * productionALl)
               {
                   result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                   result.setMessage(materialTypeMapper.getTypeNameByID(entry.getKey()) + "投料不足，已投："
                           + inputRecordMap.get(entry.getKey()) + "，至少需要投入：" +  entry.getValue() * productionALl );
                   break;
               }
            }
        }
       catch (Exception ex)
       {
           result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
           result.setMessage(ex.getMessage());
       }

        return  result;
    }
}
