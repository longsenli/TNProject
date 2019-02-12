package com.tnpy.mes.service.semifinishedBattery.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.BatteryRepairRecordMapper;
import com.tnpy.mes.mapper.mysql.BatteryScrapRecordMapper;
import com.tnpy.mes.model.mysql.BatteryRepairRecord;
import com.tnpy.mes.model.mysql.BatteryScrapRecord;
import com.tnpy.mes.service.semifinishedBattery.ISemifinishedBatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/12 10:13
 */
@Service("semifinishedBatteryService")
public class SemifinishedBatteryService implements ISemifinishedBatteryService {
    @Autowired
    private BatteryScrapRecordMapper batteryScrapRecordMapper;

    @Autowired
    private BatteryRepairRecordMapper batteryRepairRecordMapper;

    public TNPYResponse addScrapBattery(String jsonStr)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            BatteryScrapRecord batteryScrapRecord=(BatteryScrapRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), BatteryScrapRecord.class);
            batteryScrapRecord.setScraptime(new Date());
            batteryScrapRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
            batteryScrapRecordMapper.insert(batteryScrapRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getScrapBatteryByline(String lineID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = "";
            if(!"-1".equals(lineID))
            {
                filter += " where lineID = '" + lineID + "'";
            }
            filter += " order by scrapTime desc ";
            List<BatteryScrapRecord> batteryScrapRecordList = batteryScrapRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(batteryScrapRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse deleteScrapBattery(String batteryID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            batteryScrapRecordMapper.deleteByPrimaryKey(batteryID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse  addRepairBattery( String jsonStr,String type)
    {
       TNPYResponse result = new TNPYResponse();
        try
        {
            BatteryRepairRecord batteryRepairRecord=(BatteryRepairRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), BatteryRepairRecord.class);

            if("add".equals(type))
            {
                batteryRepairRecordMapper.insertSelective(batteryRepairRecord);
            }
           else if("change".equals(type))
           {
               batteryRepairRecordMapper.updateByPrimaryKey(batteryRepairRecord);
           }
           else
            {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("操作类型不明！传入为：" + type + "，应为add或change");
                return  result;
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getRepairBatteryByline(String lineID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = "";
            if(!"-1".equals(lineID))
            {
                filter += "  where lineID = '" + lineID + "' ";
            }
            filter += " order by repairTime desc ";
            List<BatteryRepairRecord> batteryRepairRecordList = batteryRepairRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(batteryRepairRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deleteRepairBattery(String batteryID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            batteryRepairRecordMapper.deleteByPrimaryKey(batteryID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
}
