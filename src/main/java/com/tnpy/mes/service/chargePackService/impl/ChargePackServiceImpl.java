package com.tnpy.mes.service.chargePackService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.mysql.BatteryGearMarkRecord;
import com.tnpy.mes.model.mysql.ChargingRackRecord;
import com.tnpy.mes.model.mysql.PileBatteryRecord;
import com.tnpy.mes.model.mysql.TidyBatteryRecord;
import com.tnpy.mes.service.chargePackService.IChargePackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-05-06 14:40
 */
@Service("chargePackService")
public class ChargePackServiceImpl implements IChargePackService {

    @Autowired
    private ChargingRackRecordMapper chargingRackRecordMapper;
    @Autowired
    private TidyBatteryRecordMapper tidyBatteryRecordMapper;
    @Autowired
    private ObjectRelationDictMapper objectRelationDictMapper;

    @Autowired
    private PileBatteryRecordMapper pileBatteryRecordMapper;

    @Autowired
    private BatteryGearMarkRecordMapper batteryGearMarkRecordMapper;
    //onRack 在架数据 pulloffhistory 下架历史数据 putonhistory 上架历史数据
    public TNPYResponse getChargingRackRecord(String plantID, String processID,String lineID,String locationID,String startTime,String endTime,String selectType)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where status != '-1' ";
            if("onRack".equals(selectType))
            {
                filter += " and pulloffDate is null ";
            }
            if("pulloffhistory".equals(selectType))
            {
                    filter += " and pulloffDate >= '" + startTime + "' ";
                    filter += " and pulloffDate <= '" + endTime + "' ";
            }
            if("putonhistory".equals(selectType))
            {
                filter += " and putonDate >= '" + startTime + "' ";
                filter += " and putonDate <= '" + endTime + "' ";
            }

            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                filter += " and processID = '" + processID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            if(!"-1".equals(locationID))
            {
                filter += " and workLocation = '" + locationID + "' ";
            }

