package com.tnpy.mes.service.workingScoreService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.WorkingScoreRecordMapper;
import com.tnpy.mes.service.workingScoreService.IWorkingScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2020-01-15 14:12
 */
@Service("workingScoreService")
public class WorkingScoreServiceImpl implements IWorkingScoreService {
    @Autowired
    private WorkingScoreRecordMapper workingScoreRecordMapper;

    public TNPYResponse getWorkingScoreDetailRecord(String staffName, String startTime, String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object,Object>> plasticUsedRecordList = workingScoreRecordMapper.getWorkScoreDetailRecord(staffName,startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plasticUsedRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getWorkingScoreDailySummaryRecord(String staffName,String startTime,String endTime)
    {

        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object,Object>> plasticUsedRecordList = workingScoreRecordMapper.getWorkingScoreDailySummaryRecord(staffName,startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plasticUsedRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse getWorkingScorePeriodSummaryRecord(String staffName,String startTime,String endTime)
    {

        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object,Object>> plasticUsedRecordList = workingScoreRecordMapper.getWorkingScorePeriodSummaryRecord(staffName,startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(plasticUsedRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
