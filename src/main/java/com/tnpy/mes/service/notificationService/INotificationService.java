package com.tnpy.mes.service.notificationService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-06-08 10:25
 */
public interface INotificationService {
    public TNPYResponse getNotificationGroup();
    public TNPYResponse changeNotificationGroup( String jsonStr);
    public TNPYResponse deteteNotificationGroup(String id);

    public TNPYResponse getNotificationTypeDetail();
    public TNPYResponse changeNotificationTypeDetail( String jsonStr);
    public TNPYResponse deteteNotificationTypeDetail(String id);

    public TNPYResponse getNotificationStaffDetail(String notificationGroupID);
    public TNPYResponse changeNotificationStaffDetail( String jsonStr);
    public TNPYResponse deteteNotificationStaffDetail(String id);
    public TNPYResponse deteteNotificationStaffDetailByParam( String groupID,String staffID);
    public TNPYResponse changeNotificationStaffByParam(  String groupID,String staffIDList,String operationType,String operatorName,String operatorID);

    public TNPYResponse getNotificationGroupDetail(String notificationGroupID);
    public TNPYResponse changeNotificationGroupDetail( String jsonStr);
    public TNPYResponse deteteNotificationGroupDetail(String id);
    public TNPYResponse changeNotificationGroupDetailByParam(  String groupID,String typeParamIDList,String operationType,String operatorName,String operatorID);
    public TNPYResponse deleteNotificationGroupDetailByParam(  String groupID,String typeParamID);

    public TNPYResponse getBasicUserInfo();
}
