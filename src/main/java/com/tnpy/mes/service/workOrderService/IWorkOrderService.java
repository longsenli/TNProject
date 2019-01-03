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
    public TNPYResponse getOrderSplit(String orderID );
    public TNPYResponse finishOrderSplit( String jsonStr );
}
