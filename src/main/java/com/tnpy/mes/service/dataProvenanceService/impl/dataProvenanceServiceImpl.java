package com.tnpy.mes.service.dataProvenanceService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.DataProvenanceRelationMapper;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.service.dataProvenanceService.IDataProvenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/10 9:34
 */
@Service("dataProvenanceService")
public class dataProvenanceServiceImpl implements IDataProvenanceService {

    @Autowired
    private MaterialRecordMapper materialRecordMapper;
    @Autowired
    private DataProvenanceRelationMapper dataProvenanceRelationMapper;
    public TNPYResponse getProvenanceByOrderID(String orderID )
    {
        TNPYResponse result = new TNPYResponse();

        List<Map<Object, Object>> dcdkList = dataProvenanceRelationMapper.selectBatteryInfoByJQCode(orderID);

        Set<String> orderList = new HashSet<>();
        orderList.add(orderID);

        try
        {
            List<String> tmpOrderList = new ArrayList<>();
            String orderFilter = "'" + orderID +"'";
            while(true)
            {
                tmpOrderList.clear();
                tmpOrderList = dataProvenanceRelationMapper.selectInOrderIDByOutOrderID(orderFilter);
                if(tmpOrderList.size() < 1)
                {
                    break;
                }
                orderFilter = "";
                for(int i = 0;i<tmpOrderList.size();i++)
                {
                    orderList.add(tmpOrderList.get(i));
                    orderFilter += "'" + tmpOrderList.get(i) +"',";
                }
                orderFilter = orderFilter.substring(0,orderFilter.length()-1);
            }

            orderFilter = "'" + orderID +"'";
            while(true)
            {
                System.out.println(orderFilter);
                tmpOrderList.clear();
                tmpOrderList = dataProvenanceRelationMapper.selectOutOrderIDByInOrderID(orderFilter);
                if(tmpOrderList.size() < 1)
                {
                    break;
                }
                orderFilter = "";
                for(int i = 0;i<tmpOrderList.size();i++)
                {
                    if(StringUtils.isEmpty(tmpOrderList.get(i)))
                    {
                        continue;
                    }
                    orderList.add(tmpOrderList.get(i));
                    orderFilter +=   "'" + tmpOrderList.get(i) +"',";
                }
                if(orderFilter.length() < 10)
                {
                    break;
                }
                orderFilter = orderFilter.substring(0,orderFilter.length()-1);
            }
            orderFilter = "";
            Iterator<String> iterator=orderList.iterator();
            while(iterator.hasNext()){
                orderFilter +=  "'" + iterator.next()+"',";
            }
            orderFilter = orderFilter.substring(0,orderFilter.length()-1);

            List<Map<Object, Object>> materialRecordList = materialRecordMapper.selectByOrderIDList(orderFilter);

            materialRecordList.addAll(dcdkList);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(materialRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    @Override
    public TNPYResponse getProvenanceByDCDK(String DKQRCode) {
        TNPYResponse result = new TNPYResponse();
        List<Map<Object, Object>> dcdkList = dataProvenanceRelationMapper.selectBatteryInfoByBottomQR(DKQRCode);
        if(dcdkList.size() ==0)
        {
            result.setMessage("查询出错！未找到底壳二维码信息！" + DKQRCode );
            return  result;
        }

        String orderID = dcdkList.get(0).get("JQID").toString();
        Set<String> orderList = new HashSet<>();
        orderList.add(orderID);
        try
        {
            List<String> tmpOrderList = new ArrayList<>();
            String orderFilter = "'" + orderID +"'";
            while(true)
            {
                tmpOrderList.clear();
                tmpOrderList = dataProvenanceRelationMapper.selectInOrderIDByOutOrderID(orderFilter);
                if(tmpOrderList.size() < 1)
                {
                    break;
                }
                orderFilter = "";
                for(int i = 0;i<tmpOrderList.size();i++)
                {
                    orderList.add(tmpOrderList.get(i));
                    orderFilter += "'" + tmpOrderList.get(i) +"',";
                }
                orderFilter = orderFilter.substring(0,orderFilter.length()-1);
            }
            orderFilter = "'" + orderID +"'";
            while(true)
            {
                tmpOrderList.clear();
                tmpOrderList = dataProvenanceRelationMapper.selectOutOrderIDByInOrderID(orderFilter);
                if(tmpOrderList.size() < 1)
                {
                    break;
                }
                orderFilter = "'";
                for(int i = 0;i<tmpOrderList.size();i++)
                {
                    orderList.add(tmpOrderList.get(i));
                    orderFilter +=   "'" + tmpOrderList.get(i) +"',";
                }
                orderFilter = orderFilter.substring(0,orderFilter.length()-1);
            }
            orderFilter = "";
            Iterator<String> iterator=orderList.iterator();
            while(iterator.hasNext()){
                orderFilter +=  "'" + iterator.next()+"',";
            }
            orderFilter = orderFilter.substring(0,orderFilter.length()-1);

            List<Map<Object, Object>> materialRecordList = materialRecordMapper.selectByOrderIDList(orderFilter);

            materialRecordList.addAll(dcdkList);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(materialRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
