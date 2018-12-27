package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.WorkOrderMapper;
import com.tnpy.mes.model.mysql.WorkOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/27 15:48
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private WorkOrderMapper workOrderMapper;

    @RequestMapping(value = "/getworkorderbyparam")
    public TNPYResponse getWorkOrderByParam() {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<WorkOrder> workOrderList = workOrderMapper.selectAll();
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
}
