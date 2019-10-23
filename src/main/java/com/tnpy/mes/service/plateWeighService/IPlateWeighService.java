package com.tnpy.mes.service.plateWeighService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-09-03 9:51
 */
public interface IPlateWeighService {
    public TNPYResponse getPlateWeighRecord(String plantID, String balanceID, String staffName, String materialName, String startTime, String endTime );
    public TNPYResponse getPlateWeighBasicData(String plantID);
    public TNPYResponse getRealtimeRecord(String plantID, String staffName, String materialName ,String balanceID);
    public TNPYResponse getQualifiedRateInfo(String plantID, String balanceID,String staffName, String materialName,String weighQualifyRange,  String startTime, String endTime);
}
