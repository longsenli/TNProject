package com.tnpy.mes.service.interphonePatrolService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-12-09 8:29
 */
public interface IInterphonePatrolService {
    public TNPYResponse getInterphonePatrolLocationInfo(String plantID, String processID);
}
