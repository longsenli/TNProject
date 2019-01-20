package com.tnpy.mes.service.workShift;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/20 9:03
 */
public interface IWorkShiftService {
    public TNPYResponse getShiftTeam(String plantID );
    public TNPYResponse changeShiftTeam( String jsonStr );
    public TNPYResponse deleteShiftTeam( String id );
    public TNPYResponse getWorkShiftDetail(String plantID,String shiftTeamID );
    public TNPYResponse changeWorkShift( String jsonStr );
    public TNPYResponse deleteWorkShift( String id );
    public TNPYResponse getWorkShiftRecord(String plantID,String processID,String workShift,String dayNight );
    public TNPYResponse changeWorkShiftRecord( String jsonStr );
    public TNPYResponse deleteWorkShiftRecord( String id );
}
