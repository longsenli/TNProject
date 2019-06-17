package com.tnpy.mes.service.safetyAndEPService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-06-03 16:31
 */
public interface ISafetyAndPEService {
    public TNPYResponse getIchnographyDetailInfo(String mainRegionID);

    public TNPYResponse getHiddenDangerManageRecord(String plantID,String selectLevel,String startTime,String endTime);
    public TNPYResponse deteteHiddenDangerManageRecord(String id);
    public TNPYResponse changeHiddenDangerManageRecord( String jsonStr);
    public TNPYResponse getHiddenDangerManageCharts(String plantID,String selectLevel,String startTime,String endTime);
}
