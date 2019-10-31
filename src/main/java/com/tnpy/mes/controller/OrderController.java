package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.workOrderService.IWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/deleteWorkOrder")
    public TNPYResponse deleteWorkOrder( String orderID ) {
        return  workOrderService.deleteWorkOrder(orderID);
    }

    @RequestMapping(value = "/changeworkorderstatus")
    public TNPYResponse changeWorkOrderStatus( String ID,String status ) {
        return  workOrderService.changeWorkOrderStatus(ID,status);
    }

    @RequestMapping(value = "/changePrintStatus")
    public TNPYResponse changePrintStatus( String workOrderID ) {
        return  workOrderService.changePrintStatus(workOrderID);
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

    @RequestMapping(value = "/getordersplittomap")
    public TNPYResponse getOrderSplitToMap(String orderID ) {
        // System.out.println("controller=============" + orderID);
        return  workOrderService.getOrderSplitToMap(orderID);
    }

    @RequestMapping(value = "/finishordersplit")
    public TNPYResponse finishOrderSplit( String jsonStr,String name ) {
        return  workOrderService.finishOrderSplit(jsonStr,name);
    }

    @RequestMapping(value = "/getsuborderbyid")
    public TNPYResponse getSubOrderByID( String id ,String type) {
        return  workOrderService.getSubOrderByID(id,type);
    }

    @RequestMapping(value = "/getsuborderbyidtomap")
    public TNPYResponse getSubOrderByIDToMap( String id ,String type,String plantID,String processID) {
        return  workOrderService.getSubOrderByIDToMap(id,type,plantID,processID);
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

    @RequestMapping(value = "/changeplanproductionrecord")
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

    @RequestMapping(value = "/getplanproductionnumber")
    public TNPYResponse getPlanProductionNumber(  String plantID,String processID,String planMonth ) {
        return  workOrderService.getPlanProductionNumber(plantID,processID,planMonth);
    }

    @RequestMapping(value = "/changeonlinematerialrecord")
    public TNPYResponse changeOnlineMaterialRecord( @RequestBody String jsonStr ) {
        return  workOrderService.changeOnlineMaterialRecord(jsonStr);
    }
    @RequestMapping(value = "/mergeonlinematerialrecord")
    public TNPYResponse mergeOnlineMaterialRecord( String mergeID ,String operator ,String processID,@RequestParam(defaultValue = "-1")String inputNumber) {
        return  workOrderService.mergeOnlineMaterialRecord(mergeID,operator,processID, inputNumber);
    }
    @RequestMapping(value = "/getonlinematerialrecord")
    public TNPYResponse getOnlineMaterialRecord(  String plantID,String processID,String lineID ,String startTime,String endTime ) {
        return  workOrderService.getOnlineMaterialRecord(plantID, processID,lineID, startTime, endTime );
    }

    @RequestMapping(value = "/deleteonlinematerialrecord")
    public TNPYResponse deleteOnlineMaterialRecord(  String id ) {
        return  workOrderService.deleteOnlineMaterialRecord(id);
    }
    
    //取消投料
    @RequestMapping(value = "/cancelinputsuborder")
    public TNPYResponse cancelInputSuborder( String subOrdderID ) {
        return  workOrderService.cancelInputSuborder(subOrdderID);
    }
    
    
    //浇铸固化室批次入窑
    @RequestMapping(value = "/pushInDryingKilnjzsuborder")
    public TNPYResponse pushInDryingKilnjzsuborder( String jsonStr,String name ) {
        return  workOrderService.pushInDryingKilnjzsuborder( jsonStr, name);
    }

    //浇铸批量单独入窑入窑
    @RequestMapping(value = "/pushindryingkilnjzbybatch")
    public TNPYResponse pushinDryingKilnJZByBatch( String orderIDList ,String name ,String equipmentID ) {
        return  workOrderService.pushinDryingKilnJZByBatch( orderIDList, name,equipmentID);
    }

  //浇铸固化室批次出窑入库
    @RequestMapping(value = "/pushOutDryingKilnjzsuborder")
    public TNPYResponse pushOutDryingKilnjzsuborder( String jsonStr,String name ) {
        return  workOrderService.pushOutDryingKilnjzsuborder( jsonStr, name);
    }

    //浇铸固化室批次出窑入库
    @RequestMapping(value = "/orderOutOfDryingKiln")
    public TNPYResponse orderOutOfDryingKiln( String plantID,String processID,String startTime,String endTime ) {
        return  workOrderService.orderOutOfDryingKiln( plantID,processID,startTime, endTime);
    }
}
