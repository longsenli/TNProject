package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.SalesOrderService.ISalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/25 9:58
 */
@RestController
@RequestMapping("/api/salesorder")
public class SalesOrderController {
    @Autowired
    private ISalesOrderService salesOrderService ;


    @RequestMapping(value = "/getsalesorderdetail")
    public TNPYResponse getSalesOrderDetail(String plantID, String productID,String status,String startTime,String endTime)
    {
        return  salesOrderService.getSalesOrderDetail(plantID,productID,status,startTime,endTime);
    }

    @RequestMapping(value = "/getsalesorderstatusanalysis")
    public TNPYResponse getSalesOrderStatusAnalysis(String plantID, String productID,String startTime,String endTime)
    {
        return  salesOrderService.getSalesOrderStatusAnalysis(plantID,productID,startTime,endTime);
    }

    @RequestMapping(value = "/getsalesorderdailywork")
    public TNPYResponse getSalesOrderDailyWork(String plantID, String productID,String startTime,String endTime)
    {
        return  salesOrderService.getSalesOrderDailyWork(plantID,productID,startTime,endTime);
    }
}
