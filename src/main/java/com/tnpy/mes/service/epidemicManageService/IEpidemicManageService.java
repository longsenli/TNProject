package com.tnpy.mes.service.epidemicManageService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2020-02-06 8:27
 */

public interface IEpidemicManageService {
    public TNPYResponse addShelfBehaviorRecord(String jsonStr);

    public TNPYResponse getShelfFilloutEpidemicRecord(String identityID);


    public TNPYResponse addNewStaffBasicInfo(String jsonStr);

    public TNPYResponse getShelfBasicInfoRecord(String name);

    public TNPYResponse getStaffEpidemicBasicInfo( String identityNo);
    public TNPYResponse addStaffEpidemicBasicInfo( String jsonStr);
    public TNPYResponse deleteStaffEpidemicBasicInfo( String identityNo);

    public TNPYResponse getStaffEpidemicBasicInfoByDepartment( String department);
    public TNPYResponse getStaffEpidemicBasicDepartmentInfo( );
    public TNPYResponse addStaffTMPTRecord(String jsonStr);
    public TNPYResponse getStaffTMPTRecord(String name, String department, String startTime, String endTime, String tmptType);

    public TNPYResponse getStaffLatestEpidemicTMPTRecord( String name);
}
