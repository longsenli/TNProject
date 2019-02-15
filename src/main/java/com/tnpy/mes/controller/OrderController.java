package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.workOrderService.IWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/27 15:48
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private IWorkOrderService workOrderService ;

    @RequestMapping(value = "/getworkorder")
    public TNPYResponse getWorkOrder( ) {
        return  workOrderService.getWorkOrder();
    }

    @RequestMapping(value = "/getworkorderbyparam")
    public TNPYResponse getWorkOrderByParam(String plantID,String processID,String lineID ) {

        return  workOrderService.getWorkOrderByParam(plantID,processID,lineID);
    }

    @RequestMapping(value = "/getcustomworkorderbyparam")
    public TNPYResponse getCustomWorkOrderByParam(String plantID,String processID,String lineID ) {

        return  workOrderService.getCustomWorkOrderByParam(plantID,processID,lineID);
    }

    @RequestMapping(value = "/getworkorderbylineid")
    public TNPYResponse getWorkOrderByLineID( String lineID ) {
        return  workOrderService.getWorkOrderByLineID(lineID);
    }

    @RequestMapping(value = "/changeworkorder")
    public TNPYResponse changeWorkOrder(@RequestBody String jsonStr ) {
        return  workOrderService.changeWorkOrder(jsonStr);
    }

    @RequestMapping(value = "/getordersplit")
    public TNPYResponse getOrderSplit(String orderID ) {
        return  workOrderService.getOrderSplit(orderID);
    }

    @RequestMapping(value = "/getordersplitaftermap")
    public TNPYResponse getOrderSplitAfterMap(String orderID ) {
        return  workOrderService.getOrderSplitAfterMap(orderID);
    }

    @RequestMapping(value = "/finishordersplit")
    public TNPYResponse finishOrderSplit( String jsonStr,String name ) {
        return  workOrderService.finishOrderSplit(jsonStr,name);
    }

    @RequestMapping(value = "/getsuborderbyid")
    public TNPYResponse getSubOrderByID( String id ) {
        return  workOrderService.getSubOrderByID(id);
    }
}
