package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.workShift.IWorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/20 9:04
 */
@RestController
@RequestMapping("/api/workshift")
public class WorkShiftController {
    @Autowired
    private IWorkShiftService workShiftService ;

    @RequestMapping(value = "/getshiftteam")
    public TNPYResponse getShiftTeam(String plantID )
    {
        return  workShiftService.getShiftTeam(plantID);
    }
    @RequestMapping(value = "/changeshiftteam")
    public TNPYResponse changeShiftTeam(@RequestBody String jsonStr ) {
        return  workShiftService.changeShiftTeam(jsonStr);
    }

    @RequestMapping(value = "/deleteshiftteam")
    public TNPYResponse deleteShiftTeam( String id ) {
        return  workShiftService.deleteShiftTeam(id);
    }


    @RequestMapping(value = "/getworkshiftdetail")
    public TNPYResponse getWorkShiftDetail(String plantID,String shiftTeamID )
    {
        return  workShiftService.getWorkShiftDetail(plantID,shiftTeamID);
    }

    @RequestMapping(value = "/changeworkshift")
    public TNPYResponse changeWorkShift(@RequestBody String jsonStr ) {
        return  workShiftService.changeWorkShift(jsonStr);
    }

    @RequestMapping(value = "/deleteworkshift")
    public TNPYResponse deleteWorkShift( String id ) {
        return  workShiftService.deleteWorkShift(id);
    }

    @RequestMapping(value = "/getworkshiftrecord")
    public TNPYResponse getWorkShiftRecord(String plantID,String processID,String workShift,String dayNight )
    {
        return  workShiftService.getWorkShiftRecord(plantID,processID,workShift,dayNight);
    }

    @RequestMapping(value = "/changeworkshiftrecord")
    public TNPYResponse changeWorkShiftRecord(@RequestBody String jsonStr ) {
        return  workShiftService.changeWorkShiftRecord(jsonStr);
    }

    @RequestMapping(value = "/deleteworkshiftrecord")
    public TNPYResponse deleteWorkShiftRecord( String id ) {
        return  workShiftService.deleteWorkShiftRecord(id);
    }
}
