package com.tnpy.mes.service.solidifyRecord.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.MaterialRecordMapper;
import com.tnpy.mes.mapper.mysql.OrderSplitMapper;
import com.tnpy.mes.mapper.mysql.SolidifyRecordMapper;
import com.tnpy.mes.model.mysql.MaterialRecord;
import com.tnpy.mes.model.mysql.OrderSplit;
import com.tnpy.mes.model.mysql.SolidifyRecord;
import com.tnpy.mes.service.solidifyRecord.ISolidifyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/15 13:19
 */
@Service("solidifyRecordService")
public class SolidifyRecordServiceImpl implements ISolidifyRecordService {
    @Autowired
    private SolidifyRecordMapper solidifyRecordMapper;

    @Autowired
    private MaterialRecordMapper materialRecordMapper;

    @Autowired
    private OrderSplitMapper orderSplitMapper;

    public TNPYResponse getSolidifyRecordByRoom(String plantID, String roomID) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<SolidifyRecord> solidifyRecordList = solidifyRecordMapper.selectByRoomID(plantID, roomID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(solidifyRecordList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getSolidifyRecordByParam(String plantID, String roomID, String solidifyStepID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String solidifyFilter = " where starttime1 >= '" + startTime + "' and starttime1 < '" + endTime + "' ";
            if (!"-1".equals(solidifyStepID)) {
                solidifyFilter += " and status = '" + solidifyStepID + "' ";
            }
            if (!"-1".equals(roomID)) {
                solidifyFilter += " and solidifyRoomID = '" + roomID + "' ";
            } else {
                solidifyFilter += " and plantID = '" + plantID + "' ";
            }
            List<SolidifyRecord> solidifyRecordList = solidifyRecordMapper.getSolidifyRecordByFilter(solidifyFilter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(solidifyRecordList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getInSolidifyRoomByParam(String plantID, String roomID) {
        TNPYResponse result = new TNPYResponse();
        try {
            String solidifyFilter = " where status != '9' ";

            if (!"-1".equals(roomID)) {
                solidifyFilter += " and solidifyRoomID = '" + roomID + "' ";
            } else {
                solidifyFilter += " and plantID = '" + plantID + "' ";
            }
            List<SolidifyRecord> solidifyRecordList = solidifyRecordMapper.getSolidifyRecordByFilter(solidifyFilter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(solidifyRecordList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse addSolidifyRecord(String id, String status, String recorder, String roomID) {
        TNPYResponse result = new TNPYResponse();
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = sdf.format(date);

            String updateStr = "solidifyRoomID = '" + roomID + "' ";
            int i = Integer.valueOf(status).intValue();
            if (i - 1 >= 1) {
                updateStr += " ,endtime" + (i - 1) + " = '" + dateStr + "'";
            }
            if (i < 4) {
                updateStr += " ,starttime" + i + " = '" + dateStr + "' ,recorder" + i + " = '" + recorder + "'";
            }

            solidifyRecordMapper.addSolidifyRecord(id, updateStr);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //result.setData(JSONObject.toJSON(solidifyRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse putinSolidifyRoom(String roomID, String orderIDList, String operatorName, String roomName) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where id in ('" + orderIDList.replaceAll("###", "','") + "' )";
            List<OrderSplit> orderInfoList = orderSplitMapper.selectByFilter(filter);
            List<Map<String, String>> putinResult = new ArrayList<Map<String, String>>();
            String[] orderArray = orderIDList.split("###");
            String orderSplitID = "";
            OrderSplit orderSplit;
            for (int i = 0; i < orderArray.length; i++) {
                Map<String, String> mapResult = new HashMap<String, String>();
                mapResult.put("orderID", orderArray[i]);
                mapResult.put("status", "失败");
                mapResult.put("returnMessage", "未获取到订单信息!");
                for (int j = 0; j < orderInfoList.size(); j++) {
                    if (!orderArray[i].equals(orderInfoList.get(j).getId())) {
                        continue;
                    }
                    orderSplit = orderInfoList.get(j);
                    orderSplitID = orderSplit.getId();

                    if (!(StatusEnum.WorkOrderStatus.finished.getIndex() + "").equals(orderSplit.getStatus())) {
                        mapResult.put("returnMessage", "该订单尚未入库！" + orderSplitID);
                        break;
                    }

                    MaterialRecord materialRecord = materialRecordMapper.selectBySuborderID(orderSplit.getId());
                    if (materialRecord == null) {
                        mapResult.put("returnMessage", "未获取到入库信息！请重试或者重新入库！" + orderSplitID);
                        break;
                    }
                    if (!materialRecord.getInputprocessid().equals(ConfigParamEnum.BasicProcessEnum.TBProcessID.getName())) {
                        mapResult.put("returnMessage", "只有涂板工序的工单才能入窑！" + orderSplitID);
                        break;
                    }

                    List<SolidifyRecord> solidifyRecordList = solidifyRecordMapper.selectByOrderID(orderSplitID);
                    if (solidifyRecordList.size() > 0) {
                        mapResult.put("returnMessage", "该工单已入窑，入窑人：" + solidifyRecordList.get(0).getRecorder1() + "，入窑时间： " + solidifyRecordList.get(0).getStarttime1() + "！" + orderSplitID);
                        break;
                    }
                    SolidifyRecord solidifyRecord = new SolidifyRecord();
                    solidifyRecord.setOrderid(orderSplitID);
                    solidifyRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                    solidifyRecord.setMaterialid(materialRecord.getMaterialid());
                    solidifyRecord.setMaterialname(materialRecord.getMaterialnameinfo());
                    solidifyRecord.setProductionnum(materialRecord.getNumber().intValue());
                    solidifyRecord.setPlantid(materialRecord.getInputplantid());
                    solidifyRecord.setSolidifyroomname(roomName);
                    solidifyRecord.setSolidifyroomid(roomID);
                    solidifyRecord.setRecorder1(operatorName);
                    solidifyRecord.setStarttime1(new Date());
                    solidifyRecord.setStatus("1");
                    solidifyRecordMapper.insertSelective(solidifyRecord);
                    mapResult.put("status", "成功");
                    mapResult.put("returnMessage", "");
                }
                putinResult.add(mapResult);
            }
            result.setData(JSONObject.toJSON(putinResult).toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("发放失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse changeSolidifyStatus(String roomID, String orderIDList, String operatorName, String status) {

        TNPYResponse result = new TNPYResponse();
        try {
            String solidifyUpdate = "  status = '" + status + "'  and solidifyRoomID = '" + roomID + "'";
            String valueUpdate = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();//取时间
            String dateStr = dateFormat.format(date);
            if ("1".equals(status)) {
                valueUpdate = " set status = '2',endtime1 = '" + dateStr + "' ,starttime2 = '" + dateStr + "' ,recorder2 = '" + operatorName + "' ";
            }

            if ("2".equals(status)) {
                valueUpdate = " set status = '3',endtime2 = '" + dateStr + "' ,starttime3 = '" + dateStr + "' ,recorder3 = '" + operatorName + "' ";
            }

            if ("3".equals(status)) {
                valueUpdate = " set status = '9',endtime3 = '" + dateStr + "'  ,outOperator = '" + operatorName + "' ";
            }

            solidifyRecordMapper.changeSolidifyStatus(valueUpdate, solidifyUpdate);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
