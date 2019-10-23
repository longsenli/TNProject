package com.tnpy.mes.service.plateWeighService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.PlateWeighRecordMapper;
import com.tnpy.mes.model.mysql.PlateWeighRecord;
import com.tnpy.mes.service.plateWeighService.IPlateWeighService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-09-03 9:52
 */
@Service("plateWeighService")
public class PlateWeighServiceImpl implements IPlateWeighService {
    @Autowired
    private PlateWeighRecordMapper plateWeighRecordMapper;
    public TNPYResponse getPlateWeighRecord(String plantID,  String balanceID, String staffName, String materialName, String startTime, String endTime ){
        TNPYResponse result = new TNPYResponse();
        try {
            String dbName = null;
            if( ConfigParamEnum.PlateWeighDBMap.containsKey(plantID))
            {
                 dbName = ConfigParamEnum.PlateWeighDBMap.get(plantID).toString();
            }
            else
            {
                result.setMessage("未查询到厂区信息，请确认！" + plantID);
                return result;
            }

            String filter = dbName + "  where time > '" + startTime + "' and time < '" + endTime + "' ";

            if(!"-1".equals(staffName))
            {
                filter += " and Operator = '" + staffName + "' ";
            }
            if(!"-1".equals(materialName))
            {
                filter += " and materialName = '" + materialName + "' ";
            }
            if(!"-1".equals(balanceID))
            {
                filter += " and balanceID = '" + balanceID + "' ";
            }
            filter += " order by time ";
            List<PlateWeighRecord> plateWeighRecordList = plateWeighRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plateWeighRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("获取称重信息出错！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse getPlateWeighBasicData(String plantID)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String dbName = null;
            if( ConfigParamEnum.PlateWeighDBMap.containsKey(plantID))
            {
                dbName = ConfigParamEnum.PlateWeighDBMap.get(plantID).toString();
            }
            else
            {
                result.setMessage("未查询到厂区信息，请确认！" + plantID);
                return result;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -49);
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果

      String sql = "(SELECT distinct materialName as name ,'1' as type FROM " +dbName+" where time > '"+dateFormat.format(date)+"')\n" +
              "union all  " +
              "(SELECT distinct Operator as name ,'2' as type FROM  " +dbName+" where time > '"+dateFormat.format(date)+"')\n" +
              "union all  " +
              "(SELECT distinct balanceID as name ,'3' as type FROM  " +dbName+" where time > '"+dateFormat.format(date)+"' order by name limit 100 )\n" +
              "order by type ;";

            List<Map<Object,Object>> plateWeighList = plateWeighRecordMapper.selectBasicData(sql);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plateWeighList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("获取称重基础信息出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getRealtimeRecord(String plantID, String staffName, String materialName,String balanceID )
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String dbName = null;
            if( ConfigParamEnum.PlateWeighDBMap.containsKey(plantID))
            {
                dbName = ConfigParamEnum.PlateWeighDBMap.get(plantID).toString();
            }
            else
            {
                result.setMessage("未查询到厂区信息，请确认！" + plantID);
                return result;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, -30);
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果

            String sql = "select DATE_FORMAT(time,'%Y-%m-%d %H:%i:%s') as time,materialName,Operator,weight,centerWeight FROM " +dbName+" where time > '"+dateFormat.format(date) +"' ";
            if(!"-1".equals(staffName))
            {
                sql += " and Operator ='" + staffName + "'";
            }
            if(!"-1".equals(materialName))
            {
                sql += " and materialName ='" + materialName + "'";
            }
            if(!"-1".equals(balanceID))
            {
                sql += " and balanceID ='" + balanceID + "'";
            }
             sql += " order by time  ";

            List<Map<Object,Object>> plateWeighList = plateWeighRecordMapper.selectBasicData(sql);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plateWeighList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("获取称重基础信息出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getQualifiedRateInfo(String plantID, String balanceID,String staffName, String materialName, String weighQualifyRange, String startTime, String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String dbName = null;
            if( ConfigParamEnum.PlateWeighDBMap.containsKey(plantID))
            {
                dbName = ConfigParamEnum.PlateWeighDBMap.get(plantID).toString();
            }
            else
            {
                result.setMessage("未查询到厂区信息，请确认！" + plantID);
                return result;
            }

            String filter = dbName + "  where time > '" + startTime + "' and time < '" + endTime + "' ";

            if(!"-1".equals(staffName))
            {
                filter += " and Operator = '" + staffName + "' ";
            }
            if(!"-1".equals(materialName))
            {
                filter += " and materialName = '" + materialName + "' ";
            }
            if(!"-1".equals(balanceID))
            {
                filter += " and balanceID = '" + balanceID + "' ";
            }

            List<Map<Object,Object>> plateWeighList = plateWeighRecordMapper.QualifiedRateInfo(filter,weighQualifyRange);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plateWeighList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("获取称重信息出错！" + ex.getMessage());
            return result;
        }
    }
}
