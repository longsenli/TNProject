package com.tnpy.mes.service.basicDataService;

import com.tnpy.common.utils.web.TNPYResponse;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 14:43
 */
public interface IBasicDataService {
    public TNPYResponse getIndustrialPlant();
    public TNPYResponse getProductionProcess();
    public TNPYResponse getProductionLine(String plantID,String processID);
    public TNPYResponse getMaterial();
    public TNPYResponse getEquipmentType();
    public TNPYResponse getOutMaterialByProcess(String processID);
}
