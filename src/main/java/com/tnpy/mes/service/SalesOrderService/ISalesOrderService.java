package com.tnpy.mes.service.SalesOrderService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/25 9:58
 */
public interface ISalesOrderService {
    public TNPYResponse getSalesOrderDetail(String plantID, String productID,String status, String startTime, String endTime);
    public TNPYResponse getSalesOrderStatusAnalysis(String plantID, String productID,String startTime,String endTime);
    public TNPYResponse getSalesOrderDailyWork(String plantID, String productID,String startTime,String endTime);
}
