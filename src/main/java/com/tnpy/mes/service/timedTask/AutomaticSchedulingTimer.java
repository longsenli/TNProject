package com.tnpy.mes.service.timedTask;

import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.mes.mapper.mysql.WorkShiftRecordMapper;
import com.tnpy.mes.mapper.mysql.WorkorderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
}
