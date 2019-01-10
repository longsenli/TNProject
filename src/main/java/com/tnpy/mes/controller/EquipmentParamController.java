package com.tnpy.mes.controller;

import com.tnpy.common.Enum.DirectoryEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.equipmentParamRecordService.IEquipmentParamRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    IEquipmentParamRecordService equipmentParamService;

    @RequestMapping(value = "/getequipmentparam")
    public TNPYResponse getEquipmentParam(String equipmentTypeID) {

       return  equipmentParamService.getEquipmentParam(equipmentTypeID);
    }

    @RequestMapping(value = "/saveequipmentparam")
    public TNPYResponse saveEquipmentParam(@RequestBody String json) {
        return  equipmentParamService.saveEquipmentParam(json);
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
            String filePath = DirectoryEnum.FileStoreLocation.EquipInfoPicture.getName();
            System.out.println("进入成功！" + fileName);
            File dir = new File(filePath);
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            System.out.println(filePath+fileName);
            //将图片保存到static文件夹里
            pictureName.transferTo(new File(filePath+fileName));
            response.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
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

        return  equipmentParamService.getEquipmentParamRecord(equipID);
    }
    @RequestMapping(value = "/getequipparamrecordbytime")
    public TNPYResponse getEquipParamRecordByTime(String startTime,String endTime, String equipID) {

        return  equipmentParamService.getEquipParamRecordByTime(startTime, endTime,equipID);
    }
    @RequestMapping(value = "/getlatestparamrecord")
    public TNPYResponse getLatestParamRecord( String plantID,String equipType,String paramID) {

        return  equipmentParamService.getLatestParamRecord(plantID, equipType, paramID);
    }

    @RequestMapping(value = "/getoneequipparamrecord")
    public TNPYResponse getOneEquipParamRecord( String startTime,String endTime,String equipID,String paramID) {

        return  equipmentParamService.getOneEquipParamRecord(startTime, endTime, equipID,paramID);
    }
}
