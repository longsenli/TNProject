package com.tnpy.mes.service.chargePackService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.mysql.*;
import com.tnpy.mes.service.chargePackService.IChargePackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private  TidyPackageBatteryInventoryMapper tidyPackageBatteryInventoryMapper;

    @Autowired
    private  PackageDetailRecordMapper packageDetailRecordMapper;

    @Autowired
    private  LoginRecordMapper loginRecordMapper ;
    //onRack 在架数据 pulloffhistory 下架历史数据 putonhistory 上架历史数据
    public TNPYResponse getChargingRackRecord(String plantID, String processID,String lineID,String locationID,String startTime,String endTime,String selectType)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where status != '-1' ";
            if("onRack".equals(selectType))
            {
                filter += " and ifnull(pulloffStaffName,1) = 1  ";
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
                //filter += " order by workLocation, putonDate asc ";

                if(!"-1".equals(locationID))
                {
                    filter += " order by workLocation, putonDate asc ";
                    workLocationList = chargingRackRecordMapper.selectByFilter(filter);
                }
                else
                {
                    workLocationList = chargingRackRecordMapper.selectByFilterOnRack(filter);
                }
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

            ChargingRackRecord chargingRackRecord = chargingRackRecordMapper.selectByPrimaryKey(id);
            LoginRecord loginRecord = new LoginRecord();
            loginRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            loginRecord.setLogintime(new Date());
            loginRecord.setUserid(id);
            loginRecord.setLoginip(JSONObject.toJSON(chargingRackRecord).toString().substring(0,195));
            loginRecordMapper.insert(loginRecord);
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
                LoginRecord loginRecord = new LoginRecord();
                loginRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                loginRecord.setLogintime(new Date());
                loginRecord.setUserid(chargingRackRecord.getStaffid());
                loginRecord.setLoginip(jsonStr.substring(0,195));
                loginRecordMapper.insert(loginRecord);

                chargingRackRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                chargingRackRecord.setStatus("1");
                List<Map<Object,Object>> numberRight = chargingRackRecordMapper.selectOnRackNumber(chargingRackRecord.getWorklocation());

                if(numberRight != null && numberRight.size() ==1 && numberRight.get(0) != null)
                {

                    int onRackNumber =Integer.parseInt(numberRight.get(0).get("onRackNumber").toString().split("\\.")[0]);
                    int maxNumber = Integer.parseInt( numberRight.get(0).get("describeInfo").toString().split("\\.")[0]);

                    if(onRackNumber + chargingRackRecord.getProductionnumber() > maxNumber)
                    {
                        result.setMessage("上架电池超出最大值，请先下架！最大上架数量为：" + maxNumber  + ",当前在架数量为：" +onRackNumber);
                        return  result;
                    }
                }
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
              //  System.out.println("======");
              chargingRackRecordMapper.updateByPrimaryKeySelective(chargingRackRecord);

                if(!StringUtils.isEmpty(chargingRackRecord.getPulloffstaffname()))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String tidyRecordFilter = " where status != '-1' and plantID = '" + chargingRackRecordOld.getPlantid()+ "' and lineID ='" + nextLineID
                            +"' and materialID = '" + chargingRackRecordOld.getMaterialid() +"'  and materialType ='" + chargingRackRecordOld.getMaterialtype()+ "' and dayTime = '" + formatter.format(chargingRackRecordOld.getPulloffdate())+"'"  ;  ;
                    TidyBatteryRecord tidyBatteryRecord = tidyBatteryRecordMapper.selectLatestRecordByFilter(tidyRecordFilter);
                    tidyBatteryRecord.setPulloffnum(tidyBatteryRecord.getPulloffnum() + chargingRackRecordOld.getRepairnumber() - chargingRackRecord.getRepairnumber());
                    tidyBatteryRecord.setCurrentnum(tidyBatteryRecord.getCurrentnum() + chargingRackRecordOld.getRepairnumber() - chargingRackRecord.getRepairnumber());
                    tidyBatteryRecordMapper.updateByPrimaryKeySelective(tidyBatteryRecord);
                }

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
    /**
                 * 充电架电池下架
     */
    public TNPYResponse pulloffChargingRackRecord( String jsonStr)
    {
        ChargingRackRecord chargingRackRecord =(ChargingRackRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), ChargingRackRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                if(formatter.format(chargingRackRecord.getPulloffdate()).equals(formatter.format(chargingRackRecord.getPutondate())) && !"5".equals(chargingRackRecord.getMaterialtype()))
                {
                    result.setMessage("当日上架的电池不能下架！");
                    return  result;
                }
            }
            catch (Exception ex2)
            {

            }

            ChargingRackRecord chargingRackRecordDB = chargingRackRecordMapper.selectByPrimaryKey(chargingRackRecord.getId());
            chargingRackRecord.setProductionnumber(chargingRackRecordDB.getProductionnumber() -(chargingRackRecordDB.getRealnumber() -chargingRackRecord.getRealnumber()) );
            if(chargingRackRecordDB.getRealnumber() > chargingRackRecord.getRealnumber())
            {
                chargingRackRecordDB.setProductionnumber(chargingRackRecordDB.getRealnumber() -chargingRackRecord.getRealnumber());
                chargingRackRecordDB.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                chargingRackRecordDB.setRealnumber(chargingRackRecordDB.getRealnumber() -chargingRackRecord.getRealnumber() );

                chargingRackRecordDB.setRepaircombine("");
                chargingRackRecordDB.setRepairnumber(Float.parseFloat("0") );
                chargingRackRecordMapper.insertSelective(chargingRackRecordDB);
            }

            chargingRackRecordMapper.updateByPrimaryKeySelective(chargingRackRecord);
            List<String> nextLineList = objectRelationDictMapper.selectNextObjectID(chargingRackRecord.getLineid(),"1002");
            String nextLineID = "-1";
            if(nextLineList.size() > 0)
            {
                nextLineID = nextLineList.get(0);
            }

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
                tidyBatteryRecord.setProcessid(ConfigParamEnum.BasicProcessEnum.BZProcessID.getName());
                tidyBatteryRecordMapper.insertSelective(tidyBatteryRecord);
            }
            else if(formatter.format(tidyBatteryRecord.getDaytime()).equals(formatter.format(chargingRackRecord.getPulloffdate())))
            {
                TidyBatteryRecord changeTidyBatteryRecord = new TidyBatteryRecord();
                changeTidyBatteryRecord.setId(tidyBatteryRecord.getId());
                changeTidyBatteryRecord.setCurrentnum(tidyBatteryRecord.getCurrentnum() +chargingRackRecord.getRealnumber());
                changeTidyBatteryRecord.setPulloffnum(tidyBatteryRecord.getPulloffnum() +chargingRackRecord.getRealnumber());
                tidyBatteryRecordMapper.updateByPrimaryKeySelective(changeTidyBatteryRecord);
            }
            else
            {
                TidyBatteryRecord insertTidyBatteryRecord = new TidyBatteryRecord();
                insertTidyBatteryRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                insertTidyBatteryRecord.setPlantid(chargingRackRecord.getPlantid());
                insertTidyBatteryRecord.setProcessid(ConfigParamEnum.BasicProcessEnum.BZProcessID.getName());
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
        	 Date dNow = new Date();   //当前时间
             Date dBefore = new Date();
             Calendar calendar = Calendar.getInstance(); //得到日历
             calendar.setTime(dNow);//把当前时间赋给日历
             calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
             dBefore = calendar.getTime();   //得到前一天的时间
//           SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
             SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
             String onWorkbenchdefDate = sdf.format(dBefore);    //格式化前一天, 整理台实时数据默认显示前一天数据, 当天一等品/二等品
             
             
             
             Date dBefore4 = new Date();
             Calendar calendar4 = Calendar.getInstance(); //得到日历
             calendar4.setTime(dNow);//把当前时间赋给日历
             calendar4.add(Calendar.DAY_OF_MONTH, -4);  //设置为前一天
             dBefore4 = calendar4.getTime();   //得到前一天的时间
//           SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
             SimpleDateFormat sdf4=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
             String defstartDate = sdf4.format(dBefore4); ////格式化前四天天, 整理台实时数据默认显示前一天数据, 一次返充二次返充默认显示4天前到今天
        	 String filter1 = "(\r\n" + 
        			"	SELECT\r\n" + 
        			"		id,\r\n" + 
        			"		plantID,\r\n" + 
        			"		processID,\r\n" + 
        			"		lineID,\r\n" + 
        			"		workLocation,\r\n" + 
        			"		currentNum ,\r\n" + 
        			"		lastNum,\r\n" + 
        			"		materialID,\r\n" + 
        			"		materialName,\r\n" + 
        			"		repairNumber ,\r\n" + 
        			"		repairID,\r\n" + 
        			"		repairName,\r\n" + 
        			"		repairTime,\r\n" + 
        			"		reason,\r\n" + 
        			"		materialType,\r\n" + 
        			"		backToChargeNum,\r\n" + 
        			"		pileNum ,\r\n" + 
        			"		packNum,\r\n" + 
        			"		pullOffNum,\r\n" + 
        			"		repairBackNum,\r\n" + 
        			"		repairCombine,\r\n" + 
        			"		dayTime,\r\n" + 
        			"		STATUS,\r\n" + 
        			"		remark,\r\n" + 
        			"		operatorID,\r\n" + 
        			"		operatorName,\r\n" + 
        			"		operatorTime\r\n" + 
        			"	FROM\r\n" + 
        			"		tb_tidybatteryrecord\r\n" + 
        			"	WHERE ";
            String filter2 = "  status != '-1' ";
            //整理台实时数据只显示前一天下架数据
            if("onWorkbench".equals(selectType))
            {
                filter2 += " and currentNum > 0 ";
//                filter2+= " and dayTime = '" + onWorkbenchdefDate + "' ";
                filter2+= " and dayTime >= '" + defstartDate + "' ";
                filter2 += " and dayTime <= '" + onWorkbenchdefDate + "' ";
            }
            if("workbenchHistory".equals(selectType))
            {
            	if(!startTime.equals(endTime)) {
	                filter2+= " and dayTime >= '" + startTime + "' ";
	                filter2 += " and dayTime <= '" + endTime + "' ";
            	}else {
            		filter2 += " and dayTime <= '" + endTime + "' ";
            	}
            }

            if(!"-1".equals(plantID))
            {
                filter2 += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                filter2 += " and processID = '" + processID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter2 += " and lineID = '" + lineID + "' ";
            }

            filter2 += "AND (\r\n" + 
            		"		materialType = '1'\r\n" + 
            		"		OR materialType = '2'\r\n" + 
            		"	)  ) UNION ALL  ";
            
            
            String filter3 = "(\r\n" + 
            		"	SELECT\r\n" + 
        			"		id,\r\n" + 
        			"		plantID,\r\n" + 
        			"		processID,\r\n" + 
        			"		lineID,\r\n" + 
        			"		workLocation,\r\n" + 
        			"		currentNum ,\r\n" + 
        			"		lastNum,\r\n" + 
        			"		materialID,\r\n" + 
        			"		materialName,\r\n" + 
        			"		repairNumber ,\r\n" + 
        			"		repairID,\r\n" + 
        			"		repairName,\r\n" + 
        			"		repairTime,\r\n" + 
        			"		reason,\r\n" + 
        			"		materialType,\r\n" + 
        			"		backToChargeNum,\r\n" + 
        			"		pileNum ,\r\n" + 
        			"		packNum,\r\n" + 
        			"		pullOffNum,\r\n" + 
        			"		repairBackNum,\r\n" + 
        			"		repairCombine,\r\n" + 
        			"		dayTime,\r\n" + 
        			"		STATUS,\r\n" + 
        			"		remark,\r\n" + 
        			"		operatorID,\r\n" + 
        			"		operatorName,\r\n" + 
        			"		operatorTime\r\n" + 
        			"	FROM\r\n" + 
        			"		tb_tidybatteryrecord\r\n" + 
        			"	WHERE ";
            String filter4 = "  status != '-1' ";
            if("onWorkbench".equals(selectType))
            {
                filter4 += " and currentNum > 0 ";
                filter4+= " and dayTime >= '" + defstartDate + "' ";
                filter4 += " and dayTime <= '" + onWorkbenchdefDate + "' ";
            }
            if("workbenchHistory".equals(selectType))
            {
                filter4+= " and dayTime >= '" + startTime + "' ";
                filter4 += " and dayTime <= '" + endTime + "' ";
            }

            if(!"-1".equals(plantID))
            {
                filter4 += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                filter4 += " and processID = '" + processID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter4 += " and lineID = '" + lineID + "' ";
            }
            

            filter4 += "AND (\r\n" + 
            		"		materialType = '3'\r\n" + 
            		"		OR materialType = '4'\r\n" + 
            		"	)  )  order by dayTime desc   ";
            
            
            
            

           
            
            StringBuilder stb = new StringBuilder();
            stb.append(filter1).append(filter2).append(filter3).append(filter4);
//            System.out.println(stb);
            
            List<TidyBatteryRecord> tidyBatteryRecordList = tidyBatteryRecordMapper.selectByFilter1(stb.toString());
            
            
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

                TidyBatteryRecord tidyBatteryRecord1 = tidyBatteryRecordMapper.selectByPrimaryKey(tidyBatteryRecord.getId());

                if(tidyBatteryRecord.getBacktochargenum() == null)
                {
                    tidyBatteryRecord.setBacktochargenum(tidyBatteryRecord1.getBacktochargenum());
                }

                if(tidyBatteryRecord.getRepairnumber() == null)
                {
                    tidyBatteryRecord.setRepairnumber(tidyBatteryRecord1.getRepairnumber());
                }

                if((tidyBatteryRecord1.getBacktochargenum() +tidyBatteryRecord1.getRepairnumber() -tidyBatteryRecord.getBacktochargenum() - tidyBatteryRecord.getRepairnumber() ) < 0)
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();//取时间

                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    if(calendar.get(Calendar.HOUR_OF_DAY ) >= 7)
                    {
                        calendar.add(Calendar.DATE, 1);
                    }
                    date = calendar.getTime();
                    String filter = " where plantID = '" + tidyBatteryRecord.getPlantid() + "' and materialID = '" + tidyBatteryRecord.getMaterialid() + "' and checkTime >'" +dateFormat.format(date) + "'";


                    List<TidyPackageBatteryInventory> tidyPackageBatteryInventoryList = tidyPackageBatteryInventoryMapper.selectByFilter(filter);
                    if(tidyPackageBatteryInventoryList.size() < 1)
                    {
                        TidyPackageBatteryInventory tidyPackageBatteryInventory = new TidyPackageBatteryInventory();
                        tidyPackageBatteryInventory.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                        tidyPackageBatteryInventory.setPlantid(tidyBatteryRecord1.getPlantid());
                        tidyPackageBatteryInventory.setMaterialid(tidyBatteryRecord1.getMaterialid());

                        tidyPackageBatteryInventory.setBackchargenewnum(  tidyBatteryRecord.getBacktochargenum().intValue()- tidyBatteryRecord1.getBacktochargenum().intValue() );
                        tidyPackageBatteryInventory.setRepairnewnum(tidyBatteryRecord.getBacktochargenum().intValue()- tidyBatteryRecord1.getBacktochargenum().intValue() );
                        tidyPackageBatteryInventory.setChecktime(new Date());
                        tidyPackageBatteryInventory.setRemark("-1");
                        tidyPackageBatteryInventoryMapper.insert(tidyPackageBatteryInventory);
                    }
                    else
                    {
                        tidyPackageBatteryInventoryList.get(0).setBackchargenewnum( tidyPackageBatteryInventoryList.get(0).getBackchargenewnum() + tidyBatteryRecord.getBacktochargenum().intValue()- tidyBatteryRecord1.getBacktochargenum().intValue() );
                        tidyPackageBatteryInventoryList.get(0).setRepairnewnum( tidyPackageBatteryInventoryList.get(0).getRepairnewnum() + tidyBatteryRecord.getBacktochargenum().intValue()- tidyBatteryRecord1.getBacktochargenum().intValue() );
                        tidyPackageBatteryInventoryMapper.updateByPrimaryKey(tidyPackageBatteryInventoryList.get(0));
                    }
                }
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
    /**
     * 电池打堆下单保存方法
     */
    public TNPYResponse addPileTidyBatteryRecord( String jsonTidyRecord,String pileNum,String perPileMaterialNum,String storeLocation)
    {
        TidyBatteryRecord tidyBatteryRecord =(TidyBatteryRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonTidyRecord), TidyBatteryRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            TidyBatteryRecord tidyBatteryRecordNew = tidyBatteryRecordMapper.selectByPrimaryKey(tidyBatteryRecord.getId());
            //打堆拖数
            int pileNumInt = Integer.parseInt(pileNum);
            //每拖数量
            int perPileMaterialNumInt = Integer.parseInt(perPileMaterialNum);
//            //剩余数量 = 当前剩余数量 - 打堆拖数*每托数量
//            tidyBatteryRecordNew.setCurrentnum(tidyBatteryRecordNew.getCurrentnum() -pileNumInt*perPileMaterialNumInt);
//            tidyBatteryRecordNew.setPilenum((float)pileNumInt*perPileMaterialNumInt);
//            tidyBatteryRecordMapper.updateByPrimaryKeySelective(tidyBatteryRecordNew);

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
                pileBatteryRecord.setId(formatter.format(now) + getNumberString(i+1));
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
    /**
     * 包装扫码查询方法
     */
    public TNPYResponse getPileRecordByPileID( String id)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where id = '" + id +"' ";
            String partfilter = " where partpileID  = '" + id +"' ";
            //扫的查询记录
        	List<PileBatteryRecord> pileBatteryRecordList = pileBatteryRecordMapper.selectByFilter(filter);
        	String status = pileBatteryRecordList.get(0).getStatus().trim();
            //第一判断扫码status,是否已经整托或者部分打堆  3 4 
            if(pileBatteryRecordList.size()>0&&(status.equals(StatusEnum.StatusFlag.finishpile.getIndex() + "") || status.equals(StatusEnum.StatusFlag.partfinishpile.getIndex() + "")
            		|| status.equals(StatusEnum.StatusFlag.partfinishpackage.getIndex() + "") || status.equals(StatusEnum.StatusFlag.finishpackage.getIndex() + "") )) {
            	//第二如果已经整托包装,则返回提示
            	if(status.equals(StatusEnum.StatusFlag.finishpackage.getIndex() + "")) {
            		result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                    result.setMessage("该二维码已整托包装扫码, 请勿重复包装扫码!" + "数量为: " + pileBatteryRecordList.get(0).getFnishpackagenum().intValue() + "扫码人: " + pileBatteryRecordList.get(0).getFinishpackagestaffname());
                    return  result;
            	}
            	//第三如果已经部分包装,则查出剩余电池
            	if(status.equals(StatusEnum.StatusFlag.partfinishpackage.getIndex() + "")) {
//            		String[] partids = pileBatteryRecordList.get(0).getPartpackageid().split("#");
//            		
//            		StringBuilder partpackfilter = new StringBuilder();
//            		partpackfilter.append("select * from tb_pilebatteryrecord where id in = ");
//            		for(int i=0; i<partids.length; i++) {
//            			if(i==0){
//            				partpackfilter.append("'"+partids[i]+"'");
//            			}else {
//            				partpackfilter.append("or id ='"+partids[i]+"'");
//            			}
//            		}
//            		List<PileBatteryRecord> partpacklist = pileBatteryRecordMapper.selectAllByFilter(partfilter.toString());
//            		Float countpartnum = new Float("0");
//            		for (PileBatteryRecord pileBatteryRecord : partpacklist) {
//            			countpartnum+=pileBatteryRecord.getFnishpackagenum();
//					}
            		result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            		 result.setData(JSONObject.toJSON(pileBatteryRecordList).toString());
                    result.setMessage("部分包装");
                    return  result;
            	}
            	result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                result.setData(JSONObject.toJSON(pileBatteryRecordList).toString());
                return  result;
            }else {
            	result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("该二维码未打堆扫码!");
                return  result;
            }
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    /**
     * 整理扫码完成入库方法
     */
    
    public TNPYResponse finishPileTidyBatteryRecord( String id,String remainpileNum,String piletotalNum, String partpileNum,String plantID,
    		String processID,String lineID,String userID,String username)
    {
//    	TidyBatteryRecord tidyBatteryRecord =(TidyBatteryRecord) JSONObject.toJavaObject(JSONObject.parseObject(jsonTidyRecord), TidyBatteryRecord.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
        	String filter = " where id = '" + id +"' ";
            // System.out.println(plantID + " 参数 " +processID);
            List<PileBatteryRecord> pileBatteryRecordList = pileBatteryRecordMapper.selectByFilter(filter);
            PileBatteryRecord pile = pileBatteryRecordList.get(0);
            TidyBatteryRecord tidyBatteryRecordNew = tidyBatteryRecordMapper.selectByPrimaryKey(pile.getTidyrecordid());
            //部分打堆扫码查询数据,只传id
            if((id!=null)&&(id!="")&&(id!="undefined")&&(!partpileNum.equals("0")&&(partpileNum.equals("partpilequery")))) {
            		if(pile.getStatus().trim().equals(StatusEnum.StatusFlag.finishpile.getIndex() + "") || pile.getStatus().trim().equals(StatusEnum.StatusFlag.partfinishpile.getIndex() + "")) {
            			result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                        result.setMessage("该二维码已打堆扫码,打堆数量: " + pile.getFinishpilenum().intValue() + "  扫码人: " + pile.getFinishpilestaffname());
                        return  result;
            		}
            		if(pile.getStatus().trim().equals(StatusEnum.StatusFlag.partfinishpackage.getIndex() + "") || pile.getStatus().trim().equals(StatusEnum.StatusFlag.finishpackage.getIndex() + "")) {
            			result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                        result.setMessage("该二维码已包装,包装数量: " + pile.getFnishpackagenum().intValue());
                        return  result;
            		}
	                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
	                result.setData(JSONObject.toJSON(pileBatteryRecordList).toString());
	                return  result;
            }
           //校验是否已经包装
            if(pile.getStatus().trim().equals(StatusEnum.StatusFlag.partfinishpackage.getIndex() + "") || pile.getStatus().trim().equals(StatusEnum.StatusFlag.finishpackage.getIndex() + "")) {
    			result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("该二维码已包装,包装数量: " + pile.getFnishpackagenum().intValue());
                return  result;
    		}
            
            Date now = new Date();
            //整理打堆部分入库
            if(Integer.parseInt(partpileNum)>0) {
            	String partfilter = " where "
            			+ "id = '" + id +"' and status = " + (StatusEnum.StatusFlag.partfinishpile.getIndex() + "" );
                //判断是否已经部分打堆
                List<PileBatteryRecord> partpileBatteryRecordList = pileBatteryRecordMapper.selectByFilter(partfilter);
            	if(!(partpileBatteryRecordList.size()>0)) {
//            		PileBatteryRecord pilemain = pile;
	            	//部分打堆将主id设置成partpileID
//	            	pile.setPartpileid(pile.getId());
	            	//设置ID
//	            	pile.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
	            	//部分打堆修改状态
	        		pile.setStatus(StatusEnum.StatusFlag.partfinishpile.getIndex() + "");
	        		//设置数量
	        		pile.setFinishpilenum((float)Integer.parseInt(partpileNum));
	        		//实际完成打堆人
	        		pile.setFinishplantid(plantID);
	        		pile.setFinishprocessid(processID);
	        		pile.setFinishlineid(lineID);
	        		pile.setFinishpilestaffid(userID);
	        		pile.setFinishpilestaffname(username);
	        		pile.setFinishpiletime(now);
                    pile.setPartpileid(id);
	        		//插入一条新部分打堆记录
//	        		pileBatteryRecordMapper.insert(pile);
	        		
	        		pileBatteryRecordMapper.updateByPrimaryKey(pile);
	        		
	        		///////////////////////////
	        		//修改主数据
	        		//部分打堆修改状态
//	        		pile.setStatus(StatusEnum.StatusFlag.partfinishpile.getIndex() + "");
	        		//设置数量
//	        		pile.setFinishpilenum((float)Integer.parseInt(partpileNum));
//	        		pileBatteryRecordMapper.updateByPrimaryKey(pilemain);
	        		//修改整理台数据
	        		int perPileMaterialNumInt =pile.getFinishpilenum().intValue();
	                //剩余数量 = 当前剩余数量 - 每次整理打堆入库数
	                tidyBatteryRecordNew.setCurrentnum(tidyBatteryRecordNew.getCurrentnum() - perPileMaterialNumInt);
	                tidyBatteryRecordNew.setPilenum(tidyBatteryRecordNew.getPilenum()+(float)perPileMaterialNumInt);
	                tidyBatteryRecordMapper.updateByPrimaryKeySelective(tidyBatteryRecordNew);
	                result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
	                result.setMessage("部分打堆扫码成功！"+ "扫码人: " +  partpileBatteryRecordList.get(0).getFinishpilestaffname());
	                return  result;
                }else {
                	result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                    result.setMessage("该二维码已部分打堆扫码, 部分打堆数量: " + partpileBatteryRecordList.get(0).getFinishpilenum().intValue() + "  扫码人: " + username);
                    return  result;
                }
            }
            //整托打堆
            else {
            	//是否已经打堆判断
            	if(!pile.getStatus().trim().equals(StatusEnum.StatusFlag.finishpile.getIndex() + "") ) {
            		if(pile.getStatus().trim().equals(StatusEnum.StatusFlag.partfinishpile.getIndex() + "") ) {
            			result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                        result.setMessage("该二维码已部分打堆扫码, 部分打堆数量: " + pile.getFinishpilenum().intValue() + "  扫码人: " + username);
                        return  result;
            		}
            		//整托打堆修改状态
            		pile.setStatus(StatusEnum.StatusFlag.finishpile.getIndex() + "");
            		//实际完成打堆人
            		pile.setFinishplantid(plantID);
            		pile.setFinishprocessid(processID);
            		pile.setFinishlineid(lineID);
            		pile.setFinishpilestaffid(userID);
            		pile.setFinishpilestaffname(username);
            		pile.setFinishpilenum(pile.getProductionnumber());
            		pile.setFinishpiletime(now);
                    pile.setPartpileid(id);
            		pileBatteryRecordMapper.updateByPrimaryKey(pile);
            		int perPileMaterialNumInt =pile.getProductionnumber().intValue();
                    //剩余数量 = 当前剩余数量 - 每次整理打堆入库数
                    tidyBatteryRecordNew.setCurrentnum(tidyBatteryRecordNew.getCurrentnum() - perPileMaterialNumInt);
                    tidyBatteryRecordNew.setPilenum(tidyBatteryRecordNew.getPilenum()+(float)perPileMaterialNumInt);
                    tidyBatteryRecordMapper.updateByPrimaryKeySelective(tidyBatteryRecordNew);
                    result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
                    result.setMessage("整托打堆成功！");
                    return  result;
            	}else {
            		result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                    result.setMessage("该二维码已整托打堆扫码, 打堆数量为"+pile.getFinishpilenum() + "  扫码人: " + pile.getFinishpilestaffname());
                    return  result;
            	}
            }
            
            
            
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错" + ex.getMessage());
        }
        return  result;
    }
    /**
     * 包装扫码保存方法
     */
    public TNPYResponse expendPileBatteryByPackage( String packagepileid,String remainpackageNum,String packagetotalNum, String packageNum,String plantID,
    		String processID,String lineID,String userID,String username)
    {
        TNPYResponse result = new TNPYResponse();
        if(Float.parseFloat(packageNum)>Float.parseFloat(packagetotalNum)) {
        	result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
        	result.setMessage("包装数量不能大于剩余可包装数量!");
            return  result;
        }
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
//            System.out.println("当前时间：" + dateFormat.format(d));
            String filter = "where id = '" + packagepileid +"'";
            //通过扫描打堆二位码id获取打堆信息
             List<PileBatteryRecord> lipile = pileBatteryRecordMapper.selectByFilter(filter);
             PileBatteryRecord originpile = lipile.get(0);
            //整托包装
            if(packageNum.equals(packagetotalNum)||packageNum==packagetotalNum )
            {
            	originpile.setFinishlineid(lineID);
            	originpile.setProcessid(processID);
            	originpile.setPlantid(plantID);
            	originpile.setFinishpackagestaffid(userID);
            	originpile.setFinishpackagestaffname(username);
            	originpile.setStatus(StatusEnum.StatusFlag.finishpackage.getIndex()+"");
            	originpile.setPackagetime(now);
            	originpile.setFnishpackagenum(Float.parseFloat(packageNum));
//            	更新PileBatteryRecord记录
                pileBatteryRecordMapper.updateByPrimaryKey(originpile);
            }
            else
            {
            	//新增部分记录
            	PileBatteryRecord partpack = new PileBatteryRecord();
            	partpack.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            	partpack.setFinishlineid(lineID);
            	partpack.setProcessid(processID);
            	partpack.setPlantid(plantID);
            	partpack.setFinishpackagestaffid(userID);
            	partpack.setFinishpackagestaffname(username);
            	partpack.setStatus(StatusEnum.StatusFlag.finishpackage.getIndex()+"");
            	partpack.setPackagetime(now);
            	partpack.setFnishpackagenum(Float.parseFloat(packageNum));
                
                //修改添加主记录
                if(originpile.getPartpackageid()!=null&&!originpile.getPartpackageid().equals("null")&&originpile.getPartpackageid().length()>0) {
                	Float ff = originpile.getFnishpackagenum();
                	String ps = originpile.getPartpackageid()==null?"":originpile.getPartpackageid();
                	if(ps.equals("")) {
                		originpile.setPartpackageid(partpack.getId());
                	}
                	originpile.setPartpackageid(ps+ "#"+ partpack.getId());
                	originpile.setStatus(StatusEnum.StatusFlag.partfinishpackage.getIndex()+"");
                	if((ff + Float.parseFloat(packageNum))>originpile.getFinishpilenum()) {
                		result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                        result.setMessage("领料失败, 领料数量大于剩余可包装数量,剩余可包数量为: " + (originpile.getFinishpilenum().intValue() - originpile.getFnishpackagenum().intValue()) );
                        return  result;
                	}
                	originpile.setFnishpackagenum(ff + Float.parseFloat(packageNum) );
                	pileBatteryRecordMapper.insert(partpack);
                	pileBatteryRecordMapper.updateByPrimaryKey(originpile);
                }else {
                	originpile.setPartpackageid(partpack.getId());
                	originpile.setStatus(StatusEnum.StatusFlag.partfinishpackage.getIndex()+"");
                	originpile.setFnishpackagenum(  Float.parseFloat(packageNum));
                	pileBatteryRecordMapper.updateByPrimaryKey(originpile);
                }
                
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
        	result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
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

                filter += " and pileTime >= '" + startTime + "' ";
                filter += " and pileTime <= '" + endTime + "' ";
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

            filter += " order by pileTime asc ";
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

    //onWorkbench 在工作台数据  workbenchHistory 工作台历史数据
    public TNPYResponse getPackageRecord(String plantID, String processID,String lineID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where status != '-1' ";

            filter += " and packageTime >= '" + startTime + "' ";
            filter += " and packageTime <= '" + endTime + "' ";


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

            filter += " order by pileTime asc ";
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

    public TNPYResponse getBatteryInventoryRecord(String plantID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where  ";
            filter += "  checkTime >= '" + startTime + "' ";
            filter += " and checkTime <= '" + endTime + "' ";
            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }

            filter += " order by checkTime desc ";
            // System.out.println(plantID + " 参数 " +processID);
            List<TidyPackageBatteryInventory> tidyPackageBatteryInventoryList = tidyPackageBatteryInventoryMapper.selectByFilter(filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(tidyPackageBatteryInventoryList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse getPackageRecordDetail(String plantID,String lineID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String groupFilter = " ";
            String filter = " where  ";
            filter += "  dayTime >= '" + startTime + "' ";
            filter += " and dayTime < '" + endTime + "' ";
            if(!"-1".equals(plantID))
            {
                filter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                filter += " and lineID = '" + lineID + "' ";
            }
            groupFilter = filter + " group by plantID,lineID,dayTime,materialName order by dayTime desc,lineID,materialName  ";
            filter += " order by dayTime desc,lineID,materialName  ";
            // System.out.println(plantID + " 参数 " +processID);
            List<Map<Object, Object>>  packageRecordList = packageDetailRecordMapper.selectRecordWithSum(groupFilter,filter);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());

            result.setData(JSONObject.toJSON(packageRecordList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }

    public TNPYResponse changeBatteryInventoryRecord( String jsonStr)
    {
        TidyPackageBatteryInventory tidyPackageBatteryInventory =(TidyPackageBatteryInventory) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr), TidyPackageBatteryInventory.class);
        TNPYResponse result = new TNPYResponse();
        try
        {
            if(StringUtils.isEmpty(tidyPackageBatteryInventory.getId()))
            {
                tidyPackageBatteryInventory.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                tidyPackageBatteryInventoryMapper.insertSelective(tidyPackageBatteryInventory);
            }
            else
            {
                tidyPackageBatteryInventoryMapper.updateByPrimaryKeySelective(tidyPackageBatteryInventory);
            }

            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("修改成功！");
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("保存失败！" + ex.getMessage());
        }
        return  result;
    }
}
