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

    public TNPYResponse getUsedMaterialInfo(String plantID , String processID,String lineID, String productDate, String classType );
    public TNPYResponse getMaterialScrapRecord(String plantID , String processID,String lineID, String scrapSelectType, String startTime, String endTime );
    public TNPYResponse saveMaterialScrapRecord( String strJson );
    public TNPYResponse deleteMaterialScrapRecord( String id );

    public TNPYResponse scrapByBatteryQrcode( String id ,String scrapPlant,String scrapProcess,String repairReason,String updateStaffID,String updateStaff) ;

    public TNPYResponse getMaterialCirculationRecord( String originalPlantID ,String destinationPlantID,String processID,String circulationType,String startTime,String endTime);
    public TNPYResponse saveMaterialCirculationRecord( String strJson );
    public TNPYResponse deleteMaterialCirculationRecord( String id );
}
