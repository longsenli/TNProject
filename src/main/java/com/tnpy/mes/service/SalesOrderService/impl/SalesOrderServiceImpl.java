package com.tnpy.mes.service.SalesOrderService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.SalesOrderMapper;
import com.tnpy.mes.model.mysql.SalesOrder;
import com.tnpy.mes.service.SalesOrderService.ISalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/25 9:59
 */
@Service("salesOrderService")
public class SalesOrderServiceImpl implements ISalesOrderService {
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    public TNPYResponse getSalesOrderDetail(String plantID, String productID,String status, String startTime, String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where salecreatetime >= '" +startTime + "' and salecreatetime <= '" + endTime + " 23:59:59' " ;
            if(!"-1".equals(plantID))
            {
                filter += " and  plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(productID))
            {
                filter += " and  materialID = '" + productID + "' ";
            }
            if(!"-1".equals(status))
            {
                filter += " and  orderstatus = '" + status + "' ";
            }
            List<SalesOrder> salesOrderList = salesOrderMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(salesOrderList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getSalesOrderStatusAnalysis(String plantID, String productID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " " ;
            if(!"-1".equals(plantID))
            {
                filter += " and  plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(productID))
            {
                filter += " and  materialID = '" + productID + "' ";
            }

            String sql = "select *,生产订单总量-已完成 as 未完成 from (select   date_format(productioninputtime, '%Y-%m-%d')  as 日期,";
             sql +="workshopname as 厂区,";
            sql += "batterymodel as 型号,packingquantity as 规格 , sum(salevolume) as 生产订单总量, case when orderstatus= '已发货' then sum(salevolume)  else sum(0) end as 已完成 from tb_salesOrder"
                    + " where productioninputtime >= '" + startTime + "' and productioninputtime <= '" + endTime + " 23:59:59'";

             sql += filter;
            sql += "group by productioninputtime,workshopname,batterymodel,packingquantity,orderstatus ) c";

            List<Map<String, Object>> salesOrderList = salesOrderMapper.selectBySQL(sql);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(salesOrderList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getSalesOrderDailyWork(String plantID, String productID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " " ;
            if(!"-1".equals(plantID))
            {
                filter += " and  plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(productID))
            {
                filter += " and  materialID = '" + productID + "' ";
            }


            String sql = "select date_format(productioninputtime, '%Y-%m-%d')  as 日期,a.workshopname as 厂区,";
            sql += "a.batterymodel as 型号,a.packingquantity as 规格,product as 生产订单总量,sale as 已完成,(product - sale) as  未完成 from (( select sum(salevolume) as product, productioninputtime,workshopname,batterymodel,packingquantity from tb_salesOrder "
                    + "where productioninputtime >= '" + startTime + "' and productioninputtime <= '" + endTime  + " 23:59:59'";

                sql += filter;
            sql+= "group by productioninputtime,workshopname,batterymodel,packingquantity) a left join (select sum(salevolume) as sale,deliverytime,workshopname,batterymodel,packingquantity from tb_salesOrder"
                    + " where deliverytime >= '" + startTime + "' and deliverytime <= '" + endTime  + " 23:59:59' ";

                sql += filter;
            sql += " group by deliverytime,workshopname,batterymodel,packingquantity) b on a.productioninputtime = b.deliverytime and a.workshopname=b.workshopname and a.batterymodel = b.batterymodel and a.packingquantity = b.packingquantity)";

            List<Map<String, Object>> salesOrderList = salesOrderMapper.selectBySQL(sql);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(salesOrderList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
