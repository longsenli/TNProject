package com.tnpy.mes.service.timedTask;

import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.model.mysql.*;
import com.tnpy.mes.service.pushNotification.impl.websocketManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

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

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private  ProductionProcessMapper productionProcessMapper;

    @Autowired
    private  MaterialSecondaryInventoryRecordMapper materialSecondaryInventoryRecordMapper;

    @Autowired
    private  MaterialInventoryRecordMapper materialInventoryRecordMapper;

    @Autowired
    private  WageDetailMapper wageDetailMapper;

    @Autowired
    private  PlanProductionRecordMapper planProductionRecordMapper;

    @Autowired
    private  WarningMessageRecordMapper warningMessageRecordMapper;

    @Autowired
    private  TidyPackageBatteryInventoryMapper tidyPackageBatteryInventoryMapper;
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
               if(true)
                   return;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            String startTime = dateFormat.format(date) ;
            String endTime = dateFormat.format(date) + " 23:59:59" ;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();
            List<IndustrialPlant> industrialPlantList = industrialPlantMapper.selectAll();
            List<Material> materialList = materialMapper.selectOutByProcess(ConfigParamEnum.BasicProcessEnum.JSProcessID.getName());
            for(int i = 0;i<industrialPlantList.size();i++)
            {
                for(int j =0 ;j<materialList.size();j++)
                {
                    String lastInventoryStr = batteryStastisInventoryRecordMapper.getSelectInventory(industrialPlantList.get(i).getId(),dateFormat.format(date) ,
                            dateFormat.format(date) + " 23:59:59",materialList.get(j).getId());
                    int lastInventoryNum = 0;
                    try
                    {
                        lastInventoryNum = Integer.parseInt( lastInventoryStr );
                    }
                    catch (Exception ex)
                    {
                        lastInventoryNum = 0;
                    }

                    String scrapNumStr = batteryScrapRecordMapper.getScrapNum(industrialPlantList.get(i).getId(),startTime,endTime,materialList.get(j).getId());
                    int scrapNum = 0;
                    try
                    {
                        scrapNum = Integer.parseInt( scrapNumStr );
                    }
                    catch (Exception ex)
                    {
                        scrapNum = 0;
                    }

                    String repairNumStr = batteryRepairRecordMapper.getRepairNum(industrialPlantList.get(i).getId(),startTime,endTime,"1",materialList.get(j).getId());
                    int repairNum = 0;
                    try
                    {
                        repairNum = Integer.parseInt( repairNumStr );
                    }
                    catch (Exception ex)
                    {
                        repairNum = 0;
                    }

                    String repairBackNumStr = batteryRepairRecordMapper.getRepairNum(industrialPlantList.get(i).getId(),startTime,endTime,"2",materialList.get(j).getId());
                    int repairBackNum = 0;
                    try
                    {
                        repairBackNum = Integer.parseInt( repairBackNumStr );
                    }
                    catch (Exception ex)
                    {
                        repairBackNum = 0;
                    }

                    String loanNumStr = batteryBorrowReturnRecordMapper.getLoanNum(industrialPlantList.get(i).getId(),startTime,endTime,materialList.get(j).getId());
                    int loanNum = 0;
                    try
                    {
                        loanNum = Integer.parseInt( loanNumStr );
                    }
                    catch (Exception ex)
                    {
                        loanNum = 0;
                    }
                    String borrowNumStr = batteryBorrowReturnRecordMapper.getBorrowNum(industrialPlantList.get(i).getId(),startTime,endTime,materialList.get(j).getId());
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
                            ConfigParamEnum.BasicProcessEnum.JSProcessID.getName(),materialList.get(j).getId());
                    int dailyProduction = 0;
                    try
                    {
                        dailyProduction = (int)Double.parseDouble(dailyProductionStr.toString());
                    }
                    catch (Exception ex)
                    {
                        dailyProduction = 0;
                    }

                    Object dailyShipmentsStr = materialRecordMapper.getBatteryShipmentNum(startTime,endTime,industrialPlantList.get(i).getId(),materialList.get(j).getId());
                    int dailyShipments = 0;
                    try
                    {
                        dailyShipments = (int)Double.parseDouble(dailyShipmentsStr.toString());
                    }
                    catch (Exception ex)
                    {
                        dailyShipments = 0;
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
                    batteryStastisInventoryRecord.setBatterytype(materialList.get(j).getId());
                    batteryStastisInventoryRecord.setDailyshipmentsnum(dailyShipments);
                    batteryStastisInventoryRecord.setCurrentstorage(lastInventoryNum -dailyShipments - loanNum - scrapNum - repairNum + repairBackNum + borrowNum + dailyProduction);
                   if((lastInventoryNum + loanNum + scrapNum + repairNum + repairBackNum + borrowNum + dailyProduction + dailyShipments) != 0)
                   {
                       batteryStastisInventoryRecordMapper.insertSelective(batteryStastisInventoryRecord);
                   }
                }

            }

        } catch (Exception ex) {
        }
    }

    @Scheduled(cron = "0 50 6 * * ?")
    public void automaticSecondaryInventoryStatistics() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            dateFormat.format(date);
            String timeFinish = "";
            String timeStart = "";
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            timeFinish = dateFormat.format(date) + " 07:00:00";
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            timeStart =dateFormat.format(date) + " 07:00:00";
            List<IndustrialPlant> industrialPlantList = industrialPlantMapper.selectAll();
            List<ProductionProcess> productionProcessList = productionProcessMapper.selectAll();
            for(int i = 0;i<industrialPlantList.size();i++) {
                for (int j = 0; j < productionProcessList.size(); j++) {
                    try
                    {
                        if(ConfigParamEnum.BasicProcessEnum.JSProcessID.getName().equals( productionProcessList.get(j).getId()))
                        {
                            materialSecondaryInventoryRecordMapper.insertJSSecondaryInventory(timeStart,timeFinish,industrialPlantList.get(i).getId(),productionProcessList.get(j).getId(),productionProcessList.get(j-1).getId(),dateFormat.format(date) + " 06:00:00");
                        }
                    }
                   catch (Exception ex)
                   {
                        System.out.println("二级库盘点出错===============" + ex.getMessage() + "  " + industrialPlantList.get(i).getName() + " " + productionProcessList.get(j).getName());
                   }
                }
            }

        } catch (Exception ex) {
        }
    }

    @Scheduled(cron = "0 54 6 * * ?")
    public void automaticInventoryStatistics() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            dateFormat.format(date);
            String timeFinish = "";
            String timeStart = "";
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            timeFinish = dateFormat.format(date) + " 07:00:00";
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            timeStart =dateFormat.format(date) + " 07:00:00";

            List<IndustrialPlant> industrialPlantList = industrialPlantMapper.selectAll();
            List<ProductionProcess> productionProcessList = productionProcessMapper.selectAll();


            for(int i = 0;i<industrialPlantList.size();i++) {

                for (int j = 0; j < productionProcessList.size(); j++) {
                    try
                    {
                        if(ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName().equals( productionProcessList.get(j).getId()))
                        {
                            materialInventoryRecordMapper.insertZHInventoryStatistics(timeStart,timeFinish,industrialPlantList.get(i).getId(),productionProcessList.get(j).getId(),productionProcessList.get(j+1).getId(),dateFormat.format(date) +" 06:00:00");
                        }

                        if(ConfigParamEnum.BasicProcessEnum.JZProcessID.getName().equals( productionProcessList.get(j).getId()))
                        {
                            materialInventoryRecordMapper.insertJZInventoryStatistics(timeStart,timeFinish,industrialPlantList.get(i).getId(),productionProcessList.get(j).getId(),productionProcessList.get(j+1).getId(),dateFormat.format(date) +" 06:00:00");
                            wageDetailMapper.insertRecord(industrialPlantList.get(i).getId(),productionProcessList.get(j).getId(),timeStart,timeFinish);
                        }
                    }
                    catch (Exception ex)
                    {
                        System.out.println("一级库盘点出错===============" + ex.getMessage() + "  " + industrialPlantList.get(i).getName() + " " + productionProcessList.get(j).getName());
                    }
                }
            }

            SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
            planProductionRecordMapper.updateFinishRate(dateFormatMonth.format(date));

            date = new Date();
            String endTime = dateFormat.format(date);
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            String startTime = dateFormat.format(date);
            calendar.add(Calendar.DATE, -8);
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            String onTidyingTime = dateFormat.format(date);
            tidyPackageBatteryInventoryMapper.insertInventoryRecord(startTime,endTime,onTidyingTime);
            tidyPackageBatteryInventoryMapper.deleteRemark();
        } catch (Exception ex) {
            System.out.println("盘点出错" + ex.getMessage());
        }
    }
    @Autowired
    private JavaMailSender jms;

    @Scheduled(fixedRate = 1000 * 60 * 5)
   // @Scheduled(fixedRate = 1000 *60)
    public void automaticPushSafetyNotification() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date();//取时间

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, -5);
            String nowDate = dateFormat.format(date);
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果

            HashSet<String> notificationtypeIDSet = new HashSet<>();
            String filter =" where  updateTime >= '" + dateFormat.format(date) +"' and updateTime < '" +nowDate + "'" ;
           List<WarningMessageRecord> warningMessageRecordList = warningMessageRecordMapper.selectByFilter(filter);

           while(true)
           {
               String notificationtypeIDStr = "";
           String messageDetail = "";
           String plantID = "###";
           for(int i =0;i<warningMessageRecordList.size();i++)
           {
               if(notificationtypeIDSet.contains(warningMessageRecordList.get(i).getNotificationtypeid()))
                   continue;
               notificationtypeIDStr = warningMessageRecordList.get(i).getNotificationtypeid();
               notificationtypeIDSet.add(notificationtypeIDStr);
               break;
           }
           if(notificationtypeIDStr.length() < 2)
               break;
          // List<> 根据消息类型寻找推送的人
           for(int i =0;i<warningMessageRecordList.size();i++)
           {
               if(!notificationtypeIDStr.equals(warningMessageRecordList.get(i).getNotificationtypeid()))
                   continue;
               messageDetail +=  warningMessageRecordList.get(i).getMessage() +".\r\n ";
               plantID += warningMessageRecordList.get(i).getPlantid() +"###";
           }
           if(warningMessageRecordList.size() <1)
           {
               return;
           }
           List<Map<Object,Object>> userInfoList = warningMessageRecordMapper.selectUserInfoByWarning(notificationtypeIDStr);
            HashSet<String> userSet = new HashSet<>();

            if(notificationtypeIDStr.equals("100001"))
            {
                plantID += "3002###";
            }
            for(int i =0;i<userInfoList.size();i++)
            {
                if(!plantID.contains(userInfoList.get(i).get("industrialplant_id").toString()) && userInfoList.get(i).get("industrialplant_id").toString().length() > 2 )
                {
                    continue;
                }
                userSet.add(userInfoList.get(i).get("userID").toString());
                if(StringUtils.isEmpty( userInfoList.get(i).get("email").toString()))
                    continue;
                /*  //发送邮件
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                //谁发的
                mailMessage.setFrom("llsbenign@163.com");
                //发给谁
                mailMessage.setTo(userInfoList.get(i).get("email").toString());
               // mailMessage.setTo("631620498@qq.com");

                //标题
                mailMessage.setSubject("MES系统预警提醒");
                //内容
                mailMessage.setText(messageDetail);

                jms.send(mailMessage);
                */
            }
            if(userSet.size() > 0)
               websocketManageService.sendInfoToUserList(messageDetail,userSet);
           }

        } catch (Exception ex) {
            System.out.println("fail======" +ex.getMessage());
        }
    }

}
