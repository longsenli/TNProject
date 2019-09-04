package com.tnpy.mes.service.wageManageService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.RewardingPunishmentDetailMapper;
import com.tnpy.mes.mapper.mysql.WageDetailMapper;
import com.tnpy.mes.service.wageManageService.IWageManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-09-04 9:46
 */
@Service("wageManageService")
public class WageManageServiceImpl implements IWageManageService {
    @Autowired
    private WageDetailMapper wageDetailMapper;

    @Autowired
    private RewardingPunishmentDetailMapper rewardingPunishmentDetailMapper;

    public TNPYResponse getProductionWageDetail(String plantID, String processID, String staffName, String startTime, String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object, Object>> mapList = wageDetailMapper.selectByFilter(plantID,processID,startTime,endTime);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getRewardingPunishmentDetail(String plantID, String processID, String staffName, String startTime, String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object, Object>> mapList = rewardingPunishmentDetailMapper.selectByFilter(plantID,processID,startTime,endTime);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
