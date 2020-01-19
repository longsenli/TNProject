package com.tnpy.mes.service.dashboardService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-04-22 10:27
 */
public interface IDashboardService {
    public TNPYResponse getDailyProduction(String plantID ,String processID,String queryTypeID,String startTime,String endTime);
    public TNPYResponse nowInDryingKilnjz(String plantID ,String processID,String queryTypeID,String startTime,String endTime);
    public TNPYResponse getWageDetail(String plantID ,String processID,String startTime,String endTime);

    public TNPYResponse getInventoryInfo(String plantID ,String processID,String dayTime);
    public TNPYResponse getProductionAndGrantInfo(String plantID ,String processID,String dayTime);
    public TNPYResponse getCXCDetailInfo(String plantID ,String processID,String startTime,String endTime);

    public TNPYResponse getDryingKilnInfo( String plantID ,String equipmentID,String queryTypeID,String startTime,String endTime);


    public TNPYResponse getProductionSummaryWorkLocation(String plantID ,String processID,String lineID,String startTime,String endTime);
    public TNPYResponse getProductionSummaryLine(String plantID ,String processID,String lineID,String startTime,String endTime);
    public TNPYResponse getProductionSummaryProcess(String plantID ,String processID,String startTime,String endTime);

    public TNPYResponse getDailyProductionSummaryPlant(String plantID ,String startTime,String endTime);
    
    public TNPYResponse getproductAccountSummaryPlant(String plantID ,String processID,String startTime,String endTime);
	public TNPYResponse getstaffproductAccountSummaryPlant(String plantID, String processID, String startTime, String endTime);
	public TNPYResponse getprocessproductAccountSummaryPlant(String plantID, String processID, String startTime,
			String endTime);
	public TNPYResponse getmonthproductAccountSummaryPlant(String plantID, String processID, String startTime,
			String endTime);
    public TNPYResponse getStaffProductAttendanceSummary(String plantID, String processID, String startTime, String endTime);
}
