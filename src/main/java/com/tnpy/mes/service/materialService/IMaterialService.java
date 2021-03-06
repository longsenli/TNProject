package com.tnpy.mes.service.materialService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:31
 */
public interface IMaterialService {
    public TNPYResponse getMaterialRecord(String expendOrderID );
    public TNPYResponse getUsableMaterial(String plantID,String materialID ,String expendOrderID);
    public TNPYResponse gainMaterialRecord(String materialRecordIDListStr,String materialOrderID, String expendOrderID, String outputter );
    public TNPYResponse gainMaterialByQR(String qrCode,String expendOrderID,String outputter );
    public TNPYResponse gainPartMaterialRecord(String materialRecordID,String materialOrderID,String number,String expendOrderID,String outputter );
    public TNPYResponse orderOutputStatistics( String startTime,String endTime,String plantID,String processID,String lineID );
    public TNPYResponse orderRemnantProductStatistics( String startTime,String endTime,String plantID,String processID,String lineID );
    public TNPYResponse batteryStatisInventory( String startTime,String endTime,String plantID );

    public TNPYResponse addGrantMaterialRecord( String orderSplitID,String operator ,int orderType ,String processID);
    public TNPYResponse notGrantMaterialDetail( String plantID,String processID,String startTime,String endTime  );
    public TNPYResponse grantAndExpendStatistics(  String startTime,String endTime,String plantID,String processID );
    public TNPYResponse addGrantMaterialRecordByBatch( String orderIDList,String operator ,String grantType,String processID, String acceptPlantID);

    public TNPYResponse getMaterialInventoryStatistics( String plantID,String processID,String startTime,String endTime );
    public TNPYResponse getSecondaryMaterialInventoryStatistics( String plantID,String processID,String startTime,String endTime );
    public TNPYResponse changeMaterialInventoryData( String id,String currentNum,String remark,String type );

    public TNPYResponse getGrantMaterialRecord( String plantID,String processID,String startTime,String endTime );

    public  TNPYResponse getMaterialRecordBySubOrderID(String qrCode,String expendOrderID );

    public TNPYResponse getMaterialRecordDetailBySubOrderID( String subOrderID);


    public TNPYResponse deteteUnqualifiedMaterialReturn(String id);
    public TNPYResponse changeUnqualifiedMaterialReturn( String jsonStr);
    public TNPYResponse getUnqualifiedMaterialReturn(String plantID,String processID,String lineID,String startTime,String endTime );
    public TNPYResponse unqualifiedMaterialRepair( String id,String repairNumber);

    public TNPYResponse getShelfProductionRecord( String staffID,String startTime,String endTime );
}
