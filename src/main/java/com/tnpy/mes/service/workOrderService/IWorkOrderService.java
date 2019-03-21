package com.tnpy.mes.service.workOrderService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:38
 */
public interface IWorkOrderService {
    public TNPYResponse getWorkOrder( );
    public TNPYResponse getWorkOrderByLineID( String lineID );
    public TNPYResponse changeWorkOrder( String jsonStr );
    public TNPYResponse changeWorkOrderStatus( String ID,String status );
    public TNPYResponse getOrderSplit(String orderID );
    public TNPYResponse finishOrderSplit( String jsonStr,String name );
    public TNPYResponse getWorkOrderByParam(String plantID,String processID,String lineID );
    public TNPYResponse getCustomWorkOrderByParam(String plantID,String processID,String lineID );
    public TNPYResponse getOrderSplitAfterMap(String orderID );
    public TNPYResponse getSubOrderByID( String id ,String type);

    public TNPYResponse getPlanProductionDashboard( String plantID,String processID,String startTime,String endTime );
    public TNPYResponse getRealtimeProductionDashboard( String plantID,String processID,String startTime,String endTime );

    public TNPYResponse cancelFinishSuborder( String subOrdderID );

    public TNPYResponse changePlanProductionRecord(  String jsonStr );
    public TNPYResponse getPlanProductionRecord(  String plantID,String processID,String startTime,String endTime );
    public TNPYResponse deletePlanProductionRecord(  String id );
    public TNPYResponse getPlanProductionNumber(  String plantID,String processID,String planMonth );

    public TNPYResponse changeOnlineMaterialRecord(  String jsonStr );
    public TNPYResponse getOnlineMaterialRecord(  String plantID,String processID,String lineID ,String startTime,String endTime ) ;
    public TNPYResponse deleteOnlineMaterialRecord(  String id );
}
