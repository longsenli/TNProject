package com.tnpy.mes.service.chargePackService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.ChargingRackRecordMapper;
import com.tnpy.mes.model.mysql.ChargingRackRecord;
import com.tnpy.mes.service.chargePackService.IChargePackService;
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
@Service("chargePackService")
public class ChargePackServiceImpl implements IChargePackService {

    @Autowired
    private ChargingRackRecordMapper chargingRackRecordMapper;

    public TNPYResponse getChargingRackRecord(String plantID,String processID)
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
            List<ChargingRackRecord> workLocationList = chargingRackRecordMapper.selectByFilter(filter);
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
    public TNPYResponse deteteChargingRackRecord(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            chargingRackRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeChargingRackRecord(String jsonStr)
    {
        ChargingRackRecord chargingRackRecord =(ChargingRackRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), ChargingRackRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(chargingRackRecord.getId()))
            {
                chargingRackRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                chargingRackRecord.setStatus("1");
                chargingRackRecordMapper.insertSelective(chargingRackRecord);
            }
            else
            {
                chargingRackRecordMapper.updateByPrimaryKey(chargingRackRecord);
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
