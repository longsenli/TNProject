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
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.UUID;

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

    @Autowired
    private MaterialTypeMapper materialTypeMapper;
    @Autowired
    private  ProcessMaterialMapper processMaterialMapper;

    @Autowired
    private MaterialRelationMapper materialRelationMapper;

    @Autowired
    private ParameterInfoMapper parameterInfoMapper;

    @Autowired
    private WorkLocationMapper workLocationMapper;

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
           // System.out.println(plantID + " 参数 " +processID);
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

    public TNPYResponse getOutMaterialByProcess(String processID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Material> materialList = materialMapper.selectOutByProcess(processID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(materialList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getInputMaterialByProcess(String processID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Material> materialList = materialMapper.selectInputByProcess(processID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(materialList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deteteProductionLine(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            productionLineMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
          //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeProductionLine( String jsonStr)
    {
        ProductionLine productionLine=(ProductionLine) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), ProductionLine.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(productionLine.getId()))
            {
                productionLine.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                productionLine.setStatus(1);
                productionLineMapper.insertSelective(productionLine);
            }
            else
            {
                productionLineMapper.updateByPrimaryKey(productionLine);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }
    public TNPYResponse getMaterialType( )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<MaterialType> materialTypeList = materialTypeMapper.selectAll();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(materialTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getMaterialByType(String typeID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Material> materialList = materialMapper.selectByType(typeID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(materialList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse deteteMaterial(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            materialMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeMaterial( String jsonStr)
    {
        Material material=(Material) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), Material.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(material.getId()))
            {
                material.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                material.setStatus(1);
                materialMapper.insertSelective(material);
            }
            else
            {
                materialMapper.updateByPrimaryKey(material);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }
    public TNPYResponse getProcessMaterialType(String processID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = "";
            if(!("-1".equals(processID)))
            {
                filter += " where processID = '" + processID + "' ";
            }
            filter +=  " order by processID ";
            List<ProcessMaterial> processMaterialList = processMaterialMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(processMaterialList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse deteteProcessMaterialType(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            processMaterialMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeProcessMaterialType( String jsonStr)
    {
        ProcessMaterial processMaterial=(ProcessMaterial) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), ProcessMaterial.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(processMaterial.getId()))
            {
                processMaterial.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                processMaterial.setStatus(1);
                processMaterialMapper.insertSelective(processMaterial);
            }
            else
            {
                processMaterialMapper.updateByPrimaryKey(processMaterial);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }


    public TNPYResponse getMaterialRelationByMaterial(String materialID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = "";
            if(!("-1".equals(materialID)))
            {
                filter += " where (inMaterialID = '" + materialID + "' ) or (outMaterialID = '" + materialID + "')";
            }
            List<MaterialRelation> materialRelationList = materialRelationMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(materialRelationList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse deteteMaterialRelation(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            materialRelationMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeMaterialRelation( String jsonStr)
    {
        MaterialRelation materialRelation=(MaterialRelation) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), MaterialRelation.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(materialRelation.getId()))
            {
                materialRelation.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                materialRelation.setStatus(1);
                materialRelationMapper.insertSelective(materialRelation);
            }
            else
            {
                materialRelationMapper.updateByPrimaryKey(materialRelation);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }

    public TNPYResponse getParameterinfo()
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<ParameterInfo> parameterInfoList = parameterInfoMapper.selectAll();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getInputMaterialByMaterial(String materialID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = "";
            if(!("-1".equals(materialID)))
            {
                filter += " where outMaterialID = '" + materialID + "'";
            }
            List<Material> materialList = materialMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(materialList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getWorkLocation(String plantID,String processID,String lineID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where status != '-1' ";
            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                filter += " and processID = '" + processID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            filter += " order by lineID,ordinal ";
            // System.out.println(plantID + " 参数 " +processID);
            List<WorkLocation> workLocationList = workLocationMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(workLocationList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deteteWorkLocation(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            workLocationMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeWorkLocation( String jsonStr)
    {
        WorkLocation workLocation=(WorkLocation) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), WorkLocation.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(workLocation.getId()))
            {
                workLocation.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                workLocation.setStatus("1");
                workLocationMapper.insertSelective(workLocation);
            }
            else
            {
                workLocationMapper.updateByPrimaryKey(workLocation);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }
}
