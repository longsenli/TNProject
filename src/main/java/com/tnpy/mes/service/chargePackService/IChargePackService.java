package com.tnpy.mes.service.chargePackService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-05-06 14:39
 */
public interface IChargePackService {
    public TNPYResponse getChargingRackRecord(String plantID, String processID) ;
    public TNPYResponse deteteChargingRackRecord(String id);
    public TNPYResponse changeChargingRackRecord( String jsonStr);
}
