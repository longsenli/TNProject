package com.tnpy.mes.service.equipmentInfoService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:22
 */
public interface IEquipmentInfoService {
    public TNPYResponse getEquipmentInfo(String typeID, String plantID);
    public TNPYResponse deleteEquipmentInfo( String equipID);
    public TNPYResponse changeEquipmentInfo( String jsonStr);
}
