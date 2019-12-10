package com.tnpy.mes.service.interphonePatrolService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-12-09 8:29
 */
public interface IInterphonePatrolService {
    public TNPYResponse getInterphonePatrolLocationInfo(String plantID, String processID);
    public TNPYResponse addInterphonePatrolRecord( String recordJson);
    public TNPYResponse deleteInterphonePatrolRecord(String id);

    public TNPYResponse getInterphonePatrolRecordDetail(String plantID,String startTime,String endTime);
    public TNPYResponse getInterphonePatrolRecordReport(String plantID,String startTime,String endTime);
}