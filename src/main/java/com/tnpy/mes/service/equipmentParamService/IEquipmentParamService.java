package com.tnpy.mes.service.equipmentParamService;

import com.tnpy.common.utils.web.TNPYResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:26
 */
public interface IEquipmentParamService {
    public TNPYResponse getEquipmentParam(String equipmentTypeID);
    public TNPYResponse saveEquipmentParam( String json);
    public TNPYResponse getEquipmentParamRecord(String equipID);
    public TNPYResponse pictureUpload(MultipartFile pictureName);
}
