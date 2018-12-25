package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.EquipmentParaRecordMapper;
import com.tnpy.mes.mapper.mysql.ParameterInfoMapper;
import com.tnpy.mes.model.mysql.EquipmentParaRecord;
import com.tnpy.mes.model.mysql.ParameterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2018/12/24 8:57
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentParamController {
    @Autowired
    private ParameterInfoMapper parameterInfoMapper;
@Autowired
private EquipmentParaRecordMapper equipmentParaRecordMapper;
    @RequestMapping(value = "/getequipmentparam")
    public TNPYResponse getEquipmentParam(String equipmentTypeID) {

        TNPYResponse result = new TNPYResponse();
        try {
            List<ParameterInfo> parameterInfoList = parameterInfoMapper.selectByEquipType(equipmentTypeID);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    @RequestMapping(value = "/saveequipmentparam")
    public TNPYResponse saveEquipmentParam(@RequestBody String json) {
        System.out.println(json);

        TNPYResponse result = new TNPYResponse();
        try {
            List<EquipmentParaRecord> equipmentParaRecordList = JSON.parseArray(json, EquipmentParaRecord.class);
            Date nowTime = new Date();
            String idStr =  UUID.randomUUID().toString();
            for(int i =0;i<equipmentParaRecordList.size();i++)
            {
                equipmentParaRecordList.get(i).setId(idStr);
                equipmentParaRecordList.get(i).setRecordtime(nowTime);
                equipmentParaRecordMapper.insertSelective(equipmentParaRecordList.get(i));
            }
            result.setStatus(1);
            result.setData("");
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    @PostMapping("/pictureupload")
    public TNPYResponse pictureUpload(MultipartFile pictureName){

        TNPYResponse response = new TNPYResponse();
        System.out.println("进入成功！");
        try {
            //获取文件名
            String fileName = pictureName.getOriginalFilename();
            //获取文件后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //重新生成文件名
            fileName = UUID.randomUUID()+suffixName;
            //指定本地文件夹存储图片
            String filePath = "D:/upload/";
            System.out.println("进入成功！" + fileName);
            File dir = new File(filePath);
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            System.out.println(filePath+fileName);
            //将图片保存到static文件夹里
            pictureName.transferTo(new File(filePath+fileName));
            response.setStatus(1);
            response.setMessage("上传成功！");
            response.setData(fileName);
            return response;
        } catch (Exception e) {
            // e.printStackTrace();
            response.setMessage("上传失败！" + e.getMessage());
            return response;
        }
    }

    @RequestMapping(value = "/getequipmentparamrecord")
    public TNPYResponse getEquipmentParamRecord( String equipID) {

        TNPYResponse result = new TNPYResponse();
        try {
            List<EquipmentParaRecord> equipmentParaRecordList = equipmentParaRecordMapper.selectRecord(equipID);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(equipmentParaRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
