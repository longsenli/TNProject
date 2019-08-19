package com.tnpy.mes.service.PlasticService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-08-19 10:44
 */
public interface IPlasticService {

    public TNPYResponse getPlasticUsedRecord(String plantID, String lineID, String locationID, String startTime, String endTime );
    public TNPYResponse addPlasticUsedRecord(String listID,String userID,String userName,String plantID,String lineID,String locationID);
}
