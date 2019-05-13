package com.tnpy.mes.service.tidyBatteryService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TidyBatteryRecordMapper;
import com.tnpy.mes.model.mysql.TidyBatteryRecord;
import com.tnpy.mes.service.tidyBatteryService.ITidyBatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-05-06 14:40
 */
@Service("tidyBatteryService")
public class TidyBatteryServiceImpl implements ITidyBatteryService {

    @Autowired
    private TidyBatteryRecordMapper tidyBatteryRecordMapper;

    public TNPYResponse getTidyBatteryRecord(String plantID,String processID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where status != '-1' ";
            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                filter += " and processID = '" + processID + "' ";
            }

            // System.out.println(plantID + " 参数 " +processID);
            List<TidyBatteryRecord> workLocationList = tidyBatteryRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(workLocationList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deteteTidyBatteryRecord(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            tidyBatteryRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeTidyBatteryRecord(String jsonStr)
    {
        TidyBatteryRecord tidyBatteryRecord =(TidyBatteryRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), TidyBatteryRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(tidyBatteryRecord.getId()))
            {
                tidyBatteryRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                tidyBatteryRecord.setStatus("1");
                tidyBatteryRecordMapper.insertSelective(tidyBatteryRecord);
            }
            else
            {
                tidyBatteryRecordMapper.updateByPrimaryKey(tidyBatteryRecord);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }
}
