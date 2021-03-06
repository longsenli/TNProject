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
    public TNPYResponse getAllProductionLine();
    public TNPYResponse getMaterial();
    public TNPYResponse getEquipmentType();
    public TNPYResponse getOutMaterialByProcess(String processID,String plantID);
    public TNPYResponse getInputMaterialByProcess(String processID);
    public TNPYResponse getIndustrialPlantByFilter(String type);

    public TNPYResponse deteteProductionLine(String id);
    public TNPYResponse changeProductionLine( String jsonStr);
    public TNPYResponse getMaterialType( );
    public TNPYResponse getMaterialByType(String typeID);

    public TNPYResponse deteteMaterial(String id);
    public TNPYResponse changeMaterial( String jsonStr);
    public TNPYResponse getProcessMaterialType(String processID);

    public TNPYResponse deteteProcessMaterialType(String id);
    public TNPYResponse changeProcessMaterialType( String jsonStr);

    public TNPYResponse getMaterialRelationByMaterial(String materialID);
    public TNPYResponse deteteMaterialRelation(String id);
    public TNPYResponse changeMaterialRelation( String jsonStr);

    public TNPYResponse getParameterinfo();

    public TNPYResponse getInputMaterialByMaterial(String materialID);

    public TNPYResponse getWorkLocation(String plantID,String processID,String lineID);
    public TNPYResponse deteteWorkLocation(String id);
    public TNPYResponse changeWorkLocation( String jsonStr);

    public TNPYResponse getWorkContentDetail(String plantID,String processID);
}
