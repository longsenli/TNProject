package com.tnpy.mes.service.workOrderService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.mass.DateUtilsDef;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.customize.CustomOrderSplitRecord;
import com.tnpy.mes.model.customize.CustomWorkOrderRecord;
import com.tnpy.mes.model.mysql.*;
import com.tnpy.mes.service.workOrderService.IWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/3 15:38
 */
@Service("workOrderService")
public class WorkOrderServiceImpl implements IWorkOrderService {
    // final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WorkOrderServiceImpl.class);
    @Autowired
    private WorkorderMapper workOrderMapper;

    @Autowired
    private OrderSplitMapper orderSplitMapper;

    @Autowired
    private MaterialRecordMapper materialRecordMapper;
    @Autowired
    private SolidifyRecordMapper solidifyRecordMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private MaterialRelationMapper materialRelationMapper;


    @Autowired
    private BatchrelationcontrolMapper batchrelationcontrolMapper;

    @Autowired
    private GrantMaterialRecordMapper grantMaterialRecordMapper;

    @Autowired
    private PlanProductionRecordMapper planProductionRecordMapper;

    @Autowired
    private OnlineMaterialRecordMapper onlineMaterialRecordMapper;

    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;

    @Autowired
    private DryingKilnJZRecordMapper dryingKilnJZRecordMapper;

    @Autowired
    private DataProvenanceRelationMapper dataProvenanceRelationMapper;

    @Autowired
    private PileBatteryRecordMapper pileBatteryRecordMapper;

    @Autowired
    private TidyBatteryRecordMapper tidyBatteryRecordMapper;

    @Autowired
    private WorkOrderTemplateMapper workOrderTemplateMapper;

