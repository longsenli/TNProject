package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.notificationService.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-06-08 10:18
 */
@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private INotificationService notificationService ;

    @RequestMapping(value = "/getnotificationgroup")
    public TNPYResponse getNotificationGroup( ) {
        return notificationService.getNotificationGroup();
    }
    @RequestMapping(value = "/changenotificationgroup")
    public TNPYResponse changeNotificationGroup( @RequestBody String jsonStr)
    {
        return notificationService.changeNotificationGroup(jsonStr);
    }
    @RequestMapping(value = "/detetenotificationgroup")
    public TNPYResponse deteteNotificationGroup(String id)
    {
        return notificationService.deteteNotificationGroup(id);
    }
    @RequestMapping(value = "/getnotificationtypedetail")
    public TNPYResponse getNotificationTypeDetail()
    {
        return notificationService.getNotificationTypeDetail();
    }


    @RequestMapping(value = "/changenotificationtypedetail")
    public TNPYResponse changeNotificationTypeDetail( @RequestBody String jsonStr)
    {
        return notificationService.changeNotificationTypeDetail(jsonStr);
    }
    @RequestMapping(value = "/detetenotificationtypedetail")
    public TNPYResponse deteteNotificationTypeDetail(String id)
    {
        return notificationService.deteteNotificationTypeDetail(id);
    }
    @RequestMapping(value = "/getnotificationstaffdetail")
    public TNPYResponse getNotificationStaffDetail(String notificationGroupID)
    {
        return notificationService.getNotificationStaffDetail(notificationGroupID);
    }
    @RequestMapping(value = "/changenotificationstaffdetail")
    public TNPYResponse changeNotificationStaffDetail( @RequestBody String jsonStr)
    {
        return notificationService.changeNotificationStaffDetail(jsonStr);
    }

    @RequestMapping(value = "/changenotificationstaffbyparam")
    public TNPYResponse changeNotificationStaffByParam(  String groupID,String staffIDList,String operationType,String operatorName,String operatorID)
    {
        return notificationService.changeNotificationStaffByParam(groupID,staffIDList,operationType,operatorName,operatorID);
    }

    @RequestMapping(value = "/detetenotificationstaffdetail")
    public TNPYResponse deteteNotificationStaffDetail(String id)
    {
        return notificationService.deteteNotificationStaffDetail(id);
    }
    @RequestMapping(value = "/detetenotificationstaffdetailbyparam")
    public TNPYResponse deteteNotificationStaffDetailByParam( String groupID,String staffID)
    {
        return notificationService.deteteNotificationStaffDetailByParam(groupID,staffID);
    }

    @RequestMapping(value = "/getnotificationgroupdetail")
    public TNPYResponse getNotificationGroupDetail(String notificationGroupID)
    {
        return notificationService.getNotificationGroupDetail(notificationGroupID);
    }
    @RequestMapping(value = "/changenotificationgroupdetail")
    public TNPYResponse changeNotificationGroupDetail( @RequestBody String jsonStr)
    {
        return notificationService.changeNotificationGroupDetail(jsonStr);
    }
    @RequestMapping(value = "/detetenotificationgroupdetail")
    public TNPYResponse deteteNotificationGroupDetail(String id)
    {
        return notificationService.deteteNotificationGroupDetail(id);
    }

    @RequestMapping(value = "/changenotificationgroupdetailbyparam")
    public TNPYResponse changeNotificationGroupDetailByParam(  String groupID,String typeParamIDList,String operationType,String operatorName,String operatorID)
    {
        return notificationService.changeNotificationGroupDetailByParam(groupID,typeParamIDList,operationType,operatorName,operatorID);
    }
    @RequestMapping(value = "/deletenotificationgroupdetailbyparam")
    public TNPYResponse deleteNotificationGroupDetailByParam(  String groupID,String typeParamID)
    {
        return notificationService.deleteNotificationGroupDetailByParam(groupID,typeParamID);
    }

    @RequestMapping(value = "/getallbasicuserinfo")
    public TNPYResponse getAllBasicUserInfo()
    {
        return notificationService.getBasicUserInfo();
    }
}
