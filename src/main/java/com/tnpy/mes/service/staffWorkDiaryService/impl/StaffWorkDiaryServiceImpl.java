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
    public TNPYResponse insertStaffComeAttendanceInfo(String qrCode, String staffID, String staffName, String classType1, String classType2, String dayTime, String workContent) {
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
            result.setMessage("成功确认" + successNumber + "人产量信息，失败" + (recordList.size() - successNumber) + "人,原因为产量已确认，" + alreadyConfirmedName);
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
                System.out.println(wageDetailList);
                System.out.println(wageDetailList.size());
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
            if ("白班".equals(classType)) {
                orderString = "'%BB" + dayTime.replaceAll("-", "") + "'";
            } else {
                orderString = "'%YB" + dayTime.replaceAll("-", "") + "'";
            }
            String teamType = dailyProductionDetailRecordMapper.getTeamType(plantID, processID, orderString);
            List<Map<Object, Object>> dailyProductionDetailRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyProductionDetailRecord(plantID, processID, orderString, dayTime, classType, teamType);
            List<Map<Object, Object>> finalDailyProductionDetailRecordTMP = new ArrayList<>();
            Map<String, String> lineProductionMap = new HashMap<>();
            Map<String, String> lineUsedMap = new HashMap<>();
            Map<String, List<Object>> lineRowProductionMap = new HashMap<>();
            Map<String, List<Object>> lineRowUsedMap = new HashMap<>();


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
                        lineRowProductionMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineRowInfo);
                        lineProductionMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")) + " " + dailyProductionDetailRecordTMP.get(i).get("materialID"));
                    }
                } else {
                    lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("materialID"));
                    lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("materialName"));
                    lineRowInfo.add(dailyProductionDetailRecordTMP.get(i).get("productionNumber"));
                    lineRowProductionMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineRowInfo);
                    lineProductionMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), " " + dailyProductionDetailRecordTMP.get(i).get("materialID"));
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
                        lineRowUsedMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineRowUsedInfo);
                        lineUsedMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")) + " " + dailyProductionDetailRecordTMP.get(i).get("usedMaterialID"));
                    }
                } else {
                    lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedMaterialID"));
                    lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedMaterialName"));
                    lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("usedNumber"));
                    lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("scrapNumber"));
                    lineRowUsedInfo.add(dailyProductionDetailRecordTMP.get(i).get("weightNumber"));
                    lineRowUsedMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), lineRowUsedInfo);
                    lineUsedMap.put(String.valueOf(dailyProductionDetailRecordTMP.get(i).get("lineID")), " " + dailyProductionDetailRecordTMP.get(i).get("usedMaterialID"));
                }
            }
            List<String> lineList = new ArrayList<>();
            for (int i = 0; i < dailyProductionDetailRecordTMP.size(); i++) {
                if (lineList.contains(dailyProductionDetailRecordTMP.get(i).get("lineID"))) {
                    continue;
                }
                lineList.add(dailyProductionDetailRecordTMP.get(i).get("lineID").toString());
                //plantID,processID,lineID,materialID,materialName,productionNumber,usedMaterialID,usedMaterialName,usedNumber,scrapNumber,weightNumber,classType,teamType,dayTime

                for (int j = 0; ; j++) {
                    if (lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).size() < j * 3 + 1 && lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).size() < j * 5 + 1) {
                        break;
                    }
                    Map<Object, Object> recordMap = new HashMap<>();
                    recordMap.put("plantID", dailyProductionDetailRecordTMP.get(i).get("plantID"));
                    recordMap.put("processID", dailyProductionDetailRecordTMP.get(i).get("processID"));
                    recordMap.put("lineID", dailyProductionDetailRecordTMP.get(i).get("lineID"));
                    recordMap.put("classType", dailyProductionDetailRecordTMP.get(i).get("classType"));
                    recordMap.put("teamType", dailyProductionDetailRecordTMP.get(i).get("teamType"));
                    recordMap.put("dayTime", dailyProductionDetailRecordTMP.get(i).get("dayTime"));
                    if (lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).size() > j * 3) {
                        recordMap.put("materialID", lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * 3));
                        recordMap.put("materialName", lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * 3 + 1));
                        recordMap.put("productionNumber", lineRowProductionMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * 3 + 2));
                    }
                    if (lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).size() > j * 5) {
                        recordMap.put("usedMaterialID", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * 5));
                        recordMap.put("usedMaterialName", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * 5 + 1));
                        recordMap.put("usedNumber", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * 5 + 2));
                        recordMap.put("scrapNumber", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * 5 + 3));
                        recordMap.put("weightNumber", lineRowUsedMap.get(dailyProductionDetailRecordTMP.get(i).get("lineID")).get(j * 5 + 4));
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
            String teamType = dailyProductionDetailRecordMapper.getTeamType(plantID, processID, orderString);
            List<String> lastProcessID = objectRelationDictMapper.selectPreviousObjectID(processID, "1001");
            if (lastProcessID.size() < 1) {
                lastProcessID.add("-1");
            }
            List<Map<Object, Object>> dailyProductionSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyProductionSummaryRecord(plantID, processID, orderString, dayTime,lastDay,lastClassType);
            List<Map<Object, Object>> dailyUsedInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyUsedInfoSummaryRecord(plantID, processID, orderString);
            List<Map<Object, Object>> dailyScrapInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyScrapInfoSummaryRecord(plantID, processID, dayTime, classType);
            List<Map<Object, Object>> dailyRecieveInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyRecieveInfoSummaryRecord(plantID, lastProcessID.get(0), startTime, endTime);
            List<Map<Object, Object>> dailyGrantInfoSummaryRecordTMP = dailyProductionDetailRecordMapper.getTMPDailyGrantInfoSummaryRecord(plantID, processID, startTime, endTime);
            List<Integer> attendanceInfo = dailyProductionDetailRecordMapper.getTMPDailyAttendanceSummaryRecord(plantID, processID, dayTime, classType);
            List<Map<Object, Object>> finalDailyProductionSummaryRecordTMP = new ArrayList<>();
            DecimalFormat dataFormat = new DecimalFormat(".00");
            int currentInventory = 0;
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
                    recordMap.put("productionMaterialID", dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                    recordMap.put("productionMaterialName", dailyProductionSummaryRecordTMP.get(i).get("materialName"));
                    recordMap.put("productionNumber", dailyProductionSummaryRecordTMP.get(i).get("productionNumber"));
                    recordMap.put("planDailyProduction", dailyProductionSummaryRecordTMP.get(i).get("planDailyProduction"));
                    recordMap.put("ratioFinish", dailyProductionSummaryRecordTMP.get(i).get("ratioFinish"));

                    recordMap.put("lastInventory", dailyProductionSummaryRecordTMP.get(i).get("lastInventory"));
                    currentInventory =Double.valueOf(dailyProductionSummaryRecordTMP.get(i).get("lastInventory").toString() ).intValue()
                            + Double.valueOf(dailyProductionSummaryRecordTMP.get(i).get("productionNumber").toString() ).intValue();



                        if(dailyGrantInfoSummaryRecordTMP.size() > i && dailyGrantInfoSummaryRecordTMP.get(i).get("materialID").equals(dailyProductionSummaryRecordTMP.get(i).get("materialID")))
                        {
                            currentInventory = currentInventory - Double.valueOf(dailyGrantInfoSummaryRecordTMP.get(i).get("grantNumber").toString() ).intValue();
                        }
                        else
                        {
                            Map<Object, Object> grantMap = new HashMap<>();
                            grantMap.put("materialID", dailyProductionSummaryRecordTMP.get(i).get("materialID"));
                            grantMap.put("materialName", dailyProductionSummaryRecordTMP.get(i).get("materialName"));
                            grantMap.put("grantNumber", 0);
                            dailyGrantInfoSummaryRecordTMP.add(i,grantMap);
                        }
                    recordMap.put("currentInventory", currentInventory);
                }

                if (i < dailyUsedInfoSummaryRecordTMP.size()) {
                    recordMap.put("usedMaterialID", dailyUsedInfoSummaryRecordTMP.get(i).get("usedMaterialID"));
                    recordMap.put("usedMaterialName", dailyUsedInfoSummaryRecordTMP.get(i).get("usedMaterialName"));
                    recordMap.put("usedNumber", dailyUsedInfoSummaryRecordTMP.get(i).get("usedNumber"));

                }

                if (i < dailyScrapInfoSummaryRecordTMP.size()) {
                    recordMap.put("scrapMaterialID", dailyScrapInfoSummaryRecordTMP.get(i).get("materialID"));
                    recordMap.put("scrapMaterialName", dailyScrapInfoSummaryRecordTMP.get(i).get("materialName"));
                    recordMap.put("scrapNumber", dailyScrapInfoSummaryRecordTMP.get(i).get("scrapNumber"));
                    recordMap.put("weightNumber", dailyScrapInfoSummaryRecordTMP.get(i).get("weightNumber"));

                }

                if (i < dailyRecieveInfoSummaryRecordTMP.size()) {
                    recordMap.put("receiveMaterialID", dailyRecieveInfoSummaryRecordTMP.get(i).get("materialID"));
                    recordMap.put("receiveMaterialName", dailyRecieveInfoSummaryRecordTMP.get(i).get("materialName"));
                    recordMap.put("receiveMaterialNumber1", dailyRecieveInfoSummaryRecordTMP.get(i).get("recieveNumber"));

                }

                if (i < dailyGrantInfoSummaryRecordTMP.size()) {
                    recordMap.put("grantMaterialID", dailyGrantInfoSummaryRecordTMP.get(i).get("materialID"));
                    recordMap.put("grantMaterialName", dailyGrantInfoSummaryRecordTMP.get(i).get("materialName"));
                    recordMap.put("grantNumber", dailyGrantInfoSummaryRecordTMP.get(i).get("grantNumber"));

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

    public TNPYResponse getDailyLineProductionDetailRecord(String plantID,String processID,String dayTime,String classType)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> dailyLineProductionDetailRecord = dailyLineProductionDetailRecordMapper.getDailyLineProductionDetailRecord(plantID,processID,dayTime,classType);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(dailyLineProductionDetailRecord));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse getDailyProcessProductionDetailRecord(String plantID,String processID,String dayTime,String classType)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> dailyProcessProductionDetailRecord = dailyProcessProductionDetailRecordMapper.getDailyProcessProductionDetailRecord(plantID,processID,dayTime,classType);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(dailyProcessProductionDetailRecord));
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}



