package com.tnpy.mes.service.staffWorkDiaryService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.mysql.*;
import com.tnpy.mes.service.staffWorkDiaryService.IStaffWorkDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    DailyProductionDetailRecordMapper dailyProductionDetailRecordMapper;

    @Autowired
    private ObjectRelationDictMapper objectRelationDictMapper;

    @Autowired
    private DailyLineProductionDetailRecordMapper dailyLineProductionDetailRecordMapper;

    @Autowired
    private DailyProcessProductionDetailRecordMapper dailyProcessProductionDetailRecordMapper;

    public TNPYResponse getStaffAttendanceInfo(String plantID, String processID, String lineID, String classType, String staffID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where dayTime >= '" + startTime + "' and dayTime <= '" + endTime + "' ";
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
            filter += " order by dayTime,classType1,lineID,worklocationID  limit 1000";
            List<Map<Object, Object>> staffAttendanceRecordList = staffAttendanceDetailMapper.selectMapRecordByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(staffAttendanceRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    // 1 上机扫码  2 下机扫码
    public TNPYResponse insertStaffComeAttendanceInfo(String qrCode, String staffID, String staffName, String classType1, String classType2, String dayTime, String workContent, String teamType) {
        TNPYResponse result = new TNPYResponse();
        try {

            String returnMsg = "";
            StaffAttendanceDetail staffAttendanceDetail = new StaffAttendanceDetail();

            if ("-1".equals(teamType)) {
                teamType = null;
            }
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
            staffAttendanceDetail.setExtd2(teamType);
            staffAttendanceDetail.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
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
            Date dateNow = new Date();

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateNow);

            calendar.add(Calendar.HOUR, -24);
            dateNow = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String filter = " where staffID = '" + staffID + "' and comeTime > '" + dateFormat.format(dateNow) + "' and  (lineID = '" + qrCode + "' or worklocationID =  '" + qrCode + "' ) order by dayTime desc";

            List<StaffAttendanceDetail> staffAttendanceDetailList = staffAttendanceDetailMapper.selectRecordByFilter(filter);

            if (staffAttendanceDetailList.size() < 1) {
                result.setMessage("下机出错！，未找到上机记录！" + qrCode);
                return result;
            }
            if (null == staffAttendanceDetailList.get(0).getGotime()) {
                if (staffAttendanceDetailMapper.updateStaffGoAttendanceInfo(qrCode, staffID) < 1) {
                    result.setMessage("下机出错！，未找到上机记录！" + qrCode);
                    return result;
                }
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage("下机成功！");
                return result;

            }
            result.setMessage("您已经下机，无需重复扫码！" + qrCode + dateFormat.format(staffAttendanceDetailList.get(0).getGotime()));
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

    public TNPYResponse confirmStaffAttendanceInfo(String staffID, String staffName, String recordID) {
        TNPYResponse result = new TNPYResponse();
        try {
            recordID = "'" + recordID.replaceAll("___", "','") + "'";
            staffAttendanceDetailMapper.updateConfirmStaffGoAttendanceInfo(staffID, staffName, recordID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("确认成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("确认失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getTMPProductionWageInfo(String plantID, String processID, String lineID, String dayString, String classType) {
        TNPYResponse result = new TNPYResponse();
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat2.parse(dayString);//取时间
            String orderInfo = "";

            if ("白班".equals(classType)) {
                orderInfo = "'%BB" + dateFormat.format(date) + "'";
            } else {
                orderInfo = "'%YB" + dateFormat.format(date) + "'";
            }
            dayString = dateFormat2.format(date);
            List<Map<Object, Object>> productionWageTMP;
            String lineFilter = "";
            if (ConfigParamEnum.BasicProcessEnum.JSProcessID.getName().equals(processID)) {
                if (!"-1".equals(lineID)) {
                    lineFilter = " and lineID = '" + lineID + "' ";
                }

                productionWageTMP = dailyProductionAndWageDetailMapper.JSProductionWageInfoByWorklocation(plantID, processID, lineFilter, dayString, classType);
            } else if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                if (!"-1".equals(lineID)) {
                    lineFilter = " and lineID = '" + lineID + "' ";
                }

                productionWageTMP = dailyProductionAndWageDetailMapper.ZHQDProductionWageInfoByWorklocation(plantID, processID, lineFilter, orderInfo, dayString, classType);
            } else if (ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID)) {
                if (!"-1".equals(lineID)) {
                    lineFilter = " and inputLineID = '" + lineID + "' ";
                }
                productionWageTMP = dailyProductionAndWageDetailMapper.orderProductionWageInfoByWorklocation(plantID, processID, lineFilter, orderInfo, dayString, classType);
            } else if (ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName().equals(processID)) {
                if (!"-1".equals(lineID)) {
                    lineFilter = " and inputLineID = '" + lineID + "' ";
                }
                productionWageTMP = dailyProductionAndWageDetailMapper.orderProductionWageInfoByLine(plantID, processID, lineFilter, orderInfo, dayString, classType);
            } else {
                if (!"-1".equals(lineID)) {
                    lineFilter = " and inputLineID = '" + lineID + "' ";
                }
                productionWageTMP = dailyProductionAndWageDetailMapper.orderProductionWageInfoByLineAVGProduction(plantID, processID, lineFilter, orderInfo, dayString, classType);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(productionWageTMP).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getFinalProductionWageInfo(String plantID, String processID, String dayString, String classType) {
        TNPYResponse result = new TNPYResponse();
        try {

            List<Map<Object, Object>> productionWageFinal = dailyProductionAndWageDetailMapper.getFinalProductionWageInfo(plantID, processID, dayString, classType);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(productionWageFinal).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse deleteConfirmProductionWageRecord(String plantID, String processID, String dayString, String classType) {
        TNPYResponse result = new TNPYResponse();
        try {
            dailyProductionAndWageDetailMapper.deleteConfirmProductionWageRecord(plantID, processID, dayString, classType);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("删除成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("删除失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse confirmProductionWageInfo(String recordJsonString, String verifierID, String verifierName) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<DailyProductionAndWageDetail> recordList = JSON.parseArray(recordJsonString, DailyProductionAndWageDetail.class);

            if (recordList.size() < 1) {
                result.setMessage("输入产量信息为空，请确认！");
                return result;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String conformedRecordList = "";
            List<DailyProductionAndWageDetail> dailyProductionAndWageDetailList = dailyProductionAndWageDetailMapper.getConfirmRecord(recordList.get(0).getPlantid(), recordList.get(0).getProcessid(),
                    dateFormat.format(recordList.get(0).getDaytime()), recordList.get(0).getClasstype1());
            if ((ConfigParamEnum.BasicProcessEnum.JZProcessID.getName() + " " + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + " " + ConfigParamEnum.BasicProcessEnum.JSProcessID.getName()).contains(recordList.get(0).getProcessid())) {
                for (int i = 0; i < dailyProductionAndWageDetailList.size(); i++) {
                    conformedRecordList += dailyProductionAndWageDetailList.get(i).getWorklocationid() + "_" + dailyProductionAndWageDetailList.get(i).getStaffid() + " ";
                }
            } else {
                for (int i = 0; i < dailyProductionAndWageDetailList.size(); i++) {
                    conformedRecordList += dailyProductionAndWageDetailList.get(i).getLineid() + "_" + dailyProductionAndWageDetailList.get(i).getStaffid() + " ";
                }
            }
//            if(dailyProductionAndWageDetailMapper.checkConfirmAlready(recordList.get(0).getPlantid(),recordList.get(0).getProcessid(),
//                  dateFormat.format(recordList.get(0).getDaytime()),  recordList.get(0).getClasstype1()) > 0)
//            {
//                result.setMessage("该工序已确认过产量！" );
//                return result;
//            }
            int successNumber = 0;
            String alreadyConfirmedName = "";
            for (int i = 0; i < recordList.size(); i++) {
                if ((ConfigParamEnum.BasicProcessEnum.JZProcessID.getName() + " " + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + " " + ConfigParamEnum.BasicProcessEnum.JSProcessID.getName()).contains(recordList.get(0).getProcessid())) {
                    if (conformedRecordList.contains(recordList.get(i).getWorklocationid() + "_" + recordList.get(i).getStaffid())) {
                        alreadyConfirmedName += recordList.get(i).getStaffname() + ",";
                        continue;
                    }
                } else if (conformedRecordList.contains(recordList.get(i).getLineid() + "_" + recordList.get(i).getStaffid())) {
                    alreadyConfirmedName += recordList.get(i).getStaffname() + ",";
                    continue;
                }
                recordList.get(i).setVerifierid(verifierID);
                recordList.get(i).setVerifiername(verifierName);
                recordList.get(i).setVerifytime(new Date());
                recordList.get(i).setStatus("1");
                successNumber++;
                dailyProductionAndWageDetailMapper.insertSelective(recordList.get(i));
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            if (recordList.size() - successNumber > 0) {
                result.setMessage("成功确认" + successNumber + "人产量信息，失败" + (recordList.size() - successNumber) + "人,原因为产量已确认，" + alreadyConfirmedName);
            } else {
                result.setMessage("成功确认" + successNumber + "人产量信息。");
            }
            //  result.setData(JSONObject.toJSONString(productionWageTMP).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("确认产量出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getLocationQRInfo(String QRCode) {
        TNPYResponse result = new TNPYResponse();
        try {

            List<Map<Object, Object>> qrCodeInfoList = workLocationMapper.selectByQRCode(QRCode);

            if (qrCodeInfoList.size() < 1) {
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

    public TNPYResponse getShelfWageDetail(String staffID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {

            List<Map<Object, Object>> wageDetailList = dailyProductionAndWageDetailMapper.getShelfWageDetail(staffID, startTime, endTime);

            if (wageDetailList.size() < 1) {
                result.setMessage("未找到该员工的工资信息，请联系班长是否确认产量！");
                return result;
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(wageDetailList));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getShelfDailyTMPDetail(String staffID, String dayTime) {
        TNPYResponse result = new TNPYResponse();
        try {

            List<Map<Object, Object>> wageDetailList = dailyProductionAndWageDetailMapper.getShelfWageDetail(staffID, dayTime, dayTime);
            if (wageDetailList.size() > 0) {

                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(wageDetailList));
                return result;
            }

            List<StaffAttendanceDetail> staffAttendanceDetailList = staffAttendanceDetailMapper.selectAttendceRecord(staffID, dayTime, dayTime);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat2.parse(dayTime);//取时间
            String orderInfo = "";
            String dayString = dateFormat2.format(date);


            if (staffAttendanceDetailList.size() < 1) {
                result.setMessage("未找到当日出勤信息，请联系班长是否确认出勤！" + dayTime);
                return result;
            }

            List<Map<Object, Object>> shelfProductionWageTMP = new LinkedList<>();
            for (int i = 0; i < staffAttendanceDetailList.size(); i++) {
                if ("白班".equals(staffAttendanceDetailList.get(i).getClasstype1())) {
                    orderInfo = "'%BB" + dateFormat.format(date) + "'";
                } else {
                    orderInfo = "'%YB" + dateFormat.format(date) + "'";
                }
                List<Map<Object, Object>> productionWageTMP;
                String lineFilter = "";
                if (ConfigParamEnum.BasicProcessEnum.JSProcessID.getName().equals(staffAttendanceDetailList.get(i).getProcessid())) {

                    lineFilter = " and lineID = '" + staffAttendanceDetailList.get(i).getLineid() + "' ";


                    productionWageTMP = dailyProductionAndWageDetailMapper.JSProductionWageInfoByWorklocation(staffAttendanceDetailList.get(i).getPlantid(), staffAttendanceDetailList.get(i).getProcessid(), lineFilter, dayString, staffAttendanceDetailList.get(i).getClasstype1());
                } else if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(staffAttendanceDetailList.get(i).getProcessid())) {

                    lineFilter = " and lineID = '" + staffAttendanceDetailList.get(i).getLineid() + "' ";

                    productionWageTMP = dailyProductionAndWageDetailMapper.ZHQDProductionWageInfoByWorklocation(staffAttendanceDetailList.get(i).getPlantid(), staffAttendanceDetailList.get(i).getProcessid(), lineFilter, orderInfo, dayString, staffAttendanceDetailList.get(i).getClasstype1());
                } else if (ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(staffAttendanceDetailList.get(i).getProcessid())) {

                    lineFilter = " and inputLineID = '" + staffAttendanceDetailList.get(i).getLineid() + "' ";

                    productionWageTMP = dailyProductionAndWageDetailMapper.orderProductionWageInfoByWorklocation(staffAttendanceDetailList.get(i).getPlantid(), staffAttendanceDetailList.get(i).getProcessid(), lineFilter, orderInfo, dayString, staffAttendanceDetailList.get(i).getClasstype1());
                } else if (ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName().equals(staffAttendanceDetailList.get(i).getProcessid())) {

                    lineFilter = " and inputLineID = '" + staffAttendanceDetailList.get(i).getLineid() + "' ";

                    productionWageTMP = dailyProductionAndWageDetailMapper.orderProductionWageInfoByLine(staffAttendanceDetailList.get(i).getPlantid(), staffAttendanceDetailList.get(i).getProcessid(), lineFilter, orderInfo, dayString, staffAttendanceDetailList.get(i).getClasstype1());
                } else {

                    lineFilter = " and inputLineID = '" + staffAttendanceDetailList.get(i).getLineid() + "' ";

                    productionWageTMP = dailyProductionAndWageDetailMapper.orderProductionWageInfoByLineAVGProduction(staffAttendanceDetailList.get(i).getPlantid(), staffAttendanceDetailList.get(i).getProcessid(), lineFilter, orderInfo, dayString, staffAttendanceDetailList.get(i).getClasstype1());
                }
                shelfProductionWageTMP.addAll(productionWageTMP);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(shelfProductionWageTMP));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }


    public TNPYResponse getTMPDailyProductionDetailRecord(String plantID, String processID, String dayTime, String classType) {
        TNPYResponse result = new TNPYResponse();
        try {
            String orderString = "";
            String startTime = "";
            String endTime = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dayTime);//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);

            if ("白班".equals(classType)) {
                orderString = "'%BB" + dayTime.replaceAll("-", "") + "'";
                startTime = dayTime + " 07:00";
                endTime = dayTime + " 19:00";
            } else {
                orderString = "'%YB" + dayTime.replaceAll("-", "") + "'";
                startTime = dayTime + " 19:00";
                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                endTime = dateFormat.format(date) + " 07:00";
            }
            String teamType = dailyProductionDetailRecordMapper.getTeamType(plantID, processID, orderString);
            List<Map<Object, Object>> dailyProductionDetailRecordTMP;
            if (ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID)) {
                dailyProductionDetailRecordTMP = dailyProductionDetailRecordMapper.getJZTMPDailyProductionDetailRecord(plantID, processID, orderString, dayTime, classType, teamType);
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(dailyProductionDetailRecordTMP));
                return result;
            }

            if (ConfigParamEnum.BasicProcessEnum.GHProcessID.getName().equals(processID)) {
                dailyProductionDetailRecordTMP = dailyProductionDetailRecordMapper.getGHSTMPDailyProductionDetailRecord(plantID, processID, startTime, endTime, dayTime, classType, teamType);
            } else if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                dailyProductionDetailRecordTMP = dailyProductionDetailRecordMapper.getZHQDTMPDailyProductionDetailRecord(plantID, processID, orderString, dayTime, classType, teamType);
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(dailyProductionDetailRecordTMP));
                return result;
            } else if (ConfigParamEnum.BasicProcessEnum.CDProcessID.getName().equals(processID)) {
                dailyProductionDetailRecordTMP = dailyProductionDetailRecordMapper.getCDTMPDailyProductionDetailRecord(plantID, processID, dayTime, dayTime + " 23:59", dayTime, classType, teamType);
            } else {
                dailyProductionDetailRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyProductionDetailRecord(plantID, processID, orderString, dayTime, classType, teamType);
            }

            List<Map<Object, Object>> finalDailyProductionDetailRecordTMP = new ArrayList<>();
            Map<String, String> lineProductionMap = new HashMap<>();
            Map<String, String> lineUsedMap = new HashMap<>();
            Map<String, List<Object>> lineRowProductionMap = new HashMap<>();
            Map<String, List<Object>> lineRowUsedMap = new HashMap<>();
            int productionListNumber = 4; //产量数组有每3个是一条记录
            int usedListNumber = 6;   //投料数组有每5个是一条记录

            for (int i = 0; i < dailyProductionDetailRecordTMP.size(); i++) {
                //  System.out.println(lineRowProductionMap.keySet().toString() + " ===" + i);
                List<Object> lineRowInfo = new ArrayList<>();
                //判断产量信息数量
                if (lineProductionMap.containsKey(dailyProductionDetailRecordTMP.get(i).get("lineID"))) {
                    if (!lineProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).contains(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("materialID")))) {
                        lineRowInfo = lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID"));
                        lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("materialID"));
                        lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("materialName"));
                        lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("productionNumber"));
                        if (dailyProductionDetailRecordTMP.get(i).containsKey("productionTransition1")) {
                            lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("productionTransition1"));
                        } else {
                            lineRowInfo.add("0");
                        }
                        lineRowProductionMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineRowInfo);
                        lineProductionMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")) + " " + dailyProductionDetailRecordTMP.get(i).get("materialID"));
                    }
                } else {
                    //  System.out.println(dailyProductionDetailRecordTMP.get(i).get("materialName") + "===34234=== " + dailyProductionDetailRecordTMP.get(i).get("usedMaterialID"));

                    if (!"-".equals(dailyProductionDetailRecordTMP.get(i).get("materialID"))) {
                        //   System.out.println(dailyProductionDetailRecordTMP.get(i).get("materialName") + "=== " + dailyProductionDetailRecordTMP.get(i).get("lineID"));
                        lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("materialID"));
                        lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("materialName"));
                        lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("productionNumber"));
                        if (dailyProductionDetailRecordTMP.get(i).containsKey("productionTransition1")) {
                            lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("productionTransition1"));
                        } else {
                            lineRowInfo.add("0");
                        }
                        lineRowProductionMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineRowInfo);
                        lineProductionMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), " - " + dailyProductionDetailRecordTMP.get(i).get("materialID"));
                    }
                }

                //判断投料信息数量
                List<Object> lineRowUsedInfo = new ArrayList<>();
                if (lineUsedMap.containsKey(dailyProductionDetailRecordTMP.get(i).get("lineID"))) {
                    if (!lineUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).contains(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("usedMaterialID")))) {
                        //  lineRowNumberUsedMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")),lineRowNumberUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")) +1);
                        lineRowUsedInfo = lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID"));
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedMaterialID"));
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedMaterialName"));
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedNumber"));
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("scrapNumber"));
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("weightNumber"));
                        if (dailyProductionDetailRecordTMP.get(i).containsKey("usedNumberTransition1")) {
                            lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedNumberTransition1"));
                        } else {
                            lineRowUsedInfo.add("0");
                        }
                        lineRowUsedMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineRowUsedInfo);
                        lineUsedMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")) + " " + dailyProductionDetailRecordTMP.get(i).get("usedMaterialID"));
                    }
                } else {
                    if (!"-".equals(dailyProductionDetailRecordTMP.get(i).get("usedMaterialID"))) {
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedMaterialID"));
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedMaterialName"));
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedNumber"));
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("scrapNumber"));
                        lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("weightNumber"));
                        if (dailyProductionDetailRecordTMP.get(i).containsKey("usedNumberTransition1")) {
                            lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedNumberTransition1"));
                        } else {
                            lineRowUsedInfo.add("0");
                        }
                        lineRowUsedMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineRowUsedInfo);
                        lineUsedMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), " - " + dailyProductionDetailRecordTMP.get(i).get("usedMaterialID"));
                    }
                }
            }

            List<String> lineList = new ArrayList<>();
            int productionListTotalNumber = 0;
            int usedListTotalNumber = 0;
            for (int i = 0; i < dailyProductionDetailRecordTMP.size(); i++) {
                if (lineList.contains(dailyProductionDetailRecordTMP.get(i).get("lineID"))) {
                    continue;
                }
                lineList.add(dailyProductionDetailRecordTMP.get(i).get("lineID").toString());
                productionListTotalNumber = 0;
                usedListTotalNumber = 0;
                if (lineRowProductionMap.containsKey(dailyProductionDetailRecordTMP.get(i).get("lineID"))) {
                    productionListTotalNumber = lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).size();
                }
                if (lineRowUsedMap.containsKey(dailyProductionDetailRecordTMP.get(i).get("lineID"))) {
                    usedListTotalNumber = lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).size();
                }
                //plantID,processID,lineID,materialID,materialName,productionNumber,usedMaterialID,usedMaterialName,usedNumber,scrapNumber,weightNumber,classType,teamType,dayTime
                for (int j = 0; ; j++) {
                    if (productionListTotalNumber < (j * productionListNumber + 1) && usedListTotalNumber < (j * usedListNumber + 1)) {
                        break;
                    }
                    Map<Object, Object> recordMap = new HashMap<>();
                    recordMap.put("plantID", dailyProductionDetailRecordTMP.get(i).get("plantID"));
                    recordMap.put("processID", dailyProductionDetailRecordTMP.get(i).get("processID"));
                    recordMap.put("lineID", dailyProductionDetailRecordTMP.get(i).get("lineID"));
                    recordMap.put("classType", dailyProductionDetailRecordTMP.get(i).get("classType"));
                    recordMap.put("teamType", dailyProductionDetailRecordTMP.get(i).get("teamType"));
                    recordMap.put("dayTime", dailyProductionDetailRecordTMP.get(i).get("dayTime"));
                    if (productionListTotalNumber >= (j + 1) * productionListNumber) {
                        recordMap.put("materialID", lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * productionListNumber));
                        recordMap.put("materialName", lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * productionListNumber + 1));
                        recordMap.put("productionNumber", lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * productionListNumber + 2));
                        recordMap.put("productionTransition1", lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * productionListNumber + 3));  // 大片报废数
                        recordMap.put("productionTransition2", 0);  // 和膏锅数
                    }
                    if (usedListTotalNumber >= (j + 1) * usedListNumber) {
                        recordMap.put("usedMaterialID", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * usedListNumber));
                        recordMap.put("usedMaterialName", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * usedListNumber + 1));
                        recordMap.put("usedNumber", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * usedListNumber + 2));
                        recordMap.put("scrapNumber", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * usedListNumber + 3));
                        recordMap.put("weightNumber", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * usedListNumber + 4));
                        recordMap.put("usedNumberTransition1", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * usedListNumber + 5));
                    }
                    if (recordMap.containsKey("usedMaterialID") || recordMap.containsKey("materialID")) {
                        finalDailyProductionDetailRecordTMP.add(recordMap);
                    }
                }
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(finalDailyProductionDetailRecordTMP));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getTMPDailyProductionSummaryRecord(String plantID, String processID, String dayTime, String classType) {

        TNPYResponse result = new TNPYResponse();
        try {
            String orderString = "";
            String startTime = "";
            String endTime = "";
            String lastDay = "";
            String lastClassType = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            boolean blGrantInfo = false;
            String grantRecordList = "";
            Date date = dateFormat.parse(dayTime);//取时间

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);

            if ("白班".equals(classType)) {
                lastClassType = "夜班";
                orderString = "'%BB" + dayTime.replaceAll("-", "") + "'";
                startTime = dayTime + " 07:00";
                endTime = dayTime + " 19:00";
                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                lastDay = dateFormat.format(date);
            } else {
                lastClassType = "白班";
                lastDay = dayTime;
                orderString = "'%YB" + dayTime.replaceAll("-", "") + "'";
                startTime = dayTime + " 19:00";
                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                endTime = dateFormat.format(date) + " 07:00";
            }

            if (ConfigParamEnum.BasicProcessEnum.GHProcessID.getName().equals(processID)) {
                List<Map<Object, Object>> dailyProductionSummaryRecordTMP = dailyProductionDetailRecordMapper.getSolidifyTMPDailyProductionSummaryRecord(plantID, processID, classType, dayTime, startTime, endTime);
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(dailyProductionSummaryRecordTMP));
                return result;
            }
            String teamType = dailyProductionDetailRecordMapper.getTeamType(plantID, processID, orderString);
            List<String> lastProcessID = objectRelationDictMapper.selectPreviousObjectID(processID, "1001");

            if (lastProcessID.size() < 1) {
                lastProcessID.add("-1");
            }
            List<Map<Object, Object>> dailyProductionSummaryRecordTMP;
            if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                dailyProductionSummaryRecordTMP = dailyProductionDetailRecordMapper.getZHQDTMPDailyProductionSummaryRecord(plantID, processID, orderString, dayTime, lastDay, lastClassType);
            } else if (ConfigParamEnum.BasicProcessEnum.CDProcessID.getName().equals(processID)) {
                dailyProductionSummaryRecordTMP = dailyProductionDetailRecordMapper.getCDTMPDailyProductionSummaryRecord(plantID, processID, orderString, dayTime,ConfigParamEnum.BasicProcessEnum.JSProcessID.getName());
            } else {
                dailyProductionSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyProductionSummaryRecord(plantID, processID, orderString, dayTime, lastDay, lastClassType);
            }
            List<Map<Object, Object>> dailyUsedInfoSummaryRecordTMP;
            if (ConfigParamEnum.BasicProcessEnum.CDProcessID.getName().equals(processID)) {
                dailyUsedInfoSummaryRecordTMP  = dailyProductionDetailRecordMapper.getTMPDailyUsedInfoSummaryRecord(plantID, ConfigParamEnum.BasicProcessEnum.JSProcessID.getName(), orderString);
            }
            else
            {
                dailyUsedInfoSummaryRecordTMP  = dailyProductionDetailRecordMapper.getTMPDailyUsedInfoSummaryRecord(plantID, processID, orderString);
            }


