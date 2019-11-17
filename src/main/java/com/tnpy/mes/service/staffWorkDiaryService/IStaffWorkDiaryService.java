package com.tnpy.mes.service.staffWorkDiaryService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-11-17 11:47
 */
public interface IStaffWorkDiaryService {
    public TNPYResponse getStaffAttendanceInfo(String plantID, String processID, String lineID, String staffID, String startTime, String endTime);
}
