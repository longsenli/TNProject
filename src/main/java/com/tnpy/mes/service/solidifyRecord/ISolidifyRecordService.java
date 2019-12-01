package com.tnpy.mes.service.solidifyRecord;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/15 13:18
 */
public interface ISolidifyRecordService {
    public TNPYResponse getSolidifyRecordByRoom(String plantID,String roomID);
    public TNPYResponse addSolidifyRecord(String id,String status,String recorder,String roomID);

    public TNPYResponse getSolidifyRecordByParam(String plantID,String roomID,String solidifyStepID,String startTime,String endTime);
    public TNPYResponse getInSolidifyRoomByParam(String plantID,String roomID);
    public TNPYResponse putinSolidifyRoom(String roomID,String orderIDList,String operatorName,String roomName);
    public TNPYResponse changeSolidifyStatus(String roomID,String orderIDList,String operatorName,String status);

    public TNPYResponse getInSolidifyRoomByParamNew(String plantID,String roomID,String status);
    public TNPYResponse changeAllSolidifyStatusAuto(String roomID,String operatorName);

    public TNPYResponse uninputSolidifyRoom(String plantID,String startTime,String endTime);
    public TNPYResponse getSolidifyRoomDetail(String plantID);

    public TNPYResponse getSolidifyWorkDetail(String plantID,String roomID,String startTime,String endTime);
}
