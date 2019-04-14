package com.tnpy.mes.service.workOrderService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
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
    private  BatchrelationcontrolMapper batchrelationcontrolMapper;

    @Autowired
    private  GrantMaterialRecordMapper grantMaterialRecordMapper;

    @Autowired
    private  PlanProductionRecordMapper planProductionRecordMapper;

    @Autowired
    private  OnlineMaterialRecordMapper onlineMaterialRecordMapper;
    
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;
    
    @Autowired
    private DryingKilnJZRecordMapper dryingKilnJZRecordMapper;

    public TNPYResponse getWorkOrder( ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Workorder> workOrderList = workOrderMapper.selectAll();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getSubOrderByID( String id,String type )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<OrderSplit> orderSplitList = null;

            if("2".equals(type.trim()))
            {
                orderSplitList  = orderSplitMapper.selectAfterMapBySubOrderName(id);
            }
            else
            {
                orderSplitList  = orderSplitMapper.selectAfterMapBySubOrderID(id);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
           // System.out.println(id + "============" + orderSplitList.size());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getSubOrderByIDToMap( String id ,String type,String plantID,String processID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<String, String>> orderSplitList = null;

            if("2".equals(type.trim()))
            {
                orderSplitList  = orderSplitMapper.selectToMapBySubOrderName(id,plantID,processID);
            }
            else
            {
                orderSplitList  = orderSplitMapper.selectToMapBySubOrderID(id,plantID,processID);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            // System.out.println(id + "============" + orderSplitList.size());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getWorkOrderByLineID( String lineID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            dateFormat.format(date);
            String timeFinish = "";
            String timeStart = "";
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if(calendar.get(Calendar.HOUR_OF_DAY) < 7)
            {
              //  timeFinish = dateFormat.format(date) + " 06:00:00";
                timeFinish = "2020-11-11 00:00:00";
                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                timeStart  =dateFormat.format(date) + " 18:00:00";
            }
            else  if(calendar.get(Calendar.HOUR_OF_DAY) > 18)
            {
                timeStart = dateFormat.format(date) + " 18:00:00";
                timeFinish = "2020-11-11 00:00:00";
               // timeFinish = dateFormat.format(date) + " 20:00:00";
            }
            else
            {
                timeStart = dateFormat.format(date) + " 06:00:00";
                timeFinish = "2020-11-11 00:00:00";
                //timeFinish = dateFormat.format(date) + " 08:00:00";
            }

            List<Workorder> workOrderList = workOrderMapper.selectByFilter(" where lineID = '" + lineID + "' and status < 4  and scheduledStartTime <='" + timeFinish
                    + "' and  scheduledStartTime >= '" +timeStart + "' order by  scheduledStartTime  ");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getWorkOrderByParam(String plantID,String processID,String lineID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = "where  plantID ='" + plantID + "' and processID = '" + processID + "' ";
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            List<Workorder> workOrderList = workOrderMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getCustomWorkOrderByParam(String plantID,String processID,String lineID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = "where  plantID ='" + plantID + "' and processID = '" + processID + "' ";
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            filter += " order by createTime desc limit 1000 ";
            List<CustomWorkOrderRecord> workOrderList = workOrderMapper.selectCustomResultByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(workOrderList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    private  String getOrderNumber(int number,int length)
    {
        String numStr = String.valueOf(number);
        String tmp = "";
        for(int i =0;i<length - numStr.length();i++)
        {
            tmp += "0";
        }
        return  tmp+numStr;
    }

    public TNPYResponse changeWorkOrder( String jsonStr ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            Workorder workorder=(Workorder) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), Workorder.class);

            if(StringUtils.isEmpty(workorder.getId()))
            {
                //workorder.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                workorder.setStatus(StatusEnum.WorkOrderStatus.ordered.getIndex() + "");
                workorder.setCreatetime(new Date());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               // System.out.println(dateFormat.format(workorder.getScheduledstarttime()));
                int orderNum = workOrderMapper.selectOrderNumber(workorder.getLineid(),dateFormat.format(workorder.getScheduledstarttime()));
                String numStr = workorder.getOrderid().substring(0,workorder.getOrderid().length() - 10) + String.valueOf(orderNum+1)
                        + workorder.getOrderid().substring(workorder.getOrderid().length() - 10,workorder.getOrderid().length() );
                workorder.setOrderid(numStr);
                workorder.setId(numStr);
                workOrderMapper.insertSelective(workorder);

                List<OrderSplit> orderSplitList = new ArrayList<>();
                for(int i =0 ;i<workorder.getBatchnum();i++) {
                    OrderSplit orderSplit = new OrderSplit();
                    //orderSplit.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                    orderSplit.setOrderid(workorder.getId());
                    orderSplit.setStatus(StatusEnum.WorkOrderStatus.ordered.getIndex() + "");
                    orderSplit.setMaterialid(workorder.getMaterialid());
                    orderSplit.setOrdersplitid(workorder.getOrderid() + getOrderNumber(i + 1,3));
                    orderSplit.setId(orderSplit.getOrdersplitid());
                    orderSplit.setProductionnum(workorder.getTotalproduction()/workorder.getBatchnum() *1.0);
                    orderSplitList.add(orderSplit);

                }
                orderSplitMapper.insertManyOrder(orderSplitList,workorder.getId());
            }
            else
            {
                workOrderMapper.updateByPrimaryKey(workorder);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("修改出错！" + ex.getMessage());
            return  result;
        }
    }


    public TNPYResponse getOrderSplit(String orderID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {

            List<OrderSplit > orderSplitList = orderSplitMapper.selectByOrderID(orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }


    public TNPYResponse getOrderSplitAfterMap(String orderID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<OrderSplit > orderSplitList = orderSplitMapper.selectAfterMapByOrderID(orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getOrderSplitToMap(String orderID ) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<String, String>> orderSplitList = orderSplitMapper.selectToMapByOrderID(orderID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(orderSplitList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeWorkOrderStatus( String ID,String status )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String productionOrder = materialRecordMapper.getProductionByOrderID(ID);
            if (!org.springframework.util.StringUtils.isEmpty(productionOrder))
            {
                result.setMessage("该工单已有入库记录不能删除！" );
                return  result;
            }
            orderSplitMapper.deleteByOrderID(ID);
            workOrderMapper.updateWorkOrderStatus(ID,status);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            //  result.setData(JSONObject.toJSONString(equipmentTypeList, SerializerFeature.WriteMapNullValue).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse finishOrderSplit( String jsonStr ,String name) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String[] inputterInfo = name.split("###");
            OrderSplit orderSplit=(OrderSplit) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), OrderSplit.class);

            OrderSplit orderSplitTMP = orderSplitMapper.selectByPrimaryKey(orderSplit.getId());
            if(orderSplitTMP.getStatus().equals(StatusEnum.WorkOrderStatus.finished.getIndex() + ""))
            {
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage("该工单已完成！" );
                return  result;
            }
            TNPYResponse judgeResult = judgeEnoughMaterial(orderSplit.getMaterialid(),orderSplit.getOrderid(),orderSplit.getProductionnum());
            if(judgeResult.getStatus() != StatusEnum.ResponseStatus.Success.getIndex())
            {
               // System.out.println(JSONObject.toJSON(judgeResult).toString());
                return  judgeResult;
            }

            orderSplit.setStatus(StatusEnum.WorkOrderStatus.finished.getIndex() + "");
            orderSplitMapper.updateByPrimaryKeySelective(orderSplit);

            MaterialRecord materialRecord = new MaterialRecord();
            materialRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            materialRecord.setInorout(StatusEnum.InOutStatus.Input.getIndex());
            materialRecord.setMaterialid(orderSplit.getMaterialid());
            materialRecord.setNumber(orderSplit.getProductionnum());
            materialRecord.setOrderid(orderSplit.getOrderid());
            materialRecord.setSuborderid(orderSplit.getId());
            materialRecord.setInputtime(new Date());
            materialRecord.setInputer(name);
            if(inputterInfo.length >3)
            {
                materialRecord.setInputer(inputterInfo[0]);
                materialRecord.setInputerid(inputterInfo[1]);
                materialRecord.setInputworklocationid(inputterInfo[2]);
                materialRecord.setMaterialnameinfo(inputterInfo[3]);
            }
            Workorder workorder = workOrderMapper.selectByPrimaryKey(orderSplit.getOrderid());
            if(workorder != null)
            {
                materialRecord.setInputplantid(workorder.getPlantid());
                materialRecord.setInputprocessid(workorder.getProcessid());
                materialRecord.setInputlineid(workorder.getLineid());

            }
            if(ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(materialRecord.getInputprocessid()))
            {
                materialRecord.setInorout(StatusEnum.InOutStatus.PreInput.getIndex());
            }
            materialRecordMapper.insert(materialRecord);
            boolean blTB = ConfigParamEnum.BasicProcessEnum.TBProcessID.getName().equals(workOrderMapper.getProcessIDByOrder(orderSplit.getOrderid()));
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

            try
            {
                if(blTB)
                {
                    String batchID = batchrelationcontrolMapper.selectTBBatchByOrderID(orderSplit.getOrderid());
                    if(org.springframework.util.StringUtils.isEmpty(batchID) || "null".equals(batchID.trim()) || batchID.length() < 6)
                    {
                        Batchrelationcontrol batchrelationcontrol = new Batchrelationcontrol();
                        batchrelationcontrol.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                        batchrelationcontrol.setRelationorderid(orderSplit.getOrderid());
                        batchrelationcontrol.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                        batchrelationcontrol.setRelationtime(new Date());
                        String batch = orderSplit.getOrdersplitid().substring(0,orderSplit.getOrdersplitid().length() - 13)
                                + orderSplit.getOrdersplitid().substring(orderSplit.getOrdersplitid().length() - 11,orderSplit.getOrdersplitid().length() - 3);
                        batchrelationcontrol.setTbbatch(batch);
                        batchrelationcontrolMapper.insert(batchrelationcontrol);
                    }
                }
            }catch (Exception ex)
            {
                result.setMessage(result.getMessage() + " " +ex.getMessage() );
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    private TNPYResponse judgeEnoughMaterial(String outMaterial,String finishOrderID,double production)
    {
        TNPYResponse result = new TNPYResponse();
        result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
        try
        {

            List<Map<Object, Object>> outMaterialProportion = materialMapper.selectProportionalityByOut(outMaterial);
            List<Map<Object, Object>> inputRecord = materialRecordMapper.selectBatchChargingByOrder(finishOrderID);
            String productionStr = materialRecordMapper.getProductionByOrderID(finishOrderID);
            Double productionALl = 0.0;
            try
            {
                productionALl = Double.parseDouble(productionStr);
            }catch (Exception ex)
            {
                productionALl = 0.0;
            }
            productionALl = productionALl + production;
            Map<String, Double> outMaterialProportionMap = new HashMap<String, Double>();
            Map<String, Double> inputRecordMap = new HashMap<String, Double>();

            String typeID= "";
            double number=-1.0;
            for (Map<Object, Object> proportionMap : outMaterialProportion)
            {
                typeID = null;
                number = -1;
                for (Map.Entry<Object, Object> entry : proportionMap.entrySet()) {
                    if (org.springframework.util.StringUtils.isEmpty(entry.getValue()))
                        break;
                    if("typeID".equals(entry.getKey()))
                    {
                        typeID = entry.getValue().toString();
                    }
                    if("proportionality".equals(entry.getKey().toString()) && entry.getValue().toString().split(":").length ==2)
                    {
                        number =Double.valueOf(entry.getValue().toString().split(":")[0]) /Double.valueOf(entry.getValue().toString().split(":")[1])  ;
                    }
                }
                if(number > -1)
                {
                    outMaterialProportionMap.put(typeID,number);
                }
            }
            for (Map<Object, Object> inputMap : inputRecord)
            {
                typeID = null;
                number = -1;
                for (Map.Entry<Object, Object> entry : inputMap.entrySet()) {
                    if("typeID".equals(entry.getKey()))
                    {
                        typeID = entry.getValue().toString();
                    }
                    if("sum".equals(entry.getKey()))
                    {
                        try
                        {
                            number = Double.valueOf(entry.getValue().toString());
                        }catch (Exception ex)
                        {
                            number = 0.0;
                        }
                    }
                }
                if(number > -1)
                {
                    inputRecordMap.put(typeID,number);
                }
            }
            for (Map.Entry<String, Double> entry : outMaterialProportionMap.entrySet()) {
               if(!inputRecordMap.containsKey(entry.getKey()))
               {

                   if(1 < entry.getValue() * productionALl)
                   {
                       result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                       result.setMessage(materialTypeMapper.getTypeNameByID(entry.getKey()) + "投料不足，已投："
                               + "0" + "，至少需要投入：" +  entry.getValue() * productionALl );
                       break;
                   }
                   else
                   {
                       break;
                   }

               }
               if(inputRecordMap.get(entry.getKey()) + 1.0 < entry.getValue() * productionALl)
               {
                   result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                   result.setMessage(materialTypeMapper.getTypeNameByID(entry.getKey()) + "投料不足，已投："
                           + inputRecordMap.get(entry.getKey()) + "，至少需要投入：" +  entry.getValue() * productionALl );
                   break;
               }
            }
        }
       catch (Exception ex)
       {
           result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
           result.setMessage(ex.getMessage());
       }

        return  result;
    }

    public TNPYResponse getPlanProductionDashboard( String plantID,String processID,String startTime,String endTime )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object, Object> > planProductionDashboardList = workOrderMapper.getPlanProductionDashboard(plantID,processID,startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(planProductionDashboardList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getRealtimeProductionDashboard( String plantID,String processID,String startTime,String endTime ){
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object, Object> > realtimeProductionDashboardList = workOrderMapper.getRealtimeProductionDashboard(plantID,processID,startTime,endTime);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(realtimeProductionDashboardList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse cancelFinishSuborder( String subOrdderID )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
           GrantMaterialRecord grantMaterialRecord =  grantMaterialRecordMapper.selectByOrderID(subOrdderID);
           if(grantMaterialRecord !=null)
           {
               result.setMessage("该工单已发料，不能取消！" );
               return  result;
           }
            orderSplitMapper.cancelFinishStatus(subOrdderID);

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changePlanProductionRecord(  String jsonStr )
    {
        TNPYResponse result = new TNPYResponse();
        try {
            PlanProductionRecord planProductionRecord = (PlanProductionRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), PlanProductionRecord.class);

            if (StringUtils.isEmpty(planProductionRecord.getId())) {
                planProductionRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                planProductionRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");

                planProductionRecordMapper.insertSelective(planProductionRecord);
            } else {
                planProductionRecordMapper.updateByPrimaryKey(planProductionRecord);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return result;
        }
        catch (Exception ex)
        {
            result.setMessage("修改出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getPlanProductionRecord(  String plantID,String processID,String startTime,String endTime )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where planMonth >= '" + startTime + "' and planMonth <= '" + endTime + "' ";
            if(!"-1".equals(plantID))
            {
                filter += " and plantID ='" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                filter += " and processID ='" + processID + "' ";
            }
            filter += " order by planMonth desc";
            List<PlanProductionRecord> planProductionRecordList = planProductionRecordMapper.getPlanProductionRecordByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(planProductionRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deletePlanProductionRecord(  String id )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            planProductionRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getPlanProductionNumber(  String plantID,String processID,String planMonth )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String strNum = planProductionRecordMapper.getplanNumber(plantID,processID,planMonth);

            int dailyProduction = 0;
            try
            {
                dailyProduction = (int)Double.parseDouble(strNum.toString());
            }
            catch (Exception ex)
            {
                dailyProduction = 0;
            }
            result.setData(dailyProduction + "");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeOnlineMaterialRecord(  String jsonStr ){
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
        }
        catch (Exception ex)
        {
            result.setMessage("修改出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getOnlineMaterialRecord(  String plantID,String processID,String lineID ,String startTime,String endTime )  {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where updateTime >= '" + startTime + "' and updateTime <= '" + endTime + "' ";
            if(!"-1".equals(plantID))
            {
                filter += " and plantID ='" + plantID + "' ";
            }
            if(!"-1".equals(processID))
        {
            filter += " and processID ='" + processID + "' ";
        }
            if(!"-1".equals(lineID))
            {
                filter += " and lineID ='" + lineID + "' ";
            }
            filter += " order by status,updateTime desc";
            List<OnlineMaterialRecord> onlineMaterialRecordList = onlineMaterialRecordMapper.getOnlineMaterialRecordByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(onlineMaterialRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    private  TNPYResponse mergeOnlineMaterialRecordJS(String mergeID ,String operator)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String mergeIDList ="'" + mergeID.replace(",","','") + "'";

            List<OnlineMaterialRecord> onlineMaterialRecordList = onlineMaterialRecordMapper.getMergeNum(mergeIDList);

            if(onlineMaterialRecordList.size() > 1)
            {
                result.setMessage("请确认合并的是同一产线，同一物料！当前种类数量是" + onlineMaterialRecordList.size());
                return  result;
            }
            if(onlineMaterialRecordList.size() < 1)
            {
                result.setMessage("未找到记录！");
                return  result;
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
            onlineMaterialRecord.setMaterialnum(onlineMaterialRecordList.get(0).getMaterialnum());
            onlineMaterialRecord.setOperator(operator);
            onlineMaterialRecordMapper.insertSelective(onlineMaterialRecord);

            onlineMaterialRecordMapper.updateStatus(mergeIDList,"2");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    private  TNPYResponse mergeOnlineMaterialRecordZH(String mergeID ,String operator)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String mergeIDList ="'" + mergeID.replace(",","','") + "'";

            List<OnlineMaterialRecord> onlineMaterialRecordList = onlineMaterialRecordMapper.getMergeNum(mergeIDList);
            if(onlineMaterialRecordList.size() > 1)
            {
                result.setMessage("请确认合并的是同一产线，同一物料！当前种类数量是" + onlineMaterialRecordList.size());
                return  result;
            }

            if(onlineMaterialRecordList.size() < 1)
            {
                result.setMessage("未找到记录！");
                return  result;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            Date now = new Date();
            String orderName = onlineMaterialRecordList.get(0).getPlantid().split("###")[1] + onlineMaterialRecordList.get(0).getProcessid().split("###")[1] + onlineMaterialRecordList.get(0).getLineid().split("###")[1]+ dateFormat.format(now);

            OnlineMaterialRecord onlineMaterialRecord = new OnlineMaterialRecord();
            onlineMaterialRecord.setId(orderName);
            onlineMaterialRecord.setUpdatetime(now);
            onlineMaterialRecord.setStatus("3");
            onlineMaterialRecord.setMaterialid(onlineMaterialRecordList.get(0).getMaterialid());
            onlineMaterialRecord.setPlantid(onlineMaterialRecordList.get(0).getPlantid().split("###")[0]);
            onlineMaterialRecord.setProcessid(onlineMaterialRecordList.get(0).getProcessid().split("###")[0]);
            onlineMaterialRecord.setLineid(onlineMaterialRecordList.get(0).getLineid().split("###")[0]);
            onlineMaterialRecord.setMaterialnum(onlineMaterialRecordList.get(0).getMaterialnum());
            onlineMaterialRecord.setOperator(operator);
            onlineMaterialRecordMapper.insertSelective(onlineMaterialRecord);

            OrderSplit orderSplit = new OrderSplit();
            orderSplit.setId(orderName);
            orderSplit.setMaterialid(onlineMaterialRecordList.get(0).getMaterialid());
            double dbNum = onlineMaterialRecordList.get(0).getMaterialnum();
            orderSplit.setProductionnum(dbNum);
            orderSplit.setOrdersplitid(orderName);
            orderSplit.setOrderid(orderName);
            orderSplit.setStatus(StatusEnum.WorkOrderStatus.finished.getIndex() + "");
            orderSplitMapper.insertSelective(orderSplit);

            MaterialRecord materialRecord = new MaterialRecord();
            materialRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            materialRecord.setNumber(orderSplit.getProductionnum());
            materialRecord.setInorout(StatusEnum.InOutStatus.Input.getIndex());
            materialRecord.setInputer(operator);
            materialRecord.setSuborderid(orderName);
            materialRecord.setMaterialid(orderSplit.getMaterialid());
            materialRecord.setInputtime(new Date());
            materialRecord.setStatus(StatusEnum.WorkOrderStatus.repairin.getIndex());
            materialRecord.setInputplantid(onlineMaterialRecord.getPlantid());
            materialRecord.setInputprocessid(onlineMaterialRecord.getProcessid());
            materialRecord.setInputlineid(onlineMaterialRecord.getLineid());
            materialRecordMapper.insertSelective(materialRecord);

            onlineMaterialRecordMapper.updateStatus(mergeIDList,"2");
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse mergeOnlineMaterialRecord( String mergeID ,String operator,String processID )
    {
        if(ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName().equals(processID))
        {
            return  mergeOnlineMaterialRecordZH(mergeID,operator);
        }
        if(ConfigParamEnum.BasicProcessEnum.JSProcessID.getName().equals(processID))
        {
            return  mergeOnlineMaterialRecordJS(mergeID,operator);
        }
        TNPYResponse result = new TNPYResponse();
        result.setMessage("该工段没有线边仓功能，如需添加请联系开发人员！");
        return  result;
    }
    public TNPYResponse deleteOnlineMaterialRecord(  String id )    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            onlineMaterialRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
	@Override
	public TNPYResponse cancelInputSuborder(String subOrdderID) {
        TNPYResponse result = new TNPYResponse();
        try
        {
        	MaterialRecord materialRecord = new MaterialRecord();
        	materialRecord.setId(subOrdderID);
        	materialRecord.setExpendorderid("");
        	materialRecord.setInorout(new Integer(1));
        	materialRecord.setOutputer("");
        	materialRecord.setOutputtime(new Date(0));
           int res =  materialRecordMapper.updateCancelInputSuborder(materialRecord);
           

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
	
	//浇铸入窑记录
	@Override
	public TNPYResponse pushInDryingKilnjzsuborder( String jsonStr ,String name) {
        TNPYResponse result = new TNPYResponse();
        try
        {
        	//浇铸干燥窑流程判断开始
        	DryingKilnJZRecord dry = (DryingKilnJZRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), DryingKilnJZRecord.class);
        	EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByPrimaryKey(dry.getDryingkilnid());
        	if(equipmentInfo==null) {
        		 result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                 result.setMessage("不是正确的干燥窑二维码" );
                 return  result;
        	}
        	//浇铸干燥窑流程判断结束
        	
            String[] inputterInfo = name.split("###");
            OrderSplit orderSplit=(OrderSplit) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), OrderSplit.class);

            OrderSplit orderSplitTMP = orderSplitMapper.selectByPrimaryKey(orderSplit.getId());
            if(orderSplitTMP.getStatus().equals(StatusEnum.WorkOrderStatus.finished.getIndex() + ""))
            {
                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage("该工单已完成！" );
                return  result;
            }
            TNPYResponse judgeResult = judgeEnoughMaterial(orderSplit.getMaterialid(),orderSplit.getOrderid(),orderSplit.getProductionnum());
            if(judgeResult.getStatus() != StatusEnum.ResponseStatus.Success.getIndex())
            {
               // System.out.println(JSONObject.toJSON(judgeResult).toString());
                return  judgeResult;
            }

            orderSplit.setStatus(StatusEnum.WorkOrderStatus.finished.getIndex() + "");
            orderSplitMapper.updateByPrimaryKeySelective(orderSplit);

            MaterialRecord materialRecord = new MaterialRecord();
            materialRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            materialRecord.setInorout(StatusEnum.InOutStatus.Input.getIndex());
            materialRecord.setMaterialid(orderSplit.getMaterialid());
            materialRecord.setNumber(orderSplit.getProductionnum());
            materialRecord.setOrderid(orderSplit.getOrderid());
            materialRecord.setSuborderid(orderSplit.getId());
            materialRecord.setInputtime(new Date());
            materialRecord.setInputer(name);
            if(inputterInfo.length >3)
            {
                materialRecord.setInputer(inputterInfo[0]);
                materialRecord.setInputerid(inputterInfo[1]);
                materialRecord.setInputworklocationid(inputterInfo[2]);
                materialRecord.setMaterialnameinfo(inputterInfo[3]);
            }
            Workorder workorder = workOrderMapper.selectByPrimaryKey(orderSplit.getOrderid());
            if(workorder != null)
            {
                materialRecord.setInputplantid(workorder.getPlantid());
                materialRecord.setInputprocessid(workorder.getProcessid());
                materialRecord.setInputlineid(workorder.getLineid());

            }
            if(ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(materialRecord.getInputprocessid()))
            {
                materialRecord.setInorout(StatusEnum.InOutStatus.PreInput.getIndex());
            }
            materialRecordMapper.insert(materialRecord);
            boolean blTB = ConfigParamEnum.BasicProcessEnum.TBProcessID.getName().equals(workOrderMapper.getProcessIDByOrder(orderSplit.getOrderid()));
            boolean blJZ = ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals(workOrderMapper.getProcessIDByOrder(orderSplit.getOrderid()));
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

            try
            {
                if(blTB)
                {
                    String batchID = batchrelationcontrolMapper.selectTBBatchByOrderID(orderSplit.getOrderid());
                    if(org.springframework.util.StringUtils.isEmpty(batchID) || "null".equals(batchID.trim()) || batchID.length() < 6)
                    {
                        Batchrelationcontrol batchrelationcontrol = new Batchrelationcontrol();
                        batchrelationcontrol.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                        batchrelationcontrol.setRelationorderid(orderSplit.getOrderid());
                        batchrelationcontrol.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                        batchrelationcontrol.setRelationtime(new Date());
                        String batch = orderSplit.getOrdersplitid().substring(0,orderSplit.getOrdersplitid().length() - 13)
                                + orderSplit.getOrdersplitid().substring(orderSplit.getOrdersplitid().length() - 11,orderSplit.getOrdersplitid().length() - 3);
                        batchrelationcontrol.setTbbatch(batch);
                        batchrelationcontrolMapper.insert(batchrelationcontrol);
                    }
                }
            }catch (Exception ex)
            {
                result.setMessage(result.getMessage() + " " +ex.getMessage() );
            }
            
            //浇铸入窑
            try
            {
                if(blJZ)
                {
    		    	List<DryingKilnJZRecord> existsRecord = dryingKilnJZRecordMapper.selectBySuborderid(orderSplit.getOrdersplitid());
    		    	if(existsRecord !=null && existsRecord.size()>0) { //判断改子工单是否取消完成后存在表中记录, 若存在则修改记录
    		    		if(existsRecord.size()>1) {
    		    			result.setMessage("发生错误,出现 一条orderSplit对应多条浇铸干燥窑记录");
    		    			result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
    	    	            return  result;
    		    		}else {
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
            		    	if(inputterInfo.length >3)
            	            {
            		    		dry.setInputername(inputterInfo[0]);
            		    		dry.setInputerid(inputterInfo[1] );
            	            }
            		    	Integer inOrOut = StatusEnum.InOutStatus.Input.getIndex();
            		    	dry.setStatus(inOrOut.toString());
            		    	dryingKilnJZRecordMapper.updateByPrimaryKey(dry);
    		    		}
    		    	}else { //若工单为首次点击完成入窑, 则新增记录
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
        		    	if(inputterInfo.length >3)
        	            {
        		    		dry.setInputername(inputterInfo[0]);
        		    		dry.setInputerid(inputterInfo[1] );
        	            }
        		    	Integer inOrOut = StatusEnum.InOutStatus.Input.getIndex();
        		    	dry.setStatus(inOrOut.toString());
        		    	dryingKilnJZRecordMapper.insert(dry);
    		    	}
    		    	result.setMessage("入窑成功!");
    	            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
    	            return  result;
                }
            }catch (Exception ex)
            {
                result.setMessage(result.getMessage() + " " +ex.getMessage() );
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
	
	//出窑
	@Override
	public TNPYResponse pushOutDryingKilnjzsuborder( String jsonStr ,String name) {
        TNPYResponse result = new TNPYResponse();
        try
        {
        	DryingKilnJZRecord dry = (DryingKilnJZRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), DryingKilnJZRecord.class);
        	EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByPrimaryKey(dry.getDryingkilnid());
        	if(equipmentInfo==null) {
        		 result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                 result.setMessage("不是正确的干燥窑二维码" );
                 return  result;
        	}
        	List<DryingKilnJZRecord> dryList = dryingKilnJZRecordMapper.selectByDryingKilnIDAndStatus(dry.getDryingkilnid());
        	if(dryList == null || dryList.size() == 0) {
        		result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setMessage("无需出窑, 该窑中没有板栅" );
                return  result;
        	}
        	//设置tb_ordersplit状态
        	//设置materialRecord状态
        	materialRecordMapper.updateByDryingkilnid(dry.getDryingkilnid(), StatusEnum.InOutStatus.Input.getIndex());
        	//更新干燥窑outputer/outputtime/状态信息
        	dryingKilnJZRecordMapper.updateByDryingKilnIDAndStatus(dry.getOutputerid(),dry.getOutputername(),new Date(),StatusEnum.InOutStatus.Output.getIndex(),dry.getDryingkilnid(),StatusEnum.InOutStatus.Input.getIndex());
        	result.setMessage("批量出窑成功!");
        	result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
