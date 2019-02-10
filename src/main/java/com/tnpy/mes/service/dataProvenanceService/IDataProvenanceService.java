package com.tnpy.mes.service.dataProvenanceService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/10 9:33
 */
public interface IDataProvenanceService {
    public TNPYResponse getProvenanceByOrderID(String orderID );
}
