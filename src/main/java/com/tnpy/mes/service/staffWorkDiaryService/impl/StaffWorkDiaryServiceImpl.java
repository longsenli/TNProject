package com.tnpy.mes.service.staffWorkDiaryService.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.ProductionLineMapper;
import com.tnpy.mes.mapper.mysql.StaffAttendanceDetailMapper;
import com.tnpy.mes.mapper.mysql.WorkLocationMapper;
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

    public TNPYResponse getStaffAttendanceInfo(String plantID, String processID, String lineID, String staffID, String startTime, String endTime) {
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
            filter += " order by dayTime desc limit 1000";
            List<Map<Object,Object>> staffAttendanceRecordList = staffAttendanceDetailMapper.selectMapRecordByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(staffAttendanceRecordList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    // 1 上机扫码  2 下机扫码
    public TNPYResponse insertStaffComeAttendanceInfo(String qrCode, String staffID, String staffName, String classType1, String classType2, String dayTime) {
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
}
