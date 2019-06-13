package com.tnpy.mes.controller;

import com.tnpy.common.Enum.DirectoryEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.service.safetyAndEPService.ISafetyAndPEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-06-03 16:25
 */
@RestController
@RequestMapping("/api/safetyandep")
public class SafetyAndEPController {
    @Autowired
    private ISafetyAndPEService safetyAndPEService ;


    @RequestMapping(value = "/getsalesorderdetail")
    public TNPYResponse getIchnographyDetailInfo(String mainRegionID)
    {
        return  safetyAndPEService.getIchnographyDetailInfo(mainRegionID);
    }

    @RequestMapping(value = "/gethiddendangermanagerecord")
    public TNPYResponse getHiddenDangerManageRecord(String plantID,String selectLevel,String startTime,String endTime) {
        return  safetyAndPEService.getHiddenDangerManageRecord(plantID,selectLevel, startTime, endTime);
    }

    @RequestMapping(value = "/detetehiddendangermanagerecord")
    public TNPYResponse deteteHiddenDangerManageRecord(String id) {
        return  safetyAndPEService.deteteHiddenDangerManageRecord(id);
    }

    @RequestMapping(value = "/changehiddendangermanagerecord")
    public TNPYResponse changeHiddenDangerManageRecord(@RequestBody String jsonStr) {
        return  safetyAndPEService.changeHiddenDangerManageRecord(jsonStr);
    }


    @PostMapping("/pictureupload")
    public TNPYResponse pictureUpload(MultipartFile pictureName){

        TNPYResponse response = new TNPYResponse();

        try {
            //获取文件名
            String fileName = pictureName.getOriginalFilename();
            //获取文件后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //重新生成文件名
            fileName = UUID.randomUUID()+suffixName;
            //指定本地文件夹存储图片
            Date date = new Date();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");

            String filePath = DirectoryEnum.FileStoreLocation.SafetyAndEPPicture.getName() + dateFormat.format(date) + "/";
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

}
