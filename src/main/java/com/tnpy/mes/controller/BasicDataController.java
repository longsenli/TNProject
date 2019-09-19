package com.tnpy.mes.controller;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.basicDataService.IBasicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/27 15:46
 */
@RestController
@RequestMapping(value ="/api/basicdata")
public class BasicDataController {
    @Autowired
    private IBasicDataService basicDataService;

    @RequestMapping(value = "/getindustrialplant")
    public TNPYResponse getIndustrialPlant() {
       return  basicDataService.getIndustrialPlant();
    }

    @RequestMapping(value = "/getindustrialplantbyfilter")
    public TNPYResponse getIndustrialPlantByFilter(String type) {
        return  basicDataService.getIndustrialPlantByFilter(type);
    }


    @RequestMapping(value = "/getproductionprocess")
    public TNPYResponse getProductionProcess() {
        return  basicDataService.getProductionProcess();
    }

    @RequestMapping(value = "/getproductionline")
    public TNPYResponse getProductionLine(String plantID,String processID) {
        return  basicDataService.getProductionLine(plantID,processID);
    }

    @RequestMapping(value = "/deleteproductionline")
    public TNPYResponse deteteProductionLine(String id) {
        return  basicDataService.deteteProductionLine(id);
    }

    @RequestMapping(value = "/changeproductionline")
    public TNPYResponse changeProductionLine(@RequestBody String jsonStr) {
        return  basicDataService.changeProductionLine(jsonStr);
    }

    @RequestMapping(value = "/getworklocation")
    public TNPYResponse getWorkLocation(String plantID,String processID,String lineID) {
        return  basicDataService.getWorkLocation(plantID,processID,lineID);
    }

    @RequestMapping(value = "/deleteworklocation")
    public TNPYResponse deteteWorkLocation(String id) {
        return  basicDataService.deteteWorkLocation(id);
    }

    @RequestMapping(value = "/changeworklocation")
    public TNPYResponse changeWorkLocation(@RequestBody String jsonStr) {
        return  basicDataService.changeWorkLocation(jsonStr);
    }

    @RequestMapping(value = "/getmaterialtype")
    public TNPYResponse getMaterialType( ) {
        return  basicDataService.getMaterialType();
    }

    @RequestMapping(value = "/getmaterial")
    public TNPYResponse getMaterial() {
        return  basicDataService.getMaterial();
    }

    @RequestMapping(value = "/getmaterialbytype")
    public TNPYResponse getMaterialByType(String typeID) {
        return  basicDataService.getMaterialByType(typeID);
    }


    @RequestMapping(value = "/getequipmenttype")
    public TNPYResponse getEquipmentType() {

        return  basicDataService.getEquipmentType();
    }

    @RequestMapping(value = "/getparameterinfo")
    public TNPYResponse getParameterinfo() {

        return  basicDataService.getParameterinfo();
    }

    @RequestMapping(value = "/getmaterialbyprocess")
    public TNPYResponse getMaterialByProcess(String processID,@RequestParam(defaultValue = "-1")String plantID) {

        return  basicDataService.getOutMaterialByProcess(processID,plantID);
    }

    @RequestMapping(value = "/getinputmaterialbyprocess")
    public TNPYResponse getInputMaterialByProcess(String processID) {

        return  basicDataService.getInputMaterialByProcess(processID);
    }
    @RequestMapping(value = "/deletematerial")
    public TNPYResponse deteteMaterial(String id) {
        return  basicDataService.deteteMaterial(id);
    }

    @RequestMapping(value = "/changematerial")
    public TNPYResponse changeMaterial(@RequestBody String jsonStr) {
        return  basicDataService.changeMaterial(jsonStr);
    }

    @RequestMapping(value = "/getprocessmaterialtype")
    public TNPYResponse getProcessMaterialType(String processID) {
        return  basicDataService.getProcessMaterialType(processID);
    }

    @RequestMapping(value = "/deleteprocessmaterialtype")
    public TNPYResponse deteteProcessMaterialType(String id) {
        return  basicDataService.deteteProcessMaterialType(id);
    }

    @RequestMapping(value = "/changeprocessmaterialtype")
    public TNPYResponse changeProcessMaterialType(@RequestBody String jsonStr) {
        return  basicDataService.changeProcessMaterialType(jsonStr);
    }

    @RequestMapping(value = "/getmaterialrelationbymaterial")
    public TNPYResponse getMaterialRelationByMaterial(String materialID) {
        return  basicDataService.getMaterialRelationByMaterial(materialID);
    }

    @RequestMapping(value = "/deletematerialrelation")
    public TNPYResponse deteteMaterialRelation(String id) {
        return  basicDataService.deteteMaterialRelation(id);
    }

    @RequestMapping(value = "/changematerialrelation")
    public TNPYResponse changeMaterialRelation(@RequestBody String jsonStr) {
        return  basicDataService.changeMaterialRelation(jsonStr);
    }

    @RequestMapping(value = "/getinputmaterialbymaterial")
    public TNPYResponse getInputMaterialByMaterial(String materialID) {
        return  basicDataService.getInputMaterialByMaterial(materialID);
    }
}
