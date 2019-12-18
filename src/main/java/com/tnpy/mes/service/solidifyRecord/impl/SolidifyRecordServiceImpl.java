package com.tnpy.mes.service.solidifyRecord.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.util.StringUtil;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.mysql.*;
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

    @Autowired
    private WorkorderMapper workorderMapper;

    @Autowired
    private BatchrelationcontrolMapper batchrelationcontrolMapper;

    @Autowired
    private  DataProvenanceRelationMapper dataProvenanceRelationMapper;

    @Autowired
    private  MaterialRelationMapper materialRelationMapper;
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
            List<SolidifyRecord> solidifyRecordList = solidifyRecordMapper.selectInSolidifyRecord(solidifyFilter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(solidifyRecordList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getInSolidifyRoomByParamNew(String plantID, String roomID, String status) {
        TNPYResponse result = new TNPYResponse();
        try {
            if("9".equals(status))
            {
                result.setMessage("查询出错！请正确选择1至3段！"  );
                return result;
            }
            String solidifyFilter = " where  ";
            if ("-1".equals(status)) {
                solidifyFilter += "  status != '9'  ";
            } else {
                solidifyFilter += " status = '" + status + "' ";
            }
            if (!"-1".equals(roomID)) {
                solidifyFilter += " and solidifyRoomID = '" + roomID + "' ";
            } else {
                solidifyFilter += " and plantID = '" + plantID + "' ";
            }
            List<SolidifyRecord> solidifyRecordList = solidifyRecordMapper.selectInSolidifyRecord(solidifyFilter);
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
            List<OrderSplit> orderInfoList = orderSplitMapper.selectByFilterWithMaterialName(filter);
            List<Map<String, String>> putinResult = new ArrayList<Map<String, String>>();
            String[] orderArray = orderIDList.split("###");

            int nowInNumber = solidifyRecordMapper.selectInNumber(roomID, "1");
            if ((nowInNumber + orderArray.length) > (int) ConfigParamEnum.DryFilnCapacityMap.get(roomID)) {
                result.setMessage(roomName + "固化室已有" + nowInNumber + "架，不能再入" + orderArray.length + "架！");
                return result;
            }
            String orderSplitID = "";
            OrderSplit orderSplit;
            boolean blAdded = false;
            for (int i = 0; i < orderArray.length; i++) {
                Map<String, String> mapResult = new HashMap<String, String>();
                mapResult.put("orderID", orderArray[i]);
                mapResult.put("status", "失败");
                mapResult.put("returnMessage", "未获取到订单信息!");
                blAdded = false;
                for (int m = 0; m < i; m++) {
                    if (orderArray[i].trim().equals(orderArray[m].trim())) {
                        // mapResult.put("returnMessage","该订单已添加!");
                        // grantResult.add(mapResult);
                        blAdded = true;
                        break;
                    }
                }
                if (blAdded)
                    continue;
                for (int j = 0; j < orderInfoList.size(); j++) {
                    if (!orderArray[i].equals(orderInfoList.get(j).getId())) {
                        continue;
                    }
                    orderSplit = orderInfoList.get(j);
                    orderSplitID = orderSplit.getId();
                    try {
                        if (!(StatusEnum.WorkOrderStatus.finished.getIndex() + "").equals(orderSplit.getStatus())) {
                            MaterialRecord materialRecord = new MaterialRecord();
                            materialRecord.setId(orderSplit.getId());
                            materialRecord.setInorout(StatusEnum.InOutStatus.Input.getIndex());
                            materialRecord.setMaterialid(orderSplit.getMaterialid());
                            materialRecord.setNumber(orderSplit.getProductionnum());
                            materialRecord.setOrderid(orderSplit.getOrderid());
                            materialRecord.setSuborderid(orderSplit.getId());
                            materialRecord.setStatus(StatusEnum.StatusFlag.using.getIndex());
                            materialRecord.setInputtime(new Date());
                            materialRecord.setInputer(operatorName);
                            materialRecord.setInputer(operatorName);
                            materialRecord.setInputerid(operatorName);
                            if (operatorName.split("###").length > 1) {
                                materialRecord.setInputer(operatorName.split("###")[0]);
                                materialRecord.setInputerid(operatorName.split("###")[1]);
                                materialRecord.setMaterialnameinfo(orderSplit.getOrdersplitid());
                            }
                            Workorder workorder = workorderMapper.selectByPrimaryKey(orderSplit.getOrderid());
                            if (workorder != null) {
                                materialRecord.setInputplantid(workorder.getPlantid());
                                materialRecord.setInputprocessid(workorder.getProcessid());
                                materialRecord.setInputlineid(workorder.getLineid());
                                if (!materialRecord.getInputprocessid().equals(ConfigParamEnum.BasicProcessEnum.TBProcessID.getName())
                                        && !materialRecord.getInputprocessid().equals(ConfigParamEnum.BasicProcessEnum.LTProcessID.getName())   ) {
                                        mapResult.put("returnMessage", "只有涂板、连涂工序的工单才能入窑！" + orderSplitID);
                                        break;
                                }
                            }
                            // orderSplit.setStatus(StatusEnum.WorkOrderStatus.finished.getIndex() + "");
                            // System.out.println(  "==============" +JSONObject.toJSON(orderSplit).toString());
                            orderSplitMapper.updateStatus(orderSplit.getId(), StatusEnum.WorkOrderStatus.finished.getIndex() + "");

                            materialRecordMapper.insert(materialRecord);

                            updateDataProvenance(materialRecord);
                            try {
                                String batchID = batchrelationcontrolMapper.selectTBBatchByOrderID(orderSplit.getOrderid());

                                if (StringUtil.isEmpty(batchID) || batchID.length() < 6) {
                                    String batch = orderSplit.getId().substring(0, orderSplit.getId().length() - 14)
                                            + orderSplit.getId().substring(orderSplit.getId().length() - 11, orderSplit.getId().length() - 3);
                                    Batchrelationcontrol batchrelationcontrol = new Batchrelationcontrol();
                                    batchrelationcontrol.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                                    batchrelationcontrol.setRelationorderid(orderSplit.getOrderid());
                                    batchrelationcontrol.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                                    batchrelationcontrol.setRelationtime(new Date());

                                    if (!StringUtil.isEmpty(batchID)) {
                                        batchrelationcontrol.setTbbatch(batch);
                                        batchrelationcontrolMapper.insert(batchrelationcontrol);
                                    }
                                }
                            } catch (Exception ex) {
                                result.setMessage(result.getMessage() + " " + ex.getMessage());
                            }
                        }
                    } catch (Exception ex2) {
                        result.setMessage(result.getMessage() + " " + ex2.getMessage().substring(0, 100));
                    }

//                    if (!(StatusEnum.WorkOrderStatus.finished.getIndex() + "").equals(orderSplit.getStatus())) {
//                        mapResult.put("returnMessage", "该订单尚未入库！" + orderSplitID);
//                        break;
//                    }

                    MaterialRecord materialRecord = materialRecordMapper.selectBySuborderID(orderSplit.getId());
                    if (materialRecord == null) {
                        mapResult.put("returnMessage", "未获取到入库信息！请重试或者重新入库！" + orderSplitID);
                        break;
                    }
                    if (!materialRecord.getInputprocessid().equals(ConfigParamEnum.BasicProcessEnum.TBProcessID.getName())
                            && !materialRecord.getInputprocessid().equals(ConfigParamEnum.BasicProcessEnum.LTProcessID.getName() ) ) {
                            mapResult.put("returnMessage", "只有涂板、连涂工序的工单才能入窑！" + orderSplitID);
                            break;
                    }

                    List<SolidifyRecord> solidifyRecordList = solidifyRecordMapper.selectByOrderID(orderSplitID);
                    if (solidifyRecordList.size() > 0) {
                        mapResult.put("returnMessage", "该工单已入窑，入窑人：" + solidifyRecordList.get(0).getRecorder1() + "，入窑时间： " + solidifyRecordList.get(0).getStarttime1() + "！" + orderSplitID);
                        break;
                    }
                    try {
                        SolidifyRecord solidifyRecord = new SolidifyRecord();
                        solidifyRecord.setOrderid(orderSplitID);
                        solidifyRecord.setId(orderSplitID);
                        solidifyRecord.setMaterialid(materialRecord.getMaterialid());
                        solidifyRecord.setMaterialname(materialRecord.getMaterialnameinfo());
                        solidifyRecord.setProductionnum(materialRecord.getNumber().intValue());
                        solidifyRecord.setPlantid(materialRecord.getInputplantid());
                        solidifyRecord.setSolidifyroomname(roomName);
                        solidifyRecord.setSolidifyroomid(roomID);
                        solidifyRecord.setRecorder1(operatorName.split("###")[0]);
                        solidifyRecord.setStarttime1(new Date());
                        solidifyRecord.setStatus("1");
                        solidifyRecordMapper.insertSelective(solidifyRecord);
                        mapResult.put("status", "成功");
                        mapResult.put("returnMessage", "");
                    } catch (Exception ex) {
                        mapResult.put("status", "失败");
                        mapResult.put("returnMessage", "该工单已入窑");
                    }
                }
                putinResult.add(mapResult);
            }
            result.setData(JSONObject.toJSON(putinResult).toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("入固化室失败！" + ex.getMessage());
            return result;
        }
    }


    private void updateDataProvenance(MaterialRecord materialRecord) {
        try {
            String materialFilte = " where outOrderID = '" + materialRecord.getOrderid() + "' and productionNum > 10 and outSubOrderID is null order by inputTime ";
            List<DataProvenanceRelation> dataProvenanceRelationList = dataProvenanceRelationMapper.selectByFilter(materialFilte);
            List<MaterialRelation> materialRelationList = materialRelationMapper.selectByOutMaterial(materialRecord.getMaterialid());

            double leftNum = 0;
            String[] listRelation;
            for (int i = 0; i < materialRelationList.size(); i++) {
                if (!materialRelationList.get(i).getProportionality().contains(":")) {
                    continue;
                }
                for (int j = 0; j < dataProvenanceRelationList.size(); j++) {
                    if (!dataProvenanceRelationList.get(j).getInputmaterialid().equals(materialRelationList.get(i).getInmaterialid())) {
                        continue;
                    }
                    listRelation = materialRelationList.get(i).getProportionality().split(":");
                    leftNum = dataProvenanceRelationList.get(j).getLeftnumber() - (Double.valueOf(listRelation[0]) * materialRecord.getNumber()) / Double.valueOf(listRelation[1]);
                    if (leftNum > 10) {
                        String jsonStr = JSONObject.toJSON(dataProvenanceRelationList.get(j)).toString();
                        // OrderSplit orderSplit=(OrderSplit) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), OrderSplit.class);
                        DataProvenanceRelation dataProvenanceRelation = (DataProvenanceRelation) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), DataProvenanceRelation.class);
                        dataProvenanceRelation.setLeftnumber(leftNum);
                        dataProvenanceRelation.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                        dataProvenanceRelationMapper.insertSelective(dataProvenanceRelation);
                    }

                    dataProvenanceRelationList.get(j).setOutsuborderid(materialRecord.getSuborderid());
                    dataProvenanceRelationList.get(j).setOutputername(materialRecord.getInputer());
                    //dataProvenanceRelationList.get(j).setOutputnumber();
                    dataProvenanceRelationList.get(j).setOutputtime(materialRecord.getInputtime());
                    dataProvenanceRelationMapper.updateByPrimaryKeySelective(dataProvenanceRelationList.get(j));

                    if (leftNum < -10) {
                        materialRecord.setNumber(materialRecord.getNumber() - (Double.valueOf(listRelation[1]) * dataProvenanceRelationList.get(j).getLeftnumber()) / Double.valueOf(listRelation[0]));
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("============= 产出关联出错！ =======" + ex.getMessage());
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

            int inRoomIDNumber = 0;
            if ("1".equals(status)) {
                inRoomIDNumber = solidifyRecordMapper.selectInNumber(roomID,"2");
                if(inRoomIDNumber > 2 && !roomID.contains("1B"))
                {
                    result.setMessage("转段出错！第二段内的物料未转移！请先进行第二段转段！" + roomID);
                    return result;
                }
                valueUpdate = " set status = '2',endtime1 = '" + dateStr + "' ,starttime2 = '" + dateStr + "' ,recorder2 = '" + operatorName + "' ";
            }

            if ("2".equals(status)) {
                inRoomIDNumber = solidifyRecordMapper.selectInNumber(roomID,"3");
                if(inRoomIDNumber > 2&& !roomID.contains("1B"))
                {
                    result.setMessage("转段出错！第三段内的物料未转移！请先进行第三段转段！" + roomID);
                    return result;
                }
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


    public TNPYResponse changeAllSolidifyStatusAuto(String roomID, String operatorName) {

        TNPYResponse result = new TNPYResponse();
        try {
            String solidifyUpdate = "  status = '" + 3 + "'  and solidifyRoomID = '" + roomID + "'";
            String valueUpdate = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();//取时间
            String dateStr = dateFormat.format(date);
            valueUpdate = " set status = '9',endtime3 = '" + dateStr + "'  ,outOperator = '" + operatorName + "' ";
            solidifyRecordMapper.changeSolidifyStatus(valueUpdate, solidifyUpdate);

            solidifyUpdate = "  status = '" + 2 + "'  and solidifyRoomID = '" + roomID + "'";
            valueUpdate = " set status = '3',endtime2 = '" + dateStr + "' ,starttime3 = '" + dateStr + "' ,recorder3 = '" + operatorName + "' ";
            solidifyRecordMapper.changeSolidifyStatus(valueUpdate, solidifyUpdate);
            solidifyUpdate = "  status = '" + 1 + "'  and solidifyRoomID = '" + roomID + "'";
            valueUpdate = " set status = '2',endtime1 = '" + dateStr + "' ,starttime2 = '" + dateStr + "' ,recorder2 = '" + operatorName + "' ";
            solidifyRecordMapper.changeSolidifyStatus(valueUpdate, solidifyUpdate);
//
//            if ("1".equals(status)) {
//                valueUpdate = " set status = '2',endtime1 = '" + dateStr + "' ,starttime2 = '" + dateStr + "' ,recorder2 = '" + operatorName + "' ";
//            }
//
//            if ("2".equals(status)) {
//                valueUpdate = " set status = '3',endtime2 = '" + dateStr + "' ,starttime3 = '" + dateStr + "' ,recorder3 = '" + operatorName + "' ";
//            }
//
//            if ("3".equals(status)) {
//                valueUpdate = " set status = '9',endtime3 = '" + dateStr + "'  ,outOperator = '" + operatorName + "' ";
//            }
//
//            solidifyRecordMapper.changeSolidifyStatus(valueUpdate, solidifyUpdate);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }


    public TNPYResponse uninputSolidifyRoom(String plantID, String startTime, String endTime) {

        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> uninputList = solidifyRecordMapper.uninputSolidifyRoom(plantID, ConfigParamEnum.BasicProcessEnum.TBProcessID.getName(), startTime, endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(uninputList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getSolidifyRoomDetail(String plantID) {

        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date date = new Date();//取时间
            List<Map<Object, Object>> solidifyRoomDetailList = solidifyRecordMapper.getSolidifyRoomDetail(plantID,dateFormat.format(date));
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(solidifyRoomDetailList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }


    public TNPYResponse getSolidifyWorkDetail(String plantID,String roomID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String solidifyFilter = "  ";
            if (!"-1".equals(roomID)) {
                solidifyFilter += "  solidifyRoomID = '" +roomID + "'  ";
            }
            List<Map<Object, Object>> solidifyRoomDetailList = solidifyRecordMapper.getSolidifyWorkRecordDetail(plantID,solidifyFilter,startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(solidifyRoomDetailList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
