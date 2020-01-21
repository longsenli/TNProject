package com.tnpy.mes.service.equipmentParamRecordService;

import com.tnpy.common.utils.web.TNPYResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:26
 */
public interface IEquipmentParamRecordService {
    public TNPYResponse getEquipmentParam(String equipmentTypeID);
    public TNPYResponse saveEquipmentParamRecord( String json);
    public TNPYResponse getEquipmentParamRecord(String equipID);
    public TNPYResponse pictureUpload(MultipartFile pictureName);
    public TNPYResponse getLatestParamRecord( String plantID,String equipType,String paramID);
    public TNPYResponse getOneEquipParamRecord( String startTime,String endTime,String equipID,String paramID);
    public TNPYResponse getEquipParamRecordByTime(String startTime,String endTime, String equipID);
    public TNPYResponse updateEquipmentParam(String params,String equipmentTypeID);
    public TNPYResponse getRecentAllParamPecord( String plantID,String equipType,String processID);

    public TNPYResponse getElectricProductionRelation(String plantID,String processID, String startTime,String endTime);
	public TNPYResponse getRecentAllParamPecordNew(String plantID, String equipType,
			String processID);
}