            List<ChargingRackRecord> workLocationList =null;
            // System.out.println(plantID + " 参数 " +processID);
            if("onRack".equals(selectType))
            {
                filter += " order by workLocation, putonDate asc ";
                workLocationList = chargingRackRecordMapper.selectByFilter(filter);
            }
            else
            {
                workLocationList = chargingRackRecordMapper.selectByFilterWithSummary( filter + " order by workLocation, putonDate asc ",filter);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(workLocationList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse deteteChargingRackRecord(String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            chargingRackRecordMapper.deleteByPrimaryKey(id);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("删除失败！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse changeChargingRackRecord(String jsonStr)
    {
        ChargingRackRecord chargingRackRecord =(ChargingRackRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), ChargingRackRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(chargingRackRecord.getId()))
            {
                chargingRackRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                chargingRackRecord.setStatus("1");
                chargingRackRecordMapper.insertSelective(chargingRackRecord);
            }
            else
            {
                ChargingRackRecord chargingRackRecordOld = chargingRackRecordMapper.selectByPrimaryKey(chargingRackRecord.getId());
                List<String> nextLineList = objectRelationDictMapper.selectNextObjectID(chargingRackRecordOld.getLineid(),"1002");
                 String nextLineID = "-1";
                if(nextLineList.size() > 0)
                {
                    nextLineID = nextLineList.get(0);
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String tidyRecordFilter = " where status != '-1' and plantID = '" + chargingRackRecordOld.getPlantid()+ "' and lineID ='" + nextLineID
                        +"' and materialID = '" + chargingRackRecordOld.getMaterialid() +"'  and materialType ='" + chargingRackRecordOld.getMaterialtype()+ "' and dayTime = '" + formatter.format(chargingRackRecordOld.getPulloffdate())+"'"  ;  ;
                chargingRackRecordMapper.updateByPrimaryKeySelective(chargingRackRecord);
                TidyBatteryRecord tidyBatteryRecord = tidyBatteryRecordMapper.selectLatestRecordByFilter(tidyRecordFilter);
                tidyBatteryRecord.setPulloffnum(tidyBatteryRecord.getPulloffnum() + chargingRackRecordOld.getRepairnumber() - chargingRackRecord.getRepairnumber());
                tidyBatteryRecord.setCurrentnum(tidyBatteryRecord.getCurrentnum() + chargingRackRecordOld.getRepairnumber() - chargingRackRecord.getRepairnumber());
                tidyBatteryRecordMapper.updateByPrimaryKeySelective(tidyBatteryRecord);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }

    public TNPYResponse pulloffChargingRackRecord( String jsonStr)
    {
        ChargingRackRecord chargingRackRecord =(ChargingRackRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), ChargingRackRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
           chargingRackRecordMapper.updateByPrimaryKeySelective(chargingRackRecord);
            List<String> nextLineList = objectRelationDictMapper.selectNextObjectID(chargingRackRecord.getLineid(),"1002");
            String nextLineID = "-1";
            if(nextLineList.size() > 0)
            {
                nextLineID = nextLineList.get(0);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
           String tidyRecordFilter = " where status != '-1' and plantID = '" + chargingRackRecord.getPlantid()+ "' and lineID ='" + nextLineID
                   +"' and materialID = '" + chargingRackRecord.getMaterialid() +"'  and materialType ='" + chargingRackRecord.getMaterialtype()+ "' order by dayTime desc limit 1"  ;


            TidyBatteryRecord tidyBatteryRecord = tidyBatteryRecordMapper.selectLatestRecordByFilter(tidyRecordFilter);
            if(tidyBatteryRecord == null)
            {
                tidyBatteryRecord = new TidyBatteryRecord();
                tidyBatteryRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                tidyBatteryRecord.setPlantid(chargingRackRecord.getPlantid());
                tidyBatteryRecord.setLineid(nextLineID);
                tidyBatteryRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
                tidyBatteryRecord.setCurrentnum(chargingRackRecord.getRealnumber());
                tidyBatteryRecord.setLastnum((float)0);
                tidyBatteryRecord.setDaytime(chargingRackRecord.getPulloffdate());
                tidyBatteryRecord.setMaterialid(chargingRackRecord.getMaterialid());
                tidyBatteryRecord.setMaterialname(chargingRackRecord.getMaterialname());
                tidyBatteryRecord.setMaterialtype(chargingRackRecord.getMaterialtype());
                tidyBatteryRecord.setPulloffnum(chargingRackRecord.getRealnumber());
                tidyBatteryRecord.setProcessid(ConfigParamEnum.BasicProcessEnum.ZLProcessID.getName());
                tidyBatteryRecordMapper.insertSelective(tidyBatteryRecord);
            }
            else if(formatter.format(tidyBatteryRecord.getDaytime()).equals(formatter.format(chargingRackRecord.getPulloffdate())))
            {
                TidyBatteryRecord changeTidyBatteryRecord = new TidyBatteryRecord();
                changeTidyBatteryRecord.setId(tidyBatteryRecord.getId());
                changeTidyBatteryRecord.setCurrentnum(tidyBatteryRecord.getCurrentnum() +chargingRackRecord.getRealnumber());
                changeTidyBatteryRecord.setPulloffnum(tidyBatteryRecord.getCurrentnum() +chargingRackRecord.getRealnumber());
                tidyBatteryRecordMapper.updateByPrimaryKeySelective(changeTidyBatteryRecord);
            }
            else
            {
                TidyBatteryRecord insertTidyBatteryRecord = new TidyBatteryRecord();
                insertTidyBatteryRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                insertTidyBatteryRecord.setPlantid(chargingRackRecord.getPlantid());
                insertTidyBatteryRecord.setProcessid(ConfigParamEnum.BasicProcessEnum.ZLProcessID.getName());
                insertTidyBatteryRecord.setLineid(nextLineID);
                insertTidyBatteryRecord.setStatus(StatusEnum.StatusFlag.using.getIndex()+ "");
                insertTidyBatteryRecord.setCurrentnum(chargingRackRecord.getRealnumber());
                insertTidyBatteryRecord.setDaytime(chargingRackRecord.getPulloffdate());
                insertTidyBatteryRecord.setMaterialid(chargingRackRecord.getMaterialid());
                insertTidyBatteryRecord.setMaterialname(chargingRackRecord.getMaterialname());
                insertTidyBatteryRecord.setMaterialtype(chargingRackRecord.getMaterialtype());
                insertTidyBatteryRecord.setPulloffnum(chargingRackRecord.getRealnumber());
                tidyBatteryRecordMapper.insertSelective(insertTidyBatteryRecord);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("下架成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("下架失败！" + ex.getMessage());
        }
        return  result;
    }

    //onWorkbench 在工作台数据  workbenchHistory 工作台历史数据
    public TNPYResponse getTidyBatteryRecord(String plantID, String processID,String lineID,String startTime,String endTime,String selectType)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where status != '-1' ";
            if("onWorkbench".equals(selectType))
            {
                filter += " and currentNum > 0 ";
            }
            if("workbenchHistory".equals(selectType))
            {
                filter += " and dayTime >= '" + startTime + "' ";
                filter += " and dayTime <= '" + endTime + "' ";
            }

            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                filter += " and processID = '" + processID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }

            filter += " order by dayTime asc ";
            // System.out.println(plantID + " 参数 " +processID);
            List<TidyBatteryRecord> tidyBatteryRecordList = tidyBatteryRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(tidyBatteryRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeTidyBatteryRecord(String jsonStr)
    {
        TidyBatteryRecord tidyBatteryRecord =(TidyBatteryRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), TidyBatteryRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(tidyBatteryRecord.getId()))
            {
                tidyBatteryRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                tidyBatteryRecord.setStatus("1");
                tidyBatteryRecordMapper.insertSelective(tidyBatteryRecord);
            }
            else
            {
                tidyBatteryRecordMapper.updateByPrimaryKeySelective(tidyBatteryRecord);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }

    private  String getNumberString(int number)
    {
        String numStr = String.valueOf(number);
        String tmp = "";
        for(int i =0;i<3 - numStr.length();i++)
        {
            tmp += "0";
        }
        return  tmp+numStr;
    }

    public TNPYResponse addPileTidyBatteryRecord( String jsonTidyRecord,String pileNum,String perPileMaterialNum,String storeLocation)
    {
        TidyBatteryRecord tidyBatteryRecord =(TidyBatteryRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonTidyRecord), TidyBatteryRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            TidyBatteryRecord tidyBatteryRecordNew = tidyBatteryRecordMapper.selectByPrimaryKey(tidyBatteryRecord.getId());
            int pileNumInt = Integer.parseInt(pileNum);
            int perPileMaterialNumInt = Integer.parseInt(perPileMaterialNum);
            tidyBatteryRecord.setCurrentnum(tidyBatteryRecordNew.getCurrentnum() -pileNumInt*perPileMaterialNumInt);
            tidyBatteryRecord.setPilenum((float)pileNumInt*perPileMaterialNumInt);
            tidyBatteryRecordMapper.updateByPrimaryKeySelective(tidyBatteryRecord);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            Date now = new Date();
            PileBatteryRecord pileBatteryRecord = new PileBatteryRecord();
            pileBatteryRecord.setBatterydate(tidyBatteryRecordNew.getDaytime());
            pileBatteryRecord.setPlantid(tidyBatteryRecordNew.getPlantid());
            pileBatteryRecord.setProcessid(tidyBatteryRecordNew.getProcessid());
            pileBatteryRecord.setLineid(tidyBatteryRecordNew.getLineid());
            pileBatteryRecord.setMaterialid(tidyBatteryRecordNew.getMaterialid());
            pileBatteryRecord.setMaterialname(tidyBatteryRecordNew.getMaterialname());
            pileBatteryRecord.setMaterialtype(tidyBatteryRecordNew.getMaterialtype());
            pileBatteryRecord.setPilestaffid(tidyBatteryRecord.getOperatorid());
            pileBatteryRecord.setPilestaffname(tidyBatteryRecord.getOperatorname());
            pileBatteryRecord.setPiletime(now);
            pileBatteryRecord.setProductionnumber(Float.valueOf(perPileMaterialNumInt) );
            pileBatteryRecord.setRemark(tidyBatteryRecord.getRemark());
            pileBatteryRecord.setTidyrecordid(tidyBatteryRecord.getId());
            pileBatteryRecord.setStatus(StatusEnum.StatusFlag.using.getIndex() + "");
            pileBatteryRecord.setLocation(storeLocation);
            for(int i =0 ;i<pileNumInt;i++)
            {
                pileBatteryRecord.setId(formatter.format(now) + getNumberString(i));
                pileBatteryRecordMapper.insert(pileBatteryRecord);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("插入失败！" + ex.getMessage());
        }
        return  result;
    }

    public TNPYResponse getPileRecordByPileID( String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where id = '" + id +"' ";
            // System.out.println(plantID + " 参数 " +processID);
            List<PileBatteryRecord> pileBatteryRecordList = pileBatteryRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(pileBatteryRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    public TNPYResponse expendPileBatteryByPackage( String id,int packageNum,int totalNum)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(packageNum == totalNum)
            {
                pileBatteryRecordMapper.updateStatusByPrimaryKey(id,StatusEnum.InOutStatus.Output.getIndex() + "");
            }
            else
            {
                String filter = " where id = '" + id +"' ";
                // System.out.println(plantID + " 参数 " +processID);
                List<PileBatteryRecord> pileBatteryRecordList1 = pileBatteryRecordMapper.selectByFilter(filter);
                List<PileBatteryRecord> pileBatteryRecordList2 = pileBatteryRecordMapper.selectByFilter(filter);
                pileBatteryRecordList1.get(0).setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                pileBatteryRecordList1.get(0).setProductionnumber((float)totalNum - packageNum );
                pileBatteryRecordMapper.insertSelective(pileBatteryRecordList1.get(0));

                pileBatteryRecordList2.get(0).setProductionnumber((float)packageNum);
                pileBatteryRecordList2.get(0).setStatus(StatusEnum.InOutStatus.Output.getIndex() + "");
                pileBatteryRecordMapper.updateByPrimaryKey(pileBatteryRecordList2.get(0));
            }
           // String filter = " where id = '" + id +"' ";
            // System.out.println(plantID + " 参数 " +processID);
            //List<PileBatteryRecord> pileBatteryRecordList = pileBatteryRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

           // result.setData(JSONObject.toJSON(pileBatteryRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("包装消耗出错！" + ex.getMessage());
            return  result;
        }
    }

    //onWorkbench 在工作台数据  workbenchHistory 工作台历史数据
    public TNPYResponse getPileTidyBatteryRecord(String plantID, String processID,String lineID,String startTime,String endTime,String selectType)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where status != '-1' ";

                filter += " and batteryDate >= '" + startTime + "' ";
                filter += " and batteryDate <= '" + endTime + "' ";


            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                filter += " and processID = '" + processID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }

            filter += " order by batteryDate asc ";
            // System.out.println(plantID + " 参数 " +processID);
            List<PileBatteryRecord> pileBatteryRecordList = pileBatteryRecordMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(pileBatteryRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getBatteryGearLineInfo(String plantID, String startTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where plantID ='" + plantID + "' and dayTime >= '" + startTime + "' order by loopNumber,sequenceNumbers";

            // System.out.println(plantID + " 参数 " +processID);
            List<Map<Object, Object>> batteryGearLineInfo = batteryGearMarkRecordMapper.selectLineInfo(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(batteryGearLineInfo).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }


    public TNPYResponse getBatteryGearLineLocationInfo(String plantID,String lineID, String startTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {

            String filter = " where plantID ='" + plantID + "' and lineID = '" + lineID+ "' and dayTime  >=  '" + startTime + "' order by loopNumber,sequenceNumbers";

            // System.out.println(plantID + " 参数 " +processID);
            List<Map<Object, Object>> batteryGearLineInfo = batteryGearMarkRecordMapper.selectLineLocationInfo(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(batteryGearLineInfo).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getBatteryGearRecordInfo(String plantID, String lineID,String workLocation,String altitude,String startTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where plantID ='" + plantID + "' and lineID = '" + lineID+ "' and locationID ='" +workLocation + "' and altitude = '" + altitude
                    +"' and dayTime  =  '" + startTime + "' order by loopNumber,sequenceNumbers";

            // System.out.println(plantID + " 参数 " +processID);
            List<BatteryGearMarkRecord> batteryGearMarkRecordList = batteryGearMarkRecordMapper.selectBatteryGearRecordInfo(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(batteryGearMarkRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
}
