package com.tnpy.mes.service.epidemicManageService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.EpidemicControlStaffInfoMapper;
import com.tnpy.mes.mapper.mysql.EpidemicControlTMPTRecordMapper;
import com.tnpy.mes.mapper.mysql.EpidemicStaffBehaviorRecordMapper;
import com.tnpy.mes.mapper.mysql.NewStaffBasicInfoStatisticsMapper;
import com.tnpy.mes.model.mysql.EpidemicControlStaffInfo;
import com.tnpy.mes.model.mysql.EpidemicControlTMPTRecord;
import com.tnpy.mes.model.mysql.EpidemicStaffBehaviorRecord;
import com.tnpy.mes.model.mysql.NewStaffBasicInfoStatistics;
import com.tnpy.mes.service.epidemicManageService.IEpidemicManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2020-02-06 8:28
 */
@Service("epidemicManageService")
public class EpidemicManageServiceImpl implements IEpidemicManageService {
    @Autowired
    private EpidemicStaffBehaviorRecordMapper epidemicStaffBehaviorRecordMapper;

    @Autowired
    private NewStaffBasicInfoStatisticsMapper newStaffBasicInfoStatisticsMapper;

    @Autowired
    private EpidemicControlTMPTRecordMapper epidemicControlTMPTRecordMapper;

    @Autowired
    private EpidemicControlStaffInfoMapper epidemicControlStaffInfoMapper;

