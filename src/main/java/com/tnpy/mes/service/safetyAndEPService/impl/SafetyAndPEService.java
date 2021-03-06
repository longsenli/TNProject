package com.tnpy.mes.service.safetyAndEPService.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.HiddenDangerManageRecordMapper;
import com.tnpy.mes.mapper.mysql.IchnographyDetailInfoMapper;
import com.tnpy.mes.mapper.mysql.WarningMessageRecordMapper;
import com.tnpy.mes.model.mysql.HiddenDangerManageRecord;
import com.tnpy.mes.model.mysql.IchnographyDetailInfo;
import com.tnpy.mes.model.mysql.WarningMessageRecord;
import com.tnpy.mes.service.safetyAndEPService.ISafetyAndPEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private WarningMessageRecordMapper warningMessageRecordMapper;
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

    public TNPYResponse getIchnographyDetailInfoSST(String mainRegionID,String startTime,String endTime){
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object,Object>> ichnographyDetailInfoList = ichnographyDetailInfoMapper.selectSSTByMinRegionID(mainRegionID,startTime,endTime);
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

    public TNPYResponse getMyReprotDanger(String name,String type)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -7);
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果

            String filter = " where status != '-1' and  reportTime >='" + dateFormat.format(date)  + "' and  reporter='" + name + "' and hiddenDangerType ='" + type+"'";
            filter += " order by reportTime desc ";
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

    public TNPYResponse getHiddenDangerManageRecord(String plantID,String selectLevel,String startTime,String endTime) {
        {
            TNPYResponse result = new TNPYResponse();
            try {
                String filter = " where status != '-1' and  reportTime >='" + startTime + "' and reportTime <'" + endTime +  "' and hiddenDangerType ='隐患上报'";

                if (!"-1".equals(plantID)) {
                    filter += " and plantID = '" + plantID + "' ";
                }
                if (!"-1".equals(selectLevel)) {
                    filter += " and dangerLevel = '" + selectLevel + "' ";
                }

                filter += " order by reportTime desc  ";
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
    public TNPYResponse getRegularInspectRecord(String staffName,String equipID,String startTime,String endTime)
    {
            TNPYResponse result = new TNPYResponse();
            try {
                String filter = " where status != '-1' and  reportTime >='" + startTime + "' and reportTime <'" + endTime +  "' and hiddenDangerType = '定点巡查' ";

                if (!"-1".equals(staffName)) {
                    filter += " and reporter = '" + staffName + "' ";
                }
                if (!"-1".equals(equipID)) {
                    filter += " and equipmentID = '" + equipID + "' ";
                }
                filter += " order by reportTime  ";
                // System.out.println(plantID + " 参数 " +processID);
                List<Map<Object, Object>> hiddenDangerManageRecordList = hiddenDangerManageRecordMapper.getRegularInspectRecord(filter);
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSON(hiddenDangerManageRecordList).toString());
                return result;
            } catch (Exception ex) {
                result.setMessage("查询出错！" + ex.getMessage());
                return result;
            }

    }
    public TNPYResponse getRegularLocationTimes(String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> hiddenDangerManageRecordList = hiddenDangerManageRecordMapper.getRegularLocationTimes(startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(hiddenDangerManageRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse getLocationInfoByQR(String qrCode)
    {
        TNPYResponse result = new TNPYResponse();
        try {

            String filter = "";
            if("-1".equals(qrCode))
            {
                filter = " where typeID = '10050' order by ordernum ";
            }
            else
            {
                filter = " where id = '" +qrCode+"'  ";
            }
            List<Map<Object,Object>> locationInfo = hiddenDangerManageRecordMapper.selectLocationInfoByQR(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(locationInfo).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
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
                hiddenDangerManageRecord.setReporttime(new Date());

                if(StringUtil.isEmpty(hiddenDangerManageRecord.getHiddendangertype()))
                {
                    hiddenDangerManageRecord.setHiddendangertype("隐患上报");
                }
                    WarningMessageRecord warningMessageRecord = new WarningMessageRecord();
                    warningMessageRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                    warningMessageRecord.setNotificationtypeid("100001");
                    warningMessageRecord.setPlantid(hiddenDangerManageRecord.getPlantid());
                    warningMessageRecord.setMessage(hiddenDangerManageRecord.getHiddendanger());
                    warningMessageRecord.setUpdater(hiddenDangerManageRecord.getReporter());
                    warningMessageRecord.setType("安全隐患");
                    warningMessageRecord.setLevel("1");
                    warningMessageRecord.setStatus("1");
                    warningMessageRecord.setUpdatetime(new Date());
                    if("隐患上报".equals(hiddenDangerManageRecord.getHiddendangertype()))
                    {
                        warningMessageRecordMapper.insertSelective(warningMessageRecord);
                    }
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
            String filter = " where status != '-1' and  reportTime >='" + startTime + "' and reportTime <'" + endTime + "' and hiddenDangerType ='隐患上报' ";

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
