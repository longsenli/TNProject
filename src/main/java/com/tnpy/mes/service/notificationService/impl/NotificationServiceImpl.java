package com.tnpy.mes.service.notificationService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.mysql.*;
import com.tnpy.mes.service.notificationService.INotificationService;
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
 * @Date: 2019-06-08 10:26
 */
@Service("notificationService")
public class NotificationServiceImpl implements INotificationService {
    @Autowired
    private NotificationGroupMapper notificationGroupMapper;
    @Autowired
    private NotificationGroupDetailMapper notificationGroupDetailMapper;
    @Autowired
    private NotificationStaffDetailMapper notificationStaffDetailMapper;
    @Autowired
    private NotificationTypeDetailMapper notificationTypeDetailMapper;

    @Autowired
    private  WarningMessageRecordMapper warningMessageRecordMapper;

    @Autowired
    private TbUserMapper tbUserMapper;


    public TNPYResponse getBasicUserInfo()
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object, Object>> basicUserInfo = tbUserMapper.selectAllBasicUserInfo();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(basicUserInfo).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getNotificationGroup(){
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<NotificationGroup> notificationGroupList = notificationGroupMapper.selectAllGroup();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(notificationGroupList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeNotificationGroup( String jsonStr)
    {
        NotificationGroup notificationGroup=(NotificationGroup) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), NotificationGroup.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(notificationGroup.getId()))
            {
                notificationGroup.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                notificationGroup.setStatus("1");
                notificationGroupMapper.insertSelective(notificationGroup);
            }
            else
            {
                notificationGroupMapper.updateByPrimaryKey(notificationGroup);
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
    public TNPYResponse deteteNotificationGroup(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            notificationGroupMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getNotificationTypeDetail(){
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<NotificationTypeDetail> notificationTypeDetailList = notificationTypeDetailMapper.selectAll();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(notificationTypeDetailList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeNotificationTypeDetail( String jsonStr)
    {
        NotificationTypeDetail notificationTypeDetail=(NotificationTypeDetail) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), NotificationTypeDetail.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(notificationTypeDetail.getId()))
            {
                notificationTypeDetail.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                notificationTypeDetail.setStatus("1");
                notificationTypeDetailMapper.insertSelective(notificationTypeDetail);
            }
            else
            {
                notificationTypeDetailMapper.updateByPrimaryKey(notificationTypeDetail);
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
    public TNPYResponse deteteNotificationTypeDetail(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            notificationTypeDetailMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }


    public TNPYResponse getNotificationStaffDetail(String notificationGroupID){
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<NotificationStaffDetail> notificationStaffDetailList = notificationStaffDetailMapper.selectByNotificationGroupID(notificationGroupID );
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(notificationStaffDetailList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeNotificationStaffDetail( String jsonStr)
    {
        NotificationStaffDetail notificationStaffDetail=(NotificationStaffDetail) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), NotificationStaffDetail.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(notificationStaffDetail.getId()))
            {
                notificationStaffDetail.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                notificationStaffDetail.setStatus("1");
                notificationStaffDetailMapper.insertSelective(notificationStaffDetail);
            }
            else
            {
                notificationStaffDetailMapper.updateByPrimaryKey(notificationStaffDetail);
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

    public TNPYResponse changeNotificationStaffByParam(  String groupID,String staffIDList,String operationType,String operatorName,String operatorID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String[] staffInfoArray = staffIDList.split("###");

            if("2".equals(operationType))
            {
                notificationStaffDetailMapper.deleteByGroupID(groupID);
            }
            String groupName = notificationGroupMapper.selectGroupNameByID(groupID);
            List<String> hasStaffList =notificationStaffDetailMapper.selectNotificationTypeByNotificationGroupID(groupID);
            for(int i =0;i< staffInfoArray.length;i+= 2)
            {
                if(hasStaffList.contains(staffInfoArray[i]))
                {
                    continue;
                }
                NotificationStaffDetail notificationStaffDetail = new NotificationStaffDetail();
                notificationStaffDetail.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                notificationStaffDetail.setStatus(StatusEnum.StatusFlag.using.getIndex() +"");
                notificationStaffDetail.setNotificationgroupid(groupID);
                notificationStaffDetail.setNotificationgroupname(groupName);
                notificationStaffDetail.setStaffid(staffInfoArray[i]);
                notificationStaffDetail.setStaffname(staffInfoArray[i+1]);
                notificationStaffDetail.setOperatorid(operatorID);
                notificationStaffDetail.setOperatorname(operatorName);
                notificationStaffDetail.setUpdatetime(new Date());
                notificationStaffDetailMapper.insertSelective(notificationStaffDetail);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("操作失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deteteNotificationStaffDetail(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            notificationStaffDetailMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse deteteNotificationStaffDetailByParam( String groupID,String staffID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            notificationStaffDetailMapper.deleteByGroupIDStaffID(groupID,staffID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }


    public TNPYResponse getNotificationGroupDetail(String notificationGroupID){
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<NotificationTypeDetail> notificationGroupDetailList = notificationGroupDetailMapper.selectByNotificationGroupID(notificationGroupID );
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(notificationGroupDetailList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeNotificationGroupDetail( String jsonStr)
    {
        NotificationGroupDetail notificationGroupDetail=(NotificationGroupDetail) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), NotificationGroupDetail.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(notificationGroupDetail.getId()))
            {
                notificationGroupDetail.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                notificationGroupDetail.setStatus("1");
                notificationGroupDetailMapper.insertSelective(notificationGroupDetail);
            }
            else
            {
                notificationGroupDetailMapper.updateByPrimaryKey(notificationGroupDetail);
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
    public TNPYResponse deteteNotificationGroupDetail(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            notificationGroupDetailMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }

    // operationType 1 为添加 2为修改
    public TNPYResponse changeNotificationGroupDetailByParam(  String groupID,String typeParamIDList,String operationType,String operatorName,String operatorID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String[] typeParamArray = typeParamIDList.split("###");

            if("2".equals(operationType))
            {
                notificationGroupDetailMapper.deleteByGroupID(groupID);
            }

            List<String> hasParamIDList =notificationGroupDetailMapper.selectNotificationTypeByNotificationGroupID(groupID);
            for(int i =0;i< typeParamArray.length;i++)
            {
                if(hasParamIDList.contains(typeParamArray[i]))
                {
                    continue;
                }
                NotificationGroupDetail notificationGroupDetail = new NotificationGroupDetail();
                notificationGroupDetail.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                notificationGroupDetail.setStatus(StatusEnum.StatusFlag.using.getIndex() +"");
                notificationGroupDetail.setNotificationgroupid(groupID);
                notificationGroupDetail.setNotificationtype(typeParamArray[i].trim());
                notificationGroupDetail.setOperatorid(operatorID);
                notificationGroupDetail.setOperatorname(operatorName);
                notificationGroupDetail.setUpdatetime(new Date());
                notificationGroupDetailMapper.insertSelective(notificationGroupDetail);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("操作失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deleteNotificationGroupDetailByParam(  String groupID,String typeParamID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            notificationGroupDetailMapper.deleteByGroupIDTypeID(groupID,typeParamID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getWaringMessageRecord(String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where updateTime >= '" + startTime + "' and updateTime < '" + endTime + "' order by updateTime desc ";
            List<WarningMessageRecord> warningMessageRecordList = warningMessageRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(warningMessageRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
