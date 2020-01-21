package com.tnpy.mes.service.equipmentParamRecordService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.EquipmentParaMapper;
import com.tnpy.mes.mapper.mysql.EquipmentParaRecordMapper;
import com.tnpy.mes.mapper.mysql.ParameterInfoMapper;
import com.tnpy.mes.model.customize.EquipParamLatestRecord;
import com.tnpy.mes.model.mysql.EquipmentParaRecord;
import com.tnpy.mes.model.mysql.ParameterInfo;
import com.tnpy.mes.service.equipmentParamRecordService.IEquipmentParamRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:26
 */
@Service("equipmentParamService")
public class EquipmentParamRecordServiceImpl implements IEquipmentParamRecordService {

    @Autowired
    private ParameterInfoMapper parameterInfoMapper;
    @Autowired
    private EquipmentParaRecordMapper equipmentParaRecordMapper;

    @Autowired
    private EquipmentParaMapper equipmentParaMapper;


    public TNPYResponse getEquipmentParam(String equipmentTypeID) {

        TNPYResponse result = new TNPYResponse();
        try {
            List<ParameterInfo> parameterInfoList = parameterInfoMapper.selectByEquipType(equipmentTypeID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }


    public TNPYResponse saveEquipmentParamRecord( String json) {
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
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData("");
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse updateEquipmentParam(String params,String equipmentTypeID)
    {
        TNPYResponse result = new TNPYResponse();
        try {

            String[] paramArray = params.split("###");
         //   System.out.println(params + "-----"+ paramArray.length);
        String insertData = " insert into tb_equipmentparam values ";
        if(paramArray.length>0)
        {
              insertData+= "('"+equipmentTypeID +"','"+  paramArray[0] +"',1)";
        }
            for(int i =1;i<paramArray.length;i++)
            {
                insertData+= ",('"+equipmentTypeID +"','"+  paramArray[i] +"',1)";

            }
            insertData +=";";
            if(paramArray.length <1)
                insertData = "";
            equipmentParaMapper.updateEquipParams(insertData,equipmentTypeID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData("");
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

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

    public TNPYResponse getEquipmentParamRecord(String equipID) {


        TNPYResponse result = new TNPYResponse();
        try {
            List<EquipmentParaRecord> equipmentParaRecordList = equipmentParaRecordMapper.selectRecord(equipID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(equipmentParaRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getLatestParamRecord( String plantID,String equipType,String paramID) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<EquipParamLatestRecord> parameterInfoList = equipmentParaRecordMapper.selectLatestRecord(plantID,equipType,paramID, ConfigParamEnum.EquipmentTypeEnum.getName(equipType));
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse getOneEquipParamRecord( String startTime,String endTime,String equipID,String paramID)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String equipType = equipmentParaRecordMapper.selectEquipType(equipID);
            String DBName = ConfigParamEnum.basicEquipParamDB;
            if(!StringUtil.isEmpty(equipType))
            {
                DBName += "_" + equipType;
            }
            List<EquipParamLatestRecord> parameterInfoList = equipmentParaRecordMapper.selectOneEquipParamRecord(DBName,startTime,endTime,equipID,paramID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getEquipParamRecordByTime(String startTime,String endTime, String equipID)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            List<EquipmentParaRecord> equipmentParaRecordList = equipmentParaRecordMapper.selectRecordByTime(startTime,endTime,equipID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(equipmentParaRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse getRecentAllParamPecord( String plantID,String equipType,String processID)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date recentTime = new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(recentTime);
            calendar.add(Calendar.MINUTE, -12);
            recentTime = calendar.getTime();
            String timeStr = dateFormat.format(recentTime);
            if(ConfigParamEnum.EquipmentTypeEnum.ZNDB.getTypeID().equals(equipType))
            {
                Date now = new Date( );
                Calendar calendar2 = new GregorianCalendar();
                calendar2.setTime(now);
                calendar2.add(Calendar.DAY_OF_MONTH, -1);
                now = calendar2.getTime();
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                timeStr =  dateFormat2.format(now);
            }
            String processFilter = "";
            if(!"-1".equals(processID))
            {
                processFilter += " and  processID = '" + processID +"' ";
            }
            List<Map<Object,Object>> parameterInfoList = equipmentParaRecordMapper.selectRecentAllParamPecord(plantID,equipType,timeStr, ConfigParamEnum.EquipmentTypeEnum.getName(equipType),processFilter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }


    public TNPYResponse getElectricProductionRelation(String plantID,String processID, String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object,Object>>  equipmentParaRecordList = equipmentParaRecordMapper.getElectricProductionRelation(plantID,processID,startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(equipmentParaRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
    
    /**
     * 新设备采集方法
     */
    public TNPYResponse getRecentAllParamPecordNew( String plantID,String equipType,String processID)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date recentTime = new Date();//取时间
            String timeStr = dateFormat.format(recentTime);
            List<LinkedHashMap<Object,Object>> parameterInfoList = null;
            //一部固化室
            if(ConfigParamEnum.EquipmentTypeEnum.GHSCJ.getTypeID().equals(equipType)&&ConfigParamEnum.EquipmentTypeEnum.GHSCJ1001.getTypeID().equals(plantID.trim()))
            {
            	  parameterInfoList = equipmentParaRecordMapper.selectSolidificationoperatingparametersacquisition(plantID,equipType,timeStr, ConfigParamEnum.EquipmentTypeEnum.GHSCJ1001.getTableName());
            }
            //二部固化室
            if(ConfigParamEnum.EquipmentTypeEnum.GHSCJ.getTypeID().equals(equipType)&&ConfigParamEnum.EquipmentTypeEnum.GHSCJ1002.getTypeID().equals(plantID.trim()))
            {
            	parameterInfoList = equipmentParaRecordMapper.selectSolidificationoperatingparametersacquisition(plantID,equipType,timeStr, ConfigParamEnum.EquipmentTypeEnum.GHSCJ1002.getTableName());
            }
            //三部固化室
            if(ConfigParamEnum.EquipmentTypeEnum.GHSCJ.getTypeID().equals(equipType)&&ConfigParamEnum.EquipmentTypeEnum.GHSCJ1003.getTypeID().equals(plantID.trim()))
            {
            	parameterInfoList = equipmentParaRecordMapper.selectSolidificationoperatingparametersacquisition(plantID,equipType,timeStr, ConfigParamEnum.EquipmentTypeEnum.GHSCJ1003.getTableName());
            }
            
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(parameterInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
