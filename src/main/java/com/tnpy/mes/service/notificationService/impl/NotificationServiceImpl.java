package com.tnpy.mes.service.notificationService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.NotificationGroupMapper;
import com.tnpy.mes.model.mysql.NotificationGroup;
import com.tnpy.mes.service.notificationService.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-06-08 10:26
 */
@Service("notificationService")
public class NotificationServiceImpl implements INotificationService {
    @Autowired
    private NotificationGroupMapper notificationGroupMapper;

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
}
