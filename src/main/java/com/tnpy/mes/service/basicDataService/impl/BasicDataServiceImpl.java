package com.tnpy.mes.service.basicDataService.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.mysql.*;
import com.tnpy.mes.service.basicDataService.IBasicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 14:47
 */
@Service("basicDataService")
public class BasicDataServiceImpl implements IBasicDataService {

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

    @Override
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
    @Override
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
    @Override
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

    @Override
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
    @Override
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
