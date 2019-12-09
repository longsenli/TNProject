package com.tnpy.mes.service.interphonePatrolService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.InterphonePatrolLocationMapper;
import com.tnpy.mes.mapper.mysql.InterphonePatrolRecordMapper;
import com.tnpy.mes.service.interphonePatrolService.IInterphonePatrolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-12-09 8:30
 */
@Service("interphonePatrolService")
public class InterphonePatrolServiceImpl implements IInterphonePatrolService {
    @Autowired
    private  InterphonePatrolLocationMapper interphonePatrolLocationMapper;

    @Autowired
    private InterphonePatrolRecordMapper interphonePatrolRecordMapper;

    public TNPYResponse getInterphonePatrolLocationInfo(String plantID, String processID)
    {
        TNPYResponse result = new TNPYResponse();
        try {
        String filter = " where status != '-1' ";
        if(!"-1".equals(plantID))
        {
            filter += " and  plantID = '" + plantID +"' ";
        }
            if(!"-1".equals(processID))
            {
                filter += " and  processID = '" + processID +"' ";
            }
        List<Map<Object,Object>> parameterInfoList = interphonePatrolLocationMapper.selectLocationInfoDetail(filter);
        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
        result.setData(JSONObject.toJSON(parameterInfoList).toString());
        return result;
    } catch (Exception ex) {
        result.setMessage("查询出错！" + ex.getMessage());
        return result;
    }
    }
}
