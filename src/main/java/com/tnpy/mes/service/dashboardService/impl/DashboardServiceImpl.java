package com.tnpy.mes.service.dashboardService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.DailyProductionSummaryLineMapper;
import com.tnpy.mes.mapper.mysql.DashboardMapper;
import com.tnpy.mes.mapper.mysql.ObjectRelationDictMapper;
import com.tnpy.mes.mapper.mysql.WageDetailMapper;
import com.tnpy.mes.service.dashboardService.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019-04-22 10:28
 */
@Service("dashboardService")
public class DashboardServiceImpl implements IDashboardService {
    @Autowired
    private DashboardMapper dashboardMapper;

    @Autowired
    private WageDetailMapper wageDetailMapper;

    @Autowired
    private ObjectRelationDictMapper objectRelationDictMapper;

    @Autowired
    private DailyProductionSummaryLineMapper dailyProductionSummaryLineMapper;

    @Override
    public TNPYResponse getDailyProduction(String plantID, String processID, String queryTypeID, String startTime, String endTime) {
        /*
        queryTypeID 前后台统一：
        <option value='byLine'>产线</option>
					<option value='byMaterial'>物料</option>
					<option value='byWorkingLocation'>工位</option>
					<option value='byStaff'>人员</option>
					<option value='byClassType'>班次</option>
					<option value='byGrantMaterial'>工序发料</option>
					<option value='byGainMaterial'>工序领料</option>
					<option value='byLineMaterial'>产线物料</option>
					<option value='byStaffAndMaterial'>人员物料</option>
					<option value='byLineExpend'>产线投料</option>
					<option value='byStaffExpend'>人员投料</option>
					<option value='byWage'>人员工资</option>
					<option value='byOrderDetail'>工单详情</option>
         */

        TNPYResponse result = new TNPYResponse();
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            //String->Date
//            Date newStartTime = sdf.parse(startTime); //new Date(startTime);
//            Date newEndTime = sdf.parse(endTime); // new Date(endTime);
//            String newStartTimeStr = sdf.format(newStartTime) + " 07:00:00";
//            //Date->Calendar
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(newEndTime);
//            //计算过期日
//            calendar.add(Calendar.DATE, 1);
//
//            //Calendar->Date
//            newEndTime = calendar.getTime();
//
//            String newEndTimeStr =sdf.format(newEndTime) + " 07:00:00";

            String querySQL = "";
            if ("byWage".equals(queryTypeID)) {
                return getWageDetail(plantID, processID, startTime.split(" ")[0], endTime.split(" ")[0]);
            }
            if ("byLine".equals(queryTypeID)) {

                if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                    querySQL = "(select '' as orderDay,'总计' as orderHour,b.lineID as inputLineID,count(1) as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by b.lineID order by b.lineID )\n" +
                            "    union all\n" +
                            "( select a.orderDay,a.orderHour,b.lineID as inputLineID, count(1) as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '1015'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by a.orderDay,a.orderHour,b.lineID order by lineID,orderDay,orderHour desc )";
                } else {
                    querySQL = "(select inputLineID,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                            "select b.inputLineID,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputLineID,orderDay,orderHour order by inputLineID,orderDay,orderHour desc ) a group by inputLineID limit 1000)\n" +
                            "union all\n" +
                            "(select b.inputLineID,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputLineID,orderDay,orderHour order by inputLineID,orderDay,orderHour desc limit 1000)";
                }
            }
            if ("byWorkingLocation".equals(queryTypeID)) {
                if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                    querySQL = "select m.* from (\n" +
                            "(select '' as orderDay,'总计' as orderHour,b.workLocation as inputWorkLocationID,count(1) as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by b.workLocation order by b.workLocation )\n" +
                            "    union all\n" +
                            "( select a.orderDay,a.orderHour,b.workLocation as inputWorkLocationID, count(1) as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by a.orderDay,a.orderHour,b.workLocation order by workLocation,orderDay,orderHour desc )" +
                            " ) m left join tb_worklocation n on m.inputWorkLocationID = n.id order by m.orderDay, n.ordinal ";
                } else {
                    querySQL = "select m.* from ((select inputWorkLocationID,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                            "select b.inputWorkLocationID,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and  scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputWorkLocationID,orderDay,orderHour order by inputWorkLocationID,orderDay,orderHour desc ) a group by inputWorkLocationID limit 1000)\n" +
                            "union all\n" +
                            "(select b.inputWorkLocationID,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputWorkLocationID,orderDay,orderHour order by inputWorkLocationID,orderDay,orderHour desc limit 1000) \n" +
                            " ) m left join tb_worklocation n on m.inputWorkLocationID = n.id order by m.orderDay, n.ordinal ";
                }
            }

            if ("byStaff".equals(queryTypeID)) {
                if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                    querySQL = "(select '' as orderDay,'总计' as orderHour,b.staffName as inputer,count(1) as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by b.staffName order by b.staffName )\n" +
                            "    union all\n" +
                            "( select a.orderDay,a.orderHour,b.staffName as inputer,count(1)  as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by a.orderDay,a.orderHour,b.staffName order by staffName,orderDay,orderHour desc )";
                } else {
                    querySQL = "(select inputer,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                            "select b.inputer,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputer,orderDay,orderHour order by orderDay,orderHour,inputer desc ) a group by inputer limit 1000)\n" +
                            "union all\n" +
                            "(select b.inputer,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputer,orderDay,orderHour order by orderDay,orderHour desc,inputer limit 1000)";
                }
            }

            if ("byMaterial".equals(queryTypeID)) {

                if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                    querySQL = "(select '' as orderDay,'总计' as orderHour,b.materialName as materialNameInfo,count(1) as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by b.materialName order by b.materialName )\n" +
                            "    union all\n" +
                            "( select a.orderDay,a.orderHour,b.materialName as materialNameInfo,count(1)  as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by a.orderDay,a.orderHour,b.materialName order by materialName,orderDay,orderHour desc )";
                } else {
                    querySQL = "(select materialNameInfo,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                            "select b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by materialNameInfo,orderDay,orderHour order by materialNameInfo,orderDay,orderHour desc ) a group by materialNameInfo limit 1000)\n" +
                            "union all\n" +
                            "(select b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and  inputLineID is not null group by materialNameInfo,orderDay,orderHour order by materialNameInfo,orderDay,orderHour desc limit 1000)";
                }
            }

            if ("byStaffAndMaterial".equals(queryTypeID)) {
                if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                    querySQL = "(select b.staffName as inputer,'' as orderDay,'总计' as orderHour,b.materialName as materialNameInfo,count(1) as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by b.staffName,b.materialName order by b.staffName,b.materialName )\n" +
                            "    union all\n" +
                            "( select b.staffName as inputer,a.orderDay,a.orderHour,b.materialName as materialNameInfo,count(1)  as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by a.orderDay,a.orderHour,b.materialName,b.staffName order by b.staffName, materialName,orderDay,orderHour desc )";
                } else {
                    querySQL = "(select inputer,materialNameInfo,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                            "select b.inputer,b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputer,materialNameInfo,orderDay,orderHour order by orderDay,orderHour desc,inputer,materialNameInfo ) a group by inputer,materialNameInfo limit 1000)\n" +
                            "union all\n" +
                            "(select b.inputer,b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputer,materialNameInfo,orderDay,orderHour order by orderDay,orderHour desc,inputer,materialNameInfo desc limit 1000)";
                }
            }
            if ("byClassType".equals(queryTypeID)) {
                if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                    querySQL = "(select '总计' as orderDay, orderHour,count(1) as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by orderHour  )\n" +
                            "    union all\n" +
                            "( select a.orderDay,a.orderHour,count(1)  as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by a.orderDay,a.orderHour order by orderDay,orderHour desc )";
                } else {
                    querySQL = "(select '总计' as orderDay, orderHour,sum(sumProduction)  as sumProduction from (\n" +
                            "select a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by orderDay,orderHour order by orderDay,orderHour desc ) a group by orderHour limit 1000)\n" +
                            "union all\n" +
                            "(select a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by orderDay,orderHour order by orderDay,orderHour desc limit 1000)";
                }
            }

            if ("byOrderDetail".equals(queryTypeID)) {
                querySQL = "(select a.orderDay,a.orderHour,b.subOrderID,b.materialNameInfo,b.number,b.inputer,b.inputTime from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where  b.status = '1' and  inputLineID is not null  order by orderDay,orderHour desc,inputTime limit 1000)";

            }

            if ("byLineMaterial".equals(queryTypeID)) {
                if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                    querySQL = "(select b.lineID as inputLineID,'' as orderDay,'总计' as orderHour,b.materialName as materialNameInfo,count(1) as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by b.lineID,b.materialName order by b.lineID,b.materialName )\n" +
                            "    union all\n" +
                            "( select b.lineID as inputLineID,a.orderDay,a.orderHour,b.materialName as materialNameInfo,count(1)  as sumProduction from ( select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            "  case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName() + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a left join tb_plasticusedrecord b on a.id = b.usedOrderID\n" +
                            "    where b.lineID is not null   group by a.orderDay,a.orderHour,b.materialName,b.lineID order by b.lineID, materialName,orderDay,orderHour desc )";

                } else if (ConfigParamEnum.BasicProcessEnum.CDProcessID.getName().equals(processID)) {
                    querySQL = "select lineID,materialName,materialType,timeStr,sum(onTotalNum) as onTotalNum,sum(offTotalNum) as offTotalNum,sum(repairNum) as repairNum,sum(realNum) as realNum from (\n" +
                            " ( select lineID,materialName,materialType,putonDate as timeStr,sum(productionNumber) as onTotalNum,0 as offTotalNum, 0 as repairNum,0 as realNum from tb_chargingrackrecord \n" +
                            "where plantID = '" + plantID + "' and  putonDate >= '" + startTime.split(" ")[0] + "' and  putonDate <= '" + endTime.split(" ")[0] + " 23:59' group by lineID,materialName,materialType,putonDate ) union all \n" +
                            "( select lineID,materialName,materialType,DATE_FORMAT(pulloffDate, '%Y-%m-%d') as timeStr,0 as onTotalNum,sum(productionNumber) as offTotalNum,sum(repairNumber) as repairNum,sum(realNumber) as realNum from tb_chargingrackrecord \n" +
                            "where plantID = '" + plantID + "' and  pulloffDate >= '" + startTime.split(" ")[0] + "' and  pulloffDate <= '" + endTime.split(" ")[0] + " 23:59'  group by lineID,materialName,materialType,timeStr ) ) a " +
                            "group by lineID,materialName,materialType,timeStr order by timeStr,lineID,materialName ";
                } else {
                    querySQL = "(select inputLineID,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction,materialNameInfo  from (\n" +
                            "select b.inputLineID,a.orderDay,a.orderHour,sum(number) as sumProduction ,materialNameInfo from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5' and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputLineID,orderDay,orderHour ,materialNameInfo order by inputLineID,orderDay,orderHour desc ) a group by inputLineID,materialNameInfo limit 1000)\n" +
                            "union all\n" +
                            "(select b.inputLineID,a.orderDay,a.orderHour,sum(number) as sumProduction ,materialNameInfo from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                            " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                            "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and status < '5'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                            "left join tb_materialrecord  b on a.id = b.orderID where b.status = '1' and inputLineID is not null group by inputLineID,orderDay,orderHour ,materialNameInfo order by orderDay,orderHour desc,inputLineID limit 1000)";

                }
            }
            if ("byLineExpend".equals(queryTypeID)) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                //String->Date
                Date newStartTime = sdf.parse(startTime); //new Date(startTime);
                Date newEndTime = sdf.parse(endTime); // new Date(endTime);
                String newStartTimeStr = sdf.format(newStartTime) + " 07:00:00";
                //Date->Calendar
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newEndTime);
                //计算过期日
                calendar.add(Calendar.DATE, 1);

                //Calendar->Date
                newEndTime = calendar.getTime();

                String newEndTimeStr = sdf.format(newEndTime) + " 07:00:00";

                if (ConfigParamEnum.BasicProcessEnum.JSProcessID.getName().equals(processID)) {
                    newStartTimeStr = startTime;
                    newEndTimeStr = endTime + " 23:59:59";
                }
                if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                    querySQL = "(select '总计' as banci,lineID as outputLineID,materialName as materialNameInfo , '' as dayTime,count(1) as number from tb_plasticusedrecord \n" +
                            "where  right( usedOrderID,8 ) >= '"+sdf2.format(newStartTime)+"' and  right( usedOrderID,8 ) <= '"+sdf2.format(newEndTime)+"' and plantID = '"+plantID+"' group  by lineID,materialName)\n" +
                            "union all\n" +
                            "(select  case when  substring( usedOrderID,-10,2) = 'BB' then '白班' else '夜班' end as banci,lineID as outputLineID,materialName as materialNameInfo,\n" +
                            "CONCAT(substring( usedOrderID,-8,4) ,'-', substring( usedOrderID,-4,2),'-' , substring( usedOrderID,-2,2) ) as dayTime,count(1) as number  from tb_plasticusedrecord \n" +
                            "where  right( usedOrderID,8 ) >= '"+sdf2.format(newStartTime)+"' and  right( usedOrderID,8 ) <= '"+sdf2.format(newEndTime)+"' and plantID ='"+plantID+"' group  by right( usedOrderID,10 ), lineID,materialName order by dayTime,lineID,banci desc limit 1000)\n" ;

                }
                else
                {
                    querySQL = " select '总计' as banci,'' as dayTime, outputLineID,materialNameInfo,sum(number) as number  from tb_materialrecord where outputPlantID = '" + plantID + "' \n" +
                            " and outputProcessID = '" + processID + "' and outputTime > '" + newStartTimeStr + "' and outputTime < '" + newEndTimeStr + "'   group by outputLineID,materialNameInfo order by outputLineID limit 1000)\n" +
                            " union all \n" +
                            "( " +
                            "select case  when left(timeStr,2) = 'BB' then '白班'  else '夜班' end as banci ,right(timeStr,8) as dayTime ,outputLineID,materialNameInfo,number from (\n" +
                            "select outputLineID,sum(number) as number,right(expendOrderID,10) as timeStr,materialNameInfo  from tb_materialrecord where outputPlantID = '" + plantID + "' \n" +
                            "and outputProcessID = '" + processID + "' and outputTime > '" + newStartTimeStr + "' and outputTime < '" + newEndTimeStr + "'  group by outputLineID,materialNameInfo,timeStr ) a  order by dayTime desc,banci,outputLineID limit 1000 ) ";
                }
            }

            if ("byStaffExpend".equals(queryTypeID)) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
                //String->Date
                Date newStartTime = sdf.parse(startTime); //new Date(startTime);
                Date newEndTime = sdf.parse(endTime); // new Date(endTime);
                String newStartTimeStr = sdf.format(newStartTime) + " 07:00:00";
                //Date->Calendar
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newEndTime);
                //计算过期日
                calendar.add(Calendar.DATE, 1);

                //Calendar->Date
                newEndTime = calendar.getTime();

                String newEndTimeStr = sdf.format(newEndTime) + " 07:00:00";

                if (ConfigParamEnum.BasicProcessEnum.ZHQDProcessID.getName().equals(processID)) {
                    querySQL = "(select '总计' as banci,staffName as outputer,materialName as materialNameInfo, '' as dayTime,count(1) as number  from tb_plasticusedrecord \n" +
                            "where  right( usedOrderID,8 ) >= '" + sdf2.format(newStartTime) + "' and  right( usedOrderID,8 ) <= '" + sdf2.format(newEndTime) + "' and plantID = '" + plantID + "' group  by staffName,materialName)\n" +
                            "union all\n" +
                            "(select  case when  substring( usedOrderID,-10,2) = 'BB' then '白班' else '夜班' end as banci,staffName  as outputer,materialName as materialNameInfo,\n" +
                            "CONCAT(substring( usedOrderID,-8,4) ,'-', substring( usedOrderID,-4,2),'-' , substring( usedOrderID,-2,2) ) as dayTime,count(1)  as number from tb_plasticusedrecord \n" +
                            "where  right( usedOrderID,8 ) >= '" + sdf2.format(newStartTime) + "' and  right( usedOrderID,8 ) <= '" + sdf2.format(newEndTime) + "' and plantID ='" + plantID + "' group  by right( usedOrderID,10 ), staffName,materialName order by dayTime,lineID,banci desc limit 1000)\n";

                } else {
                    querySQL = "(\n" +
                            " select '总计' as banci,'' as dayTime, outputer,materialNameInfo,sum(number) as number  from tb_materialrecord where outputPlantID = '" + plantID + "' \n" +
                            "and outputProcessID = '" + processID + "' and outputTime > '" + newStartTimeStr + "' and outputTime < '" + newEndTimeStr + "'  group by outputer,materialNameInfo order by outputer limit 1000)\n" +
                            " union all \n" +
                            "( \n" +
                            "select case  when left(timeStr,2) = 'BB' then '白班'  else '夜班' end as banci ,right(timeStr,8) as dayTime ,outputer,materialNameInfo,number from (\n" +
                            "select outputer,sum(number) as number,right(expendOrderID,10) as timeStr,materialNameInfo  from tb_materialrecord where outputPlantID = '" + plantID + "' \n" +
                            "and outputProcessID = '" + processID + "' and outputTime > '" + newStartTimeStr + "' and outputTime < '" + newEndTimeStr + "'  group by outputer,materialNameInfo,timeStr ) a  order by dayTime desc,banci, outputer  limit 1000)";
                }
            }

            if ("byGrantMaterial".equals(queryTypeID)) {
                if (ConfigParamEnum.BasicProcessEnum.ZHProcessID.getName().equals(processID)) {
                    querySQL = "( select b.name as materialName,a.number,'总计' as classType, '' as grantDayTime ,'' as acceptPlant from (  select batteryType,sum(number ) as number from tb_grantmaterialrecord \n" +
                            " where plantID = '" + plantID + "' and processID = '" + processID + "' and grantTime > '" + startTime + "' and grantTime < '" + endTime + " 23:59:59' group by batteryType ) a left join sys_material b on a.batteryType = b.id  order by materialName limit 100 ) \n" +
                            " union all\n" +
                            "( select c.materialName,c.number,'白班' as classType,c.grantDayTime,d.name as acceptPlant from ( select b.name as materialName,a.number,a.acceptPlantID,a.grantDayTime from (  select batteryType,sum(number ) as number,\n" +
                            " acceptPlantID, DATE_FORMAT(grantTime, '%Y-%m-%d') as grantDayTime from tb_grantmaterialrecord where plantID = '" + plantID + "' and processID = '" + processID + "' and grantTime > '" + startTime + "' \n" +
                            " and grantTime < '" + endTime + " 23:59' group by grantDayTime,batteryType,acceptPlantID ) a left join sys_material b on a.batteryType = b.id  ) c left join sys_industrialplant d on c.acceptPlantID = d.id order by grantDayTime,materialName limit 1000 )";
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date newEndTime = sdf.parse(endTime); // new Date(endTime);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(newEndTime);
                    calendar.add(Calendar.DATE, 1);
                    newEndTime = calendar.getTime();
                    endTime = sdf.format(newEndTime) + " 07:00";

                    Date newStartTime = sdf.parse(startTime); //new Date(startTime);
                    startTime = sdf.format(newStartTime);
                    querySQL = "( select b.name as materialName,a.number,'总计' as classType, '' as grantDayTime ,'' as acceptPlant from (  select batteryType,sum(number ) as number from tb_grantmaterialrecord \n" +
                            " where plantID = '" + plantID + "' and processID = '" + processID + "' and grantTime > '" + startTime + " 07:00' and grantTime < '" + endTime + "' group by batteryType ) a left join sys_material b on a.batteryType = b.id  order by materialName limit 100 ) \n" +
                            " union all\n" +
                            "( select c.materialName,c.number,c.classType,c.grantDayTime,d.name as acceptPlant from ( select b.name as materialName,a.number,a.classType,a.acceptPlantID,a.grantDayTime from (  select batteryType,sum(number ) as number,\n" +
                            " case  when date_format(grantTime,'%H') < '19' and date_format(grantTime,'%H') > '06' then '白班'  else '夜班' end  as classType," +
                            " acceptPlantID, case when date_format(grantTime,'%H') < '07' then  DATE_FORMAT(date_add(grantTime, interval -1 day), '%Y-%m-%d') else DATE_FORMAT(grantTime, '%Y-%m-%d') end as grantDayTime" +
                            "  from tb_grantmaterialrecord where plantID = '" + plantID + "' and processID = '" + processID + "' and grantTime > '" + startTime + " 07:00' \n" +
                            " and grantTime < '" + endTime + "' group by grantDayTime,classType,batteryType,acceptPlantID ) a left join sys_material b on a.batteryType = b.id  ) c left join sys_industrialplant d on c.acceptPlantID = d.id order by grantDayTime,classType desc,materialName limit 1000 )";
                }
            }

            if ("byGainMaterial".equals(queryTypeID)) {
                List<String> lastProcessID = objectRelationDictMapper.selectPreviousObjectID(processID, "1001");
                if (lastProcessID.size() < 1) {
                    result.setMessage("未获取到前一工序信息！不知道发料工序！");
                    return result;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date newEndTime = sdf.parse(endTime); // new Date(endTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newEndTime);
                calendar.add(Calendar.DATE, 1);
                newEndTime = calendar.getTime();
                endTime = sdf.format(newEndTime);

                processID = lastProcessID.get(0);
                Date newStartTime = sdf.parse(startTime); //new Date(startTime);
                startTime = sdf.format(newStartTime);
                if (ConfigParamEnum.BasicProcessEnum.JSProcessID.getName().equals(processID)) {
                    endTime = endTime + " 23:29";
                    querySQL = "( select b.name as materialName,a.number,'总计' as classType,'' as grantDayTime ,'' as grantPlant from (  select batteryType,sum(number ) as number from tb_grantmaterialrecord \n" +
                            " where acceptPlantID = '" + plantID + "' and processID = '" + processID + "' and grantTime > '" + startTime + "' and grantTime < '" + endTime + "' group by batteryType ) a left join sys_material b on a.batteryType = b.id  order by materialName limit 100 ) \n" +
                            " union all\n" +
                            "( select  c.materialName,c.number,c.classType,c.grantDayTime,d.name as grantPlant from ( select b.name as materialName,a.classType,a.number,a.plantID,a.grantDayTime from (  select batteryType,sum(number ) as number,\n" +
                            "  '白班'    as classType," +
                            " plantID,  DATE_FORMAT(grantTime, '%Y-%m-%d')  as grantDayTime" +
                            " from tb_grantmaterialrecord where acceptPlantID = '" + plantID + "' and processID = '" + processID + "' and grantTime > '" + startTime + "' \n" +
                            " and grantTime < '" + endTime + "' group by grantDayTime,batteryType,plantID ) a left join sys_material b on a.batteryType = b.id  ) c left join sys_industrialplant d on c.plantID = d.id order by grantDayTime,materialName limit 1000 )";
                } else {
                    startTime = startTime + " 07:00";
                    endTime = sdf.format(newEndTime) + " 07:00";
                    querySQL = "( select b.name as materialName,a.number,'总计' as classType,'' as grantDayTime ,'' as grantPlant from (  select batteryType,sum(number ) as number from tb_grantmaterialrecord \n" +
                            " where acceptPlantID = '" + plantID + "' and processID = '" + processID + "' and grantTime > '" + startTime + "' and grantTime < '" + endTime + "' group by batteryType ) a left join sys_material b on a.batteryType = b.id  order by materialName limit 100 ) \n" +
                            " union all\n" +
                            "( select  c.materialName,c.number,c.classType,c.grantDayTime,d.name as grantPlant from ( select b.name as materialName,a.classType,a.number,a.plantID,a.grantDayTime from (  select batteryType,sum(number ) as number,\n" +
                            " case  when date_format(grantTime,'%H') < '19' and date_format(grantTime,'%H') > '06' then '白班'  else '夜班' end  as classType," +
                            " plantID, case when date_format(grantTime,'%H') < '07' then  DATE_FORMAT(date_add(grantTime, interval -1 day), '%Y-%m-%d') else DATE_FORMAT(grantTime, '%Y-%m-%d') end as grantDayTime" +
                            " from tb_grantmaterialrecord where acceptPlantID = '" + plantID + "' and processID = '" + processID + "' and grantTime > '" + startTime + "' \n" +
                            " and grantTime < '" + endTime + "' group by grantDayTime,classType,batteryType,plantID ) a left join sys_material b on a.batteryType = b.id  ) c left join sys_industrialplant d on c.plantID = d.id order by grantDayTime,classType desc,materialName limit 1000 )";
                }
            }

            if ("byScrapMaterial".equals(queryTypeID)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //String->Date
                Date newStartTime = sdf.parse(startTime); //new Date(startTime);
                Date newEndTime = sdf.parse(endTime); // new Date(endTime);

                querySQL = "( SELECT materialName,sum(value) as scrapNumber,'' as classType,'' as  productDayStr FROM ilpsdb.tb_materialscraprecord \n" +
                        " where plantID = '" + plantID + "' and processID = '" + processID + "' and productDay >= '" + sdf.format(newStartTime) + "'  and productDay <= '" + sdf.format(newEndTime) + "' group by materialName order by materialName limit 100)\n" +
                        " union all \n" +
                        " ( SELECT materialName,sum(value) as scrapNumber,classType,DATE_FORMAT(productDay, '%Y-%m-%d') as productDayStr FROM ilpsdb.tb_materialscraprecord \n" +
                        " where plantID = '" + plantID + "' and processID = '" + processID + "' and productDay >= '" + sdf.format(newStartTime) + "'  and productDay <= '" + sdf.format(newEndTime) + "'  group by productDayStr,classType,materialName order by productDayStr,classType,materialName  limit 1000)";
            }

            List<Map<Object, Object>> mapList = dashboardMapper.getDailyProduction(querySQL);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }

    }

    @Override
    public TNPYResponse nowInDryingKilnjz(String plantID, String processID, String queryTypeID, String startTime,
                                          String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String sql = "(\r\n" +
                    "	SELECT\r\n" +
                    "		d.id,\r\n" +
                    "		d.dryingKilnID,\r\n" +
                    "		d.dryingKilnName,\r\n" +
                    "		d.suborderID,\r\n" +
                    "		d.plantID,\r\n" +
                    "		p.'name' AS plantname,\r\n" +
                    "		d.lineID,\r\n" +
                    "		o.'name' AS linename,\r\n" +
                    "		d.workLocationID,\r\n" +
                    "		d.workLocationName,\r\n" +
                    "		d.inputerName,\r\n" +
                    "		d.inputTime,\r\n" +
                    "		d.inputerID,\r\n" +
                    "		d.materialID,\r\n" +
                    "		d.materialName,\r\n" +
                    "		d.materialQuantity\r\n" +
                    "	FROM\r\n" +
                    "		tb_dryingkilnjzrecord d,\r\n" +
                    "		sys_industrialplant p,\r\n" +
                    "		sys_productionline o\r\n" +
                    "	WHERE\r\n" +
                    "		d.plantID = p.id\r\n" +
                    "	AND d.lineID = o.id\r\n" +
                    "	AND d.plantID = '" + plantID + "'" +
                    "	AND d.outputerName IS NULL\r\n" +
                    ")\r\n" +
                    "UNION ALL\r\n" +
                    "	(\r\n" +
                    "		SELECT\r\n" +
                    "			a.id,\r\n" +
                    "			'总计' AS dryingKilnID,\r\n" +
                    "			a.dryingKilnName,\r\n" +
                    "			'',\r\n" +
                    "			'',\r\n" +
                    "			'',\r\n" +
                    "			'',\r\n" +
                    "			'',\r\n" +
                    "			'',\r\n" +
                    "			'',\r\n" +
                    "			'',\r\n" +
                    "			'',\r\n" +
                    "			'片数共计',\r\n" +
                    "			sum(a.materialQuantity),\r\n" +
                    "			'拖数共计: ',\r\n" +
                    "			COUNT(a.id) AS num\r\n" +
                    "		FROM\r\n" +
                    "			tb_dryingkilnjzrecord a\r\n" +
                    "		WHERE\r\n" +
                    "	     a.plantID = '" + plantID + "'" +
                    "		AND a.outputerName IS NULL\r\n" +
                    "		GROUP BY\r\n" +
                    "			a.dryingKilnID\r\n" +
                    "	)\r\n" +
                    "ORDER BY\r\n" +
                    "	dryingKilnID DESC";
//		System.out.println(sql);
            List<Map<Object, Object>> mapList = dashboardMapper.getNowInDryingKilnjz(sql);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            ex.printStackTrace();
            return result;
        }
    }

    // 1 在窑数据查询  2入窑记录查询 3 出窑记录查询
    @Override
    public TNPYResponse getDryingKilnInfo(String plantID, String equipmentID, String queryTypeID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            if (startTime.length() > 20 && startTime.split(" ").length == 2) {
                startTime = startTime.split(" ")[0];
                endTime = endTime.split(" ")[0];
            } else if (startTime.split(" ").length == 3) {
                startTime = startTime.split(" ")[0] + " " + startTime.split(" ")[1];
                endTime = endTime.split(" ")[0] + " " + endTime.split(" ")[1];
            }
            String equipmentIDFilter = "";
            if (!"-1".equals(equipmentID)) {
                equipmentIDFilter = " and d.dryingKilnID = '" + equipmentID + "' ";
            }
            String sql = "";
            if ("1".equals(queryTypeID)) {
                sql = "(\r\n" +
                        "	SELECT\r\n" +
                        "		d.id,\r\n" +
                        "		d.dryingKilnID,\r\n" +
                        "		d.dryingKilnName,\r\n" +
                        "		d.suborderID,\r\n" +
                        "		o.name AS linename,\r\n" +
                        "		d.workLocationName,\r\n" +
                        "		d.inputerName,\r\n" +
                        "		d.inputTime,\r\n" +
                        "		d.inputerID,\r\n" +
                        "		d.materialName,\r\n" +
                        "		d.materialQuantity\r\n" +
                        "	FROM\r\n" +
                        "		tb_dryingkilnjzrecord d,\r\n" +
                        "		sys_industrialplant p,\r\n" +
                        "		sys_productionline o\r\n" +
                        "	WHERE\r\n" +
                        "		d.plantID = p.id\r\n" + equipmentIDFilter +
                        "	AND d.lineID = o.id\r\n" +
                        "	AND d.plantID = '" + plantID + "'" +
                        "	AND d.outputerName IS NULL\r\n" +
                        ")\r\n" +
                        "UNION ALL\r\n" +
                        "	(\r\n" +
                        "		SELECT\r\n" +
                        "			d.id,\r\n" +
                        "			'总计' AS dryingKilnID,\r\n" +
                        "			d.dryingKilnName,\r\n" +
                        "			d.materialName,\r\n" +
                        "			'产量总计：',\r\n" +
                        "			'',\r\n" +
                        "			sum(d.materialQuantity),\r\n" +
                        "		'',\r\n" +
                        "			'',\r\n" +
                        "			'拖数共计: ',\r\n" +
                        "			COUNT(d.id) AS num\r\n" +
                        "		FROM\r\n" +
                        "			tb_dryingkilnjzrecord d\r\n" +
                        "		WHERE\r\n" +
                        "	     d.plantID = '" + plantID + "'" + equipmentIDFilter +
                        "		AND d.outputerName IS NULL\r\n" +
                        "		GROUP BY\r\n" +
                        "			d.dryingKilnID,d.materialName\r\n" +
                        "	)\r\n" +
                        "ORDER BY\r\n" +
                        "	dryingKilnID DESC,inputTime";
            }

            if ("2".equals(queryTypeID)) {
                sql = "(\r\n" +
                        "	SELECT\r\n" +
                        "		d.id,\r\n" +
                        "		d.dryingKilnID,\r\n" +
                        "		d.dryingKilnName,\r\n" +
                        "		d.suborderID,\r\n" +
                        "		d.plantID,\r\n" +
                        "		p.name AS plantname,\r\n" +
                        "		d.lineID,\r\n" +
                        "		o.name AS linename,\r\n" +
                        "		d.workLocationID,\r\n" +
                        "		d.workLocationName,\r\n" +
                        "		d.inputerName,\r\n" +
                        "		d.inputTime,\r\n" +
                        "		d.inputerID,\r\n" +
                        "		d.materialID,\r\n" +
                        "		d.materialName,\r\n" +
                        "		d.materialQuantity\r\n" +
                        "	FROM\r\n" +
                        "		tb_dryingkilnjzrecord d,\r\n" +
                        "		sys_industrialplant p,\r\n" +
                        "		sys_productionline o\r\n" +
                        "	WHERE\r\n" +
                        "		d.plantID = p.id\r\n" + equipmentIDFilter +
                        "	AND d.lineID = o.id\r\n" +
                        "	AND d.plantID = '" + plantID + "'" +
                        "	AND d.inputTime  >= '" + startTime + "' AND d.inputTime  < '" + endTime + "'\r\n" +
                        ")\r\n" +
                        "UNION ALL\r\n" +
                        "	(\r\n" +
                        "		SELECT\r\n" +
                        "			d.id,\r\n" +
                        "			'总计' AS dryingKilnID,\r\n" +
                        "			d.dryingKilnName,\r\n" +
                        "			d.materialName,\r\n" +
                        "			'',\r\n" +
                        "			'',\r\n" +
                        "			'',\r\n" +
                        "			'产量总计',\r\n" +
                        "			'',\r\n" +
                        "			'',\r\n" +
                        "			sum(d.materialQuantity),\r\n" +
                        "			'',\r\n" +
                        "			'',\r\n" +
                        "			sum(d.materialQuantity),\r\n" +
                        "			'拖数共计: ',\r\n" +
                        "			COUNT(d.id) AS num\r\n" +
                        "		FROM\r\n" +
                        "			tb_dryingkilnjzrecord d\r\n" +
                        "		WHERE\r\n" +
                        "	     d.plantID = '" + plantID + "'" + equipmentIDFilter +
                        "	AND d.inputTime  >= '" + startTime + "' AND d.inputTime  < '" + endTime + "'\r\n" +

                        "		GROUP BY\r\n" +
                        "			d.dryingKilnID,d.materialName\r\n" +
                        "	)\r\n" +
                        "ORDER BY\r\n" +
                        "	dryingKilnID DESC,inputTime";
            }

            if ("3".equals(queryTypeID)) {
                sql = "(\r\n" +
                        "	SELECT\r\n" +
                        "		d.id,\r\n" +
                        "		d.dryingKilnID,\r\n" +
                        "		d.dryingKilnName,\r\n" +
                        "		d.suborderID,\r\n" +
                        "		d.plantID,\r\n" +
                        "		p.name AS plantname,\r\n" +
                        "		d.lineID, \r\n" +
                        "		o.name AS linename,\r\n" +
                        "		d.workLocationID,\r\n" +
                        "		d.workLocationName,\r\n" +
                        "		d.inputerName,\r\n" +
                        "		d.inputTime,\r\n" +
                        "		d.inputerID,\r\n" +
                        "		d.materialID, \r\n" +
                        "		d.materialName,\r\n" +
                        "		d.materialQuantity\r\n" +
                        "	FROM\r\n" +
                        "		tb_dryingkilnjzrecord d,\r\n" +
                        "		sys_industrialplant p,\r\n" +
                        "		sys_productionline o\r\n" +
                        "	WHERE\r\n" +
                        "		d.plantID = p.id\r\n" + equipmentIDFilter +
                        "	AND d.lineID = o.id\r\n" +
                        "	AND d.plantID = '" + plantID + "'" +
                        "	AND d.outputTime  >= '" + startTime + "' AND d.outputTime  < '" + endTime + "'\r\n" +
                        ")\r\n" +
                        "UNION ALL\r\n" +
                        "	(\r\n" +
                        "		SELECT\r\n" +
                        "			d.id,\r\n" +
                        "			'总计' AS dryingKilnID,\r\n" +
                        "			d.dryingKilnName,\r\n" +
                        "			d.materialName,\r\n" +
                        "			'',\r\n" +
                        "			'',\r\n" +
                        "			'',\r\n" +
                        "			'产量总计',\r\n" +
                        "			'',\r\n" +
                        "			'',\r\n" +
                        "			sum(d.materialQuantity),\r\n" +
                        "			'',\r\n" +
                        "			'',\r\n" +
                        "			'',\r\n" +
                        "			'拖数共计: ',\r\n" +
                        "			COUNT(d.id) AS num\r\n" +
                        "		FROM\r\n" +
                        "			tb_dryingkilnjzrecord d\r\n" +
                        "		WHERE\r\n" +
                        "	     d.plantID = '" + plantID + "'" + equipmentIDFilter +
                        "	AND d.outputTime  >= '" + startTime + "' AND d.outputTime  < '" + endTime + "'\r\n" +
                        "		GROUP BY\r\n" +
                        "			d.dryingKilnID,d.materialName\r\n" +
                        "	)\r\n" +
                        "ORDER BY\r\n" +
                        "	dryingKilnID DESC,inputTime";
            }

            //System.out.println(sql);
            List<Map<Object, Object>> mapList = dashboardMapper.getNowInDryingKilnjz(sql);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            ex.printStackTrace();
            return result;
        }
    }

    public TNPYResponse getWageDetail(String plantID, String processID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            List<Map<Object, Object>> mapList = wageDetailMapper.selectByFilter(plantID, processID, startTime, endTime);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getInventoryInfo(String plantID, String processID, String dayTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String sqlFilter = " select materialID,plantID,processID,currentNum ,ifnull(extend1,0) as planExpend,ifnull(extend2,0) as cycleTime from tb_materialinventoryrecord where status != '-1' ";
            if (!"-1".equals(plantID)) {
                sqlFilter += " and plantID ='" + plantID + "' ";
            }
            if (!"-1".equals(processID)) {
                sqlFilter += " and processID ='" + processID + "' ";
            }


            //查询某日库存
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format1.parse(dayTime);//取时间
            sqlFilter += " and updateTime < '" + format1.format(date) + " 12:00' and  updateTime > '" + format1.format(date) + " 03:00' ";

            sqlFilter = " select a.*,b.name from ( " + sqlFilter + " ) a left join sys_material b on a.materialID = b.id";

            List<Map<Object, Object>> mapList = dashboardMapper.getInventoryInfo(sqlFilter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }


    public TNPYResponse getProductionAndGrantInfo(String plantID, String processID, String dayTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String sqlFilter = " select materialID,plantID,processID,productionNum,extend1 as planExpend,extend2 as cycleTime, date_format(updateTime, '%Y-%m-%d') as updateTime from tb_materialinventoryrecord where status != '-1' ";
            if (!"-1".equals(plantID)) {
                sqlFilter += " and plantID ='" + plantID + "' ";
            }
            if (!"-1".equals(processID)) {
                sqlFilter += " and processID ='" + processID + "' ";
            }

            //查询某日库存
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format1.parse(dayTime);//取时间
            sqlFilter += " and updateTime < '" + format1.format(date) + " 12:00' and  updateTime > '" + format1.format(date) + " 03:00' ";

            sqlFilter = " select a.*,b.name from ( " + sqlFilter + " ) a left join sys_material b on a.materialID = b.id order by a.updateTime asc,b.name";
            List<Map<Object, Object>> mapList = dashboardMapper.getInventoryInfo(sqlFilter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getCXCDetailInfo(String plantID, String processID, String startTime, String endTime) {
        TNPYResponse result = new TNPYResponse();
        try {
            String sqlFilter = "  select c.*,d.name from (  select a.materialID,a.sumProductionNum,a.sumGrantNum,b.currentNum from " +
                    "( select materialID,sum(productionNum) as sumProductionNum ,sum(expendNum)  as sumGrantNum from tb_materialinventoryrecord  where plantID = '" + plantID + "' and processID = '" + processID + "' " +
                    " and  updateTime > '" + startTime + "' and updateTime < '" + endTime + "' group by materialID ) a left join ( " +
                    "select materialID,currentNum from tb_materialinventoryrecord  where plantID = '" + plantID + "' and processID = '" + processID + "' and updateTime > '" + endTime.split(" ")[0]
                    + "' and updateTime < '" + endTime + "'  group by materialID ) b  " + " on a.materialID = b.materialID ) c left join sys_material d on c.materialID = d.id order by name";

            List<Map<Object, Object>> mapList = dashboardMapper.getInventoryInfo(sqlFilter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }


    public TNPYResponse getProductionSummaryWorkLocation(String plantID ,String processID,String lineID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String sqlFilter = " where dayTime >= '" + startTime.split(" ")[0] +"' and dayTime <= '" + endTime.split(" ")[0] + "' ";
            if(!"-1".equals(plantID))
            {
                sqlFilter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                sqlFilter += " and processID = '" + processID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                sqlFilter += " and lineID = '" + lineID + "' ";
            }

            List<Map<Object, Object>> mapList = dailyProductionSummaryLineMapper.selectDailyProductionSummaryWorklocation(sqlFilter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse getProductionSummaryLine(String plantID ,String processID,String lineID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String sqlFilter = " where dayTime >= '" + startTime.split(" ")[0] +"' and dayTime <= '" + endTime.split(" ")[0] + "' ";
            if(!"-1".equals(plantID))
            {
                sqlFilter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                sqlFilter += " and processID = '" + processID + "' ";
            }
            if(!"-1".equals(lineID))
            {
                sqlFilter += " and lineID = '" + lineID + "' ";
            }

            List<Map<Object, Object>> mapList = dailyProductionSummaryLineMapper.selectDailyProductionSummaryLine(sqlFilter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
    public TNPYResponse getProductionSummaryProcess(String plantID ,String processID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {
            String sqlFilter = " where dayTime >= '" + startTime.split(" ")[0] +"' and dayTime <= '" + endTime.split(" ")[0] + "' ";
            if(!"-1".equals(plantID))
            {
                sqlFilter += " and plantID = '" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                sqlFilter += " and processID = '" + processID + "' ";
            }
            List<Map<Object, Object>> mapList = dailyProductionSummaryLineMapper.selectDailyProductionSummaryProcess(sqlFilter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }

    public TNPYResponse getDailyProductionSummaryPlant(String plantID ,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try {

            String sqlFilter = "select date_format(dayTime, '%Y-%m-%d') as dayTime,processID,classType1,materialName, production,paixu from (\n" +
        "( select '总计' as classType1,processID,'工序汇总' as materialName,dayTime,sum(production) as production,'1003' as paixu from tb_dailyproductionsummaryprocess where plantID = '"+
        plantID+"' and dayTime >= '" + startTime.split(" ")[0] +"' and dayTime <= '" + endTime.split(" ")[0] + "'  group by processID,dayTime )\n" +
        "union all\n" +
        "( select classType1,processID,'班次汇总' as materialName,dayTime,sum(production) as production, '1002' as paixu from tb_dailyproductionsummaryprocess where plantID = '"+
        plantID+"' and dayTime >= '" + startTime.split(" ")[0] +"' and dayTime <= '" + endTime.split(" ")[0] + "'  group by processID,classType1,dayTime )\n" +
        "union all\n" +
        "( select classType1,processID,materialName,dayTime,production ,'1001' as paixu from tb_dailyproductionsummaryprocess where plantID = '"+
        plantID+"' and dayTime >= '" + startTime.split(" ")[0] +"' and dayTime <= '" + endTime.split(" ")[0] + "'  )\n" +
        " ) a order by dayTime,processID,CONVERT(classType1 USING gbk) ,paixu";

            List<Map<Object, Object>> mapList = dailyProductionSummaryLineMapper.selectDailyProductionSummary(sqlFilter);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return result;
        } catch (Exception ex) {
            result.setMessage("查询出错！" + ex.getMessage());
            return result;
        }
    }
}
