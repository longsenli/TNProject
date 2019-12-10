package com.tnpy.mes.service.interphonePatrolService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.InterphonePatrolLocationMapper;
import com.tnpy.mes.mapper.mysql.InterphonePatrolRecordMapper;
import com.tnpy.mes.model.mysql.InterphonePatrolRecord;
import com.tnpy.mes.service.interphonePatrolService.IInterphonePatrolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-12-09 8:30
 */
@Service("interphonePatrolService")
public class InterphonePatrolServiceImpl implements IInterphonePatrolService {
    @Autowired
    private InterphonePatrolLocationMapper interphonePatrolLocationMapper;

    @Autowired
    private InterphonePatrolRecordMapper interphonePatrolRecordMapper;

    public TNPYResponse getInterphonePatrolLocationInfo(String plantID, String processID) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where status != '-1' ";
            if (!"-1".equals(plantID)) {
                filter += " and  plantID = '" + plantID + "' ";
            }
            if (!"-1".equals(processID)) {
                filter += " and  processID = '" + processID + "' ";
            }
            List<Map<Object, Object>> parameterInfoList = interphonePatrolLocationMapper.selectLocationInfoDetail(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse addInterphonePatrolRecord(String recordJson) {
        TNPYResponse result = new TNPYResponse();
        try {

            List<InterphonePatrolRecord> interphonePatrolRecordList = new ArrayList<>();
            interphonePatrolRecordList = JSONObject.parseArray(recordJson, InterphonePatrolRecord.class);
            if (interphonePatrolRecordList.size() < 1) {
                result.setMessage("操作失败！未获取到巡查信息！");
                return result;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            String timeString = "";
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if (calendar.get(Calendar.HOUR_OF_DAY) < 16) {
                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            }
            timeString = dateFormat.format(date);

            if (interphonePatrolRecordMapper.checkRecordExit(timeString, interphonePatrolRecordList.get(0).getOrdinal()) > 1) {
                result.setMessage(timeString + "日第" + interphonePatrolRecordList.get(0).getOrdinal().toString() + "次巡查结果已保存！");
                return result;
            }

            for (int i = 0; i < interphonePatrolRecordList.size(); i++) {
                interphonePatrolRecordList.get(i).setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                interphonePatrolRecordList.get(i).setDaytime(timeString);
                interphonePatrolRecordList.get(i).setStatus("1");
                interphonePatrolRecordList.get(i).setPatroltime(new Date());
                interphonePatrolRecordMapper.insertSelective(interphonePatrolRecordList.get(i));
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("操作失败！" + ex.getMessage().substring(0, 100));
            return result;
        }
    }

    public TNPYResponse deleteInterphonePatrolRecord(String id) {
        TNPYResponse result = new TNPYResponse();
        try {
            interphonePatrolRecordMapper.deleteByUpdateStatus(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData("删除成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("删除失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getInterphonePatrolRecordDetail(String plantID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where status != '-1' and dayTime = '" +startTime + "' " ;
            if(!"-1".equals(plantID))
            {
                filter += " and  plantID = '" + plantID +"' ";
            }

            List<Map<Object,Object>> parameterInfoList = interphonePatrolRecordMapper.getInterphonePatrolRecordDetail(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getInterphonePatrolRecordReport(String plantID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {


            List<Map<Object,Object>> parameterInfoList = interphonePatrolRecordMapper.getInterphonePatrolRecordReport(startTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
