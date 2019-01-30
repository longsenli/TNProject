package com.tnpy.mes.service.materialService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:31
 */
public interface IMaterialService {
    public TNPYResponse getMaterialRecord(String expendOrderID );
    public TNPYResponse getUsableMaterial(String plantID,String materialID );
    public TNPYResponse gainMaterialRecord(String materialIDListStr, String expendOrderID, String outputter );
    public TNPYResponse gainMaterialByQR(String qrCode,String expendOrderID,String outputter );
    public TNPYResponse gainPartMaterialRecord(String materialID,String number,String expendOrderID,String outputter );
}