    public TNPYResponse addShelfBehaviorRecord(String jsonStr) {
        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Map stringToMap = JSONObject.parseObject(jsonStr);
            if (epidemicStaffBehaviorRecordMapper.selectRecordCount(String.valueOf(stringToMap.get("identityID")), String.valueOf(stringToMap.get("daytime"))) > 0) {
                result.setMessage("该身份证号，当日已经登记！" + stringToMap.get("identityID") + "   " + stringToMap.get("daytime"));
                return result;
            }

            EpidemicStaffBehaviorRecord epidemicStaffBehaviorRecord = new EpidemicStaffBehaviorRecord();
            epidemicStaffBehaviorRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            epidemicStaffBehaviorRecord.setName(String.valueOf(stringToMap.get("staffName")));
            epidemicStaffBehaviorRecord.setIdentityid(String.valueOf(stringToMap.get("identityID")));
            epidemicStaffBehaviorRecord.setDepartment(String.valueOf(stringToMap.get("department")));
            epidemicStaffBehaviorRecord.setProcessorjob(String.valueOf(stringToMap.get("process")));
            epidemicStaffBehaviorRecord.setDaytime(dateFormat.parse(String.valueOf(stringToMap.get("daytime"))));
            epidemicStaffBehaviorRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
            epidemicStaffBehaviorRecord.setStaylocation(String.valueOf(stringToMap.get("stayLocation")));
            epidemicStaffBehaviorRecord.setContactseverity(String.valueOf(stringToMap.get("contactSeverity")));
            epidemicStaffBehaviorRecord.setAbnormalshelf(String.valueOf(stringToMap.get("abnormalShelf")));
            epidemicStaffBehaviorRecord.setAbnormalpartner(String.valueOf(stringToMap.get("abnormalPartner")));
            epidemicStaffBehaviorRecord.setQuarantine(String.valueOf(stringToMap.get("quarantine")));
            epidemicStaffBehaviorRecord.setRemark(String.valueOf(stringToMap.get("remark")));
            epidemicStaffBehaviorRecord.setUpdatetime(new Date());
            epidemicStaffBehaviorRecord.setExtd1(String.valueOf(stringToMap.get("telephone")));
            epidemicStaffBehaviorRecordMapper.insert(epidemicStaffBehaviorRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            return result;
        } catch (Exception ex) {
            result.setMessage("添加失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getShelfFilloutEpidemicRecord(String identityID) {
        TNPYResponse result = new TNPYResponse();
        try {


            List<Map<Object, Object>> shelfFilloutEpidemicRecordList = epidemicStaffBehaviorRecordMapper.getShelfFilloutEpidemicRecord(identityID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(shelfFilloutEpidemicRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse addNewStaffBasicInfo(String jsonStr) {

        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Map stringToMap = JSONObject.parseObject(jsonStr);

            NewStaffBasicInfoStatistics newStaffBasicInfoStatistics = new NewStaffBasicInfoStatistics();
            newStaffBasicInfoStatistics.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            newStaffBasicInfoStatistics.setName(String.valueOf(stringToMap.get("staffName")));
            newStaffBasicInfoStatistics.setSex(String.valueOf(stringToMap.get("sex")));
            newStaffBasicInfoStatistics.setAge(String.valueOf(stringToMap.get("age")));
            newStaffBasicInfoStatistics.setTelephonenumber(String.valueOf(stringToMap.get("telephone")));
            // newStaffBasicInfoStatistics.setIdentityno(String.valueOf(stringToMap.get("identityID")));

            newStaffBasicInfoStatistics.setFamilylocation(String.valueOf(stringToMap.get("familyLocation")));
            newStaffBasicInfoStatistics.setEducationlevel(String.valueOf(stringToMap.get("educationLevel")));
            newStaffBasicInfoStatistics.setEmploymentobjective(String.valueOf(stringToMap.get("employmentObjective")));
            newStaffBasicInfoStatistics.setRemark(String.valueOf(stringToMap.get("remark")));
            newStaffBasicInfoStatistics.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
            newStaffBasicInfoStatistics.setUpdatetime(new Date());

            newStaffBasicInfoStatisticsMapper.insertSelective(newStaffBasicInfoStatistics);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            return result;
        } catch (Exception ex) {
            result.setMessage("添加失败！" + ex.getMessage());
            return result;
        }

    }

    public TNPYResponse getShelfBasicInfoRecord(String name) {

        TNPYResponse result = new TNPYResponse();
        try {

            if ("全部".equals(name.trim())) {
                List<Map<Object, Object>> newStaffBasicInfo = newStaffBasicInfoStatisticsMapper.getAllNewStaffBasicInfo();
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSON(newStaffBasicInfo).toString());
                return result;
            }
            List<Map<Object, Object>> newStaffBasicInfo = newStaffBasicInfoStatisticsMapper.getNewStaffBasicInfo(name);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(newStaffBasicInfo).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse addStaffTMPTRecord(String jsonStr) {
        TNPYResponse result = new TNPYResponse();
        try {

            EpidemicControlTMPTRecord epidemicControlTMPTRecord =(EpidemicControlTMPTRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), EpidemicControlTMPTRecord.class);

            if(StringUtils.isEmpty(epidemicControlTMPTRecord.getExtd2()) )
            {
                result.setMessage("该账号不具有温度登记权限，请确认后使用！");
                return result;
            }
            else if( !epidemicControlTMPTRecord.getExtd2().startsWith("7"))
            {
                result.setMessage("该账号不具有温度登记权限，请确认后使用！");
                return result;
            }
            epidemicControlTMPTRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            epidemicControlTMPTRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
            epidemicControlTMPTRecord.setUpdatetime(new Date());

            epidemicControlTMPTRecordMapper.insertSelective(epidemicControlTMPTRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            return result;
        } catch (Exception ex) {
            result.setMessage("添加失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getStaffEpidemicBasicDepartmentInfo( )
    {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> newStaffBasicInfo = epidemicControlTMPTRecordMapper.getStaffEpidemicBasicDepartmentInfo();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(newStaffBasicInfo).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse getStaffTMPTRecord(String name, String department, String startTime, String endTime, String tmptType) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where status = '1' ";
            if(!"-1".equals(name))
            {
                filter += " and name = '" + name + "' ";
            }
            if(!"-1".equals(department))
            {
                filter += " and department = '" + department + "' ";
            }
            if(!"-1".equals(startTime))
            {
                filter += " and updateTime > '" + startTime + "'    and updateTime < '" + endTime + "' ";
            }
            if(!"-1".equals(tmptType))
            {
                filter += " and temperature = '" + tmptType + "' ";
            }
            List<Map<Object, Object>> newStaffBasicInfo = epidemicControlTMPTRecordMapper.getStaffEpidemicTMPTRecord(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(newStaffBasicInfo).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getStaffEpidemicBasicInfo( String identityNo)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> newStaffBasicInfo = epidemicControlTMPTRecordMapper.getStaffEpidemicBasicInfo(identityNo);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(newStaffBasicInfo).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse addStaffEpidemicBasicInfo( String jsonStr)
    {
        TNPYResponse result = new TNPYResponse();
        try {

            EpidemicControlStaffInfo epidemicControlStaffInfo =(EpidemicControlStaffInfo) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), EpidemicControlStaffInfo.class);

            epidemicControlStaffInfo.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
            epidemicControlStaffInfo.setUpdatetime(new Date());

            epidemicControlStaffInfoMapper.insertSelective(epidemicControlStaffInfo);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("添加失败！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse deleteStaffEpidemicBasicInfo( String identityNo)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            epidemicControlStaffInfoMapper.deleteByPrimaryKey(identityNo);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("添加失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getStaffEpidemicBasicInfoByDepartment( String department)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> newStaffBasicInfo = epidemicControlTMPTRecordMapper.getStaffEpidemicBasicInfoByDepartment(department);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(newStaffBasicInfo).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getStaffLatestEpidemicTMPTRecord( String name)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where status = '1' ";
            if(!"-1".equals(name))
            {
                filter += " and name = '" + name + "' ";
            }

            List<Map<Object, Object>> newStaffBasicInfo = epidemicControlTMPTRecordMapper.getStaffLatestEpidemicTMPTRecord(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(newStaffBasicInfo).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
