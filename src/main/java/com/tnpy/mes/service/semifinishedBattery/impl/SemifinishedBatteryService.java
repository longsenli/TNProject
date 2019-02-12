package com.tnpy.mes.service.semifinishedBattery.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.BatteryRepairRecordMapper;
import com.tnpy.mes.mapper.mysql.BatteryScrapRecordMapper;
import com.tnpy.mes.model.mysql.BatteryScrapRecord;
import com.tnpy.mes.service.semifinishedBattery.ISemifinishedBatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            batteryScrapRecordMapper.insert(batteryScrapRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getScrapBatteryByline(String lineID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<BatteryScrapRecord> batteryScrapRecordList = batteryScrapRecordMapper.selectByFilter("");
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
}
