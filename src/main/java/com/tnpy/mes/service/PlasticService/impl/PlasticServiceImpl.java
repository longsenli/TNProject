package com.tnpy.mes.service.PlasticService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.mapper.mysql.PlasticUsedRecordMapper;
import com.tnpy.mes.model.mysql.MaterialRecord;
import com.tnpy.mes.model.mysql.PlasticUsedRecord;
import com.tnpy.mes.service.PlasticService.IPlasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-08-19 10:45
 */
@Service("plasticService")
public class PlasticServiceImpl  implements IPlasticService {
    @Autowired
    private PlasticUsedRecordMapper plasticUsedRecordMapper;

    @Autowired
    private MaterialRecordMapper materialRecordMapper;

    public TNPYResponse getPlasticUsedRecord(String plantID, String lineID, String locationID, String startTime, String endTime )
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter ="";
            if(startTime.substring(0,10).equals(endTime.substring(0,10))  )
            {
                filter += " where usedOrderID like  '%BB" + startTime.substring(0,10).replaceAll("-","") + "'  ";
            }
            else if(startTime.contains("19:") && endTime.contains("07:"))
            {
                filter += " where usedOrderID like  '%YB" + startTime.substring(0,10).replaceAll("-","") + "'  ";
            }
            else
            {
                filter += " where usedTime > '" + startTime + "' and usedTime < '" + endTime + "' ";
            }
            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            if(!"-1".equals(locationID))
            {
                filter += " and workLocation = '" + locationID + "' ";
            }
            filter += " group by workLocation,materialName ";

            List<Map<Object,Object>> plasticUsedRecordList = plasticUsedRecordMapper.selectByParam(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plasticUsedRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse addPlasticUsedRecord(String listID,String userID,String userName,String plantID,String lineID,String locationID,String orderID,String orderIDZH,String materialIDZH,String materialNameZH ){
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<String, String>> grantResult = new  ArrayList<Map<String, String>>();
            String[] orderArray = listID.split("###");

            int count2 = plasticUsedRecordMapper.checkMaterialUsable(orderID, materialIDZH);
            if (count2 < 1) {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage(materialNameZH + "不能够使用该极群！");
                return result;
            }

            MaterialRecord materialRecord = materialRecordMapper.selectBySuborderID(orderID);

            int remainNumber = Double.valueOf(materialRecord.getInputworklocationid()).intValue();
           // System.out.println("========" + Double.valueOf(materialRecord.getInputworklocationid()).intValue());
            if( orderArray.length >remainNumber)
            {
                for(int i =0;i<orderArray.length;i++) {
                    Map<String, String> mapResult = new HashMap<String, String>();
                    mapResult.put("orderID", orderArray[i]);
                    mapResult.put("status", "失败");
                    mapResult.put("returnMessage", "物料不足!剩余：" + materialRecord.getInputworklocationid() + "，本次需使用：" + orderArray.length);
                    grantResult.add(mapResult);
                }

                result.setData(JSONObject.toJSON(grantResult).toString());
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                return  result;
             }
            int currentUsedNumber = 0;
            for(int i =0;i<orderArray.length;i++)
            {
                Map<String, String> mapResult = new HashMap<String, String>();
                mapResult.put("orderID",orderArray[i]);
                mapResult.put("status","失败");
                mapResult.put("returnMessage","失败!");

                try
                {
                    PlasticUsedRecord plasticUsedRecord = new PlasticUsedRecord();
                    plasticUsedRecord.setId(orderArray[i]);
                    plasticUsedRecord.setLineid(lineID);
                    plasticUsedRecord.setPlantid(plantID);
                    plasticUsedRecord.setWorklocation(locationID);
                    plasticUsedRecord.setStaffid(userID);
                    plasticUsedRecord.setStaffname(userName);
                    plasticUsedRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                    plasticUsedRecord.setUsedtime(new Date());
                    if(materialRecord != null)
                    {
                        plasticUsedRecord.setJqid(materialRecord.getSuborderid());
                        plasticUsedRecord.setJqstaff(materialRecord.getInputer());
                        plasticUsedRecord.setJqtime(materialRecord.getInputtime());
                    }
                    plasticUsedRecord.setUsedorderid(orderIDZH);
                    plasticUsedRecord.setMaterialid(materialIDZH);
                    plasticUsedRecord.setMaterialname(materialNameZH);
                    plasticUsedRecordMapper.insert(plasticUsedRecord);
                    mapResult.put("status","成功");
                    mapResult.put("returnMessage","成功!");
                    currentUsedNumber++;
                }
                catch (Exception ex)
                {
                    if(ex.getMessage().contains("Duplicate entry"))
                    {
                        mapResult.put("returnMessage","该底壳已扫码");
                    }
                    else
                    {
                        mapResult.put("returnMessage",ex.getMessage());
                    }

                }

                grantResult.add(mapResult);
            }
            materialRecordMapper.updateJQNumber(materialRecord.getId(),(remainNumber - currentUsedNumber) +"");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            dateFormat.format(date);
            String timeFinish = "";
            String timeStart = "";
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if (calendar.get(Calendar.HOUR_OF_DAY) > 6 && calendar.get(Calendar.HOUR_OF_DAY) < 19) {
                timeStart = dateFormat.format(date) + " 07:00:00";
                timeFinish = dateFormat.format(date) + " 19:00:00";

            } else  if (calendar.get(Calendar.HOUR_OF_DAY) < 7){
             //   timeStart = dateFormat.format(date) + " 19:00:00";
                timeFinish = dateFormat.format(date) + " 07:00:00";

                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                timeStart = dateFormat.format(date) + " 19:00:00";
            }
            else
            {
                timeStart = dateFormat.format(date) + " 19:00:00";

                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                timeFinish = dateFormat.format(date) + " 7:00:00";
            }
            int finishedNumber = plasticUsedRecordMapper.selectPlasticUsedNumberByOrder(locationID,"'%" + orderIDZH.substring(orderIDZH.length() - 10) +"'");
            System.out.println(orderIDZH.substring(orderIDZH.length() - 10,10));
            result.setData(JSONObject.toJSON(grantResult).toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage((remainNumber - currentUsedNumber) + "___" +finishedNumber);
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("底壳投料失败！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse plasticDataProvenance(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where id ='" + id + "'  ";

            List<Map<Object,Object>> plasticUsedRecordList = plasticUsedRecordMapper.selectByParam(filter);

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plasticUsedRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getgetInputTotalNumberByClass(String plantID, String lineID, String locationID, String workOrder)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = "";
            if(!StringUtils.isEmpty(workOrder) && workOrder.length() > 10)
            {
                filter += "where usedOrderID like '%" + workOrder.substring(workOrder.length() - 10) +"' ";
            }
            else
            {
                filter += " where usedOrderID = '" + workOrder +"' ";
            }
            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            if(!"-1".equals(locationID))
            {
                filter += " and workLocation = '" + locationID + "' ";
            }
            filter += " group by workLocation,materialName ";

            List<Map<Object,Object>> plasticUsedRecordList = plasticUsedRecordMapper.selectByParam(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plasticUsedRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
