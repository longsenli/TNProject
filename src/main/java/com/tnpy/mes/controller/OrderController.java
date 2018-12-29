package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.mapper.mysql.OrderSplitMapper;
import com.tnpy.mes.mapper.mysql.WorkorderMapper;
import com.tnpy.mes.model.mysql.MaterialRecord;
import com.tnpy.mes.model.mysql.OrderSplit;
import com.tnpy.mes.model.mysql.Workorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/27 15:48
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private WorkorderMapper workOrderMapper;

    @Autowired
    private  OrderSplitMapper orderSplitMapper;

    @Autowired
    private MaterialRecordMapper  materialRecordMapper;

    @RequestMapping(value = "/getworkorder")
    public TNPYResponse getWorkOrder( ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Workorder> workOrderList = workOrderMapper.selectAll();
            result.setStatus(1);
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    @RequestMapping(value = "/getworkorderbylineid")
    public TNPYResponse getWorkOrderByLineID( String lineID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Workorder> workOrderList = workOrderMapper.selectByFilter(" where lineID = '" + lineID + "' and status < 4");
            result.setStatus(1);
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
    @RequestMapping(value = "/changeworkorder")
    public TNPYResponse changeWorkOrder(@RequestBody String jsonStr ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            Workorder workorder=(Workorder) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), Workorder.class);

            if(StringUtils.isEmpty(workorder.getId()))
            {
                workorder.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                workorder.setStatus(1);
                workOrderMapper.insertSelective(workorder);

                List<OrderSplit> orderSplitList = new ArrayList<>();;
                for(int i =0 ;i<workorder.getBatchnum();i++) {
                    OrderSplit orderSplit = new OrderSplit();
                    orderSplit.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                    orderSplit.setOrderid(workorder.getId());
                    orderSplit.setStatus(1);
                    orderSplit.setMaterialid(workorder.getMaterialid());
                    orderSplit.setOrdersplitid(workorder.getOrderid() +getorderNumber(i,3));
                    orderSplit.setProductionnum(workorder.getTotalproduction()/workorder.getBatchnum() *1.0);
                    orderSplitList.add(orderSplit);
                }
                orderSplitMapper.insertManyOrder(orderSplitList,workorder.getId());
            }
            else
            {
                workOrderMapper.updateByPrimaryKey(workorder);
            }
            result.setStatus(1);
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("修改出错！" + ex.getMessage());
            return  result;
        }
    }

    @RequestMapping(value = "/getordersplit")
    public TNPYResponse getOrderSplit(String orderID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<OrderSplit > orderSplitList = orderSplitMapper.selectByOrderID(orderID);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    @RequestMapping(value = "/finishordersplit")
    public TNPYResponse finishOrderSplit(@RequestBody String jsonStr ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            OrderSplit orderSplit=(OrderSplit) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), OrderSplit.class);
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