    public TNPYResponse getWorkOrder() {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Workorder> workOrderList = workOrderMapper.selectAll();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getSubOrderByID(String id, String type) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<CustomOrderSplitRecord> orderSplitList = null;

            if ("2".equals(type.trim())) {
                orderSplitList = orderSplitMapper.selectAfterMapBySubOrderName(id);
            } else {
                orderSplitList = orderSplitMapper.selectAfterMapBySubOrderID(id);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            if (orderSplitList.size() > 0)
                result.setMessage(orderSplitList.get(0).getMaterialName() + ": " + orderSplitList.get(0).getProductionnum());
            // System.out.println(id + "============" + orderSplitList.size());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getSubOrderByIDToMap(String id, String type, String plantID, String processID) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<String, String>> orderSplitList = null;
            if (ConfigParamEnum.BasicProcessEnum.BZProcessID.getName().equals(processID)) {
                orderSplitList = pileBatteryRecordMapper.selectToOrderFinishInfo(id);
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                // System.out.println(id + "============" + orderSplitList.size());
                result.setData(JSONObject.toJSON(orderSplitList).toString());
                return result;
            }
            if ("2".equals(type.trim())) {
                orderSplitList = orderSplitMapper.selectToMapBySubOrderName(id, plantID, processID);
            } else {
                orderSplitList = orderSplitMapper.selectToMapBySubOrderID(id, plantID, processID);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            // System.out.println(id + "============" + orderSplitList.size());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getWorkOrderByLineID(String lineID) {
        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            dateFormat.format(date);
            String timeFinish = "";
            String timeStart = "";
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if (calendar.get(Calendar.HOUR_OF_DAY) < 7) {
                //  timeFinish = dateFormat.format(date) + " 06:00:00";
                timeFinish = "2030-11-11 00:00:00";
                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                timeStart = dateFormat.format(date) + " 18:00:00";
            } else if (calendar.get(Calendar.HOUR_OF_DAY) > 18) {
                timeStart = dateFormat.format(date) + " 06:00:00";
                timeFinish = "2030-11-11 00:00:00";
                // timeFinish = dateFormat.format(date) + " 20:00:00";
            } else {
                timeStart = dateFormat.format(date) + " 06:00:00";

                timeFinish = "2030-11-11 00:00:00";
                //timeFinish = dateFormat.format(date) + " 08:00:00";
            }

            List<Workorder> workOrderList = workOrderMapper.selectByFilter(" where lineID = '" + lineID + "' and status < 4  and scheduledStartTime <='" + timeFinish
                    + "' and  scheduledStartTime >= '" + timeStart + "' order by  scheduledStartTime  ");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getWorkOrderByParam(String plantID, String processID, String lineID) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = "where  plantID ='" + plantID + "' and processID = '" + processID + "' ";
            if (!"-1".equals(lineID)) {
                filter += " and lineID = '" + lineID + "' ";
            }
            List<Workorder> workOrderList = workOrderMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getCustomWorkOrderByParam(String plantID, String processID, String lineID) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = "where  plantID ='" + plantID + "' and processID = '" + processID + "' ";
            if (!"-1".equals(lineID)) {
                filter += " and lineID = '" + lineID + "'";
            }
            filter += " order by scheduledStartTime desc limit 500 ";
            //  filter += " order by scheduledStartTime desc,lineID asc  limit 1000 ";
            // System.out.println("============");
            List<CustomWorkOrderRecord> workOrderList = workOrderMapper.selectCustomResultByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    private String getOrderNumber(int number, int length) {
        String numStr = String.valueOf(number);
        String tmp = "";
        for (int i = 0; i < length - numStr.length(); i++) {
            tmp += "0";
        }
        return tmp + numStr;
    }

    public TNPYResponse addMissingWorkOrder(Workorder workorder) {
        TNPYResponse result = new TNPYResponse();
        try {
            //    Workorder workorder = (Workorder) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), Workorder.class);

            if (StringUtils.isEmpty(workorder.getId())) {
                //workorder.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());

                workorder.setCreatetime(new Date());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // System.out.println(dateFormat.format(workorder.getScheduledstarttime()));
                //  int orderNum = workOrderMapper.selectOrderNumber(workorder.getLineid(),dateFormat.format(workorder.getScheduledstarttime()));

                List<String> idList = workOrderMapper.selectOrderIDList(workorder.getLineid(), dateFormat.format(workorder.getScheduledstarttime()));
                // int orderNum = 0;
                String numStr = "";
                for (int i = 0; ; i++) {
                    numStr = workorder.getOrderid().substring(0, workorder.getOrderid().length() - 10) + "BD" + String.valueOf(i + 1)
                            + workorder.getOrderid().substring(workorder.getOrderid().length() - 10, workorder.getOrderid().length());
                    if (idList.contains(numStr)) {
                        continue;
                    } else
                        break;
                }
                if (numStr.length() < 3) {
                    result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                    result.setMessage("自定义工单失败！");
                    return result;
                }

                workorder.setOrderid(numStr);
                workorder.setId(numStr);
                workOrderMapper.insertSelective(workorder);

                List<OrderSplit> orderSplitList = new ArrayList<>();
                for (int i = 0; i < workorder.getBatchnum(); i++) {
                    OrderSplit orderSplit = new OrderSplit();
                    orderSplit.setOrderid(workorder.getId());
                    orderSplit.setStatus(StatusEnum.WorkOrderStatus.finished.getIndex() + "");
                    orderSplit.setMaterialid(workorder.getMaterialid());
                    orderSplit.setOrdersplitid(workorder.getOrderid() + getOrderNumber(i + 1, 3));
                    orderSplit.setId(orderSplit.getOrdersplitid());
                    orderSplit.setProductionnum(workorder.getTotalproduction() / workorder.getBatchnum() * 1.0);
                    orderSplitList.add(orderSplit);
                }
                orderSplitMapper.insertManyOrder(orderSplitList, workorder.getId());

                if(ConfigParamEnum.BasicProcessEnum.BBProcessID.getName().equals(workorder.getProcessid()))
                {
                    orderSplitMapper.autoBBFinishOrderByOrderID(workorder.getId());
                }
                else
                {
                    orderSplitMapper.autoFinishOrderByOrderID(workorder.getId());
                }

            } else {
                workOrderMapper.updateByPrimaryKey(workorder);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("修改出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse changeWorkOrder(String jsonStr) {
        TNPYResponse result = new TNPYResponse();
        try {
            Workorder workorder = (Workorder) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), Workorder.class);

            if (StringUtils.isEmpty(workorder.getId())) {
                if ((StatusEnum.WorkOrderStatus.addmissing.getIndex() + "").equals(workorder.getStatus())) {
                    return addMissingWorkOrder(workorder);
                }
                //workorder.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                workorder.setStatus(StatusEnum.WorkOrderStatus.ordered.getIndex() + "");
                workorder.setCreatetime(new Date());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // System.out.println(dateFormat.format(workorder.getScheduledstarttime()));
                //  int orderNum = workOrderMapper.selectOrderNumber(workorder.getLineid(),dateFormat.format(workorder.getScheduledstarttime()));

                List<String> idList = workOrderMapper.selectOrderIDList(workorder.getLineid(), dateFormat.format(workorder.getScheduledstarttime()));
                // int orderNum = 0;
                String numStr = "";
                for (int i = 0; ; i++) {
                    numStr = workorder.getOrderid().substring(0, workorder.getOrderid().length() - 10) + String.valueOf(i + 1)
                            + workorder.getOrderid().substring(workorder.getOrderid().length() - 10, workorder.getOrderid().length());
                    if (idList.contains(numStr)) {
                        continue;
                    } else
                        break;
                }
                if (numStr.length() < 3) {
                    result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                    result.setMessage("自定义工单失败！");
                    return result;
                }

                workorder.setOrderid(numStr);
                workorder.setId(numStr);
                workOrderMapper.insertSelective(workorder);

                List<OrderSplit> orderSplitList = new ArrayList<>();
                for (int i = 0; i < workorder.getBatchnum(); i++) {
                    OrderSplit orderSplit = new OrderSplit();
                    orderSplit.setOrderid(workorder.getId());
                    orderSplit.setStatus(StatusEnum.WorkOrderStatus.ordered.getIndex() + "");
                    orderSplit.setMaterialid(workorder.getMaterialid());
                    orderSplit.setOrdersplitid(workorder.getOrderid() + getOrderNumber(i + 1, 3));
                    orderSplit.setId(orderSplit.getOrdersplitid());
                    orderSplit.setProductionnum(workorder.getTotalproduction() / workorder.getBatchnum() * 1.0);
                    orderSplitList.add(orderSplit);

                }
                orderSplitMapper.insertManyOrder(orderSplitList, workorder.getId());
            } else {
                workOrderMapper.updateByPrimaryKey(workorder);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("修改出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse deleteWorkOrder(String orderID) {
        TNPYResponse result = new TNPYResponse();
        try {
            Workorder workorder = workOrderMapper.selectByPrimaryKey(orderID);
            if (workorder.getStatus().equals(StatusEnum.WorkOrderStatus.printed.getIndex() + "")) {
                result.setMessage("该工单已打印，不能删除！");
                return result;
            }
            String productionOrder = materialRecordMapper.getProductionByOrderID(orderID);
            if (!org.springframework.util.StringUtils.isEmpty(productionOrder)) {
                result.setMessage("该工单已有入库记录不能删除！");
                return result;
            }

            if (!org.springframework.util.StringUtils.isEmpty(productionOrder)) {
                result.setMessage("该工单已有入库记录不能删除！");
                return result;
            }

            orderSplitMapper.deleteByOrderID(orderID);
            workOrderMapper.deleteByPrimaryKey(orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("删除失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getOrderSplit(String orderID) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<OrderSplit> orderSplitList = orderSplitMapper.selectByOrderID(orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }


    public TNPYResponse getOrderSplitAfterMap(String orderID) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<OrderSplit> orderSplitList = orderSplitMapper.selectAfterMapByOrderID(orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getOrderSplitToMap(String orderID) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<String, String>> orderSplitList = orderSplitMapper.selectToMapByOrderID(orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse changePrintStatus(String workOrderID) {
        TNPYResponse result = new TNPYResponse();
        try {

            workOrderMapper.updateWorkOrderPrintStatus(workOrderID, StatusEnum.WorkOrderStatus.printed.getIndex() + "", StatusEnum.WorkOrderStatus.finished.getIndex() + "");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("变更失败！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse changeWorkOrderStatus(String ID, String status) {
        TNPYResponse result = new TNPYResponse();
        try {
            String productionOrder = materialRecordMapper.getProductionByOrderID(ID);
            if (!org.springframework.util.StringUtils.isEmpty(productionOrder)) {
                result.setMessage("该工单已有入库记录不能删除！");
                return result;
            }
            orderSplitMapper.deleteByOrderID(ID);
            workOrderMapper.updateWorkOrderStatus(ID, status);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("删除失败！" + ex.getMessage());
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

    public TNPYResponse finishOrderSplit(String jsonStr, String name) {
        TNPYResponse result = new TNPYResponse();
        try {
            String[] inputterInfo = name.split("###");
            OrderSplit orderSplit = (OrderSplit) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), OrderSplit.class);

            if (ConfigParamEnum.BasicProcessEnum.BZProcessID.getName().equals(orderSplit.getOrderid())) {
                PileBatteryRecord pileBatteryRecord = pileBatteryRecordMapper.selectByPrimaryKey(orderSplit.getId());
                if (!"1".equals(pileBatteryRecord.getStatus())) {
                    result.setMessage("该二维码已打堆！");
                    return result;
                }
                pileBatteryRecord.setStatus("4");
                pileBatteryRecord.setFinishpilenum(orderSplit.getProductionnum().floatValue());
                if (inputterInfo.length > 3) {
                    pileBatteryRecord.setFinishpilestaffid(inputterInfo[1]);
                    pileBatteryRecord.setFinishpilestaffname(inputterInfo[0]);
                }
                pileBatteryRecord.setFinishpiletime(new Date());
                pileBatteryRecord.setPartpileid(pileBatteryRecord.getId());
                pileBatteryRecordMapper.updateByPrimaryKeySelective(pileBatteryRecord);

                tidyBatteryRecordMapper.updateCurrentNumAfterPile(pileBatteryRecord.getTidyrecordid(), pileBatteryRecord.getFinishpilenum().intValue() + "");
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                return result;
            }
            Workorder workorder = workOrderMapper.selectByPrimaryKey(orderSplit.getOrderid());

            OrderSplit orderSplitTMP = orderSplitMapper.selectByPrimaryKey(orderSplit.getId());
            if (orderSplitTMP.getStatus().equals(StatusEnum.WorkOrderStatus.finished.getIndex() + "")) {
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage("该工单已完成！");
                return result;
            }
            if (orderSplit.getProductionnum() == null) {
                orderSplit.setProductionnum(orderSplitTMP.getProductionnum());
            }

            if( materialRecordMapper.usedMaterialNumberLimitFlag( workorder.getPlantid(), workorder.getProcessid()) > 0)
            {
                TNPYResponse judgeResult = judgeEnoughMaterial(orderSplit.getMaterialid(), orderSplit.getOrderid(), orderSplit.getProductionnum());
                if (judgeResult.getStatus() != StatusEnum.ResponseStatus.Success.getIndex()) {
                    // System.out.println(JSONObject.toJSON(judgeResult).toString());
                    return judgeResult;
                }
            }


            orderSplit.setStatus(StatusEnum.WorkOrderStatus.finished.getIndex() + "");
            orderSplitMapper.updateByPrimaryKeySelective(orderSplit);

            MaterialRecord materialRecord = new MaterialRecord();
            materialRecord.setId(orderSplit.getId());
            materialRecord.setInorout(StatusEnum.InOutStatus.Input.getIndex());
            materialRecord.setMaterialid(orderSplit.getMaterialid());
            materialRecord.setNumber(orderSplit.getProductionnum());
            materialRecord.setOrderid(orderSplit.getOrderid());
            materialRecord.setSuborderid(orderSplit.getId());
            materialRecord.setStatus(StatusEnum.StatusFlag.using.getIndex());
            materialRecord.setInputtime(new Date());
            materialRecord.setInputer(name);
            if (inputterInfo.length > 3) {
                materialRecord.setInputer(inputterInfo[0]);
                materialRecord.setInputerid(inputterInfo[1]);
                materialRecord.setInputworklocationid(inputterInfo[2]);
                materialRecord.setMaterialnameinfo(inputterInfo[3]);
            }
            if (workorder != null) {
                materialRecord.setInputplantid(workorder.getPlantid());
                materialRecord.setInputprocessid(workorder.getProcessid());
                materialRecord.setInputlineid(workorder.getLineid());

            }
            if (ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(materialRecord.getInputprocessid())) {
                materialRecord.setInorout(StatusEnum.InOutStatus.PreInput.getIndex());
            }
            if (ConfigParamEnum.BasicProcessEnum.BBProcessID.getName().equals(workorder.getProcessid())) {
                materialRecord.setInputworklocationid(orderSplit.getProductionnum() + "");
            }
            materialRecordMapper.insert(materialRecord);
            boolean blTB = ConfigParamEnum.BasicProcessEnum.TBProcessID.getName().equals(workOrderMapper.getProcessIDByOrder(orderSplit.getOrderid()));
         /*
            try
            {
                if(blTB)
                {
                    SolidifyRecord solidifyRecord = new SolidifyRecord();
                    solidifyRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                    solidifyRecord.setMaterialid(orderSplit.getMaterialid());
                    solidifyRecord.setOrderid(orderSplit.getOrderid());
                    solidifyRecord.setOrdersplitid(orderSplit.getId());
                    solidifyRecord.setOrdersplitname(orderSplit.getOrdersplitid());
                    solidifyRecordMapper.insertSelective(solidifyRecord);
                }
            }catch (Exception ex)
            {
                result.setMessage(result.getMessage() + " " +ex.getMessage() );
            }
*/
            try {
                if (blTB) {
                    String batchID = batchrelationcontrolMapper.selectTBBatchByOrderID(orderSplit.getOrderid());
                    if (org.springframework.util.StringUtils.isEmpty(batchID) || "null".equals(batchID.trim()) || batchID.length() < 6) {
                        Batchrelationcontrol batchrelationcontrol = new Batchrelationcontrol();
                        batchrelationcontrol.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                        batchrelationcontrol.setRelationorderid(orderSplit.getOrderid());
                        batchrelationcontrol.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                        batchrelationcontrol.setRelationtime(new Date());
                        String batch = orderSplit.getOrdersplitid().substring(0, orderSplit.getOrdersplitid().length() - 13)
                                + orderSplit.getOrdersplitid().substring(orderSplit.getOrdersplitid().length() - 11, orderSplit.getOrdersplitid().length() - 3);
                        batchrelationcontrol.setTbbatch(batch);
                        batchrelationcontrolMapper.insert(batchrelationcontrol);
                    }
                }
            } catch (Exception ex) {
                result.setMessage(result.getMessage() + " " + ex.getMessage());
            }
            updateDataProvenance(materialRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            // System.out.println("====完成工单失败！====" + jsonStr + "====" + name);
            result.setMessage("查询出错！" + ex.getMessage().substring(0, 100));
            return result;
        }
    }

    private TNPYResponse judgeEnoughMaterial(String outMaterial, String finishOrderID, double production) {
        TNPYResponse result = new TNPYResponse();
        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
        try {

            List<Map<Object, Object>> outMaterialProportion = materialMapper.selectProportionalityByOut(outMaterial);
            List<Map<Object, Object>> inputRecord = materialRecordMapper.selectBatchChargingByOrder(finishOrderID);
            String productionStr = materialRecordMapper.getProductionByOrderID(finishOrderID);
            Double productionALl = 0.0;
            try {
                productionALl = Double.parseDouble(productionStr);
            } catch (Exception ex) {
                productionALl = 0.0;
            }
            productionALl = productionALl + production;
            Map<String, Double> outMaterialProportionMap = new HashMap<String, Double>();
            Map<String, Double> inputRecordMap = new HashMap<String, Double>();

            String typeID = "";
            double number = -1.0;
            for (Map<Object, Object> proportionMap : outMaterialProportion) {
                typeID = null;
                number = -1;
                for (Map.Entry<Object, Object> entry : proportionMap.entrySet()) {
                    if (org.springframework.util.StringUtils.isEmpty(entry.getValue()))
                        break;
                    if ("typeID".equals(entry.getKey())) {
                        typeID = entry.getValue().toString();
                    }
                    if ("proportionality".equals(entry.getKey().toString()) && entry.getValue().toString().split(":").length == 2) {
                        number = Double.valueOf(entry.getValue().toString().split(":")[0]) / Double.valueOf(entry.getValue().toString().split(":")[1]);
                    }
                }
                if (number > -1) {
                    outMaterialProportionMap.put(typeID, number);
                }
            }
            for (Map<Object, Object> inputMap : inputRecord) {
                typeID = null;
                number = -1;
                for (Map.Entry<Object, Object> entry : inputMap.entrySet()) {
                    if ("typeID".equals(entry.getKey())) {
                        typeID = entry.getValue().toString();
                    }
                    if ("sum".equals(entry.getKey())) {
                        try {
                            number = Double.valueOf(entry.getValue().toString());
                        } catch (Exception ex) {
                            number = 0.0;
                        }
                    }
                }
                if (number > -1) {
                    inputRecordMap.put(typeID, number);
                }
            }
            for (Map.Entry<String, Double> entry : outMaterialProportionMap.entrySet()) {
                if (!inputRecordMap.containsKey(entry.getKey())) {

                    if (1 < entry.getValue() * productionALl) {
                        result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                        result.setMessage(materialTypeMapper.getTypeNameByID(entry.getKey()) + "投料不足，已投："
                                + "0" + "，至少需要投入：" + entry.getValue() * productionALl);
                        break;
                    } else {
                        break;
                    }
                }
                if (inputRecordMap.get(entry.getKey()) + 1.0 < entry.getValue() * productionALl) {
                    result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                    result.setMessage(materialTypeMapper.getTypeNameByID(entry.getKey()) + "投料不足，已投："
                            + inputRecordMap.get(entry.getKey()) + "，至少需要投入：" + entry.getValue() * productionALl);
                    break;
                }
            }
        } catch (Exception ex) {
            result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
            result.setMessage(ex.getMessage());
        }

        return result;
    }

    public TNPYResponse getPlanProductionDashboard(String plantID, String processID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> planProductionDashboardList;
            //暂时不显示包装丝网订单产量
//            if (ConfigParamEnum.BasicProcessEnum.BZProcessID.getName().equals(processID)) {
//                planProductionDashboardList = workOrderMapper.getPlanProductionBZDashboard(plantID, startTime.split(" ")[0], endTime.split(" ")[0]);
//            } else
            {
                planProductionDashboardList = workOrderMapper.getPlanProductionDashboard(plantID, processID, startTime, endTime);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(planProductionDashboardList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getRealtimeProductionDashboard(String plantID, String processID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> realtimeProductionDashboardList;
            if (ConfigParamEnum.BasicProcessEnum.JSProcessID.getName().equals(processID)) {

                startTime = startTime.split(" ")[0];
                endTime = endTime.split(" ")[0] + " 23:00";

                if (workOrderMapper.checkProcessOrderNumber(plantID, processID, startTime, endTime) > 0) {
                    if (workOrderMapper.getJSSumProduction(plantID, processID, startTime, endTime) < 2) {
                        realtimeProductionDashboardList = workOrderMapper.getRealtimeGainNumberDashboard(plantID, processID, startTime, endTime);
                        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                        result.setData(JSONObject.toJSON(realtimeProductionDashboardList).toString());
                        return result;
                    } else {
                        realtimeProductionDashboardList = workOrderMapper.getRealtimeProductionDashboard(plantID, processID, startTime, endTime);
                        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                        result.setData(JSONObject.toJSON(realtimeProductionDashboardList).toString());
                        return result;
                    }
                } else {
                    realtimeProductionDashboardList = workOrderMapper.getRealtimeGainNumberWithoutOrderDashboard(plantID, processID, startTime, endTime);
                    result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                    result.setData(JSONObject.toJSON(realtimeProductionDashboardList).toString());
                    return result;
                }

            }

            if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {

                realtimeProductionDashboardList = workOrderMapper.getZHQDRealtimeProductionDashboard(plantID, processID, startTime, endTime);
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSON(realtimeProductionDashboardList).toString());
                return result;
            }

            if (ConfigParamEnum.BasicProcessEnum.BZProcessID.getName().equals(processID)) {
                startTime = startTime.split(" ")[0];
                endTime = endTime.split(" ")[0] + " 23:00";

                if (workOrderMapper.checkProcessOrderNumber(plantID, processID, startTime, endTime) > 0) {
                    if (workOrderMapper.getJSSumProduction(plantID, processID, startTime, endTime) < 2) {
                        realtimeProductionDashboardList = workOrderMapper.getRealtimeGainNumberDashboard(plantID, processID, startTime, endTime);
                        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                        result.setData(JSONObject.toJSON(realtimeProductionDashboardList).toString());
                        return result;
                    } else {
                        realtimeProductionDashboardList = workOrderMapper.getRealtimeProductionDashboard(plantID, processID, startTime, endTime);
                        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                        result.setData(JSONObject.toJSON(realtimeProductionDashboardList).toString());
                        return result;
                    }
                } else {
                    realtimeProductionDashboardList = workOrderMapper.getBZRealtimeGainNumberWithoutOrderDashboard(plantID, startTime, endTime);
                    result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                    result.setData(JSONObject.toJSON(realtimeProductionDashboardList).toString());
                    return result;
                }

                //realtimeProductionDashboardList = workOrderMapper.getRealtimeProductionBZDashboard(plantID, startTime.split(" ")[0], endTime.split(" ")[0]);
            } else {
                realtimeProductionDashboardList = workOrderMapper.getRealtimeProductionDashboard(plantID, processID, startTime, endTime);
            }


            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(realtimeProductionDashboardList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse cancelFinishSuborder(String subOrdderID) {
        TNPYResponse result = new TNPYResponse();
        try {
            if (subOrdderID.contains("___")) {

                PileBatteryRecord pileBatteryRecord = pileBatteryRecordMapper.selectByPrimaryKey(subOrdderID.split("___")[0]);
                if ("5".equals(pileBatteryRecord.getStatus())) {
                    result.setMessage("该工单已投料，不能取消！");
                    return result;
                }
                if ("1".equals(pileBatteryRecord.getStatus())) {
                    result.setMessage("该工单尚未打堆！");
                    return result;
                }
                pileBatteryRecordMapper.cancelPileRecordSuborder(pileBatteryRecord.getId());
                tidyBatteryRecordMapper.updateCurrentNumAfterCancelPile(pileBatteryRecord.getTidyrecordid(), pileBatteryRecord.getFinishpilenum().intValue() + "");
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                return result;
            }
            GrantMaterialRecord grantMaterialRecord = grantMaterialRecordMapper.selectByOrderID(subOrdderID);
            if (grantMaterialRecord != null) {
                result.setMessage("该工单已发料，不能取消！");
                return result;
            }
            //orderSplitMapper.cancelFinishStatus(subOrdderID);
            if (workOrderMapper.cancelFinishWorkOrder(subOrdderID) < 1) {
                result.setMessage("取消失败，该工单尚未完工或已被使用！");
                return result;
            }
            orderSplitMapper.updateStatus(subOrdderID, StatusEnum.WorkOrderStatus.ordered.getIndex() + "");

            //浇铸时效硬化窑
            String jzflag = subOrdderID.substring(1, 10);
            if (jzflag.contains("JZ")) {
                dryingKilnJZRecordMapper.deleteBySubOrderId(subOrdderID);
            }
            if (jzflag.contains("TB")) {
                solidifyRecordMapper.deleteByPrimaryKey(subOrdderID);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse changePlanProductionRecord(String jsonStr) {
        TNPYResponse result = new TNPYResponse();
        try {
            PlanProductionRecord planProductionRecord = (PlanProductionRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), PlanProductionRecord.class);

            if (StringUtils.isEmpty(planProductionRecord.getId())) {
                if ("2".equals(planProductionRecord.getStatus())) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String startDate = planProductionRecord.getPlanmonth().split("___")[0];
                    String endDate = planProductionRecord.getPlanmonth().split("___")[1];
                    Date date = dateFormat.parse(startDate);
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    for (; ; ) {
                        planProductionRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                        planProductionRecord.setPlanmonth(dateFormat.format(calendar.getTime()));
                        planProductionRecordMapper.insertSelective(planProductionRecord);
                        calendar.add(Calendar.DATE, 1);
                        if (endDate.compareTo(dateFormat.format(calendar.getTime())) < 0) {
                            break;
                        }
                    }
                } else {
                    planProductionRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());

                    planProductionRecord.setStatus("1");

                    if(planProductionRecord.getPlandailyproduction()  ==null ||  planProductionRecord.getPlandailyproduction() < 1)
                    {
                        planProductionRecord.setPlandailyproduction(planProductionRecord.getPlanproduction()/30);
                    }
                    planProductionRecordMapper.insertSelective(planProductionRecord);
                }
            } else {
                if ("2".equals(planProductionRecord.getStatus())) {
                    planProductionRecord.setPlanmonth(planProductionRecord.getPlanmonth().split("___")[0]);
                }
                planProductionRecordMapper.updateByPrimaryKey(planProductionRecord);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("修改出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getPlanProductionRecord(String plantID, String processID, String startTime, String endTime, String slctType) {
        TNPYResponse result = new TNPYResponse();
        try {
            if ("1".equals(slctType)) {
                startTime = startTime.substring(0, 7);
                endTime = endTime.substring(0, 7);
            }
            String filter = " where planMonth >= '" + startTime + "' and planMonth <= '" + endTime + "' and status = '" + slctType + "' ";
            if (!"-1".equals(plantID)) {
                filter += " and plantID ='" + plantID + "' ";
            }
            if (!"-1".equals(processID)) {
                filter += " and processID ='" + processID + "' ";
            }
            filter += " order by plantID,processID,materialName,planMonth desc";

            List<PlanProductionRecord> planProductionRecordList = planProductionRecordMapper.getPlanProductionRecordByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(planProductionRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse deletePlanProductionRecord(String id) {
        TNPYResponse result = new TNPYResponse();
        try {
            planProductionRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getPlanProductionNumber(String plantID, String processID, String planMonth) {
        TNPYResponse result = new TNPYResponse();
        try {
            String strNum = planProductionRecordMapper.getplanNumber(plantID, processID, planMonth);

            int dailyProduction = 0;
            try {
                dailyProduction = (int) Double.parseDouble(strNum.toString());
            } catch (Exception ex) {
                dailyProduction = 0;
            }
            result.setData(dailyProduction + "");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse changeOnlineMaterialRecord(String jsonStr) {
        TNPYResponse result = new TNPYResponse();
        try {
            OnlineMaterialRecord onlineMaterialRecord = (OnlineMaterialRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), OnlineMaterialRecord.class);

            if (StringUtils.isEmpty(onlineMaterialRecord.getId())) {
                onlineMaterialRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                onlineMaterialRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                onlineMaterialRecord.setUpdatetime(new Date());
                onlineMaterialRecordMapper.insertSelective(onlineMaterialRecord);
            } else {
                onlineMaterialRecordMapper.updateByPrimaryKey(onlineMaterialRecord);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        } catch (Exception ex) {
            result.setMessage("修改出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getOnlineMaterialRecord(String plantID, String processID, String lineID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = " where updateTime >= '" + startTime + "' and updateTime <= '" + endTime + "' ";
            if (!"-1".equals(plantID)) {
                filter += " and plantID ='" + plantID + "' ";
            }
            if (!"-1".equals(processID)) {
                filter += " and processID ='" + processID + "' ";
            }
            if (!"-1".equals(lineID)) {
                filter += " and lineID ='" + lineID + "' ";
            }
            filter += " order by status,updateTime desc";
            List<OnlineMaterialRecord> onlineMaterialRecordList = onlineMaterialRecordMapper.getOnlineMaterialRecordByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(onlineMaterialRecordList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    private TNPYResponse mergeOnlineMaterialRecordJS(String mergeID, String operator, String inputNumber) {
        TNPYResponse result = new TNPYResponse();
        try {
            String mergeIDList = "'" + mergeID.replace(",", "','") + "'";

            List<OnlineMaterialRecord> onlineMaterialRecordList = onlineMaterialRecordMapper.getMergeNum(mergeIDList);

            if (onlineMaterialRecordList.size() > 1) {
                result.setMessage("请确认合并的是同一产线，同一物料！当前种类数量是" + onlineMaterialRecordList.size());
                return result;
            }
            if (onlineMaterialRecordList.size() < 1) {
                result.setMessage("未找到记录！");
                return result;
            }

            if ("-1".equals(inputNumber)) {
                inputNumber = onlineMaterialRecordList.get(0).getMaterialnum().toString();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            Date now = new Date();
            OnlineMaterialRecord onlineMaterialRecord = new OnlineMaterialRecord();
            onlineMaterialRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            onlineMaterialRecord.setUpdatetime(now);
            onlineMaterialRecord.setStatus("3");
            onlineMaterialRecord.setMaterialid(onlineMaterialRecordList.get(0).getMaterialid());
            onlineMaterialRecord.setPlantid(onlineMaterialRecordList.get(0).getPlantid().split("###")[0]);
            onlineMaterialRecord.setProcessid(onlineMaterialRecordList.get(0).getProcessid().split("###")[0]);
            onlineMaterialRecord.setLineid(onlineMaterialRecordList.get(0).getLineid().split("###")[0]);
            onlineMaterialRecord.setMaterialnum(Integer.parseInt(inputNumber));
            onlineMaterialRecord.setClasstype((onlineMaterialRecordList.get(0).getMaterialnum() - Integer.parseInt(inputNumber)) + "");
            onlineMaterialRecord.setOperator(operator);
            onlineMaterialRecordMapper.insertSelective(onlineMaterialRecord);

            onlineMaterialRecordMapper.updateStatus(mergeIDList, "2");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    private TNPYResponse mergeOnlineMaterialRecordZH(String mergeID, String operator, String inputNumber) {
        TNPYResponse result = new TNPYResponse();
        try {
            String mergeIDList = "'" + mergeID.replace(",", "','") + "'";

            List<OnlineMaterialRecord> onlineMaterialRecordList = onlineMaterialRecordMapper.getMergeNum(mergeIDList);
            if (onlineMaterialRecordList.size() > 1) {
                result.setMessage("请确认合并的是同一产线，同一物料！当前种类数量是" + onlineMaterialRecordList.size());
                return result;
            }

            if (onlineMaterialRecordList.size() < 1) {
                result.setMessage("未找到记录！");
                return result;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date now = new Date();
            String orderName = onlineMaterialRecordList.get(0).getPlantid().split("###")[1] + onlineMaterialRecordList.get(0).getProcessid().split("###")[1] + onlineMaterialRecordList.get(0).getLineid().split("###")[1]+ "WXRK" + dateFormat.format(now);
            if ("-1".equals(inputNumber)) {
                inputNumber = onlineMaterialRecordList.get(0).getMaterialnum().toString();
            }
            double dbNum = Integer.parseInt(inputNumber);

            OnlineMaterialRecord onlineMaterialRecord = new OnlineMaterialRecord();
            onlineMaterialRecord.setId(orderName);
            onlineMaterialRecord.setUpdatetime(now);
            onlineMaterialRecord.setStatus("3");
            onlineMaterialRecord.setMaterialid(onlineMaterialRecordList.get(0).getMaterialid());
            onlineMaterialRecord.setPlantid(onlineMaterialRecordList.get(0).getPlantid().split("###")[0]);
            onlineMaterialRecord.setProcessid(onlineMaterialRecordList.get(0).getProcessid().split("###")[0]);
            onlineMaterialRecord.setLineid(onlineMaterialRecordList.get(0).getLineid().split("###")[0]);
            onlineMaterialRecord.setMaterialnum(Integer.parseInt(inputNumber));
            onlineMaterialRecord.setClasstype((onlineMaterialRecordList.get(0).getMaterialnum() - Integer.parseInt(inputNumber)) + "");
            onlineMaterialRecord.setOperator(operator);
            onlineMaterialRecordMapper.insertSelective(onlineMaterialRecord);

            OrderSplit orderSplit = new OrderSplit();
            orderSplit.setId(orderName);
            orderSplit.setMaterialid(onlineMaterialRecordList.get(0).getMaterialid());

            orderSplit.setProductionnum(dbNum);
            orderSplit.setOrdersplitid(orderName);
            orderSplit.setOrderid(orderName);
            orderSplit.setStatus(StatusEnum.WorkOrderStatus.finished.getIndex() + "");
            orderSplitMapper.insertSelective(orderSplit);

            MaterialRecord materialRecord = new MaterialRecord();
            materialRecord.setId(orderName);
            materialRecord.setNumber(orderSplit.getProductionnum());
            materialRecord.setInorout(StatusEnum.InOutStatus.Input.getIndex());
            materialRecord.setInputer(operator);
            materialRecord.setOrderid(orderName.substring(0,orderName.length()-6));
            materialRecord.setSuborderid(orderName);
            materialRecord.setMaterialid(orderSplit.getMaterialid());
            materialRecord.setInputtime(new Date());
            materialRecord.setStatus(StatusEnum.WorkOrderStatus.repairin.getIndex());
            materialRecord.setInputplantid(onlineMaterialRecord.getPlantid());
            materialRecord.setInputprocessid(onlineMaterialRecord.getProcessid());
            materialRecord.setInputlineid(onlineMaterialRecord.getLineid());
            materialRecord.setMaterialnameinfo(onlineMaterialRecordList.get(0).getOperator());
            materialRecordMapper.insertSelective(materialRecord);

            onlineMaterialRecordMapper.updateStatus(mergeIDList, "2");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse mergeOnlineMaterialRecord(String mergeID, String operator, String processID, String inputNumber) {
        if (ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName().equals(processID)) {
            return mergeOnlineMaterialRecordZH(mergeID, operator, inputNumber);
        }
        if (ConfigParamEnum.BasicProcessEnum.JSProcessID.getName().equals(processID)) {
            return mergeOnlineMaterialRecordJS(mergeID, operator, inputNumber);
        }
        TNPYResponse result = new TNPYResponse();
        result.setMessage("该工段没有线边仓功能，如需添加请联系开发人员！");
        return result;
    }

    public TNPYResponse deleteOnlineMaterialRecord(String id) {
        TNPYResponse result = new TNPYResponse();
        try {
            onlineMaterialRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    @Override
    public TNPYResponse cancelInputSuborder(String subOrdderID) {
        TNPYResponse result = new TNPYResponse();
        try {
            if (subOrdderID.contains("___")) {
                pileBatteryRecordMapper.cancelInputSuborder(subOrdderID.split("___")[0]);
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                return result;
            }
            MaterialRecord materialRecord = new MaterialRecord();
            materialRecord.setId(subOrdderID);
            materialRecord.setExpendorderid("");
            materialRecord.setInorout(new Integer(1));
            materialRecord.setOutputer("");
            materialRecord.setOutputtime(new Date(0));
            int res = materialRecordMapper.updateCancelInputSuborder(materialRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse pushinDryingKilnJZByBatch(String orderIDList, String name, String equipmentID) {
        TNPYResponse result = new TNPYResponse();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByPrimaryKey(equipmentID);
            if (equipmentInfo == null) {
                //result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage("不是正确的干燥窑二维码");
                return result;
            }
            //判断是否是浇铸干燥窑二维码结束
            //判断浇铸干燥窑是否已满
            int exitsIndry = dryingKilnJZRecordMapper.selectExitsInDryRecord(equipmentID);
            //Integer capacity = ConfigParamEnum.EquipmentCapacity.DRYKILNZY.getNum();
            //如果窑满了
            if (exitsIndry + orderIDList.split("###").length > (int) (ConfigParamEnum.DryFilnCapacityMap.get(equipmentID))) {
                // result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage(equipmentInfo.getName() + "已有" + exitsIndry + "拖，最多能够入!" + ConfigParamEnum.DryFilnCapacityMap.get(equipmentID) + "拖！");
                return result;
            }

            String filter = " where id in ('" + orderIDList.replaceAll("###", "','") + "' )";
            List<OrderSplit> orderInfoList = orderSplitMapper.selectByFilter(filter);
            List<Map<String, String>> grantResult = new ArrayList<Map<String, String>>();
            String[] orderArray = orderIDList.split("###");
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

                    if (!(StatusEnum.WorkOrderStatus.finished.getIndex() + "").equals(orderSplit.getStatus())) {
                        mapResult.put("returnMessage", "该订单尚未完成！" + orderSplitID);
                        break;
                    }

                    List<DryingKilnJZRecord> existsRecord = dryingKilnJZRecordMapper.selectBySuborderid(orderSplit.getOrdersplitid());
                    if (existsRecord != null && existsRecord.size() > 0) { //判断改子工单是否取消完成后存在表中记录, 若存在则修改记录

                        if ("2".equals(existsRecord.get(0).getStatus())) {
                            mapResult.put("returnMessage", "发生错误,出现工单已出窑,入窑人" + existsRecord.get(0).getInputername() + dateFormat.format(existsRecord.get(0).getInputtime()) +
                                    ",出窑人" + existsRecord.get(0).getOutputername() + dateFormat.format(existsRecord.get(0).getOutputtime()));
                        } else {
                            mapResult.put("returnMessage", "发生错误,出现工单已入窑" + existsRecord.get(0).getInputername() + dateFormat.format(existsRecord.get(0).getInputtime()));
                        }
                        break;
                    }

                    MaterialRecord materialRecord = materialRecordMapper.selectBySuborderIDAndInOut(orderSplit.getId(), StatusEnum.InOutStatus.PreInput.getIndex() + "");
                    if (materialRecord == null) {
                        mapResult.put("returnMessage", "未获取到入库信息！请重试或者重新入库！" + orderSplitID);
                        break;
                    }
                    try {
                        DryingKilnJZRecord dryingKilnJZRecord = new DryingKilnJZRecord();
                        dryingKilnJZRecord.setId(materialRecord.getSuborderid());
                        dryingKilnJZRecord.setDryingkilnid(equipmentInfo.getId());
                        dryingKilnJZRecord.setDryingkilnname(equipmentInfo.getName());
                        // dryingKilnJZRecord.setInputerid(name);
                        dryingKilnJZRecord.setInputername(name);
                        dryingKilnJZRecord.setInputtime(new Date());
                        dryingKilnJZRecord.setLineid(materialRecord.getInputlineid());
                        dryingKilnJZRecord.setMaterialid(materialRecord.getMaterialid());
                        dryingKilnJZRecord.setMaterialname(materialRecord.getMaterialnameinfo());
                        dryingKilnJZRecord.setMaterialquantity(materialRecord.getNumber().intValue());
                        dryingKilnJZRecord.setPlantid(materialRecord.getInputplantid());
                        dryingKilnJZRecord.setStatus(StatusEnum.InOutStatus.Input.getIndex() + "");
                        dryingKilnJZRecord.setSuborderid(materialRecord.getSuborderid());
                        dryingKilnJZRecord.setWorklocationid(materialRecord.getInputworklocationid());
                        dryingKilnJZRecordMapper.insert(dryingKilnJZRecord);
                        mapResult.put("status", "成功");
                        mapResult.put("returnMessage", "");
                    } catch (Exception ex) {
                        mapResult.put("status", "失败");
                        mapResult.put("returnMessage", "该工单已入窑!");
                    }
                }
                grantResult.add(mapResult);
            }
            result.setData(JSONObject.toJSON(grantResult).toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("入窑失败！" + ex.getMessage());
            return result;
        }
    }

    //浇铸入窑记录
    @Override
    public TNPYResponse pushInDryingKilnjzsuborder(String jsonStr, String name) {
        TNPYResponse result = new TNPYResponse();
        try {
            //判断是否是浇铸干燥窑二维码
            DryingKilnJZRecord dry = (DryingKilnJZRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), DryingKilnJZRecord.class);
            EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByPrimaryKey(dry.getDryingkilnid());
            if (equipmentInfo == null) {
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage("不是正确的干燥窑二维码");
                return result;
            }
            //判断是否是浇铸干燥窑二维码结束
            //判断浇铸干燥窑是否已满
            int exitsIndry = dryingKilnJZRecordMapper.selectExitsInDryRecord(dry.getDryingkilnid());
            Integer capacity = ConfigParamEnum.EquipmentCapacity.DRYKILNZY.getNum();
            //如果窑满了
            if (exitsIndry > capacity) {
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage(equipmentInfo.getName() + "中已满, 请批量出窑后再入窑!!");
                return result;
            }
            String[] inputterInfo = name.split("###");
            TNPYResponse resultInsertRecord = finishOrderSplit(jsonStr, name);
            if (resultInsertRecord.getStatus() != StatusEnum.ResponseStatus.Success.getIndex()) {
                return resultInsertRecord;
            }
            OrderSplit orderSplit = (OrderSplit) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), OrderSplit.class);
            boolean blJZ = ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(workOrderMapper.getProcessIDByOrder(orderSplit.getOrderid()));
            //浇铸入窑
            try {
                if (blJZ) {
                    List<DryingKilnJZRecord> existsRecord = dryingKilnJZRecordMapper.selectBySuborderid(orderSplit.getOrdersplitid());
                    if (existsRecord != null && existsRecord.size() > 0) { //判断改子工单是否取消完成后存在表中记录, 若存在则修改记录
                        if (existsRecord.size() > 1) {
                            result.setMessage("发生错误,出现 一条orderSplit对应多条浇铸干燥窑记录");
                            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                            return result;
                        } else {
                            //设置ID
                            dry.setId(existsRecord.get(0).getId());
                            //设置入窑时间为调用接口时间
                            dry.setInputtime(new Date());
                            //设置物料名称
//            		    	dry.setMaterialname(materialMapper.selectByPrimaryKey(dry.getMaterialid()).getName());
                            //设置suborderid
                            dry.setSuborderid(orderSplit.getOrdersplitid());
                            //设置干燥窑名称
                            dry.setDryingkilnname(equipmentInfoMapper.selectByPrimaryKey(dry.getDryingkilnid()).getName());
                            if (inputterInfo.length > 2) {
                                dry.setInputername(inputterInfo[0]);
                                dry.setInputerid(inputterInfo[1]);
                            }
                            Integer inOrOut = StatusEnum.InOutStatus.Input.getIndex();
                            dry.setStatus(inOrOut.toString());
                            dryingKilnJZRecordMapper.updateByPrimaryKey(dry);
                        }
                    } else { //若工单为首次点击完成入窑, 则新增记录
                        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                        //设置新的主键ID值
                        dry.setId(uuid);
                        //设置入窑时间为调用接口时间
                        dry.setInputtime(new Date());
                        //设置物料名称
//        		    	dry.setMaterialname(materialMapper.selectByPrimaryKey(dry.getMaterialid()).getName());
                        //设置suborderid
                        dry.setSuborderid(orderSplit.getOrdersplitid());
                        //设置干燥窑名称
                        dry.setDryingkilnname(equipmentInfoMapper.selectByPrimaryKey(dry.getDryingkilnid()).getName());
                        if (inputterInfo.length > 2) {
                            dry.setInputername(inputterInfo[0]);
                            dry.setInputerid(inputterInfo[1]);
                        }
                        Integer inOrOut = StatusEnum.InOutStatus.Input.getIndex();
                        dry.setStatus(inOrOut.toString());
                        dryingKilnJZRecordMapper.insert(dry);
                    }
                    result.setMessage("入窑成功!");
                    result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                    return result;
                }
            } catch (Exception ex) {
                result.setMessage(result.getMessage() + " " + ex.getMessage());
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    //出窑
    @Override
    public TNPYResponse pushOutDryingKilnjzsuborder(String jsonStr, String name) {
        TNPYResponse result = new TNPYResponse();
        try {
            DryingKilnJZRecord dry = (DryingKilnJZRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), DryingKilnJZRecord.class);
            EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByPrimaryKey(dry.getDryingkilnid());
            if (equipmentInfo == null) {
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage("不是正确的干燥窑二维码");
                return result;
            }
            List<DryingKilnJZRecord> dryList = dryingKilnJZRecordMapper.selectByDryingKilnIDAndStatus(dry.getDryingkilnid());
            if (dryList == null || dryList.size() == 0) {
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage("无需出窑, 该窑中没有板栅");
                return result;
            }
            //设置tb_ordersplit状态
            //设置materialRecord状态
            materialRecordMapper.updateByDryingkilnid(dry.getDryingkilnid(), StatusEnum.InOutStatus.Input.getIndex());
            //更新干燥窑outputer/outputtime/状态信息
            dryingKilnJZRecordMapper.updateByDryingKilnIDAndStatus(dry.getOutputerid(), dry.getOutputername(), new Date(), StatusEnum.InOutStatus.Output.getIndex(), dry.getDryingkilnid(), StatusEnum.InOutStatus.Input.getIndex());
            result.setMessage("批量出窑成功!");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse orderOutOfDryingKiln(String plantID, String processID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> oderList = null;
            if (ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(processID.trim())) {
                oderList = dryingKilnJZRecordMapper.orderOutOfDryingKiln(plantID, ConfigParamEnum.BasicProcessEnum.JZProcessID.getName(), startTime, endTime);
            }

            if (null != oderList) {
                result.setData(JSONObject.toJSON(oderList).toString());
                result.setMessage("查询成功!");
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse addWorkorderTemplateBatch(String orderForepart, String orderMidpiece, String orderPosterior, String creator, String recordJsonString) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<WorkOrderTemplate> workOrderTemplateList = JSON.parseArray(recordJsonString, WorkOrderTemplate.class);

            if (workOrderTemplateList.size() < 1) {
                result.setMessage("模板信息为空，请确认信息！");
                return result;
            }
            String timeStart = "";
            if (orderPosterior.startsWith("BB")) {
                timeStart = orderPosterior.substring(2, 6) + "-" + orderPosterior.substring(6, 8) + "-" + orderPosterior.substring(8, 10) + " 07:00:00";
            } else {
                timeStart = orderPosterior.substring(2, 6) + "-" + orderPosterior.substring(6, 8) + "-" + orderPosterior.substring(8, 10) + " 19:00:00";
            }

            List<Map<Object, Object>> lineInfoList = workOrderMapper.getLineShortNameList(workOrderTemplateList.get(0).getPlantid(), workOrderTemplateList.get(0).getProcessid());
            List<String> idList = workOrderMapper.selectOrderIDListByProcess(workOrderTemplateList.get(0).getProcessid(), timeStart);

            Map<String, String> lineShortNameMap = new HashMap<>();
            for (int j = 0; j < lineInfoList.size(); j++) {
                lineShortNameMap.put(lineInfoList.get(0).get("id").toString(), lineInfoList.get(0).get("shortname").toString());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<OrderSplit> orderSplitList = new ArrayList<>();
            Workorder workorder = new Workorder();
            String numStr = "";
            for (int i = 0; i < workOrderTemplateList.size(); i++) {
                orderSplitList.clear();
                numStr ="";
                workorder.setStatus(StatusEnum.WorkOrderStatus.ordered.getIndex() + "");
                workorder.setCreatetime(new Date());
                workorder.setOpenstaff(creator);
                workorder.setScheduledstarttime(dateFormat.parse(timeStart));
                workorder.setBatchnum(workOrderTemplateList.get(i).getBatchnum());
                workorder.setTotalproduction(workOrderTemplateList.get(i).getTotalproduction());
                workorder.setMaterialid(workOrderTemplateList.get(i).getMaterialid());
                workorder.setUnits(orderMidpiece);
                workorder.setPlantid(workOrderTemplateList.get(i).getPlantid());
                workorder.setProcessid(workOrderTemplateList.get(i).getProcessid());
                workorder.setLineid(workOrderTemplateList.get(i).getLineid());

                for (int m = 0; ; m++) {
                    numStr = orderForepart + lineShortNameMap.get(workOrderTemplateList.get(i).getLineid()) + orderMidpiece + String.valueOf(m + 1)
                            + orderPosterior;
                    if (idList.contains(numStr)) {
                        continue;
                    } else
                        break;
                }
                if (numStr.length() < 3) {
                    result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                    result.setMessage("自定义工单失败！");
                    return result;
                }
                workorder.setOrderid(numStr);
                workorder.setId(numStr);
                workOrderMapper.insertSelective(workorder);
                idList.add(numStr);

                for (int n = 0; n < workorder.getBatchnum(); n++) {
                    OrderSplit orderSplit = new OrderSplit();
                    orderSplit.setOrderid(workorder.getId());
                    orderSplit.setStatus(StatusEnum.WorkOrderStatus.ordered.getIndex() + "");
                    orderSplit.setMaterialid(workorder.getMaterialid());
                    orderSplit.setOrdersplitid(workorder.getOrderid() + getOrderNumber(n + 1, 3));
                    orderSplit.setId(orderSplit.getOrdersplitid());
                    orderSplit.setProductionnum(workorder.getTotalproduction() / workorder.getBatchnum() * 1.0);
                    orderSplitList.add(orderSplit);
                }
                orderSplitMapper.insertManyOrder(orderSplitList, workorder.getId());

            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        } catch (Exception ex) {

            result.setMessage("修改出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse addWorkorderTemplate(String jsonStr) {
        TNPYResponse result = new TNPYResponse();
        try {
            WorkOrderTemplate workOrderTemplate = (WorkOrderTemplate) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), WorkOrderTemplate.class);

            if (StringUtils.isEmpty(workOrderTemplate.getId())) {

                workOrderTemplate.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                workOrderTemplate.setCreatetime(new Date());
                workOrderTemplate.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                workOrderTemplateMapper.insertSelective(workOrderTemplate);
            } else {
                workOrderTemplateMapper.updateByPrimaryKey(workOrderTemplate);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        } catch (Exception ex) {

            result.setMessage("修改出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse deleteWorkorderTemplate(String id) {
        TNPYResponse result = new TNPYResponse();
        try {
            workOrderTemplateMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getWorkorderTemplate(String plantID, String processID, String lineID) {
        TNPYResponse result = new TNPYResponse();
        try {
            String filter = "where status != '-1' and  plantID ='" + plantID + "' and processID = '" + processID + "' ";
            if (!"-1".equals(lineID)) {
                filter += " and lineID = '" + lineID + "' ";
            }
            List<Map<Object, Object>> workorderTemplateList = workOrderTemplateMapper.selectWorkOrderTemplateByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(workorderTemplateList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

	@Override
	public TNPYResponse workOrderPutIntoManage(String plantID, String processID, String lineID, String startTime, String endTime,
			String classType) {
		TNPYResponse result = new TNPYResponse();
        try {
        	// 判断日期是否合法
			if (!DateUtilsDef.isDateCheck(startTime) && !DateUtilsDef.isDateCheck(endTime)) {
				result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
				result.setMessage("日期不合法");
				return result;
			}
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	StringBuilder sqlfilter  = new StringBuilder();
        	sqlfilter.append("1 = 1  ") ;
        	if (!"-1".equals(plantID)) {
        		sqlfilter .append(" and m.outputPlantID = '" + plantID + "' ") ;
            }else {
            	result.setMessage("请选择厂区");
                return result;
            }
        	if (!"-1".equals(processID)) {
        		sqlfilter .append(" and m.outputProcessID = '" + processID + "' ") ;
            }else {
            	result.setMessage("请选择工序");
                return result;
            }
        	if (!"-1".equals(lineID)) {
        		sqlfilter .append(" and m.outputLineID = '" + lineID + "' ") ;
            }
        	
        	sqlfilter.append(" and DATE_FORMAT( m.outputTime ,'%Y-%m-%d') >= DATE_FORMAT( '"+startTime+"' ,'%Y-%m-%d') ");
        	sqlfilter.append(" and DATE_FORMAT( m.outputTime ,'%Y-%m-%d') <= DATE_FORMAT( '"+endTime+"' ,'%Y-%m-%d') ");
        	
        	//如果是包板
            if (ConfigParamEnum.BasicProcessEnum.BBProcessID.getName().equals(processID.trim())) {
            	if ("白班".equals(classType)) {
            		sqlfilter.append(" and m.expendOrderID REGEXP 'BB*BB' ");
                } 
            	if ("夜班".equals(classType))  {
            		sqlfilter.append(" and m.expendOrderID REGEXP 'BB*YB' ");
                }
            	if ("全部".equals(classType))  {
                }
            	
            }else {
            	if ("白班".equals(classType)) {
            		sqlfilter.append(" and m.expendOrderID REGEXP 'BB' ");
                } 
            	if ("夜班".equals(classType))  {
            		sqlfilter.append(" and m.expendOrderID REGEXP 'YB' ");
                }
            	if ("全部".equals(classType))  {
                }
            }
          
            List<LinkedHashMap<Object, Object>> workorderTemplateList = workOrderTemplateMapper.workOrderPutIntoManage(sqlfilter.toString());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSONString(workorderTemplateList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
	}
}
