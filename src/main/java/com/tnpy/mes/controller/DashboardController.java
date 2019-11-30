package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.dashboardService.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/20 9:04
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private IDashboardService dashboardService ;

    @RequestMapping(value = "/getdailyproduction")
    public TNPYResponse getDailyProduction(String plantID ,String processID,String queryTypeID,String startTime,String endTime)
    {

        return  dashboardService.getDailyProduction(plantID,processID,queryTypeID,startTime,endTime);
    }

    //浇铸时效硬化窑当前在窑数据
    @RequestMapping(value = "/nowInDryingKilnjz")
    public TNPYResponse nowInDryingKilnjz( String plantID ,String processID,String queryTypeID,String startTime,String endTime) {
       return dashboardService.nowInDryingKilnjz(plantID, processID, queryTypeID, startTime, endTime);
    }

    // 1 在窑数据查询  2入窑记录查询 3 出窑记录查询
    @RequestMapping(value = "/getDryingKilnInfo")
    public TNPYResponse getDryingKilnInfo( String plantID ,String equipmentID,String queryTypeID,String startTime,String endTime) {
        return dashboardService.getDryingKilnInfo(plantID, equipmentID, queryTypeID, startTime, endTime);
    }

    @RequestMapping(value = "/getinventoryinfo")
    public TNPYResponse getInventoryInfo(String plantID ,String processID,String dayTime)
    {
        return dashboardService.getInventoryInfo(plantID, processID, dayTime);
    }

    @RequestMapping(value = "/getproductionandgrantinfo")
    public TNPYResponse getProductionAndGrantInfo(String plantID ,String processID,String dayTime)
    {
        return dashboardService.getProductionAndGrantInfo(plantID, processID, dayTime);
    }

    @RequestMapping(value = "/getCXCDetailInfo")
    public TNPYResponse getCXCDetailInfo(String plantID ,String processID,String startTime,String endTime)
    {
        return dashboardService.getCXCDetailInfo(plantID, processID, startTime,endTime);
    }

    @RequestMapping(value = "/getProductionSummaryWorkLocation")
    public TNPYResponse getProductionSummaryWorkLocation(String plantID ,String processID,String lineID,String startTime,String endTime)
    {
        return dashboardService.getProductionSummaryWorkLocation(plantID, processID, lineID,startTime,endTime);
    }

    @RequestMapping(value = "/getProductionSummaryLine")
    public TNPYResponse getProductionSummaryLine(String plantID ,String processID,String lineID,String startTime,String endTime)
    {
        return dashboardService.getProductionSummaryLine(plantID, processID, lineID,startTime,endTime);
    }

    @RequestMapping(value = "/getProductionSummaryProcess")
    public TNPYResponse getProductionSummaryProcess(String plantID ,String processID,String startTime,String endTime)
    {
        return dashboardService.getProductionSummaryProcess(plantID, processID,startTime,endTime);
    }
    @RequestMapping(value = "/getDailyProductionSummaryPlant")
    public TNPYResponse getDailyProductionSummaryPlant(String plantID ,String startTime,String endTime)
    {
        return dashboardService.getDailyProductionSummaryPlant(plantID,startTime,endTime);
    }
    
    @RequestMapping(value = "/getproductAccountSummaryPlant")
    public TNPYResponse getproductAccountSummaryPlant(String plantID ,String processID,String startTime,String endTime)
    {
        return dashboardService.getproductAccountSummaryPlant(plantID, processID,startTime,endTime);
    }
    
    @RequestMapping(value = "/getstaffproductAccountSummaryPlant")
    public TNPYResponse getstaffproductAccountSummaryPlant(String plantID ,String processID,String startTime,String endTime)
    {
        return dashboardService.getstaffproductAccountSummaryPlant(plantID, processID,startTime,endTime);
    }
    @RequestMapping(value = "/getprocessproductAccountSummaryPlant")
    public TNPYResponse getprocessproductAccountSummaryPlant(String plantID ,String processID,String startTime,String endTime)
    {
        return dashboardService.getprocessproductAccountSummaryPlant(plantID, processID,startTime,endTime);
    }
}
