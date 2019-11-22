package com.tnpy.mes.service.staffWorkDiaryService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.DailyProductionAndWageDetailMapper;
import com.tnpy.mes.mapper.mysql.ProductionLineMapper;
import com.tnpy.mes.mapper.mysql.StaffAttendanceDetailMapper;
import com.tnpy.mes.mapper.mysql.WorkLocationMapper;
import com.tnpy.mes.model.mysql.DailyProductionAndWageDetail;
import com.tnpy.mes.model.mysql.ProductionLine;
import com.tnpy.mes.model.mysql.StaffAttendanceDetail;
import com.tnpy.mes.model.mysql.WorkLocation;
import com.tnpy.mes.service.staffWorkDiaryService.IStaffWorkDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-11-17 11:47
 */
@Service("staffWorkDiaryService")
public class StaffWorkDiaryServiceImpl implements IStaffWorkDiaryService {
    @Autowired
    private StaffAttendanceDetailMapper staffAttendanceDetailMapper;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private WorkLocationMapper workLocationMapper;

    @Autowired
    DailyProductionAndWageDetailMapper dailyProductionAndWageDetailMapper;

    public TNPYResponse getStaffAttendanceInfo(String plantID, String processID, String lineID,String classType, String staffID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where dayTime >= '" + startTime + "' and dayTime <= '" +endTime + "' ";
            if (!"-1".equals(plantID)) {
                filter += " and plantID = '" + plantID + "' ";
            }
            if (!"-1".equals(processID)) {
                filter += " and processID = '" + processID + "' ";
            }
            if (!"-1".equals(lineID)) {
                filter += " and lineID = '" + lineID + "' ";
            }
            if (!"-1".equals(staffID)) {
                filter += " and staffID = '" + staffID + "' ";
            }
            if (!"-1".equals(classType)) {
                filter += " and classType1 = '" + classType + "' ";
            }
            filter += " order by dayTime desc limit 1000";
            List<Map<Object,Object>> staffAttendanceRecordList = staffAttendanceDetailMapper.selectMapRecordByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(staffAttendanceRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    // 1 上机扫码  2 下机扫码
    public TNPYResponse insertStaffComeAttendanceInfo(String qrCode, String staffID, String staffName, String classType1, String classType2, String dayTime,String workContent) {
        TNPYResponse result = new TNPYResponse();
        try {

            String returnMsg = "";
            StaffAttendanceDetail staffAttendanceDetail = new StaffAttendanceDetail();

            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

            WorkLocation workLocation = workLocationMapper.selectByPrimaryKey(qrCode);
            if (workLocation == null) {
                ProductionLine productionLine = productionLineMapper.selectByPrimaryKey(qrCode);
                {
                    if (productionLine == null) {
                        result.setMessage("未获取到二维码信息，请确认是正确的二维码！" + qrCode);
                        return result;
                    } else {
                        staffAttendanceDetail.setPlantid(productionLine.getPlantid());
                        staffAttendanceDetail.setProcessid(productionLine.getProcessid());
                        staffAttendanceDetail.setLineid(productionLine.getId());
                    }
                }
            } else {
                staffAttendanceDetail.setPlantid(workLocation.getPlantid());
                staffAttendanceDetail.setProcessid(workLocation.getProcessid());
                staffAttendanceDetail.setLineid(workLocation.getLineid());
                staffAttendanceDetail.setWorklocationid(workLocation.getId());
            }
            String filter = "";
            if (staffAttendanceDetail.getWorklocationid() == null) {
                returnMsg = staffAttendanceDetail.getPlantid() + "___" + staffAttendanceDetail.getProcessid() + "___" + staffAttendanceDetail.getLineid() + "___" + " ";
                filter = " where lineID = '" + qrCode + "' and staffID = '" + staffID + "' and dayTime = '" + dayTime + "' and classType1 = '" + classType1 + "'";

            } else {
                returnMsg = staffAttendanceDetail.getPlantid() + "___" + staffAttendanceDetail.getProcessid() + "___" + staffAttendanceDetail.getLineid() + "___" + staffAttendanceDetail.getWorklocationid();
                filter = " where worklocationID = '" + qrCode + "' and staffID = '" + staffID + "' and dayTime = '" + dayTime + "' and classType1 = '" + classType1 + "'";
            }
            List<StaffAttendanceDetail> staffAttendanceDetailList = staffAttendanceDetailMapper.selectRecordByFilter(filter);
            if (staffAttendanceDetailList.size() > 0) {
                result.setMessage(staffName + "已经扫过二维码信息！" + qrCode);
                return result;
            }
            staffAttendanceDetail.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            staffAttendanceDetail.setStaffid(staffID);
            staffAttendanceDetail.setStaffname(staffName);
            staffAttendanceDetail.setClasstype1(classType1);
            staffAttendanceDetail.setClasstype2(classType2);
            staffAttendanceDetail.setDaytime(dateFormat2.parse(dayTime));
            staffAttendanceDetail.setCometime(new Date());
            staffAttendanceDetail.setExtd1(workContent);
            staffAttendanceDetail.setStatus(StatusEnum.StatusFlag.using.getIndex()+ "");
            staffAttendanceDetailMapper.insert(staffAttendanceDetail);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(returnMsg);
            result.setMessage("上机成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse insertStaffGoAttendanceInfo(String qrCode, String staffID) {
        TNPYResponse result = new TNPYResponse();
        try {
            if (staffAttendanceDetailMapper.updateStaffGoAttendanceInfo(qrCode, staffID) < 1) {
                result.setMessage("下机出错！，未找到上机记录！" + qrCode);
                return result;
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("下机成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("下机出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse deleteStaffAttendanceInfo(String id) {
        TNPYResponse result = new TNPYResponse();
        try {

            staffAttendanceDetailMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("删除成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("删除失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse confirmStaffAttendanceInfo(String staffID,String staffName,String recordID)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            recordID ="'" + recordID.replaceAll("___","','") + "'";
            staffAttendanceDetailMapper.updateConfirmStaffGoAttendanceInfo(staffID,staffName,recordID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("确认成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("确认失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getTMPProductionWageInfo(String plantID,String processID,String dayString,String classType)
    {
        TNPYResponse result = new TNPYResponse();
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat2.parse(dayString);//取时间
            String orderInfo = "" ;

            if("白班".equals(classType))
            {
                orderInfo = "'%BB" + dateFormat.format(date) + "'";
            }
            else
            {
                orderInfo = "'%YB" + dateFormat.format(date) + "'";
            }
            dayString = dateFormat2.format(date);
            List<Map<Object,Object>> productionWageTMP;

            if(ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID))
            {
                productionWageTMP= dailyProductionAndWageDetailMapper.ZHQDProductionWageInfoByWorklocation(plantID,processID,orderInfo,dayString,classType);
            }
            else  if(ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID))
            {
                productionWageTMP= dailyProductionAndWageDetailMapper.orderProductionWageInfoByWorklocation(plantID,processID,orderInfo,dayString,classType);
            }
            else
            {
                productionWageTMP= dailyProductionAndWageDetailMapper.orderProductionWageInfoByLine(plantID,processID,orderInfo,dayString,classType);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(productionWageTMP).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getFinalProductionWageInfo(String plantID,String processID,String dayString,String classType)
    {
        TNPYResponse result = new TNPYResponse();
        try {

            List<Map<Object,Object>> productionWageFinal = dailyProductionAndWageDetailMapper.getFinalProductionWageInfo(plantID,processID,dayString,classType);
        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
        result.setData(JSONObject.toJSONString(productionWageFinal).toString());
        return result;
    } catch (Exception ex) {
        result.setMessage("查询出错！" + ex.getMessage());
        return result;
    }
    }
    public TNPYResponse deleteConfirmProductionWageRecord(String plantID,String processID,String dayString,String classType)
    {
        TNPYResponse result = new TNPYResponse();
        try {

            dailyProductionAndWageDetailMapper.deleteConfirmProductionWageRecord(plantID,processID,dayString,classType);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("删除成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("删除失败！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse confirmProductionWageInfo( String recordJsonString,String verifierID,String verifierName)
    {
        TNPYResponse result = new TNPYResponse();
        try {


            List<DailyProductionAndWageDetail> recordList = JSON.parseArray(recordJsonString,DailyProductionAndWageDetail.class);

            if(recordList.size() < 1)
            {
                result.setMessage("输入产量信息为空，请确认！" );
                return result;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if(dailyProductionAndWageDetailMapper.checkConfirmAlready(recordList.get(0).getPlantid(),recordList.get(0).getProcessid(),
                  dateFormat.format(recordList.get(0).getDaytime()),  recordList.get(0).getClasstype1()) > 0)
            {
                result.setMessage("该工序已确认过产量！" );
                return result;
            }
            for(int i =0;i< recordList.size();i++)
            {
                recordList.get(i).setVerifierid(verifierID);
                recordList.get(i).setVerifiername(verifierName);
                recordList.get(i).setVerifytime(new Date());
                recordList.get(i).setStatus("1");
                dailyProductionAndWageDetailMapper.insertSelective(recordList.get(i));
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
          //  result.setData(JSONObject.toJSONString(productionWageTMP).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("确认产量出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getLocationQRInfo(String QRCode)
    {
        TNPYResponse result = new TNPYResponse();
        try {

            List<Map<Object,Object>> qrCodeInfoList = workLocationMapper.selectByQRCode(QRCode);

            if (qrCodeInfoList.size() < 1 ) {
                qrCodeInfoList = productionLineMapper.selectByQRCode(QRCode);
                {
                    if (qrCodeInfoList.size() < 1) {
                        result.setMessage("未获取到二维码信息，请确认是正确的二维码！" + QRCode);
                        return result;
                    }
                }
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(qrCodeInfoList));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
