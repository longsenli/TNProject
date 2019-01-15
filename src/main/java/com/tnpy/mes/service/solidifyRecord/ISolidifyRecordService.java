package com.tnpy.mes.service.solidifyRecord;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/15 13:18
 */
public interface ISolidifyRecordService {
    public TNPYResponse getSolidifyRecordByRoom(String plantID,String roomID);
    public TNPYResponse addSolidifyRecord(String id,String status,String recorder,String roomID);
}
