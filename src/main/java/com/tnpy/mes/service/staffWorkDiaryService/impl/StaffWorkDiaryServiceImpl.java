package com.tnpy.mes.service.staffWorkDiaryService.impl;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.staffWorkDiaryService.IStaffWorkDiaryService;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-11-17 11:47
 */
@Service("staffWorkDiaryService")
public class StaffWorkDiaryServiceImpl implements IStaffWorkDiaryService {
    public TNPYResponse getStaffAttendanceInfo(String plantID, String processID, String lineID, String staffID, String startTime, String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
          
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
