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

    @RequestMapping(value = "/changeworkorderstatus")
    public TNPYResponse changeWorkOrderStatus( String ID,String status ) {
        return  workOrderService.changeWorkOrderStatus(ID,status);
    }

    @RequestMapping(value = "/getordersplit")
    public TNPYResponse getOrderSplit(String orderID ) {
        return  workOrderService.getOrderSplit(orderID);
    }

    @RequestMapping(value = "/getordersplitaftermap")
    public TNPYResponse getOrderSplitAfterMap(String orderID ) {
       // System.out.println("controller=============" + orderID);
        return  workOrderService.getOrderSplitAfterMap(orderID);
    }

    @RequestMapping(value = "/finishordersplit")
    public TNPYResponse finishOrderSplit( String jsonStr,String name ) {
        return  workOrderService.finishOrderSplit(jsonStr,name);
    }

    @RequestMapping(value = "/getsuborderbyid")
    public TNPYResponse getSubOrderByID( String id ,String type) {
        return  workOrderService.getSubOrderByID(id,type);
    }

    @RequestMapping(value = "/getplanproductiondashboard")
    public TNPYResponse getPlanProductionDashboard( String plantID,String processID,String startTime,String endTime ) {
        return  workOrderService.getPlanProductionDashboard(plantID,processID,startTime,endTime);
    }

    @RequestMapping(value = "/getrealtimeproductiondashboard")
    public TNPYResponse getRealtimeProductionDashboard( String plantID,String processID,String startTime,String endTime ) {
        return  workOrderService.getRealtimeProductionDashboard(plantID,processID,startTime,endTime);
    }


    @RequestMapping(value = "/cancelfinishsuborder")
    public TNPYResponse cancelFinishSuborder( String subOrdderID ) {
        return  workOrderService.cancelFinishSuborder(subOrdderID);
    }

    @RequestMapping(value = "/addplanproductionrecord")
    public TNPYResponse changePlanProductionRecord( @RequestBody String jsonStr ) {
        return  workOrderService.changePlanProductionRecord(jsonStr);
    }

    @RequestMapping(value = "/getplanproductionrecord")
    public TNPYResponse getPlanProductionRecord(  String plantID,String processID,String startTime,String endTime ) {
        return  workOrderService.getPlanProductionRecord(plantID, processID, startTime, endTime );
    }

    @RequestMapping(value = "/deleteplanproductionrecord")
    public TNPYResponse deletePlanProductionRecord(  String id ) {
        return  workOrderService.deletePlanProductionRecord(id);
    }
}
