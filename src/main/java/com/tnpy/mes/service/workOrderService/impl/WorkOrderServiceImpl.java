package com.tnpy.mes.service.workOrderService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.mapper.mysql.OrderSplitMapper;
import com.tnpy.mes.mapper.mysql.WorkorderMapper;
import com.tnpy.mes.model.customize.CustomWorkOrderRecord;
import com.tnpy.mes.model.mysql.MaterialRecord;
import com.tnpy.mes.model.mysql.OrderSplit;
import com.tnpy.mes.model.mysql.Workorder;
import com.tnpy.mes.service.workOrderService.IWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
            List<Workorder> workOrderList = workOrderMapper.selectByFilter(" where lineID = '" + lineID + "' and status < 4");
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

    private  String getorderNumber(int number,int length)
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

                List<OrderSplit> orderSplitList = new ArrayList<>();;
                for(int i =0 ;i<workorder.getBatchnum();i++) {
                    OrderSplit orderSplit = new OrderSplit();
                    orderSplit.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                    orderSplit.setOrderid(workorder.getId());
                    orderSplit.setStatus(StatusEnum.WorkOrderStatus.ordered.getIndex() + "");
                    orderSplit.setMaterialid(workorder.getMaterialid());
                    orderSplit.setOrdersplitid(workorder.getOrderid() +getorderNumber(i + 1,3));
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

    public TNPYResponse finishOrderSplit( String jsonStr ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            OrderSplit orderSplit=(OrderSplit) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), OrderSplit.class);
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
            materialRecordMapper.insert(materialRecord);

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
