package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.mysql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/27 15:46
 */
@RestController
@RequestMapping("/api/basicdata")
public class BasicDataController {
    @Autowired
    private IndustrialPlantMapper industrialPlantMapper;

    @Autowired
    private ProductionProcessMapper productionProcessMapper;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private EquipmentTypeMapper equipmentTypeMapper;

    @RequestMapping(value = "/getindustrialplant")
    public TNPYResponse getIndustrialPlant() {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<IndustrialPlant> industrialPlantList = industrialPlantMapper.selectAll();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(industrialPlantList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    @RequestMapping(value = "/getproductionprocess")
    public TNPYResponse getProductionProcess() {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<ProductionProcess> productionProcessList = productionProcessMapper.selectAll();

            result.setData(JSONObject.toJSON(productionProcessList).toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
           return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    @RequestMapping(value = "/getproductionline")
    public TNPYResponse getProductionLine(String plantID,String processID) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            System.out.println(plantID + " 参数 " +processID);
            List<ProductionLine> productionLineList = productionLineMapper.selectByPlantProcess(plantID,processID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(productionLineList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }


    @RequestMapping(value = "/getmaterial")
    public TNPYResponse getMaterial() {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Material> materialList = materialMapper.selectAll();
            result.setData(JSONObject.toJSON(materialList).toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    @RequestMapping(value = "/getequipmenttype")
    public TNPYResponse getEquipmentType() {

        TNPYResponse result = new TNPYResponse();
        try
        {
            List<EquipmentType> equipmentTypeList = equipmentTypeMapper.selectAllType();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }

    }

}
