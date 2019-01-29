package com.tnpy.mes.service.scrapInfoService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/29 9:10
 */
public interface IScrapInfoService {
    public TNPYResponse getScrapInfo(String plantID , String processID,String lineID, String startTime, String endTime );
    public TNPYResponse saveScrapInfo(String strJson );
    public TNPYResponse getMaterialScrapInfo( String materialID, String orderID );
}