//            if (ConfigParamEnum.BasicProcessEnum.BBProcessID.getName().equals(processID)) {
//                dailyUsedInfoSummaryRecordTMP  = dailyProductionDetailRecordMapper.getTMPBBDailyUsedInfoSummaryRecord(plantID, processID, orderString);
//            }
//            else
//            {
//                dailyUsedInfoSummaryRecordTMP  = dailyProductionDetailRecordMapper.getTMPDailyUsedInfoSummaryRecord(plantID, processID, orderString);
//            }
            List<Map<Object, Object>> dailyScrapInfoSummaryRecordTMP;
            if (ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID)) {
                dailyScrapInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getJZTMPDailyScrapInfoSummaryRecord(plantID, processID, startTime, endTime);
            } else {
                dailyScrapInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyScrapInfoSummaryRecord(plantID, processID, dayTime, classType);
            }

            List<Map<Object, Object>> dailyRecieveInfoSummaryRecordTMP;
            if (ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID)) {
                dailyRecieveInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyRecieveInfoSummaryRecord(plantID, processID, startTime, endTime);
            } else   if (ConfigParamEnum.BasicProcessEnum.FBProcessID.getName().equals(processID)) {
                dailyRecieveInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getFBTMPDailyRecieveInfoSummaryRecord(plantID, ConfigParamEnum.BasicProcessEnum.TBProcessID.getName(), startTime, endTime);
            }  else   if (ConfigParamEnum.BasicProcessEnum.CDProcessID.getName().equals(processID)) {
                dailyRecieveInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyRecieveInfoSummaryRecord(plantID, ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName(), startTime.split(" ")[0], startTime.split(" ")[0] + " 23:59");
            } else {
                dailyRecieveInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyRecieveInfoSummaryRecord(plantID, lastProcessID.get(0), startTime, endTime);
            }

            List<Map<Object, Object>> dailyGrantInfoSummaryRecordTMP;
            if (ConfigParamEnum.BasicProcessEnum.CDProcessID.getName().equals(processID)) {
                dailyGrantInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getCDTMPDailyGrantInfoSummaryRecord(plantID, processID, startTime.split(" ")[0], endTime.split(" ")[0] + " 23:59");
            }else  if (ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName().equals(processID)) {
                if ("白班".equals(classType))
                {
                    dailyGrantInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyGrantInfoSummaryRecord(plantID, processID, startTime.split(" ")[0], startTime.split(" ")[0] + " 23:59");
                }
                else
                {
                    dailyGrantInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyGrantInfoSummaryRecord(plantID, processID, endTime, startTime);
                }
            }else {
                dailyGrantInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyGrantInfoSummaryRecord(plantID, processID, startTime, endTime);
            }
            List<Integer> attendanceInfo = new ArrayList<>();
            if (ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID)) {
                attendanceInfo = dailyProductionDetailRecordMapper.getGWTMPDailyAttendanceSummaryRecord(plantID, processID, dayTime, classType);
            } else if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                attendanceInfo = dailyProductionDetailRecordMapper.getGWTMPDailyAttendanceSummaryRecord(plantID, processID, dayTime, classType);
            } else {
                attendanceInfo = dailyProductionDetailRecordMapper.getTMPDailyAttendanceSummaryRecord(plantID, processID, dayTime, classType);
            }
            List<Map<Object, Object>> finalDailyProductionSummaryRecordTMP = new ArrayList<>();
            DecimalFormat dataFormat = new DecimalFormat(".00");
            int currentInventory = 0;
            if (ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID)) {
                List<Map<Object, Object>> TBScrapInfo = dailyProductionDetailRecordMapper.getTMPDailyScrapInfoSummaryRecord(plantID, ConfigParamEnum.BasicProcessEnum.TBProcessID.getName(), dayTime, classType);

                List<String> materialName = new ArrayList<>();
                for (int i = 0; ; i++) {
                    if (i >= dailyProductionSummaryRecordTMP.size() && i >= dailyUsedInfoSummaryRecordTMP.size() && i >= dailyScrapInfoSummaryRecordTMP.size() && i >= dailyRecieveInfoSummaryRecordTMP.size() && i >= dailyGrantInfoSummaryRecordTMP.size()) {
                        break;
                    }
                    if (dailyProductionSummaryRecordTMP.size() > i && !materialName.contains(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyGrantInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyScrapInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyRecieveInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"));
                    }

                }
                for(int i =0;i<materialName.size(); )
                {
                    if(materialName.get(i).contains("null"))
                    {
                        materialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }

                materialName.sort(Comparator.comparing(String::trim));
                for (int i = 0; i < materialName.size(); i++) {
                    currentInventory = 0;
                    Map<Object, Object> recordMap = new HashMap<>();
                    recordMap.put("plantID", plantID);
                    recordMap.put("processID", processID);
                    recordMap.put("classType", classType);
                    recordMap.put("teamType", teamType);
                    recordMap.put("dayTime", dayTime);
                    recordMap.put("attendanceNumber", attendanceInfo.get(0));
                    recordMap.put("machineNumber", attendanceInfo.get(1));
                    recordMap.put("actualMachineNumber", attendanceInfo.get(2));
                    recordMap.put("productionMachineRatio", dataFormat.format(attendanceInfo.get(2) * 1.0 / attendanceInfo.get(1) * 100));
                    blGrantInfo = false;

                    for (int j = 0; j < dailyProductionSummaryRecordTMP.size(); j++) {
                        if (materialName.get(i).contains(dailyProductionSummaryRecordTMP.get(j).get("materialID").toString())) {
                            recordMap.put("productionMaterialID", dailyProductionSummaryRecordTMP.get(j).get("materialID"));
                            recordMap.put("productionMaterialName", dailyProductionSummaryRecordTMP.get(j).get("materialName"));
                            recordMap.put("productionNumber", dailyProductionSummaryRecordTMP.get(j).get("productionNumber"));
                            recordMap.put("planDailyProduction", dailyProductionSummaryRecordTMP.get(j).get("planDailyProduction"));
                            recordMap.put("ratioFinish", dailyProductionSummaryRecordTMP.get(j).get("ratioFinish"));
                            recordMap.put("lastInventory", dailyProductionSummaryRecordTMP.get(j).get("lastInventory"));
                            blGrantInfo = true;
                            break;
                        }
                    }
                    if (!blGrantInfo) {
                        recordMap.put("productionMaterialID", materialName.get(i).split("___")[1]);
                        recordMap.put("productionMaterialName", materialName.get(i).split("___")[0]);
                        recordMap.put("productionNumber", 0);
                        recordMap.put("planDailyProduction", 0);
                        recordMap.put("ratioFinish", 0);
                        recordMap.put("lastInventory", 0);
                    }

                     blGrantInfo = false;

                    for (int j = 0; j < dailyGrantInfoSummaryRecordTMP.size(); j++) {
                        if (materialName.get(i).contains(dailyGrantInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                            currentInventory = currentInventory - Double.valueOf(dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumber").toString()).intValue();
                            recordMap.put("grantMaterialID", dailyGrantInfoSummaryRecordTMP.get(j).get("materialID"));
                            recordMap.put("grantMaterialName", dailyGrantInfoSummaryRecordTMP.get(j).get("materialName"));
                            recordMap.put("grantNumber", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumber"));
                            recordMap.put("grantNumberTransition1", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition1"));
                            recordMap.put("grantNumberTransition2", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition2"));
                            recordMap.put("grantNumberTransition3", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition3"));

                            blGrantInfo = true;
                            break;
                        }
                    }
                    if (!blGrantInfo) {
                        recordMap.put("grantMaterialID", materialName.get(i).split("___")[1]);
                        recordMap.put("grantMaterialName", materialName.get(i).split("___")[0]);
                        recordMap.put("grantNumber", 0);
                        recordMap.put("grantNumberTransition1", 0);
                        recordMap.put("grantNumberTransition2", 0);
                        recordMap.put("grantNumberTransition3", 0);
                    }
                    currentInventory = Double.valueOf(recordMap.get("lastInventory").toString()).intValue() + Double.valueOf(recordMap.get("productionNumber").toString()).intValue()
                            - Double.valueOf(recordMap.get("grantNumber").toString()).intValue();
                    recordMap.put("currentInventory", currentInventory);
                    recordMap.put("inventoryTransition1", currentInventory);
                   blGrantInfo = false;
                    for (int j = 0; j < dailyScrapInfoSummaryRecordTMP.size(); j++) {
                        if (materialName.get(i).contains(dailyScrapInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                            recordMap.put("scrapMaterialID", dailyScrapInfoSummaryRecordTMP.get(j).get("materialID"));
                            recordMap.put("scrapMaterialName", dailyScrapInfoSummaryRecordTMP.get(j).get("materialName"));
                            recordMap.put("scrapNumber", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumber"));
                            recordMap.put("scrapNumberTransition1", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumberTransition1"));
                            recordMap.put("scrapNumberTransition2", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumberTransition2"));
                            recordMap.put("scrapNumberTransition3", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumberTransition3"));
                            blGrantInfo = true;
                            break;
                        }
                    }

                    if (!blGrantInfo) {
                        recordMap.put("scrapMaterialID", materialName.get(i).split("___")[1]);
                        recordMap.put("scrapMaterialName", materialName.get(i).split("___")[0]);
                        recordMap.put("scrapNumber", 0);
                        recordMap.put("scrapNumberTransition1", 0);
                        recordMap.put("scrapNumberTransition2", 0);
                        recordMap.put("scrapNumberTransition3", 0);
                    }
                    blGrantInfo = false;
                    for (int j = 0; j < TBScrapInfo.size(); j++) {
                        if (materialName.get(i).contains(TBScrapInfo.get(j).get("materialID").toString())) {
                            recordMap.put("weightNumber", TBScrapInfo.get(j).get("scrapNumber"));
                            blGrantInfo = true;
                            break;
                        }
                    }
                    if (!blGrantInfo) {
                        recordMap.put("weightNumber", 0);
                    }
                    blGrantInfo = false;
                    for (int j = 0; j < dailyRecieveInfoSummaryRecordTMP.size(); j++) {
                        if (materialName.get(i).contains(dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                            recordMap.put("receiveMaterialID", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID"));
                            recordMap.put("receiveMaterialName", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialName"));
                            recordMap.put("receiveNumber", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveNumber"));
                            recordMap.put("receiveMaterialNumber1", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber1"));
                            recordMap.put("receiveMaterialNumber2", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber2"));
                            recordMap.put("receiveMaterialNumber3", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber3"));
                            blGrantInfo = true;
                            break;
                        }
                    }
                    if (!blGrantInfo) {
                        recordMap.put("receiveMaterialID", materialName.get(i).split("___")[1]);
                        recordMap.put("receiveMaterialName", materialName.get(i).split("___")[0]);
                        recordMap.put("receiveNumber", 0);
                        recordMap.put("receiveMaterialNumber1", 0);
                        recordMap.put("receiveMaterialNumber2", 0);
                        recordMap.put("receiveMaterialNumber3", 0);
                    }


                    recordMap.put("usedNumber", Double.valueOf(recordMap.get("scrapNumber").toString()).intValue()
                            + Double.valueOf(recordMap.get("weightNumber").toString()).intValue());   // 合计不良
                    recordMap.put("usedNumberTransition1", Double.valueOf(recordMap.get("productionNumber").toString()).intValue()
                            - Double.valueOf(recordMap.get("usedNumber").toString()).intValue());    // 上报产量
                    recordMap.put("usedNumberTransition2",  Double.valueOf(recordMap.get("grantNumber").toString()).intValue()
                            - Double.valueOf(recordMap.get("usedNumber").toString()).intValue());  // 上报发料
                    finalDailyProductionSummaryRecordTMP.add(recordMap);
                }
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(finalDailyProductionSummaryRecordTMP));
                return result;
            }

            if (ConfigParamEnum.BasicProcessEnum.TBProcessID.getName().equals(processID)) {
                List<String> materialName = new ArrayList<>();
                List<String> TBMaterialName = new ArrayList<>();
                List<Map<Object, Object>> lastOnlineInventory = dailyProductionDetailRecordMapper.getTMPTBOnlineMterialSummaryRecord(plantID, processID, dayTime, lastClassType);

                List<Map<Object, Object>> materialReturnRecord = dailyProductionDetailRecordMapper.getTBTMPReturnMaterialSummaryRecord(plantID, processID, startTime, endTime);

                List<Map<Object, Object>> nextProcessReturn = dailyProductionDetailRecordMapper.getTBTMPReturnMaterialSummaryRecord(plantID,ConfigParamEnum.BasicProcessEnum.FBProcessID.getName(),startTime,endTime);
                for (int i = 0; i < dailyProductionSummaryRecordTMP.size(); i++) {
                    if (i >= dailyProductionSummaryRecordTMP.size() && i >= nextProcessReturn.size() ) {
                        break;
                    }
                    if (dailyProductionSummaryRecordTMP.size() > i && !TBMaterialName.contains(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"))) {
                        TBMaterialName.add(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (nextProcessReturn.size() > i && !TBMaterialName.contains(nextProcessReturn.get(i).get("materialName") + "___" + nextProcessReturn.get(i).get("materialID"))) {
                        TBMaterialName.add(nextProcessReturn.get(i).get("materialName") + "___" + nextProcessReturn.get(i).get("materialID"));
                    }
                }

                for (int i = 0; ; i++) {
                    if (i >= lastOnlineInventory.size() && i >= dailyUsedInfoSummaryRecordTMP.size() && i >= dailyScrapInfoSummaryRecordTMP.size()
                            && i >= materialReturnRecord.size() && i >= dailyRecieveInfoSummaryRecordTMP.size() && i >= dailyGrantInfoSummaryRecordTMP.size()) {
                        break;
                    }
                    if (lastOnlineInventory.size() > i && !materialName.contains(lastOnlineInventory.get(i).get("materialName") + "___" + lastOnlineInventory.get(i).get("materialID"))) {
                        materialName.add(lastOnlineInventory.get(i).get("materialName") + "___" + lastOnlineInventory.get(i).get("materialID"));
                    }
                    if (dailyGrantInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyScrapInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))
                            && !TBMaterialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyRecieveInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (materialReturnRecord.size() > i && !materialName.contains(materialReturnRecord.get(i).get("materialName") + "___" + materialReturnRecord.get(i).get("materialID"))) {
                        materialName.add(materialReturnRecord.get(i).get("materialName") + "___" + materialReturnRecord.get(i).get("materialID"));
                    }
                }

                for(int i =0;i<TBMaterialName.size(); )
                {
                    if(TBMaterialName.get(i).contains("null"))
                    {
                        TBMaterialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }
                TBMaterialName.sort(Comparator.comparing(String::trim));

                for(int i =0;i<materialName.size(); )
                {
                    if(materialName.get(i).contains("null"))
                    {
                        materialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }
                materialName.sort(Comparator.comparing(String::trim));
                for (int i = 0; ; i++) {
                    currentInventory = 0;
                    if (i >= TBMaterialName.size() && i >= materialName.size()) {
                        break;
                    }
                    Map<Object, Object> recordMap = new HashMap<>();
                    recordMap.put("plantID", plantID);
                    recordMap.put("processID", processID);
                    recordMap.put("classType", classType);
                    recordMap.put("teamType", teamType);
                    recordMap.put("dayTime", dayTime);
                    recordMap.put("attendanceNumber", attendanceInfo.get(0));
                    recordMap.put("machineNumber", attendanceInfo.get(1));
                    recordMap.put("actualMachineNumber", attendanceInfo.get(2));
                    recordMap.put("productionMachineRatio", dataFormat.format(attendanceInfo.get(2) * 1.0 / attendanceInfo.get(1) * 100));

                    if (i < TBMaterialName.size()) {

                        blGrantInfo = false;
                        for (int j = 0; j < dailyProductionSummaryRecordTMP.size(); j++) {
                            if (TBMaterialName.get(i).contains(dailyProductionSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("productionMaterialID", dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                                recordMap.put("productionMaterialName", dailyProductionSummaryRecordTMP.get(i).get("materialName"));
                                recordMap.put("productionNumber", dailyProductionSummaryRecordTMP.get(i).get("productionNumber"));
                                recordMap.put("planDailyProduction", dailyProductionSummaryRecordTMP.get(i).get("planDailyProduction"));
                                recordMap.put("ratioFinish", dailyProductionSummaryRecordTMP.get(i).get("ratioFinish"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("productionMaterialID", TBMaterialName.get(i).split("___")[1]);
                            recordMap.put("productionMaterialName", TBMaterialName.get(i).split("___")[0]);
                            recordMap.put("productionNumber", 0);
                            recordMap.put("planDailyProduction", 0);
                            recordMap.put("ratioFinish", 0);
                        }

                        blGrantInfo = false;
                        for (int j = 0; j < dailyScrapInfoSummaryRecordTMP.size(); j++) {
                            if (TBMaterialName.get(i).contains(dailyScrapInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                //作为本工序大片的报废
                                recordMap.put("productionTransition1", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("productionTransition1", 0);
                        }

                        blGrantInfo = false;
                        for (int j = 0; j < nextProcessReturn.size(); j++) {
                            if (TBMaterialName.get(i).contains(nextProcessReturn.get(j).get("materialID").toString())) {
                                //作为本工序大片的报废
                                recordMap.put("productionTransition2", nextProcessReturn.get(j).get("scrapNumber"));   //分板红冲
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("productionTransition2", 0);    // 作为红冲数量
                        }
                        recordMap.put("productionTransition3",Double.valueOf(recordMap.get("productionNumber").toString()).intValue() - Double.valueOf(recordMap.get("productionTransition2").toString()).intValue()  );    // 上报产量
                           //    recordMap.put("grantNumberTransition2",  Double.valueOf(recordMap.get("productionTransition3").toString()).intValue() + Double.valueOf(recordMap.get("grantNumberTransition1").toString()).intValue() );  // 上报投料

                    }
                    if (i < materialName.size()) {
                        blGrantInfo = false;
                        for (int j = 0; j < lastOnlineInventory.size(); j++) {
                            if (materialName.get(i).contains(lastOnlineInventory.get(j).get("materialID").toString())) {
                                if(lastOnlineInventory.get(j).get("currentInventory") == null)
                                {
                                    recordMap.put("lastInventory", 0);
                                }
                                else
                                {
                                    recordMap.put("lastInventory", lastOnlineInventory.get(j).get("currentInventory"));
                                }
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("lastInventory", 0);
                        }

                        blGrantInfo = false;
                        for (int j = 0; j < dailyRecieveInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("receiveMaterialID", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("receiveMaterialName", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("receiveNumber", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveNumber"));
                                recordMap.put("receiveMaterialNumber1", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber1"));
                                recordMap.put("receiveMaterialNumber2", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber2"));
                                recordMap.put("receiveMaterialNumber3", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber3"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("receiveMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("receiveMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("receiveNumber", 0);
                            recordMap.put("receiveMaterialNumber1", 0);
                            recordMap.put("receiveMaterialNumber2", 0);
                            recordMap.put("receiveMaterialNumber3", 0);
                        }
                        blGrantInfo = false;
                        for (int j = 0; j < dailyUsedInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyUsedInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("usedMaterialID", dailyUsedInfoSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("usedMaterialName", dailyUsedInfoSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("usedNumber", dailyUsedInfoSummaryRecordTMP.get(j).get("usedNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("usedMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("usedMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("usedNumber", 0);
                        }

                        currentInventory =  Double.valueOf(recordMap.get("lastInventory").toString()).intValue()
                                + Double.valueOf(recordMap.get("receiveNumber").toString()).intValue() - Double.valueOf(recordMap.get("usedNumber").toString()).intValue();
                        recordMap.put("currentInventory", currentInventory);
                        recordMap.put("inventoryTransition1", currentInventory);
                        blGrantInfo = false;
                        for (int j = 0; j < materialReturnRecord.size(); j++) {
                            if (materialName.get(i).contains(materialReturnRecord.get(j).get("materialID").toString())) {
                                recordMap.put("scrapMaterialID", materialReturnRecord.get(j).get("materialID"));
                                recordMap.put("scrapMaterialName", materialReturnRecord.get(j).get("materialName"));
                                recordMap.put("scrapNumber", materialReturnRecord.get(j).get("scrapNumber"));
                                recordMap.put("scrapNumberTransition1", materialReturnRecord.get(j).get("scrapNumberTransition1"));
                                recordMap.put("scrapNumberTransition2", materialReturnRecord.get(j).get("scrapNumberTransition2"));
                                recordMap.put("scrapNumberTransition3", materialReturnRecord.get(j).get("scrapNumberTransition3"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("scrapMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("scrapMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("scrapNumber", 0);           // 红冲板栅
                            recordMap.put("scrapNumberTransition1", 0);
                            recordMap.put("scrapNumberTransition2", 0);
                            recordMap.put("scrapNumberTransition3", 0);
                        }

                        blGrantInfo = false;
                        for (int j = 0; j < dailyScrapInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyScrapInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                //作为本工序自身造成的报废
                                recordMap.put("weightNumber", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumber"));   // 报废板栅
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("weightNumber", 0);
                        }

                        recordMap.put("reveiveType",  Double.valueOf(recordMap.get("receiveNumber").toString()).intValue() - Double.valueOf(recordMap.get("scrapNumber").toString()).intValue() -
                                Double.valueOf(recordMap.get("weightNumber").toString()).intValue());  // 上报领用
                        recordMap.put("grantNumberTransition1",  Double.valueOf(recordMap.get("scrapNumber").toString()).intValue() + Double.valueOf(recordMap.get("weightNumber").toString()).intValue());  // 合计总的不良

                    }
                    if(!recordMap.containsKey("currentInventory"))
                    {
                        recordMap.put("currentInventory", 0);
                        recordMap.put("inventoryTransition1", 0);
                    }
                    finalDailyProductionSummaryRecordTMP.add(recordMap);
                }
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(finalDailyProductionSummaryRecordTMP));
                return result;
            }
            if (ConfigParamEnum.BasicProcessEnum.FBProcessID.getName().equals(processID)) {

                List<String> materialName = new ArrayList<>();
                List<String> TBMaterialName = new ArrayList<>();
                List<Map<Object, Object>> lastOnlineInventory = dailyProductionDetailRecordMapper.getTMPFBOnlineMterialSummaryRecord(plantID, processID, dayTime, lastClassType);

               // List<Map<Object, Object>> materialGHRecord = dailyProductionDetailRecordMapper.getTMGHOutMaterialSummaryRecord(plantID, startTime, endTime);

                for (int i = 0; i < dailyProductionSummaryRecordTMP.size(); i++) {
                    if (i >= dailyProductionSummaryRecordTMP.size()   ) {
                        break;
                    }
                    if (dailyProductionSummaryRecordTMP.size() > i && !TBMaterialName.contains(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"))) {
                        TBMaterialName.add(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                    }
                }

                for (int i = 0; ; i++) {
                    if (i >= lastOnlineInventory.size() && i >= dailyUsedInfoSummaryRecordTMP.size() && i >= dailyScrapInfoSummaryRecordTMP.size()
                           && i >= dailyRecieveInfoSummaryRecordTMP.size() && i >= dailyGrantInfoSummaryRecordTMP.size()) {
                        break;
                    }
                    if (lastOnlineInventory.size() > i && !materialName.contains(lastOnlineInventory.get(i).get("materialName") + "___" + lastOnlineInventory.get(i).get("materialID"))) {
                        materialName.add(lastOnlineInventory.get(i).get("materialName") + "___" + lastOnlineInventory.get(i).get("materialID"));
                    }
                    if (dailyGrantInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyScrapInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))
                            && !TBMaterialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyRecieveInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"));
                    }

                }
                for(int i =0;i<TBMaterialName.size(); )
                {
                    if(TBMaterialName.get(i).contains("null"))
                    {
                        TBMaterialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }

                for(int i =0;i<materialName.size(); )
                {
                    if(materialName.get(i).contains("null"))
                    {
                        materialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }
                materialName.sort(Comparator.comparing(String::trim));

                for (int i = 0; ; i++) {

                    if (i >= TBMaterialName.size() && i >= materialName.size()) {
                        break;
                    }
                    Map<Object, Object> recordMap = new HashMap<>();
                    recordMap.put("plantID", plantID);
                    recordMap.put("processID", processID);
                    recordMap.put("classType", classType);
                    recordMap.put("teamType", teamType);
                    recordMap.put("dayTime", dayTime);
                    recordMap.put("attendanceNumber", attendanceInfo.get(0));
                    recordMap.put("machineNumber", attendanceInfo.get(1));
                    recordMap.put("actualMachineNumber", attendanceInfo.get(2));
                    recordMap.put("productionMachineRatio", dataFormat.format(attendanceInfo.get(2) * 1.0 / attendanceInfo.get(1) * 100));

                    if (i < TBMaterialName.size()) {
                        blGrantInfo = false;
                        for (int j = 0; j < dailyProductionSummaryRecordTMP.size(); j++) {
                            if (TBMaterialName.get(i).contains(dailyProductionSummaryRecordTMP.get(j).get("materialID").toString())) {

                                recordMap.put("productionMaterialID", dailyProductionSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("productionMaterialName", dailyProductionSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("productionNumber", dailyProductionSummaryRecordTMP.get(j).get("productionNumber"));
                                recordMap.put("planDailyProduction", dailyProductionSummaryRecordTMP.get(j).get("planDailyProduction"));
                                recordMap.put("ratioFinish", dailyProductionSummaryRecordTMP.get(j).get("ratioFinish"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("productionMaterialID", TBMaterialName.get(i).split("___")[1]);
                            recordMap.put("productionMaterialName", TBMaterialName.get(i).split("___")[0]);
                            recordMap.put("productionNumber", 0);
                            recordMap.put("planDailyProduction", 0);
                            recordMap.put("ratioFinish", 0);
                        }

                        blGrantInfo = false;
                        for (int j = 0; j < dailyScrapInfoSummaryRecordTMP.size(); j++) {
                            if (TBMaterialName.get(i).contains(dailyScrapInfoSummaryRecordTMP.get(j).get("materialID").toString())) {

                                recordMap.put("scrapMaterialID", dailyScrapInfoSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("scrapMaterialName", dailyScrapInfoSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("scrapNumber", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumber"));
                                recordMap.put("weightNumber", dailyScrapInfoSummaryRecordTMP.get(j).get("weightNumber"));

                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("scrapMaterialID", TBMaterialName.get(i).split("___")[1]);
                            recordMap.put("scrapMaterialName", TBMaterialName.get(i).split("___")[0]);
                            recordMap.put("scrapNumber", 0);
                            recordMap.put("weightNumber", 0);
                        }

                    }

                    if (i < materialName.size()) {
                        blGrantInfo = false;

                        for (int j = 0; j < lastOnlineInventory.size(); j++) {
                            if (lastOnlineInventory.get(j).get("materialID") != null && materialName.get(i).contains(lastOnlineInventory.get(j).get("materialID").toString())) {
                                if(lastOnlineInventory.get(j).get("currentInventory") == null)
                                {
                                    recordMap.put("reveiveType", 0);
                                }
                                else
                                {
                                    recordMap.put("reveiveType", lastOnlineInventory.get(j).get("currentInventory"));   // 上次大片结余
                                }
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("reveiveType", 0);
                        }

                        blGrantInfo = false;
                        for (int j = 0; j < dailyRecieveInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("receiveMaterialID", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("receiveMaterialName", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("receiveNumber", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveNumber"));
                                recordMap.put("receiveMaterialNumber1", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber1"));
                                recordMap.put("receiveMaterialNumber2", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber2"));
                                recordMap.put("receiveMaterialNumber3", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber3"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("receiveMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("receiveMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("receiveNumber", 0);
                            recordMap.put("receiveMaterialNumber1", 0);
                            recordMap.put("receiveMaterialNumber2", 0);
                            recordMap.put("receiveMaterialNumber3", 0);
                        }

                        blGrantInfo = false;
                        for (int j = 0; j < dailyUsedInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyUsedInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("usedNumber", dailyUsedInfoSummaryRecordTMP.get(j).get("usedNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("usedNumber", 0);
                        }
                        currentInventory = Double.valueOf(recordMap.get("reveiveType").toString()).intValue()
                                + Double.valueOf(recordMap.get("receiveNumber").toString()).intValue() - Double.valueOf(recordMap.get("usedNumber").toString()).intValue();
                        recordMap.put("usedNumberTransition1", currentInventory);
                        recordMap.put("usedNumberTransition2", currentInventory);

//                        blGrantInfo = false;
//                        for (int j = 0; j < dailyScrapInfoSummaryRecordTMP.size(); j++) {
//                            if (materialName.get(i).contains(dailyScrapInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
//                                //作为本工序自身造成的报废
//                                recordMap.put("weightNumber", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumber"));
//                                blGrantInfo = true;
//                                break;
//                            }
//                        }
//                        if (!blGrantInfo) {
//                            recordMap.put("weightNumber", 0);
//                        }

                    }
                    if(!recordMap.containsKey("currentInventory"))
                    {
                        recordMap.put("currentInventory", 0);
                        recordMap.put("inventoryTransition1", 0);
                    }
                    if(!recordMap.containsKey("usedNumberTransition2"))
                    {
                        recordMap.put("reveiveType", 0);
                        recordMap.put("usedNumberTransition1", 0);
                        recordMap.put("usedNumberTransition2", 0);
                    }
                    finalDailyProductionSummaryRecordTMP.add(recordMap);
                }

                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(finalDailyProductionSummaryRecordTMP));
                return result;
            }
            if (ConfigParamEnum.BasicProcessEnum.BBProcessID.getName().equals(processID)) {

                List<String> materialName = new ArrayList<>();
                List<String> TBMaterialName = new ArrayList<>();
                List<Map<Object, Object>> materialUsedDetailInfo = dailyProductionDetailRecordMapper.getTMPBBDailyUsedInfoSummaryRecord(plantID, processID, orderString);

                for (int i = 0; i < dailyProductionSummaryRecordTMP.size(); i++) {
                    if (i >= dailyProductionSummaryRecordTMP.size() && i >= dailyGrantInfoSummaryRecordTMP.size()  ) {
                        break;
                    }
                    if (dailyProductionSummaryRecordTMP.size() > i && !TBMaterialName.contains(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"))) {
                        TBMaterialName.add(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                    }

                    if (dailyGrantInfoSummaryRecordTMP.size() > i && !TBMaterialName.contains(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        TBMaterialName.add(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                }

                for (int i = 0; ; i++) {
                    if ( i >= dailyUsedInfoSummaryRecordTMP.size() && i >= dailyScrapInfoSummaryRecordTMP.size()
                            && i >= dailyRecieveInfoSummaryRecordTMP.size() ) {
                        break;
                    }
                    if (dailyUsedInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyUsedInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyUsedInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyUsedInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyUsedInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyScrapInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))
                            && !TBMaterialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyRecieveInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"));
                    }

                }
                for(int i =0;i<TBMaterialName.size(); )
                {
                    if(TBMaterialName.get(i).contains("null"))
                    {
                        TBMaterialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }
                for(int i =0;i<materialName.size(); )
                {
                    if(materialName.get(i).contains("null"))
                    {
                        materialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }
                materialName.sort(Comparator.comparing(String::trim));

                for (int i = 0; ; i++) {

                    if (i >= TBMaterialName.size() && i >= materialName.size()) {
                        break;
                    }
                    Map<Object, Object> recordMap = new HashMap<>();
                    recordMap.put("plantID", plantID);
                    recordMap.put("processID", processID);
                    recordMap.put("classType", classType);
                    recordMap.put("teamType", teamType);
                    recordMap.put("dayTime", dayTime);
                    recordMap.put("attendanceNumber", attendanceInfo.get(0));
                    recordMap.put("machineNumber", attendanceInfo.get(1));
                    recordMap.put("actualMachineNumber", attendanceInfo.get(2));
                    recordMap.put("productionMachineRatio", dataFormat.format(attendanceInfo.get(2) * 1.0 / attendanceInfo.get(1) * 100));

                    if (i < TBMaterialName.size()) {
                        blGrantInfo = false;
                        for (int j = 0; j < dailyProductionSummaryRecordTMP.size(); j++) {
                            if (TBMaterialName.get(i).contains(dailyProductionSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("productionMaterialID", dailyProductionSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("productionMaterialName", dailyProductionSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("productionNumber", dailyProductionSummaryRecordTMP.get(j).get("productionNumber"));
                                recordMap.put("planDailyProduction", dailyProductionSummaryRecordTMP.get(j).get("planDailyProduction"));
                                recordMap.put("ratioFinish", dailyProductionSummaryRecordTMP.get(j).get("ratioFinish"));
                                recordMap.put("lastInventory", dailyProductionSummaryRecordTMP.get(j).get("lastInventory"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("productionMaterialID", TBMaterialName.get(i).split("___")[1]);
                            recordMap.put("productionMaterialName", TBMaterialName.get(i).split("___")[0]);
                            recordMap.put("productionNumber", 0);
                            recordMap.put("planDailyProduction", 0);
                            recordMap.put("ratioFinish", 0);
                            recordMap.put("lastInventory", 0);
                        }
                        blGrantInfo = false;
                        for (int j = 0; j < dailyGrantInfoSummaryRecordTMP.size(); j++) {
                            if (TBMaterialName.get(i).contains(dailyGrantInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("grantNumber", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumber"));
                                recordMap.put("grantNumberTransition1", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition1"));
                                recordMap.put("grantNumberTransition2", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition2"));
                                recordMap.put("grantNumberTransition3", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition3"));

                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("grantNumber", 0);
                            recordMap.put("grantNumberTransition1", 0);
                            recordMap.put("grantNumberTransition2", 0);
                            recordMap.put("grantNumberTransition3", 0);
                        }
                        currentInventory = Double.valueOf(recordMap.get("lastInventory").toString()).intValue()
                                + Double.valueOf(recordMap.get("productionNumber").toString()).intValue() - Double.valueOf(recordMap.get("grantNumber").toString()).intValue();
                        recordMap.put("currentInventory", currentInventory);
                        recordMap.put("inventoryTransition1", currentInventory);
                    }

                    if (i < materialName.size()) {

                        blGrantInfo = false;
                        for (int j = 0; j < dailyRecieveInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("receiveMaterialID", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("receiveMaterialName", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("receiveNumber", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveNumber"));
                                recordMap.put("receiveMaterialNumber1", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber1"));
                                recordMap.put("receiveMaterialNumber2", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber2"));
                                recordMap.put("receiveMaterialNumber3", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber3"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("receiveMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("receiveMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("receiveNumber", 0);
                            recordMap.put("receiveMaterialNumber1", 0);
                            recordMap.put("receiveMaterialNumber2", 0);
                            recordMap.put("receiveMaterialNumber3", 0);
                        }
                        blGrantInfo = false;
                        for (int j = 0; j < dailyUsedInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyUsedInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("usedNumber", dailyUsedInfoSummaryRecordTMP.get(j).get("usedNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("usedNumber", 0);
                        }
                        blGrantInfo = false;
                        for (int j = 0; j < dailyScrapInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyScrapInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("scrapNumber", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumber"));
                                recordMap.put("weightNumber", dailyScrapInfoSummaryRecordTMP.get(j).get("weightNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("scrapNumber", 0);
                            recordMap.put("weightNumber", 0);
                        }
                    }
                    if(!recordMap.containsKey("currentInventory"))
                    {
                        recordMap.put("currentInventory", 0);
                        recordMap.put("inventoryTransition1", 0);
                    }
                    finalDailyProductionSummaryRecordTMP.add(recordMap);
                }

                for(int i =0;i<TBMaterialName.size();i++)
                {

                    for(int j =0;j<materialUsedDetailInfo.size();j++)
                    {
                        if (TBMaterialName.get(i).contains(materialUsedDetailInfo.get(j).get("inputMaterialID").toString())) {
                            Map<Object, Object> recordMap = new HashMap<>();
                            recordMap.put("plantID", plantID);
                            recordMap.put("processID", processID);
                            recordMap.put("classType", classType);
                            recordMap.put("teamType", teamType);
                            recordMap.put("dayTime", dayTime);
                            recordMap.put("productionMaterialID", TBMaterialName.get(i).split("___")[1]);
                            recordMap.put("productionMaterialName", TBMaterialName.get(i).split("___")[0]);
                            recordMap.put("receiveMaterialID", materialUsedDetailInfo.get(j).get("materialID"));
                            recordMap.put("receiveMaterialName", materialUsedDetailInfo.get(j).get("materialName"));
                            recordMap.put("reveiveType", "BBYLMX");   // 包板用料明细

                            recordMap.put("usedNumber",  materialUsedDetailInfo.get(j).get("usedNumber"));
                            recordMap.put("currentInventory", 0);
                            recordMap.put("inventoryTransition1", 0);
                            finalDailyProductionSummaryRecordTMP.add(recordMap);
                        }
                    }
                }

                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(finalDailyProductionSummaryRecordTMP));
                return result;
            }
            if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {

                List<String> materialName = new ArrayList<>();
                List<String> TBMaterialName = new ArrayList<>();
                List<Map<Object, Object>> lastOnlineInventory = dailyProductionDetailRecordMapper.getTMPTBOnlineMterialSummaryRecord(plantID, processID, dayTime, lastClassType);

                List<Map<Object, Object>> materialReturnRecord = dailyProductionDetailRecordMapper.getTBTMPReturnMaterialSummaryRecord(plantID, processID, startTime, endTime);

                for (int i = 0; i < dailyProductionSummaryRecordTMP.size(); i++) {
                    TBMaterialName.add(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                }
                for (int i = 0; ; i++) {
                    if (i >= lastOnlineInventory.size() && i >= dailyUsedInfoSummaryRecordTMP.size() && i >= dailyScrapInfoSummaryRecordTMP.size()
                            && i >= materialReturnRecord.size() && i >= dailyRecieveInfoSummaryRecordTMP.size() && i >= dailyGrantInfoSummaryRecordTMP.size()) {
                        break;
                    }
                    if (lastOnlineInventory.size() > i && !materialName.contains(lastOnlineInventory.get(i).get("materialName") + "___" + lastOnlineInventory.get(i).get("materialID"))) {
                        materialName.add(lastOnlineInventory.get(i).get("materialName") + "___" + lastOnlineInventory.get(i).get("materialID"));
                    }
                    if (dailyGrantInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyScrapInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))
                            && !TBMaterialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyRecieveInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (materialReturnRecord.size() > i && !materialName.contains(materialReturnRecord.get(i).get("materialName") + "___" + materialReturnRecord.get(i).get("materialID"))) {
                        materialName.add(materialReturnRecord.get(i).get("materialName") + "___" + materialReturnRecord.get(i).get("materialID"));
                    }
                }
                for(int i =0;i<TBMaterialName.size(); )
                {
                    if(TBMaterialName.get(i).contains("null"))
                    {
                        TBMaterialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }

                for(int i =0;i<materialName.size(); )
                {
                    if(materialName.get(i).contains("null"))
                    {
                        materialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }
                materialName.sort(Comparator.comparing(String::trim));

                for (int i = 0; ; i++) {
                    currentInventory = 0;
                    if (i >= TBMaterialName.size() && i >= materialName.size()) {
                        break;
                    }
                    Map<Object, Object> recordMap = new HashMap<>();
                    recordMap.put("plantID", plantID);
                    recordMap.put("processID", processID);
                    recordMap.put("classType", classType);
                    recordMap.put("teamType", teamType);
                    recordMap.put("dayTime", dayTime);
                    recordMap.put("attendanceNumber", attendanceInfo.get(0));
                    recordMap.put("machineNumber", attendanceInfo.get(1));
                    recordMap.put("actualMachineNumber", attendanceInfo.get(2));
                    recordMap.put("productionMachineRatio", dataFormat.format(attendanceInfo.get(2) * 1.0 / attendanceInfo.get(1) * 100));

                    if (i < dailyProductionSummaryRecordTMP.size()) {
                        recordMap.put("productionMaterialID", dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                        recordMap.put("productionMaterialName", dailyProductionSummaryRecordTMP.get(i).get("materialName"));
                        recordMap.put("productionNumber", dailyProductionSummaryRecordTMP.get(i).get("productionNumber"));
                        recordMap.put("planDailyProduction", dailyProductionSummaryRecordTMP.get(i).get("planDailyProduction"));
                        recordMap.put("ratioFinish", dailyProductionSummaryRecordTMP.get(i).get("ratioFinish"));
                        blGrantInfo = false;
                        for (int j = 0; j < dailyScrapInfoSummaryRecordTMP.size(); j++) {
                            if (dailyScrapInfoSummaryRecordTMP.get(j).get("materialID") != null && TBMaterialName.get(i).contains(dailyScrapInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("productionTransition1", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("productionTransition1", 0);
                        }
                    }

                    if (i < materialName.size()) {
                        blGrantInfo = false;

                        for (int j = 0; j < lastOnlineInventory.size(); j++) {
                            if (lastOnlineInventory.get(j).get("materialID") != null && materialName.get(i).contains(lastOnlineInventory.get(j).get("materialID").toString())) {
                                if(lastOnlineInventory.get(j).get("currentInventory") == null)
                                {
                                    recordMap.put("lastInventory", 0);
                                }
                                else
                                {
                                    recordMap.put("lastInventory", lastOnlineInventory.get(j).get("currentInventory"));
                                }
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("lastInventory", 0);
                        }

                        blGrantInfo = false;
                        for (int j = 0; j < dailyRecieveInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("receiveMaterialID", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("receiveMaterialName", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("receiveNumber", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveNumber"));
                                recordMap.put("receiveMaterialNumber1", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber1"));
                                recordMap.put("receiveMaterialNumber2", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber2"));
                                recordMap.put("receiveMaterialNumber3", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber3"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("receiveMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("receiveMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("receiveNumber", 0);
                            recordMap.put("receiveMaterialNumber1", 0);
                            recordMap.put("receiveMaterialNumber2", 0);
                            recordMap.put("receiveMaterialNumber3", 0);
                        }


                        blGrantInfo = false;
                        for (int j = 0; j < dailyUsedInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyUsedInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("usedMaterialID", dailyUsedInfoSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("usedMaterialName", dailyUsedInfoSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("usedNumber", dailyUsedInfoSummaryRecordTMP.get(j).get("usedNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("usedMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("usedMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("usedNumber", 0);
                        }
                        currentInventory = Double.valueOf(recordMap.get("lastInventory").toString()).intValue()
                                + Double.valueOf(recordMap.get("receiveNumber").toString()).intValue() - Double.valueOf(recordMap.get("usedNumber").toString()).intValue();
                        recordMap.put("currentInventory", currentInventory);
                        recordMap.put("inventoryTransition1", currentInventory);
//                        blGrantInfo = false;
//                        for (int j = 0; j < materialReturnRecord.size(); j++) {
//                            if (materialName.get(i).contains(materialReturnRecord.get(j).get("materialID").toString())) {
//                                recordMap.put("scrapMaterialID", materialReturnRecord.get(j).get("materialID"));
//                                recordMap.put("scrapMaterialName", materialReturnRecord.get(j).get("materialName"));
//                                recordMap.put("scrapNumber", materialReturnRecord.get(j).get("scrapNumber"));
//                                recordMap.put("scrapNumberTransition1", materialReturnRecord.get(j).get("scrapNumberTransition1"));
//                                recordMap.put("scrapNumberTransition2", materialReturnRecord.get(j).get("scrapNumberTransition2"));
//                                recordMap.put("scrapNumberTransition3", materialReturnRecord.get(j).get("scrapNumberTransition3"));
//                                blGrantInfo = true;
//                                break;
//                            }
//                        }
//                        if (!blGrantInfo) {
//                            recordMap.put("scrapMaterialID", materialName.get(i).split("___")[1]);
//                            recordMap.put("scrapMaterialName", materialName.get(i).split("___")[0]);
//                            recordMap.put("scrapNumber", 0);
//                            recordMap.put("scrapNumberTransition1", 0);
//                            recordMap.put("scrapNumberTransition2", 0);
//                            recordMap.put("scrapNumberTransition3", 0);
//                        }

                        blGrantInfo = false;
                        for (int j = 0; j < dailyScrapInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyScrapInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                //作为本工序自身造成的报废
                                recordMap.put("weightNumber", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("weightNumber", 0);
                        }

                    }
                    if(!recordMap.containsKey("currentInventory"))
                    {
                        recordMap.put("currentInventory", 0);
                        recordMap.put("inventoryTransition1", 0);
                    }

                    finalDailyProductionSummaryRecordTMP.add(recordMap);
                }

                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(finalDailyProductionSummaryRecordTMP));
                return result;
            }
            if (ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName().equals(processID)) {
                List<String> materialName = new ArrayList<>();
                List<Map<Object, Object>> lastOnlineInventory = dailyProductionDetailRecordMapper.getTMPZPXTOnlineMterialSummaryRecord(plantID, processID, startTime, endTime);

                List<Map<Object, Object>> materialReturnRecord = dailyProductionDetailRecordMapper.getJZTMPDailyScrapInfoSummaryRecord(plantID, processID, startTime, endTime);

                for (int i = 0; ; i++) {
                    if (i >= lastOnlineInventory.size() && i >= dailyUsedInfoSummaryRecordTMP.size() && i >= dailyScrapInfoSummaryRecordTMP.size()
                            && i >= materialReturnRecord.size() && i >= dailyRecieveInfoSummaryRecordTMP.size() && i >= dailyGrantInfoSummaryRecordTMP.size()
                    && i>=dailyProductionSummaryRecordTMP.size() ) {
                        break;
                    }
                    if (dailyProductionSummaryRecordTMP.size() > i && !materialName.contains(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                    }

                    if (lastOnlineInventory.size() > i && !materialName.contains(lastOnlineInventory.get(i).get("materialName") + "___" + lastOnlineInventory.get(i).get("materialID"))) {
                        materialName.add(lastOnlineInventory.get(i).get("materialName") + "___" + lastOnlineInventory.get(i).get("materialID"));
                    }
                    if (dailyGrantInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyScrapInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (dailyRecieveInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"));
                    }
                    if (materialReturnRecord.size() > i && !materialName.contains(materialReturnRecord.get(i).get("materialName") + "___" + materialReturnRecord.get(i).get("materialID"))) {
                        materialName.add(materialReturnRecord.get(i).get("materialName") + "___" + materialReturnRecord.get(i).get("materialID"));
                    }
                }
                materialName.sort(Comparator.comparing(String::trim));
                for (int i = 0; ; i++) {
                    //System.out.println(i + "=====" + TBMaterialName.get(i) + "===" + materialName.get(i));
                    if ( i >= materialName.size()) {
                        break;
                    }
                    Map<Object, Object> recordMap = new HashMap<>();
                    recordMap.put("plantID", plantID);
                    recordMap.put("processID", processID);
                    recordMap.put("classType", classType);
                    recordMap.put("teamType", teamType);
                    recordMap.put("dayTime", dayTime);
                    recordMap.put("attendanceNumber", attendanceInfo.get(0));
                    recordMap.put("machineNumber", attendanceInfo.get(1));
                    recordMap.put("actualMachineNumber", attendanceInfo.get(2));
                    recordMap.put("productionMachineRatio", dataFormat.format(attendanceInfo.get(2) * 1.0 / attendanceInfo.get(1) * 100));
                    blGrantInfo = false;
                    if (i < materialName.size()) {
                        for (int j = 0; j < dailyProductionSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyProductionSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("productionMaterialID", dailyProductionSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("productionMaterialName", dailyProductionSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("productionNumber", dailyProductionSummaryRecordTMP.get(j).get("productionNumber"));
                                recordMap.put("planDailyProduction", dailyProductionSummaryRecordTMP.get(j).get("planDailyProduction"));
                                recordMap.put("ratioFinish", dailyProductionSummaryRecordTMP.get(j).get("ratioFinish"));
                                recordMap.put("lastInventory",dailyProductionSummaryRecordTMP.get(j).get("lastInventory"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("productionMaterialID",  materialName.get(i).split("___")[1]);
                            recordMap.put("productionMaterialName",  materialName.get(i).split("___")[0]);
                            recordMap.put("productionNumber", 0);
                            recordMap.put("planDailyProduction", 0);
                            recordMap.put("ratioFinish", 0);
                            recordMap.put("lastInventory", 0);
                        }
                        blGrantInfo = false;
                        for (int j = 0; j < dailyScrapInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyScrapInfoSummaryRecordTMP.get(j).get("materialID").toString())) {

                                recordMap.put("weightNumber", dailyScrapInfoSummaryRecordTMP.get(j).get("scrapNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("weightNumber", 0);
                        }
                        for (int j = 0; j < dailyGrantInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyGrantInfoSummaryRecordTMP.get(j).get("materialID").toString())) {

                                recordMap.put("grantMaterialID", dailyGrantInfoSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("grantMaterialName", dailyGrantInfoSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("grantNumber", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumber"));
                                recordMap.put("grantNumberTransition1", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition1"));
                                recordMap.put("grantNumberTransition2", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition2"));
                                recordMap.put("grantNumberTransition3", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition3"));

                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("grantMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("grantMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("grantNumber", 0);
                            recordMap.put("grantNumberTransition1", 0);
                            recordMap.put("grantNumberTransition2", 0);
                            recordMap.put("grantNumberTransition3", 0);
                        }
                         currentInventory = Double.valueOf(recordMap.get("lastInventory").toString()).intValue() + Double.valueOf(recordMap.get("productionNumber").toString()).intValue()
                                - Double.valueOf(recordMap.get("grantNumber").toString()).intValue();
                        recordMap.put("currentInventory", currentInventory);
                        recordMap.put("inventoryTransition1", currentInventory);
                        blGrantInfo = false;
                        for (int j = 0; j < materialReturnRecord.size(); j++) {
                            if (materialName.get(i).contains(materialReturnRecord.get(j).get("materialID").toString())) {
                                recordMap.put("receiveMaterialID", materialReturnRecord.get(j).get("materialID"));
                                recordMap.put("receiveMaterialName", materialReturnRecord.get(j).get("materialName"));
                                recordMap.put("receiveNumber", materialReturnRecord.get(j).get("scrapNumber"));
                                recordMap.put("receiveMaterialNumber1", materialReturnRecord.get(j).get("scrapNumberTransition1"));
                                recordMap.put("receiveMaterialNumber2", materialReturnRecord.get(j).get("scrapNumberTransition2"));
                                recordMap.put("receiveMaterialNumber3", materialReturnRecord.get(j).get("scrapNumberTransition3"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("receiveMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("receiveMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("receiveNumber", 0);
                            recordMap.put("receiveMaterialNumber1", 0);
                            recordMap.put("receiveMaterialNumber2", 0);
                            recordMap.put("receiveMaterialNumber3", 0);
                        }
                        blGrantInfo = false;
                        for (int j = 0; j < lastOnlineInventory.size(); j++) {
                            if (materialName.get(i).contains(lastOnlineInventory.get(j).get("materialID").toString())) {
                                recordMap.put("usedMaterialID", materialName.get(i).split("___")[1]);
                                recordMap.put("usedMaterialName", materialName.get(i).split("___")[0]);
                                recordMap.put("usedNumber", lastOnlineInventory.get(j).get("reinputNum"));
                                recordMap.put("usedNumberTransition1", lastOnlineInventory.get(j).get("newReturn"));
                                recordMap.put("usedNumberTransition2", lastOnlineInventory.get(j).get("onlineNum"));
                                recordMap.put("reveiveType", lastOnlineInventory.get(j).get("onlineNum"));  //保存修改后的线边仓数量

                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("usedMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("usedMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("usedNumber", 0);
                            recordMap.put("usedNumberTransition1", 0);
                            recordMap.put("usedNumberTransition2", 0);
                            recordMap.put("reveiveType", 0);  //保存修改后的线边仓数量
                        }

                    }
                    if(!recordMap.containsKey("currentInventory"))
                    {
                        recordMap.put("currentInventory", 0);
                        recordMap.put("inventoryTransition1", 0);
                    }
                    if(!recordMap.containsKey("reveiveType"))
                    {
                        recordMap.put("reveiveType", 0);
                    }
                    finalDailyProductionSummaryRecordTMP.add(recordMap);
                }
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(finalDailyProductionSummaryRecordTMP));
                return result;
            }
            if (ConfigParamEnum.BasicProcessEnum.CDProcessID.getName().equals(processID)) {

                List<String> materialName = new ArrayList<>();
                List<String> TBMaterialName = new ArrayList<>();
                List<Map<Object, Object>> JSUsedMaterialScrap = dailyProductionDetailRecordMapper.getTMPDailyScrapInfoSummaryRecord(plantID, ConfigParamEnum.BasicProcessEnum.JSProcessID.getName(), dayTime, classType);
                List<Map<Object, Object>> JSMaterialInventory = dailyProductionDetailRecordMapper.getTMPJSSecondInventoryRecord(plantID,processID, lastDay, "白班");

                for (int i = 0;  ; i++) {
                    if (i >= dailyProductionSummaryRecordTMP.size() && i >= dailyScrapInfoSummaryRecordTMP.size() && i >= dailyGrantInfoSummaryRecordTMP.size()  ) {
                        break;
                    }
                    if (dailyProductionSummaryRecordTMP.size() > i && !TBMaterialName.contains(dailyProductionSummaryRecordTMP.get(i).get("materialType") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialName")
                            + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"))) {
                        TBMaterialName.add(dailyProductionSummaryRecordTMP.get(i).get("materialType") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialName") + "___" + dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                    }

                    if (dailyGrantInfoSummaryRecordTMP.size() > i && !TBMaterialName.contains(dailyGrantInfoSummaryRecordTMP.get(i).get("materialType") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialName")
                            + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        TBMaterialName.add(dailyGrantInfoSummaryRecordTMP.get(i).get("materialType") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"));
                    }

//                    if (dailyScrapInfoSummaryRecordTMP.size() > i && !TBMaterialName.contains(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"))) {
//                        TBMaterialName.add(dailyScrapInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"));
//                    }
                }

                for (int i = 0; ; i++) {
                    if ( i >= dailyUsedInfoSummaryRecordTMP.size()
                            && i >= dailyRecieveInfoSummaryRecordTMP.size()  && i >= JSUsedMaterialScrap.size() && i >= JSMaterialInventory.size()  ) {
                        break;
                    }
                    if (dailyUsedInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyUsedInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyUsedInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyUsedInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyUsedInfoSummaryRecordTMP.get(i).get("materialID"));
                    }

                    if (dailyRecieveInfoSummaryRecordTMP.size() > i && !materialName.contains(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"))) {
                        materialName.add(dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName") + "___" + dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"));
                    }

                    if (JSUsedMaterialScrap.size() > i && !materialName.contains(JSUsedMaterialScrap.get(i).get("materialName") + "___" + JSUsedMaterialScrap.get(i).get("materialID"))) {
                        materialName.add(JSUsedMaterialScrap.get(i).get("materialName") + "___" + JSUsedMaterialScrap.get(i).get("materialID"));
                    }
                    if (JSMaterialInventory.size() > i && !materialName.contains(JSMaterialInventory.get(i).get("materialName") + "___" + JSMaterialInventory.get(i).get("materialID"))) {
                        materialName.add(JSMaterialInventory.get(i).get("materialName") + "___" + JSMaterialInventory.get(i).get("materialID"));
                    }
                }
                for(int i =0;i<TBMaterialName.size(); )
                {
                    if(TBMaterialName.get(i).contains("null"))
                    {
                        TBMaterialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }

                TBMaterialName.sort(Comparator.comparing(String::trim));
                for(int i =0;i<materialName.size(); )
                {
                    if(materialName.get(i).contains("null"))
                    {
                        materialName.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }
                materialName.sort(Comparator.comparing(String::trim));

                for (int i = 0; ; i++) {

                    if (i >= TBMaterialName.size() && i >= materialName.size()) {
                        break;
                    }
                    Map<Object, Object> recordMap = new HashMap<>();
                    recordMap.put("plantID", plantID);
                    recordMap.put("processID", processID);
                    recordMap.put("classType", classType);
                    recordMap.put("teamType", teamType);
                    recordMap.put("dayTime", dayTime);
                    recordMap.put("attendanceNumber", attendanceInfo.get(0));
                    recordMap.put("machineNumber", attendanceInfo.get(1));
                    recordMap.put("actualMachineNumber", attendanceInfo.get(2));
                    recordMap.put("productionMachineRatio", dataFormat.format(attendanceInfo.get(2) * 1.0 / attendanceInfo.get(1) * 100));

                    if (i < TBMaterialName.size()) {
                        blGrantInfo = false;
                        for (int j = 0; j < dailyProductionSummaryRecordTMP.size(); j++) {
                            if (TBMaterialName.get(i).contains(dailyProductionSummaryRecordTMP.get(j).get("materialType") + "___" + dailyProductionSummaryRecordTMP.get(j).get("materialName")
                                    + "___" + dailyProductionSummaryRecordTMP.get(j).get("materialID"))) {
                                recordMap.put("productionMaterialID", dailyProductionSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("productionMaterialName", dailyProductionSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("productionTransition1", dailyProductionSummaryRecordTMP.get(j).get("materialType"));
                                recordMap.put("productionNumber", dailyProductionSummaryRecordTMP.get(j).get("productionNumber"));
                                recordMap.put("planDailyProduction", dailyProductionSummaryRecordTMP.get(j).get("planDailyProduction"));
                                recordMap.put("ratioFinish", dailyProductionSummaryRecordTMP.get(j).get("ratioFinish"));

                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("productionMaterialID", TBMaterialName.get(i).split("___")[2]);
                            recordMap.put("productionMaterialName", TBMaterialName.get(i).split("___")[1]);
                            recordMap.put("productionTransition1",TBMaterialName.get(i).split("___")[0]);
                            recordMap.put("productionNumber", 0);
                            recordMap.put("planDailyProduction", 0);
                            recordMap.put("ratioFinish", 0);

                        }
                        blGrantInfo = false;
                        for (int j = 0; j < dailyGrantInfoSummaryRecordTMP.size(); j++) {
                            if (TBMaterialName.get(i).contains(dailyGrantInfoSummaryRecordTMP.get(j).get("materialType") + "___" + dailyGrantInfoSummaryRecordTMP.get(j).get("materialName")
                                    + "___" + dailyGrantInfoSummaryRecordTMP.get(j).get("materialID"))) {
                                recordMap.put("grantNumber", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("grantNumber", 0);
                        }
                    }

                    if (i < materialName.size()) {

                        blGrantInfo = false;
                        for (int j = 0; j < dailyRecieveInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("receiveMaterialID", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialID"));
                                recordMap.put("receiveMaterialName", dailyRecieveInfoSummaryRecordTMP.get(j).get("materialName"));
                                recordMap.put("receiveNumber", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveNumber"));
                                recordMap.put("receiveMaterialNumber1", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber1"));
                                recordMap.put("receiveMaterialNumber2", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber2"));
                                recordMap.put("receiveMaterialNumber3", dailyRecieveInfoSummaryRecordTMP.get(j).get("receiveMaterialNumber3"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("receiveMaterialID", materialName.get(i).split("___")[1]);
                            recordMap.put("receiveMaterialName", materialName.get(i).split("___")[0]);
                            recordMap.put("receiveNumber", 0);
                            recordMap.put("receiveMaterialNumber1", 0);
                            recordMap.put("receiveMaterialNumber2", 0);
                            recordMap.put("receiveMaterialNumber3", 0);
                        }
                        blGrantInfo = false;
                        for (int j = 0; j < dailyUsedInfoSummaryRecordTMP.size(); j++) {
                            if (materialName.get(i).contains(dailyUsedInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                                recordMap.put("usedNumber", dailyUsedInfoSummaryRecordTMP.get(j).get("usedNumber"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("usedNumber", 0);
                        }
                        blGrantInfo = false;
                        for (int j = 0; j < JSUsedMaterialScrap.size(); j++) {
                            if (materialName.get(i).contains(JSUsedMaterialScrap.get(j).get("materialID").toString())) {
                                recordMap.put("scrapNumber", JSUsedMaterialScrap.get(j).get("scrapNumber"));

                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("scrapNumber", 0);
                        }

                        blGrantInfo = false;
                        for (int j = 0; j < JSMaterialInventory.size(); j++) {
                            if (materialName.get(i).contains(JSMaterialInventory.get(j).get("materialID").toString())) {
                                recordMap.put("lastInventory", JSMaterialInventory.get(j).get("currentInventory"));
                                blGrantInfo = true;
                                break;
                            }
                        }
                        if (!blGrantInfo) {
                            recordMap.put("lastInventory", 0);
                        }

                        currentInventory = Double.valueOf(recordMap.get("lastInventory").toString()).intValue()
                                + Double.valueOf(recordMap.get("receiveNumber").toString()).intValue() - Double.valueOf(recordMap.get("usedNumber").toString()).intValue();
                        recordMap.put("currentInventory", currentInventory);
                        recordMap.put("inventoryTransition1", currentInventory);

                    }
                    if(!recordMap.containsKey("currentInventory"))
                    {
                        recordMap.put("currentInventory", 0);
                        recordMap.put("inventoryTransition1", 0);
                    }
                    finalDailyProductionSummaryRecordTMP.add(recordMap);
                }


                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSONString(finalDailyProductionSummaryRecordTMP));
                return result;
            }
            for (int i = 0; ; i++) {
                if (i >= dailyProductionSummaryRecordTMP.size() && i >= dailyUsedInfoSummaryRecordTMP.size() && i >= dailyScrapInfoSummaryRecordTMP.size() && i >= dailyRecieveInfoSummaryRecordTMP.size() && i >= dailyGrantInfoSummaryRecordTMP.size()) {

                    break;
                }
                Map<Object, Object> recordMap = new HashMap<>();
                recordMap.put("plantID", plantID);
                recordMap.put("processID", processID);
                recordMap.put("classType", classType);
                recordMap.put("teamType", teamType);
                recordMap.put("dayTime", dayTime);
                recordMap.put("attendanceNumber", attendanceInfo.get(0));
                recordMap.put("machineNumber", attendanceInfo.get(1));
                recordMap.put("actualMachineNumber", attendanceInfo.get(2));
                recordMap.put("productionMachineRatio", dataFormat.format(attendanceInfo.get(2) * 1.0 / attendanceInfo.get(1) * 100));

                if (i < dailyProductionSummaryRecordTMP.size()) {
                    blGrantInfo = false;
                    recordMap.put("productionMaterialID", dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                    recordMap.put("productionMaterialName", dailyProductionSummaryRecordTMP.get(i).get("materialName"));
                    recordMap.put("productionNumber", dailyProductionSummaryRecordTMP.get(i).get("productionNumber"));
                    recordMap.put("planDailyProduction", dailyProductionSummaryRecordTMP.get(i).get("planDailyProduction"));
                    recordMap.put("ratioFinish", dailyProductionSummaryRecordTMP.get(i).get("ratioFinish"));

                    recordMap.put("lastInventory", dailyProductionSummaryRecordTMP.get(i).get("lastInventory"));
                    currentInventory = Double.valueOf(dailyProductionSummaryRecordTMP.get(i).get("lastInventory").toString()).intValue()
                            + Double.valueOf(dailyProductionSummaryRecordTMP.get(i).get("productionNumber").toString()).intValue();
                    for (int j = 0; j < dailyGrantInfoSummaryRecordTMP.size(); j++) {
                        if (dailyGrantInfoSummaryRecordTMP.get(j).get("materialID").equals(dailyProductionSummaryRecordTMP.get(i).get("materialID"))) {
                            currentInventory = currentInventory - Double.valueOf(dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumber").toString()).intValue();
                            recordMap.put("grantMaterialID", dailyGrantInfoSummaryRecordTMP.get(j).get("materialID"));
                            recordMap.put("grantMaterialName", dailyGrantInfoSummaryRecordTMP.get(j).get("materialName"));
                            recordMap.put("grantNumber", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumber"));
                            recordMap.put("grantNumberTransition1", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition1"));
                            recordMap.put("grantNumberTransition2", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition2"));
                            recordMap.put("grantNumberTransition3", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition3"));
                            grantRecordList += " " + dailyGrantInfoSummaryRecordTMP.get(j).get("materialID");
                            blGrantInfo = true;
                            break;
                        }
                    }
                    if (!blGrantInfo) {
                        recordMap.put("grantMaterialID", dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                        recordMap.put("grantMaterialName", dailyProductionSummaryRecordTMP.get(i).get("materialName"));
                        recordMap.put("grantNumber", 0);
                        recordMap.put("grantNumberTransition1", 0);
                        recordMap.put("grantNumberTransition2", 0);
                        recordMap.put("grantNumberTransition3", 0);
                    }
                    recordMap.put("currentInventory", currentInventory);
                    recordMap.put("inventoryTransition1", currentInventory);
                }

                if (i < dailyUsedInfoSummaryRecordTMP.size()) {
                    recordMap.put("usedMaterialID", dailyUsedInfoSummaryRecordTMP.get(i).get("materialID"));
                    recordMap.put("usedMaterialName", dailyUsedInfoSummaryRecordTMP.get(i).get("materialName"));
                    recordMap.put("usedNumber", dailyUsedInfoSummaryRecordTMP.get(i).get("usedNumber"));

                }

                if (i < dailyScrapInfoSummaryRecordTMP.size()) {
                    recordMap.put("scrapMaterialID", dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"));
                    recordMap.put("scrapMaterialName", dailyScrapInfoSummaryRecordTMP.get(i).get("materialName"));
                    recordMap.put("scrapNumber", dailyScrapInfoSummaryRecordTMP.get(i).get("scrapNumber"));
                    recordMap.put("weightNumber", dailyScrapInfoSummaryRecordTMP.get(i).get("weightNumber"));
                    recordMap.put("scrapNumberTransition1", dailyScrapInfoSummaryRecordTMP.get(i).get("scrapNumberTransition1"));
                    recordMap.put("scrapNumberTransition2", dailyScrapInfoSummaryRecordTMP.get(i).get("scrapNumberTransition2"));
                    recordMap.put("scrapNumberTransition3", dailyScrapInfoSummaryRecordTMP.get(i).get("scrapNumberTransition3"));

                }

                if (i < dailyRecieveInfoSummaryRecordTMP.size()) {
                    recordMap.put("receiveMaterialID", dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"));
                    recordMap.put("receiveMaterialName", dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName"));
                    recordMap.put("receiveNumber", dailyRecieveInfoSummaryRecordTMP.get(i).get("receiveNumber"));
                    recordMap.put("receiveMaterialNumber1", dailyRecieveInfoSummaryRecordTMP.get(i).get("receiveMaterialNumber1"));
                    recordMap.put("receiveMaterialNumber2", dailyRecieveInfoSummaryRecordTMP.get(i).get("receiveMaterialNumber2"));
                    recordMap.put("receiveMaterialNumber3", dailyRecieveInfoSummaryRecordTMP.get(i).get("receiveMaterialNumber3"));
                }
                if (!recordMap.containsKey("grantMaterialID")) {
                    for (int j = 0; j < dailyGrantInfoSummaryRecordTMP.size(); j++) {
                        if (grantRecordList.contains(dailyGrantInfoSummaryRecordTMP.get(j).get("materialID").toString())) {
                            continue;
                        }
                        grantRecordList += " " + dailyGrantInfoSummaryRecordTMP.get(j).get("materialID");
                        recordMap.put("grantMaterialID", dailyGrantInfoSummaryRecordTMP.get(j).get("materialID"));
                        recordMap.put("grantMaterialName", dailyGrantInfoSummaryRecordTMP.get(j).get("materialName"));
                        recordMap.put("grantNumber", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumber"));
                        recordMap.put("grantNumberTransition1", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition1"));
                        recordMap.put("grantNumberTransition2", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition2"));
                        recordMap.put("grantNumberTransition3", dailyGrantInfoSummaryRecordTMP.get(j).get("grantNumberTransition3"));
                        break;
                    }
                }
                if(!recordMap.containsKey("currentInventory"))
                {
                    recordMap.put("currentInventory", 0);
                    recordMap.put("inventoryTransition1", 0);
                }
                finalDailyProductionSummaryRecordTMP.add(recordMap);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(finalDailyProductionSummaryRecordTMP));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse saveDailyLineProductionDetailRecord(String jsonStr) {

        TNPYResponse result = new TNPYResponse();
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeNow = dateFormat.format(new Date());
            List<DailyLineProductionDetailRecord> recordList = JSON.parseArray(jsonStr, DailyLineProductionDetailRecord.class);
            if (recordList.size() > 0) {

                if (dailyLineProductionDetailRecordMapper.selectConfirmNumber(recordList.get(0).getPlantid(), recordList.get(0).getProcessid(), recordList.get(0).getDaytime(), recordList.get(0).getClasstype()) > 0) {
                    result.setMessage("该工序已确认！");
                    return result;
                }
            } else {
                result.setMessage("传入数据为空！");
                return result;
            }
            for (int i = 0; i < recordList.size(); i++) {
                recordList.get(i).setStatus("1");
                recordList.get(i).setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                recordList.get(i).setExtend1(recordList.get(0).getExtend1());
                recordList.get(i).setExtend2(timeNow);
                dailyLineProductionDetailRecordMapper.insertSelective(recordList.get(i));
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("确认成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse saveDailyProcessProductionDetailRecord(String jsonStr) {
        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeNow = dateFormat.format(new Date());
            List<DailyProcessProductionDetailRecord> recordList = JSON.parseArray(jsonStr, DailyProcessProductionDetailRecord.class);
            if (recordList.size() > 0) {
                if (dailyProcessProductionDetailRecordMapper.selectConfirmNumber(recordList.get(0).getPlantid(), recordList.get(0).getProcessid(), recordList.get(0).getDaytime(), recordList.get(0).getClasstype()) > 0) {
                    result.setMessage("该工序已确认！");
                    return result;
                }
            } else {
                result.setMessage("传入数据为空！");
                return result;
            }
            for (int i = 0; i < recordList.size(); i++) {
                recordList.get(i).setStatus("1");
                recordList.get(i).setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                recordList.get(i).setExtend1(recordList.get(0).getExtend1());
                recordList.get(i).setExtend2(timeNow);
                dailyProcessProductionDetailRecordMapper.insertSelective(recordList.get(i));
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("确认成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getDailyLineProductionDetailRecord(String plantID, String processID, String dayTime, String classType) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> dailyLineProductionDetailRecord;
                    if("-1".equals(classType))
                    {
                        if(ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID))
                        {
                            dailyLineProductionDetailRecord  = dailyLineProductionDetailRecordMapper.getDailyLineProductionDetailRecord(plantID, processID, dayTime, classType);
                        }
                        else if(ConfigParamEnum.BasicProcessEnum.TBProcessID.getName().equals(processID))
                        {
                            dailyLineProductionDetailRecord  = dailyLineProductionDetailRecordMapper.getDailyLineProductionDetailRecord(plantID, processID, dayTime, classType);
                        }
                        else {
                            dailyLineProductionDetailRecord  = dailyLineProductionDetailRecordMapper.getDailyLineProductionDetailRecord(plantID, processID, dayTime, classType);
                        }
                    }
                    else
                    {
                        dailyLineProductionDetailRecord  = dailyLineProductionDetailRecordMapper.getDailyLineProductionDetailRecord(plantID, processID, dayTime, classType);
                    }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(dailyLineProductionDetailRecord));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getDailyProcessProductionDetailRecord(String plantID, String processID, String dayTime, String classType) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> dailyProcessProductionDetailRecord ;
            if("-1".equals(classType))
            {
                if(ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID))
                {
                    dailyProcessProductionDetailRecord = dailyProcessProductionDetailRecordMapper.getJZDailyProcessProductionAllDayRecord(plantID, processID, dayTime, classType);
                }
               else if(ConfigParamEnum.BasicProcessEnum.TBProcessID.getName().equals(processID))
                {
                    dailyProcessProductionDetailRecord = new ArrayList<>();
                    List<Map<Object, Object>> dailyTBProductionList = dailyProcessProductionDetailRecordMapper.getTBDailyProductionAllDayRecord(plantID, processID, dayTime, classType);
                    List<Map<Object, Object>> dailyTBUsedInfoList = dailyProcessProductionDetailRecordMapper.getTBDailyUsedInfoAllDayRecord(plantID, processID, dayTime, classType);
                    for (int i = 0; ; i++) {

                        if (i >= dailyTBProductionList.size() && i >= dailyTBUsedInfoList.size()) {
                            break;
                        }
                        Map<Object, Object> recordMap = new HashMap<>();
                        recordMap.put("plantID", plantID);
                        recordMap.put("processID", processID);
                        recordMap.put("classType", classType);
                        recordMap.put("dayTime", dayTime);
                       if(i<dailyTBProductionList.size())
                       {
                           recordMap.put("productionMaterialID", dailyTBProductionList.get(i).get("productionMaterialID"));
                           recordMap.put("productionMaterialName", dailyTBProductionList.get(i).get("productionMaterialName"));
                           recordMap.put("productionNumber", dailyTBProductionList.get(i).get("productionNumber"));
                           recordMap.put("productionTransition1", dailyTBProductionList.get(i).get("productionTransition1"));
                           recordMap.put("planDailyProduction", dailyTBProductionList.get(i).get("planDailyProduction"));
                           recordMap.put("ratioFinish", dailyTBProductionList.get(i).get("ratioFinish"));
                       }
                       if(i<dailyTBUsedInfoList.size())
                       {
                           recordMap.put("receiveMaterialID", dailyTBUsedInfoList.get(i).get("receiveMaterialID"));
                           recordMap.put("receiveMaterialName", dailyTBUsedInfoList.get(i).get("receiveMaterialName"));
                           recordMap.put("currentInventory", dailyTBUsedInfoList.get(i).get("currentInventory"));
                           recordMap.put("lastInventory", dailyTBUsedInfoList.get(i).get("lastInventory"));
                           recordMap.put("inventoryTransition1", dailyTBUsedInfoList.get(i).get("inventoryTransition1"));
                           recordMap.put("receiveNumber", dailyTBUsedInfoList.get(i).get("receiveNumber"));
                           recordMap.put("receiveMaterialNumber1", dailyTBUsedInfoList.get(i).get("receiveMaterialNumber1"));
                           recordMap.put("receiveMaterialNumber2", dailyTBUsedInfoList.get(i).get("receiveMaterialNumber2"));
                           recordMap.put("receiveMaterialNumber3", dailyTBUsedInfoList.get(i).get("receiveMaterialNumber3"));
                           recordMap.put("scrapNumber", dailyTBUsedInfoList.get(i).get("scrapNumber"));
                           recordMap.put("scrapNumberTransition1", dailyTBUsedInfoList.get(i).get("scrapNumberTransition1"));
                           recordMap.put("scrapNumberTransition2", dailyTBUsedInfoList.get(i).get("scrapNumberTransition2"));
                           recordMap.put("scrapNumberTransition3", dailyTBUsedInfoList.get(i).get("scrapNumberTransition3"));
                           recordMap.put("usedNumber", dailyTBUsedInfoList.get(i).get("usedNumber"));
                           recordMap.put("weightNumber", dailyTBUsedInfoList.get(i).get("weightNumber"));
                       }
                        dailyProcessProductionDetailRecord.add(recordMap);
                    }
                }else if(ConfigParamEnum.BasicProcessEnum.FBProcessID.getName().equals(processID))
                {
                    dailyProcessProductionDetailRecord = new ArrayList<>();
                    List<Map<Object, Object>> dailyTBProductionList = dailyProcessProductionDetailRecordMapper.getFBDailyProductionAllDayRecord(plantID, processID, dayTime, classType);
                    List<Map<Object, Object>> dailyTBUsedInfoList = dailyProcessProductionDetailRecordMapper.getFBDailyUsedInfoAllDayRecord(plantID, processID, dayTime, classType);
                    for (int i = 0; ; i++) {

                        if (i >= dailyTBProductionList.size() && i >= dailyTBUsedInfoList.size()) {
                            break;
                        }
                        Map<Object, Object> recordMap = new HashMap<>();
                        recordMap.put("plantID", plantID);
                        recordMap.put("processID", processID);
                        recordMap.put("classType", classType);
                        recordMap.put("dayTime", dayTime);
                        if(i<dailyTBProductionList.size())
                        {
                            recordMap.put("productionMaterialID", dailyTBProductionList.get(i).get("productionMaterialID"));
                            recordMap.put("productionMaterialName", dailyTBProductionList.get(i).get("productionMaterialName"));
                            recordMap.put("productionNumber", dailyTBProductionList.get(i).get("productionNumber"));
                            recordMap.put("weightNumber", dailyTBProductionList.get(i).get("weightNumber"));
                            recordMap.put("scrapNumber", dailyTBProductionList.get(i).get("scrapNumber"));
                            recordMap.put("planDailyProduction", dailyTBProductionList.get(i).get("planDailyProduction"));
                            recordMap.put("ratioFinish", dailyTBProductionList.get(i).get("ratioFinish"));
                        }
                        if(i<dailyTBUsedInfoList.size())
                        {
                            recordMap.put("receiveMaterialID", dailyTBUsedInfoList.get(i).get("receiveMaterialID"));
                            recordMap.put("receiveMaterialName", dailyTBUsedInfoList.get(i).get("receiveMaterialName"));
                            recordMap.put("reveiveType", dailyTBUsedInfoList.get(i).get("reveiveType"));   // 上次结余
                            recordMap.put("usedNumberTransition1", dailyTBUsedInfoList.get(i).get("usedNumberTransition1"));  // 理论结余
                            recordMap.put("usedNumberTransition2", dailyTBUsedInfoList.get(i).get("usedNumberTransition2"));  // 实盘
                            recordMap.put("receiveNumber", dailyTBUsedInfoList.get(i).get("receiveNumber"));
                            recordMap.put("receiveMaterialNumber1", dailyTBUsedInfoList.get(i).get("receiveMaterialNumber1"));
                            recordMap.put("receiveMaterialNumber2", dailyTBUsedInfoList.get(i).get("receiveMaterialNumber2"));
                            recordMap.put("receiveMaterialNumber3", dailyTBUsedInfoList.get(i).get("receiveMaterialNumber3"));
                            recordMap.put("usedNumber", dailyTBUsedInfoList.get(i).get("usedNumber"));
                        }
                        dailyProcessProductionDetailRecord.add(recordMap);
                    }
                }else if(ConfigParamEnum.BasicProcessEnum.BBProcessID.getName().equals(processID))
                {
                    dailyProcessProductionDetailRecord = new ArrayList<>();
                    List<Map<Object, Object>> dailyTBProductionList = dailyProcessProductionDetailRecordMapper.getBBDailyProductionAllDayRecord(plantID, processID, dayTime, classType);
                    List<Map<Object, Object>> dailyTBUsedInfoList = dailyProcessProductionDetailRecordMapper.getBBDailyUsedInfoAllDayRecord(plantID, processID, dayTime, classType);
                    List<Map<Object, Object>> dailyBBUsedToInputInfoList = dailyProcessProductionDetailRecordMapper.getBBDailyUsedToInputAllDayRecord(plantID, processID, dayTime, classType);

                    for (int i = 0; ; i++) {

                        if (i >= dailyTBProductionList.size() && i >= dailyTBUsedInfoList.size()) {
                            break;
                        }
                        Map<Object, Object> recordMap = new HashMap<>();
                        recordMap.put("plantID", plantID);
                        recordMap.put("processID", processID);
                        recordMap.put("classType", classType);
                        recordMap.put("dayTime", dayTime);
                        if(i<dailyTBProductionList.size())
                        {
                            recordMap.put("productionMaterialID", dailyTBProductionList.get(i).get("productionMaterialID"));
                            recordMap.put("productionMaterialName", dailyTBProductionList.get(i).get("productionMaterialName"));
                            recordMap.put("productionNumber", dailyTBProductionList.get(i).get("productionNumber"));
                            recordMap.put("currentInventory", dailyTBProductionList.get(i).get("currentInventory"));
                            recordMap.put("lastInventory", dailyTBProductionList.get(i).get("lastInventory"));
                            recordMap.put("inventoryTransition1", dailyTBProductionList.get(i).get("inventoryTransition1"));
                            recordMap.put("planDailyProduction", dailyTBProductionList.get(i).get("planDailyProduction"));
                            recordMap.put("ratioFinish", dailyTBProductionList.get(i).get("ratioFinish"));

                            recordMap.put("grantNumber", dailyTBProductionList.get(i).get("grantNumber"));
                            recordMap.put("grantNumberTransition1", dailyTBProductionList.get(i).get("grantNumberTransition1"));
                            recordMap.put("grantNumberTransition2", dailyTBProductionList.get(i).get("grantNumberTransition2"));
                            recordMap.put("grantNumberTransition3", dailyTBProductionList.get(i).get("grantNumberTransition3"));

                        }
                        if(i<dailyTBUsedInfoList.size())
                        {
                            recordMap.put("receiveMaterialID", dailyTBUsedInfoList.get(i).get("receiveMaterialID"));
                            recordMap.put("receiveMaterialName", dailyTBUsedInfoList.get(i).get("receiveMaterialName"));
                            recordMap.put("weightNumber", dailyTBUsedInfoList.get(i).get("weightNumber"));   // 上次结余
                            recordMap.put("scrapNumber", dailyTBUsedInfoList.get(i).get("scrapNumber"));  // 理论结余
                            recordMap.put("usedNumber", dailyTBUsedInfoList.get(i).get("usedNumber"));
                        }
                        dailyProcessProductionDetailRecord.add(recordMap);
                    }

                    for(int i =0;i< dailyBBUsedToInputInfoList.size();i++)
                    {
                        Map<Object, Object> recordMap = new HashMap<>();
                        recordMap.put("plantID", plantID);
                        recordMap.put("processID", processID);
                        recordMap.put("classType", classType);
                        recordMap.put("dayTime", dayTime);

                        recordMap.put("productionMaterialID", dailyBBUsedToInputInfoList.get(i).get("productionMaterialID"));
                        recordMap.put("productionMaterialName", dailyBBUsedToInputInfoList.get(i).get("productionMaterialName"));
                        recordMap.put("receiveMaterialID", dailyBBUsedToInputInfoList.get(i).get("receiveMaterialID"));
                        recordMap.put("receiveMaterialName", dailyBBUsedToInputInfoList.get(i).get("receiveMaterialName"));
                        recordMap.put("usedNumber", dailyBBUsedToInputInfoList.get(i).get("usedNumber"));

                        dailyProcessProductionDetailRecord.add(recordMap);
                    }
                }else if(ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID))
                {
                    dailyProcessProductionDetailRecord = new ArrayList<>();
                    List<Map<Object, Object>> dailyTBProductionList = dailyProcessProductionDetailRecordMapper.getTBDailyProductionAllDayRecord(plantID, processID, dayTime, classType);
                    List<Map<Object, Object>> dailyTBUsedInfoList = dailyProcessProductionDetailRecordMapper.getTBDailyUsedInfoAllDayRecord(plantID, processID, dayTime, classType);
                    for (int i = 0; ; i++) {

                        if (i >= dailyTBProductionList.size() && i >= dailyTBUsedInfoList.size()) {
                            break;
                        }
                        Map<Object, Object> recordMap = new HashMap<>();
                        recordMap.put("plantID", plantID);
                        recordMap.put("processID", processID);
                        recordMap.put("classType", classType);
                        recordMap.put("dayTime", dayTime);
                        if(i<dailyTBProductionList.size())
                        {
                            recordMap.put("productionMaterialID", dailyTBProductionList.get(i).get("productionMaterialID"));
                            recordMap.put("productionMaterialName", dailyTBProductionList.get(i).get("productionMaterialName"));
                            recordMap.put("productionNumber", dailyTBProductionList.get(i).get("productionNumber"));
                            recordMap.put("productionTransition1", dailyTBProductionList.get(i).get("productionTransition1"));
                            recordMap.put("planDailyProduction", dailyTBProductionList.get(i).get("planDailyProduction"));
                            recordMap.put("ratioFinish", dailyTBProductionList.get(i).get("ratioFinish"));
                        }
                        if(i<dailyTBUsedInfoList.size())
                        {
                            recordMap.put("receiveMaterialID", dailyTBUsedInfoList.get(i).get("receiveMaterialID"));
                            recordMap.put("receiveMaterialName", dailyTBUsedInfoList.get(i).get("receiveMaterialName"));
                            recordMap.put("currentInventory", dailyTBUsedInfoList.get(i).get("currentInventory"));
                            recordMap.put("lastInventory", dailyTBUsedInfoList.get(i).get("lastInventory"));
                            recordMap.put("inventoryTransition1", dailyTBUsedInfoList.get(i).get("inventoryTransition1"));
                            recordMap.put("receiveNumber", dailyTBUsedInfoList.get(i).get("receiveNumber"));
                            recordMap.put("receiveMaterialNumber1", dailyTBUsedInfoList.get(i).get("receiveMaterialNumber1"));
                            recordMap.put("receiveMaterialNumber2", dailyTBUsedInfoList.get(i).get("receiveMaterialNumber2"));
                            recordMap.put("receiveMaterialNumber3", dailyTBUsedInfoList.get(i).get("receiveMaterialNumber3"));
                            recordMap.put("scrapNumber", dailyTBUsedInfoList.get(i).get("scrapNumber"));
                            recordMap.put("scrapNumberTransition1", dailyTBUsedInfoList.get(i).get("scrapNumberTransition1"));
                            recordMap.put("scrapNumberTransition2", dailyTBUsedInfoList.get(i).get("scrapNumberTransition2"));
                            recordMap.put("scrapNumberTransition3", dailyTBUsedInfoList.get(i).get("scrapNumberTransition3"));
                            recordMap.put("usedNumber", dailyTBUsedInfoList.get(i).get("usedNumber"));
                            recordMap.put("weightNumber", dailyTBUsedInfoList.get(i).get("weightNumber"));
                        }
                        dailyProcessProductionDetailRecord.add(recordMap);
                    }
                }else
                {
                    dailyProcessProductionDetailRecord = dailyProcessProductionDetailRecordMapper.getDailyProcessProductionDetailRecord(plantID, processID, dayTime, classType);
                }
            }
            else
            {
               if(ConfigParamEnum.BasicProcessEnum.BBProcessID.getName().equals(processID))
                {
                    dailyProcessProductionDetailRecord = dailyProcessProductionDetailRecordMapper.getBBDailyProcessProductionDetailRecord(plantID, processID, dayTime, classType);
                }
               else
               {
                   dailyProcessProductionDetailRecord = dailyProcessProductionDetailRecordMapper.getDailyProcessProductionDetailRecord(plantID, processID, dayTime, classType);
               }
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(dailyProcessProductionDetailRecord));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }


    public TNPYResponse getStaffAttendanceSummary(String plantID, String processID, String lineID, String startTime, String endTime, String teamType) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where dayTime >= '" + startTime + "'  and  dayTime <= '" + endTime + "'  and verifierName is not null  ";
            if (!"-1".equals(plantID)) {
                filter += " and plantID = '" + plantID + "' ";
            }
            if (!"-1".equals(processID)) {
                filter += " and processID = '" + processID + "' ";
            }
            if (!"-1".equals(lineID)) {
                filter += " and lineID = '" + lineID + "' ";
            }
            if (!"-1".equals(teamType)) {
                filter += " and extd2 = '" + teamType + "' ";
            }

            List<Map<Object, Object>> dailyAttendanceDetailRecord = staffAttendanceDetailMapper.getStaffAttendanceSummary(filter);

            List<Map<Object, Object>> staffAttendanceSummaryRecord = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDay = dateFormat.parse(startTime);

            Calendar calendar = new GregorianCalendar();

            String addedStaff = " ";
            String currentStaff = "";
            int attendanceNumber = 0;
            Map<Object, Object> staffAttendanceSummaryMap = new HashMap<>();
            staffAttendanceSummaryMap.put("staffName", "总计");
            for (; ; ) {
                if (dateFormat.format(startDay).compareTo(endTime) > 0) {
                    break;
                }
                staffAttendanceSummaryMap.put(dateFormat.format(startDay), 0);
                calendar.setTime(startDay);
                calendar.add(Calendar.DATE, 1);
                startDay = calendar.getTime();   //这个时间就是日期往后推一天的结果
            }
            staffAttendanceSummaryMap.put("totalAttendance", 0);

            for (int i = 0; i < dailyAttendanceDetailRecord.size(); ) {
                if (!addedStaff.contains(dailyAttendanceDetailRecord.get(i).get("staffName").toString())) {
                    addedStaff += " " + dailyAttendanceDetailRecord.get(i).get("staffName").toString();
                    currentStaff = dailyAttendanceDetailRecord.get(i).get("staffName").toString();
                    startDay = dateFormat.parse(startTime);
                    attendanceNumber = 0;
                    Map<Object, Object> staffAttendanceMap = new HashMap<>();
                    staffAttendanceMap.put("staffName", dailyAttendanceDetailRecord.get(i).get("staffName").toString());
                    for (; ; ) {
                        if (dateFormat.format(startDay).compareTo(endTime) > 0) {
                            break;
                        }
                        if (i < dailyAttendanceDetailRecord.size() && dateFormat.format(startDay).equals(dailyAttendanceDetailRecord.get(i).get("dayTime").toString()) && currentStaff.equals(dailyAttendanceDetailRecord.get(i).get("staffName").toString())) {
                            staffAttendanceMap.put(dateFormat.format(startDay), "1");
                            staffAttendanceSummaryMap.put(dateFormat.format(startDay), Integer.parseInt(staffAttendanceSummaryMap.get(dateFormat.format(startDay)).toString()) + 1);
                            attendanceNumber++;
                            i++;
                        } else {
                            staffAttendanceMap.put(dateFormat.format(startDay), "0");
                        }
                        calendar.setTime(startDay);
                        calendar.add(Calendar.DATE, 1);
                        startDay = calendar.getTime();   //这个时间就是日期往后推一天的结果
                    }
                    staffAttendanceMap.put("totalAttendance", attendanceNumber);
                    staffAttendanceSummaryMap.put("totalAttendance", Integer.parseInt(staffAttendanceSummaryMap.get("totalAttendance").toString()) + attendanceNumber);
                    staffAttendanceSummaryRecord.add(staffAttendanceMap);
                }
            }
            staffAttendanceSummaryRecord.add(staffAttendanceSummaryMap);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(staffAttendanceSummaryRecord));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}









