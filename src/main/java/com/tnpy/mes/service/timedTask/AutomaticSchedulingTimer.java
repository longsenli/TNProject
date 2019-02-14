package com.tnpy.mes.service.timedTask;

import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.mysql.BatteryStastisInventoryRecord;
import com.tnpy.mes.model.mysql.IndustrialPlant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/21 14:30
 */

@Component //使spring管理
@EnableScheduling //定时任务注解
public class AutomaticSchedulingTimer {
    @Autowired
    private WorkShiftRecordMapper workShiftRecordMapper;
    @Autowired
    private WorkorderMapper workorderMapper;

    @Autowired
    private BatteryRepairRecordMapper batteryRepairRecordMapper;

    @Autowired
    private BatteryScrapRecordMapper batteryScrapRecordMapper;

    @Autowired
    private BatteryBorrowReturnRecordMapper batteryBorrowReturnRecordMapper;
    @Autowired
    private  BatteryStastisInventoryRecordMapper batteryStastisInventoryRecordMapper;

    @Autowired
    private  IndustrialPlantMapper industrialPlantMapper;

    @Autowired
    private  MaterialRecordMapper materialRecordMapper;
    /**
     * 每天晚上21:50:30运行
     */
    @Scheduled(cron = "00 45 23 * * ?")
    public void automaticScheduling() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            String ybShift = workShiftRecordMapper.getYBTeam(dateFormat.format(date));

            calendar.add(Calendar.DATE, 1);//把日期往后增加一天.正数往后推,负数往前推
            int dayNum = calendar.get(Calendar.DATE);
            if (dayNum == 16 || dayNum == 1) {
                if ("A".equals(ybShift))
                    ybShift = "B";
                else
                    ybShift = "A";
            }
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果

            workShiftRecordMapper.automaticScheduling(ybShift, dateFormat.format(date));
        } catch (Exception ex) {

        }
    }

    @Scheduled(cron = "0 58 6,18 * * ?")
    public void automaticOrderStatus() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            dateFormat.format(date);
            String timeFinish = "";
            String timeStart = "";
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if(calendar.get(Calendar.HOUR_OF_DAY) < 9)
            {
                timeStart = dateFormat.format(date) + " 07:00:00";

                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                timeFinish =dateFormat.format(date) + " 19:00:00";
            }
            else
            {
                timeStart = dateFormat.format(date) + " 19:00:00";
                timeFinish = dateFormat.format(date) + " 07:00:00";
            }
            workorderMapper.finishOrder(timeFinish,StatusEnum.WorkOrderStatus.finished.getIndex() + "");
            workorderMapper.startOrder(timeStart, StatusEnum.WorkOrderStatus.doing.getIndex() + "");
        } catch (Exception ex) {

        }
    }

    @Scheduled(cron = "0 50 23 * * ?")
    public void automaticBatteryStatisInventory() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            String startTime = dateFormat.format(date) ;
            String endTime = dateFormat.format(date) + " 23:59:59" ;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();
            List<IndustrialPlant> industrialPlantList = industrialPlantMapper.selectAll();
            for(int i = 0;i<industrialPlantList.size();i++)
            {
                String lastInventoryStr = batteryStastisInventoryRecordMapper.getSelectInventory(industrialPlantList.get(i).getId(),dateFormat.format(date) ,dateFormat.format(date) + " 23:59:59");
                int lastInventoryNum = 0;
               try
                {
                    lastInventoryNum = Integer.parseInt( lastInventoryStr );
                }
               catch (Exception ex)
               {
                   lastInventoryNum = 0;
               }

               String scrapNumStr = batteryScrapRecordMapper.getScrapNum(industrialPlantList.get(i).getId(),startTime,endTime);
               int scrapNum = 0;
                try
                {
                    scrapNum = Integer.parseInt( scrapNumStr );
                }
                catch (Exception ex)
                {
                    scrapNum = 0;
                }

                String repairNumStr = batteryRepairRecordMapper.getRepairNum(industrialPlantList.get(i).getId(),startTime,endTime,"1");
                int repairNum = 0;
                try
                {
                    repairNum = Integer.parseInt( repairNumStr );
                }
                catch (Exception ex)
                {
                    repairNum = 0;
                }

                String repairBackNumStr = batteryRepairRecordMapper.getRepairNum(industrialPlantList.get(i).getId(),startTime,endTime,"2");
                int repairBackNum = 0;
                try
                {
                    repairBackNum = Integer.parseInt( repairBackNumStr );
                }
                catch (Exception ex)
                {
                    repairBackNum = 0;
                }

                String loanNumStr = batteryBorrowReturnRecordMapper.getLoanNum(industrialPlantList.get(i).getId(),startTime,endTime);
                int loanNum = 0;
                try
                {
                    loanNum = Integer.parseInt( loanNumStr );
                }
                catch (Exception ex)
                {
                    loanNum = 0;
                }
                String borrowNumStr = batteryBorrowReturnRecordMapper.getBorrowNum(industrialPlantList.get(i).getId(),startTime,endTime);
                int borrowNum = 0;
                try
                {
                    borrowNum = Integer.parseInt( borrowNumStr );
                }
                catch (Exception ex)
                {
                    borrowNum = 0;
                }

                Object dailyProductionStr = materialRecordMapper.getJSProcessBatteryProduction(startTime,endTime,industrialPlantList.get(i).getId(),
                        ConfigParamEnum.BasicProcessEnum.JSProcessID.getName());
                int dailyProduction = 0;
                try
                {
                    dailyProduction = (int)Double.parseDouble(dailyProductionStr.toString());
                }
                catch (Exception ex)
                {
                    dailyProduction = 0;
                }
                BatteryStastisInventoryRecord batteryStastisInventoryRecord = new BatteryStastisInventoryRecord();
                batteryStastisInventoryRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                batteryStastisInventoryRecord.setBorrownum(borrowNum);
                batteryStastisInventoryRecord.setLaststorage(lastInventoryNum);
                batteryStastisInventoryRecord.setLoannum(loanNum);
                batteryStastisInventoryRecord.setPlantid(industrialPlantList.get(i).getId());
                batteryStastisInventoryRecord.setRepairbacknum(repairBackNum);
                batteryStastisInventoryRecord.setRepairnum(repairNum);
                batteryStastisInventoryRecord.setScrapnum(scrapNum);
                batteryStastisInventoryRecord.setStatus(StatusEnum.StatusFlag.using.getIndex()+ "");
                batteryStastisInventoryRecord.setUpdatetime(new Date());
                batteryStastisInventoryRecord.setDailyproduction(dailyProduction);
                batteryStastisInventoryRecord.setCurrentstorage(lastInventoryNum - loanNum - scrapNum - repairNum + repairBackNum + borrowNum + dailyProduction);
                batteryStastisInventoryRecordMapper.insertSelective(batteryStastisInventoryRecord);
            }

        } catch (Exception ex) {
        }
    }
}
