package com.tnpy.mes.service.safetyAndEPService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.HiddenDangerManageRecordMapper;
import com.tnpy.mes.mapper.mysql.IchnographyDetailInfoMapper;
import com.tnpy.mes.model.mysql.HiddenDangerManageRecord;
import com.tnpy.mes.model.mysql.IchnographyDetailInfo;
import com.tnpy.mes.service.safetyAndEPService.ISafetyAndPEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-06-03 16:31
 */
@Service("safetyAndPEService")
public class SafetyAndPEService implements ISafetyAndPEService {

    @Autowired
    private IchnographyDetailInfoMapper ichnographyDetailInfoMapper;

    @Autowired
    private HiddenDangerManageRecordMapper hiddenDangerManageRecordMapper;

    public TNPYResponse getIchnographyDetailInfo(String mainRegionID){
        TNPYResponse result = new TNPYResponse();
        try
        {
           List<IchnographyDetailInfo> ichnographyDetailInfoList = ichnographyDetailInfoMapper.selectByMinRegionID(mainRegionID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(ichnographyDetailInfoList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getHiddenDangerManageRecord(String plantID,String selectLevel,String startTime,String endTime) {
        {
            TNPYResponse result = new TNPYResponse();
            try {
                String filter = " where status != '-1' and  reportTime >='" + startTime + "' and reportTime <'" + endTime + "' ";

                if (!"-1".equals(plantID)) {
                    filter += " and plantID = '" + plantID + "' ";
                }
                if (!"-1".equals(selectLevel)) {
                    filter += " and dangerLevel = '" + selectLevel + "' ";
                }

                filter += " order by reportTime  ";
                // System.out.println(plantID + " 参数 " +processID);
                List<HiddenDangerManageRecord> hiddenDangerManageRecordList = hiddenDangerManageRecordMapper.selectByFilter(filter);
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

                result.setData(JSONObject.toJSON(hiddenDangerManageRecordList).toString());
                return result;
            } catch (Exception ex) {
                result.setMessage("查询出错！" + ex.getMessage());
                return result;
            }
        }
    }
    public TNPYResponse deteteHiddenDangerManageRecord(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            hiddenDangerManageRecordMapper.deleteByChangeStatus(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeHiddenDangerManageRecord( String jsonStr)
    {
        HiddenDangerManageRecord hiddenDangerManageRecord =(HiddenDangerManageRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), HiddenDangerManageRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(hiddenDangerManageRecord.getId()))
            {
                hiddenDangerManageRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                hiddenDangerManageRecord.setStatus("1");
                hiddenDangerManageRecordMapper.insertSelective(hiddenDangerManageRecord);
            }
            else
            {
                hiddenDangerManageRecord.setDealtime(new Date());
                hiddenDangerManageRecordMapper.dealHiddenDangerManageRecord(hiddenDangerManageRecord.getId(),hiddenDangerManageRecord.getDealinfo(),hiddenDangerManageRecord.getDealstaff(),
                        hiddenDangerManageRecord.getRemark(),hiddenDangerManageRecord.getDealpicture(),hiddenDangerManageRecord.getDealtime());
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

    public TNPYResponse getHiddenDangerManageCharts(String plantID,String selectLevel,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where status != '-1' and  reportTime >='" + startTime + "' and reportTime <'" + endTime + "' ";

            if (!"-1".equals(plantID)) {
                filter += " and plantID = '" + plantID + "' ";
            }
            if (!"-1".equals(selectLevel)) {
                filter += " and dangerLevel = '" + selectLevel + "' ";
            }

            filter += " group by plantID,status  ";
            // System.out.println(plantID + " 参数 " +processID);
            List<Map<Object, Object>> hiddenDangerManageRecordList = hiddenDangerManageRecordMapper.selectRecordSummaryByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(hiddenDangerManageRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
