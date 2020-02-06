package com.tnpy.mes.service.epidemicManageService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2020-02-06 8:27
 */

public interface IEpidemicManageService {
    public TNPYResponse addShelfBehaviorRecord( String jsonStr);
    public TNPYResponse getShelfFilloutEpidemicRecord( String identityID);
}
