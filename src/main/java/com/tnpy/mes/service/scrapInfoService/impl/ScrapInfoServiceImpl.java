package com.tnpy.mes.service.scrapInfoService.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialScrapRecordMapper;
import com.tnpy.mes.mapper.mysql.WorkOrderScrapInfoMapper;
import com.tnpy.mes.model.mysql.Material;
import com.tnpy.mes.model.mysql.MaterialScrapRecord;
import com.tnpy.mes.model.mysql.WorkOrderScrapInfo;
import com.tnpy.mes.service.scrapInfoService.IScrapInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/29 9:10
 */
@Service("scrapinfoservice")
public class ScrapInfoServiceImpl implements IScrapInfoService {
    @Autowired
    private WorkOrderScrapInfoMapper workOrderScrapInfoMapper;

    @Autowired
    private MaterialScrapRecordMapper materialScrapRecordMapper;


    public TNPYResponse getScrapInfo(String plantID, String processID, String lineID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<WorkOrderScrapInfo> workOrderScrapInfoList = workOrderScrapInfoMapper.selectByParams(plantID, processID, lineID.trim(), startTime, endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderScrapInfoList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse saveScrapInfo(String strJson) {
        TNPYResponse result = new TNPYResponse();
        try {
            //System.out.println(strJson);
            JSONObject jso = JSONObject.parseObject(strJson);
            Map<String, String> jsonMap = JSONObject.toJavaObject(jso, Map.class);
            List<WorkOrderScrapInfo> workOrderScrapInfoList = new ArrayList<>();

            for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
                if (!entry.getKey().contains("###"))
                    continue;
                if (StringUtils.isEmpty(entry.getValue()))
                    continue;
                WorkOrderScrapInfo workOrderScrapInfo = new WorkOrderScrapInfo();
                workOrderScrapInfo.setOrderid(jsonMap.get("orderid"));
                workOrderScrapInfo.setMaterialid(entry.getKey().split("###")[0]);
                workOrderScrapInfo.setMaterialname(entry.getKey().split("###")[1]);
                workOrderScrapInfo.setOrdername(jsonMap.get("ordername"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                workOrderScrapInfo.setOrdertime(formatter.parse(jsonMap.get("ordertime")));
                workOrderScrapInfo.setValue(Float.parseFloat(entry.getValue()));
                workOrderScrapInfo.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                workOrderScrapInfoList.add(workOrderScrapInfo);
            }
            workOrderScrapInfoMapper.insertManyOrderScrap(workOrderScrapInfoList, jsonMap.get("orderid"));
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("报废出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getMaterialScrapInfo(String materialID, String orderID) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Material> materialList = workOrderScrapInfoMapper.getMaterialScrapInfo(materialID, orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(materialList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getUsedMaterialInfo(String plantID, String processID, String lineID, String productDate, String classType) {
        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date tmp = dateFormat.parse(productDate);
            String productionTime = dateFormat.format(tmp);
            if ("白班".equals(classType)) {
                productionTime += " 07:00";
            }
            if ("夜班".equals(classType)) {
                productionTime += " 19:00";
            }
            List<Map<Object, Object>> usedMaterialInfoList = materialScrapRecordMapper.getUsedMaterialInfo(lineID, productionTime);
            result.setData(JSONObject.toJSONString(usedMaterialInfoList, SerializerFeature.WriteMapNullValue));
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询可用物料失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getMaterialScrapRecord(String plantID, String processID, String lineID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = "";
            if ("-1".equals(lineID)) {
                filter = " and plantID = '" + plantID + "' and processID = '" + processID + "'";
            } else {
                filter = " and lineID = '" + lineID + "'";
            }
            //  System.out.println(filter + "==========" + startTime + "=====" +endTime);
            List<Map<Object, Object>> materialScrapRecordList = materialScrapRecordMapper.getMaterialScrapRecord(filter, startTime, endTime);
            result.setData(JSONObject.toJSON(materialScrapRecordList).toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse saveMaterialScrapRecord(String strJson) {
        TNPYResponse result = new TNPYResponse();
        try {

            //System.out.println(strJson);
            JSONObject jso = JSONObject.parseObject(strJson);
            Map<String, String> jsonMap = JSONObject.toJavaObject(jso, Map.class);
            MaterialScrapRecord materialScrapRecord = new MaterialScrapRecord();
            materialScrapRecord.setPlantid(jsonMap.get("plantID"));
            materialScrapRecord.setProcessid(jsonMap.get("processID"));
            materialScrapRecord.setLineid(jsonMap.get("lineID"));
            materialScrapRecord.setClasstype(jsonMap.get("classType"));

            materialScrapRecord.setRemark(jsonMap.get("remark"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            materialScrapRecord.setProductday(dateFormat.parse(jsonMap.get("productDate")));
            materialScrapRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
            materialScrapRecord.setUpdatetime(new Date());
            materialScrapRecord.setUpdatestaff(jsonMap.get("updateStaff"));

            for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
                if (!entry.getKey().contains("###"))
                    continue;
                if (StringUtils.isEmpty(entry.getValue()))
                    continue;
                materialScrapRecord.setMaterialid(entry.getKey().split("###")[0]);
                materialScrapRecord.setMaterialname(entry.getKey().split("###")[1]);
                materialScrapRecord.setValue(Float.parseFloat(entry.getValue()));
                materialScrapRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                if (materialScrapRecord.getValue() <= 0) {
                    continue;
                }
                materialScrapRecordMapper.insert(materialScrapRecord);

            }
            //workOrderScrapInfoMapper.insertManyOrderScrap(workOrderScrapInfoList,jsonMap.get("orderid"));
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("报废出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse deleteMaterialScrapRecord(String id) {
        TNPYResponse result = new TNPYResponse();
        try {
            materialScrapRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData("删除成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("删除失败！" + ex.getMessage());
            return result;
        }
    }
}
