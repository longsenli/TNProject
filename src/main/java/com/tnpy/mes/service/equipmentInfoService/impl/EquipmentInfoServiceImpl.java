package com.tnpy.mes.service.equipmentInfoService.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.EquipmentInfoMapper;
import com.tnpy.mes.model.mysql.EquipmentInfo;
import com.tnpy.mes.service.equipmentInfoService.IEquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:22
 */
@Service("equipmentInfoService")
public class EquipmentInfoServiceImpl implements IEquipmentInfoService {

    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;


    public TNPYResponse getEquipmentInfo(String typeID,String plantID) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectByType(typeID,plantID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(equipmentInfoList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }


    public TNPYResponse deleteEquipmentInfo( String equipID) {
        TNPYResponse result = new TNPYResponse();
        System.out.println("id delete  "  + equipID);
        try
        {
            equipmentInfoMapper.deleteByPrimaryKey(equipID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeEquipmentInfo( String jsonStr) {

        EquipmentInfo equipmentInfo=(EquipmentInfo) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), EquipmentInfo.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(equipmentInfo.getId()))
            {
                equipmentInfo.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                equipmentInfoMapper.insertSelective(equipmentInfo);
            }
            else
            {
                equipmentInfoMapper.updateByPrimaryKey(equipmentInfo);
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
