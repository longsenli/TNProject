package com.tnpy.mes.service.scrapInfoService.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialScrapRecordMapper;
import com.tnpy.mes.mapper.mysql.ObjectRelationDictMapper;
import com.tnpy.mes.mapper.mysql.PlasticUsedRecordMapper;
import com.tnpy.mes.mapper.mysql.WorkOrderScrapInfoMapper;
import com.tnpy.mes.model.mysql.Material;
import com.tnpy.mes.model.mysql.MaterialScrapRecord;
import com.tnpy.mes.model.mysql.PlasticUsedRecord;
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

    @Autowired
    private ObjectRelationDictMapper objectRelationDictMapper;

    @Autowired
    private PlasticUsedRecordMapper plasticUsedRecordMapper;

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

            if(ConfigParamEnum.BasicProcessEnum.JSProcessID.getName().equals(processID))
            {
//                List<String> nextList = objectRelationDictMapper.selectNextObjectID(lineID,"1002");
//                if(nextList.size() != 1)
//                {
//                    result.setMessage("获取后续产线失败！");
//                    return result;
//                }
                List<Map<Object, Object>> usedMaterialInfoList = materialScrapRecordMapper.getJSUsedMaterialInfoWithExpend(lineID, dateFormat.format(tmp),dateFormat.format(tmp) + " 23:59");
                result.setData(JSONObject.toJSONString(usedMaterialInfoList, SerializerFeature.WriteMapNullValue));
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                return result;
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

    public TNPYResponse getMaterialScrapRecord(String plantID, String processID, String lineID,String scrapSelectType,  String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = "";
            if ("-1".equals(lineID)) {
                filter = " and plantID = '" + plantID + "' and processID = '" + processID + "'";
            } else {
                filter = " and lineID = '" + lineID + "'";
            }
            if (!"-1".equals(scrapSelectType)) {
                filter += " and operateType = '" + scrapSelectType + "' ";
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
            materialScrapRecord.setOperatetype(jsonMap.get("operateType"));
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

    public TNPYResponse scrapByBatteryQrcode( String id ,String scrapPlant,String scrapProcess,String repairReason,String updateStaffID,String updateStaff)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            PlasticUsedRecord plasticUsedRecord =plasticUsedRecordMapper.selectByPrimaryKey(id);
            if(plasticUsedRecord == null)
            {
                result.setMessage("未找到该底壳的信息！" + id);
                return result;
            }
            plasticUsedRecordMapper.updateScrapInfo(id,"2",repairReason);
            MaterialScrapRecord materialScrapRecord = new MaterialScrapRecord();
            materialScrapRecord.setId(id);
            materialScrapRecord.setOperatetype("电池底壳");
            materialScrapRecord.setUpdatestaff(updateStaff);
            materialScrapRecord.setUpdatetime( new Date());
            materialScrapRecord.setStatus("1");
            materialScrapRecord.setValue(Float.valueOf(1));
            materialScrapRecord.setMaterialname(plasticUsedRecord.getMaterialname());
            materialScrapRecord.setMaterialid(plasticUsedRecord.getMaterialid());
            materialScrapRecord.setRemark(repairReason);
            materialScrapRecord.setPlantid(plasticUsedRecord.getPlantid());
            materialScrapRecord.setProcessid(scrapProcess);
            materialScrapRecord.setLineid((plasticUsedRecord.getLineid()));
            materialScrapRecord.setLocationid(plasticUsedRecord.getWorklocation());
            materialScrapRecord.setOrdername(updateStaffID);
            materialScrapRecord.setOrderid(plasticUsedRecord.getStaffname());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if (calendar.get(Calendar.HOUR_OF_DAY) < 7) {
                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                date = dateFormat.parse(dateFormat.format(date));
                materialScrapRecord.setProductday(date);
                materialScrapRecord.setClasstype("夜班");

            } else  if (calendar.get(Calendar.HOUR_OF_DAY) > 18){
                date = dateFormat.parse(dateFormat.format(date));
                materialScrapRecord.setProductday(date);
                materialScrapRecord.setClasstype("夜班");
            }
            else
            {
                date = dateFormat.parse(dateFormat.format(date));
                materialScrapRecord.setProductday(date);
                materialScrapRecord.setClasstype("白班");
            }

            materialScrapRecordMapper.insert(materialScrapRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData("报修成功！");
            return result;
        } catch (Exception ex) {
            if(ex.getMessage().contains("uplicate"))
            {
                result.setMessage("报修失败！该电池已报修！" );
            }
            else
            {
                result.setMessage("报修失败！" + ex.getMessage());
            }
            return result;
        }
    }
}
