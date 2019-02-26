package com.tnpy.mes.service.semifinishedBattery.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.BatteryBorrowReturnRecordMapper;
import com.tnpy.mes.mapper.mysql.BatteryRepairRecordMapper;
import com.tnpy.mes.mapper.mysql.BatteryScrapRecordMapper;
import com.tnpy.mes.mapper.mysql.BatteryShipmentNumRecordMapper;
import com.tnpy.mes.model.mysql.BatteryBorrowReturnRecord;
import com.tnpy.mes.model.mysql.BatteryRepairRecord;
import com.tnpy.mes.model.mysql.BatteryScrapRecord;
import com.tnpy.mes.model.mysql.BatteryShipmentNumRecord;
import com.tnpy.mes.service.semifinishedBattery.ISemifinishedBatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/2/12 10:13
 */
@Service("semifinishedBatteryService")
public class SemifinishedBatteryService implements ISemifinishedBatteryService {
    @Autowired
    private BatteryScrapRecordMapper batteryScrapRecordMapper;

    @Autowired
    private BatteryRepairRecordMapper batteryRepairRecordMapper;

    @Autowired
    private BatteryBorrowReturnRecordMapper batteryBorrowReturnRecordMapper;

    @Autowired
    private BatteryShipmentNumRecordMapper batteryShipmentNumRecordMapper;
    public TNPYResponse addScrapBattery(String jsonStr,int scrapNum)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            BatteryScrapRecord batteryScrapRecord=(BatteryScrapRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), BatteryScrapRecord.class);
           String scrapStatus = "1";
            batteryScrapRecord.setScraptime(new Date());
            batteryScrapRecord.setStatus(scrapStatus);
            if(scrapNum < 0)
           {
               batteryScrapRecordMapper.insert(batteryScrapRecord);
           }
            else
            {
                List<BatteryScrapRecord> batteryScrapRecordList = new ArrayList<>();
                for(int i =0 ;i<scrapNum;i++) {
                    BatteryScrapRecord batteryScrapRecordTMP = new BatteryScrapRecord();
                    batteryScrapRecordTMP.setStatus(scrapStatus);
                    batteryScrapRecordTMP.setBatteryid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                    batteryScrapRecordTMP.setBatterytype(batteryScrapRecord.getBatterytype());
                    batteryScrapRecordTMP.setLineid(batteryScrapRecord.getLineid());
                    batteryScrapRecordTMP.setPlantid(batteryScrapRecord.getPlantid());
                    batteryScrapRecordTMP.setScrapreason(batteryScrapRecord.getScrapreason());
                    batteryScrapRecordTMP.setScrapstaff(batteryScrapRecord.getScrapstaff());
                    batteryScrapRecordTMP.setScraptype(batteryScrapRecord.getScraptype());
                    batteryScrapRecordTMP.setScraptime(batteryScrapRecord.getScraptime());
                    batteryScrapRecordList.add(batteryScrapRecordTMP);
                }
                batteryScrapRecordMapper.insertManyScrapRecord(batteryScrapRecordList);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getScrapBatteryByline(String lineID,String plantID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where plantID = '" + plantID + "'";
            if(!"-1".equals(lineID))
            {
                filter += "  and lineID = '" + lineID + "' ";
            }
            filter += " order by scrapTime desc ";
            List<BatteryScrapRecord> batteryScrapRecordList = batteryScrapRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(batteryScrapRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse deleteScrapBattery(String batteryID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            batteryScrapRecordMapper.deleteByPrimaryKey(batteryID);
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

    public TNPYResponse addRepairBattery( String jsonStr,String type,int number)
    {
       TNPYResponse result = new TNPYResponse();
        try
        {
            BatteryRepairRecord batteryRepairRecord=(BatteryRepairRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), BatteryRepairRecord.class);

            if("add".equals(type))
            {
                if(number < 0)
                {
                    if(batteryRepairRecord.getBacktime() == null)
                    {
                        batteryRepairRecord.setStatus("1");
                    }
                    else
                        batteryRepairRecord.setStatus("2");
                    batteryRepairRecordMapper.insertSelective(batteryRepairRecord);
                }
               else
                {

                        List<BatteryRepairRecord> batteryRepairRecordList = new ArrayList<>();
                        for(int i =0 ;i<number;i++) {
                            BatteryRepairRecord batteryRepairRecordTMP = new BatteryRepairRecord();
                            batteryRepairRecordTMP.setStatus("1");
                            batteryRepairRecordTMP.setBatteryid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                            batteryRepairRecordTMP.setBatterytype(batteryRepairRecord.getBatterytype());
                            batteryRepairRecordTMP.setLineid(batteryRepairRecord.getLineid());
                            batteryRepairRecordTMP.setPlantid(batteryRepairRecord.getPlantid());
                            batteryRepairRecordTMP.setRepairreason(batteryRepairRecord.getRepairreason());
                            batteryRepairRecordTMP.setReportstaff(batteryRepairRecord.getReportstaff());
                            batteryRepairRecordTMP.setRepairtime(batteryRepairRecord.getRepairtime());

                            batteryRepairRecordList.add(batteryRepairRecordTMP);
                        }
                        batteryRepairRecordMapper.insertManyRepairRecord(batteryRepairRecordList);

                }
            }
           else if("change".equals(type))
           {
               if(batteryRepairRecord.getBacktime() != null)
               {
                   batteryRepairRecord.setStatus("2");
               }
               batteryRepairRecordMapper.updateByPrimaryKey(batteryRepairRecord);
           }
           else
            {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("操作类型不明！传入为：" + type + "，应为add或change");
                return  result;
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getRepairBatteryByline(String lineID,String plantID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where plantID = '" + plantID + "'";
            if(!"-1".equals(lineID))
            {
                filter += "  and lineID = '" + lineID + "' ";
            }
            filter += " order by repairTime desc ";
            List<BatteryRepairRecord> batteryRepairRecordList = batteryRepairRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(batteryRepairRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deleteRepairBattery(String batteryID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            batteryRepairRecordMapper.deleteByPrimaryKey(batteryID);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse addBorrowReturnRecord( String jsonStr)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            BatteryBorrowReturnRecord batteryBorrowReturnRecord=(BatteryBorrowReturnRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), BatteryBorrowReturnRecord.class);
            batteryBorrowReturnRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
            batteryBorrowReturnRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            batteryBorrowReturnRecordMapper.insert(batteryBorrowReturnRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse getBorrowReturnRecord(String outPlantID,String inPlantID,String startTime,String endTime,String batteryType)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where updateTime >= '" + startTime + "' and updateTime <= '" + endTime + "' ";
            if(!"-1".equals(outPlantID))
            {
                filter += "  and outPlantID = '" + outPlantID + "' ";
            }
            if(!"-1".equals(inPlantID))
            {
                filter += "  and inPlantID = '" + inPlantID + "' ";
            }
            if(!"-1".equals(batteryType))
            {
                filter += "  and batteryType = '" + batteryType + "' ";
            }
            filter += " order by updateTime desc, outPlantID";
            List<BatteryBorrowReturnRecord> batteryBorrowReturnRecordList = batteryBorrowReturnRecordMapper.getBorrowReturnRecordByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(batteryBorrowReturnRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deleteBorrowReturnRecord(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            batteryBorrowReturnRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getBatteryShipmentnumRecord(String plantID,String typeID)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where plantID = '" + plantID + "'  ";
            if(!"-1".equals(typeID))
            {
                filter += "  and materialID = '" + typeID + "' ";
            }

            filter += " order by shipmentTime desc, batteryType";
            List<BatteryShipmentNumRecord> batteryShipmentNumRecordList = batteryShipmentNumRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(batteryShipmentNumRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse addBatteryShipmentnumRecord(String jsonStr )
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            BatteryShipmentNumRecord batteryShipmentNumRecord = (BatteryShipmentNumRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), BatteryShipmentNumRecord.class);

            batteryShipmentNumRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            batteryShipmentNumRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
            batteryShipmentNumRecord.setShipmenttime(new Date());
            batteryShipmentNumRecordMapper.insert(batteryShipmentNumRecord);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("添加出错！" + ex.getMessage());
            return  result;
        }

    }
    public TNPYResponse deleteBatteryShipmentnumRecord(String id)
    {

        TNPYResponse result = new TNPYResponse();
        try
        {
            batteryShipmentNumRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
}
