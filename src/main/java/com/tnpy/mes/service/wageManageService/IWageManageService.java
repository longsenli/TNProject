package com.tnpy.mes.service.wageManageService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-09-04 9:45
 */

public interface IWageManageService {
    public TNPYResponse getProductionWageDetail(String plantID, String processID, String staffName, String startTime, String endTime );

    public TNPYResponse getRewardingPunishmentDetail(String plantID, String processID, String staffName, String startTime, String endTime);
    public TNPYResponse changeRewardingPunishmentDetail( String jsonStr );
    public TNPYResponse deleteRewardingPunishmentDetail( String recordID );

    public TNPYResponse getPaystubDetail(String plantID, String processID, String staffName, String startTime, String endTime);
    public TNPYResponse changePaystubDetail( String jsonStr );
    public TNPYResponse deletePaystubDetail( String recordID );
}
