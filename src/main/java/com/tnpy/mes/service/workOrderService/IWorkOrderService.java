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
    public TNPYResponse changePrintStatus( String workOrderID );
    public TNPYResponse getOrderSplit(String orderID );
    public TNPYResponse finishOrderSplit( String jsonStr,String name );
    public TNPYResponse getWorkOrderByParam(String plantID,String processID,String lineID );
    public TNPYResponse getCustomWorkOrderByParam(String plantID,String processID,String lineID );
    public TNPYResponse getOrderSplitAfterMap(String orderID );
    public TNPYResponse getSubOrderByID( String id ,String type);
    public TNPYResponse deleteWorkOrder( String orderID );

    public TNPYResponse getOrderSplitToMap(String orderID );
    public TNPYResponse getSubOrderByIDToMap( String id ,String type,String plantID,String processID);

    public TNPYResponse getPlanProductionDashboard( String plantID,String processID,String startTime,String endTime );
    public TNPYResponse getRealtimeProductionDashboard( String plantID,String processID,String startTime,String endTime );

    public TNPYResponse cancelFinishSuborder( String subOrdderID );

    public TNPYResponse changePlanProductionRecord(  String jsonStr );
    public TNPYResponse getPlanProductionRecord(  String plantID,String processID,String startTime,String endTime,String slctType );
    public TNPYResponse deletePlanProductionRecord(  String id );
    public TNPYResponse getPlanProductionNumber(  String plantID,String processID,String planMonth );

    public TNPYResponse changeOnlineMaterialRecord(  String jsonStr );
    public TNPYResponse getOnlineMaterialRecord(  String plantID,String processID,String lineID ,String startTime,String endTime ) ;
    public TNPYResponse deleteOnlineMaterialRecord(  String id );
    public TNPYResponse mergeOnlineMaterialRecord( String mergeID ,String operator,String processID,String inputNumber );
    
    public TNPYResponse cancelInputSuborder( String subOrdderID );
    
    //浇铸干燥窑入窑
    public TNPYResponse pushInDryingKilnjzsuborder( String jsonStr ,String name );

    //浇铸批量入窑
    public TNPYResponse pushinDryingKilnJZByBatch( String orderIDList ,String name ,String equipmentID);

    //浇铸干燥窑出窑
    public TNPYResponse pushOutDryingKilnjzsuborder( String jsonStr ,String name );
    //未入窑记录
    public TNPYResponse  orderOutOfDryingKiln( String plantID,String processID,String startTime,String endTime );

    public TNPYResponse addWorkorderTemplate( String jsonStr );
    public TNPYResponse deleteWorkorderTemplate( String id ) ;

    public TNPYResponse getWorkorderTemplate( String plantID,String processID,String lineID );

    public TNPYResponse addWorkorderTemplateBatch( String orderForepart ,String orderMidpiece ,String orderPosterior ,String creator ,String recordJsonString );
    
    public TNPYResponse workOrderPutIntoManage(String plantID,String processID,String lineID,String startTime, String endTime, String classType);
}
