package com.tnpy.mes.service.dashboardService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.ConfigParamEnum;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.mass.DateUtilsDef;
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
                    querySQL = " ( select '总计' as banci,'' as dayTime, outputLineID,materialNameInfo,sum(number) as number  from tb_materialrecord where outputPlantID = '" + plantID + "' \n" +
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

                querySQL = "( SELECT materialName,round(sum(value) ,2) as scrapNumber,round(sum(weight),2) as weightNumber,'合计' as classType,'' as  productDayStr FROM tb_materialscraprecord \n" +
                        " where plantID = '" + plantID + "' and processID = '" + processID + "' and productDay >= '" + sdf.format(newStartTime) + "'  and productDay <= '" + sdf.format(newEndTime) + "' group by materialName order by materialName limit 100)\n" +
                        " union all \n" +
                        " ( SELECT materialName,round(sum(value),2) as scrapNumber,round(sum(weight),2) as weightNumber,classType,DATE_FORMAT(productDay, '%Y-%m-%d') as productDayStr FROM tb_materialscraprecord \n" +
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

	@Override
	public TNPYResponse getproductAccountSummaryPlant(String plantID, String processID, String startTime,
			String endTime) {
		TNPYResponse result = new TNPYResponse();

		try {
			// 判断日期是否合法
			if (!DateUtilsDef.isDateCheck(startTime) && !DateUtilsDef.isDateCheck(endTime)) {
				result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
				result.setMessage("日期不合法");
				return result;
			}
			// 获取日期范围天数
			List<String> rangdate = DateUtilsDef.getEveryday(startTime, endTime);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");
			String date = dateFormat.format(new Date());
			/////////////////////////////////////// 方法二/////////////////////
			StringBuilder stb = new StringBuilder();
			StringBuilder stbtotal = new StringBuilder();
			for (String fdate : rangdate) {
				if (fdate.equals(rangdate.get(rangdate.size() - 1))) {
					stb.append(" sum( CASE p.dayTime WHEN '" + fdate + "' THEN p.production ELSE 0 END ) as  '" + fdate
							+ "日" + "' ");
					stbtotal.append("`" + fdate + "日`  as 合计");
				} else {
					stb.append(" sum( CASE p.dayTime WHEN '" + fdate + "' THEN p.production ELSE 0 END ) as  '" + fdate
							+ "日" + "' ,");
					stbtotal.append("`" + fdate + "日` +");
				}
			}
			String sqlfilter = "select * , " + stbtotal + " from (( SELECT\r\n" +
//                    		"	p.plantID,\r\n" + 
//                    		"	p.processID,\r\n" + 
					" i.`name` AS '厂区',\r\n" + "			s.`name` AS '工序', s.ordinal,\r\n"
					+ "CONCAT(s.`name`,p.classType1) as '班组',\r\n" + "  '1' as flag,	p.materialName as '型号',\r\n" + stb.toString()
					+ " FROM\r\n"
					+ "	tb_dailyproductionsummaryline p LEFT JOIN sys_material m ON p.materialID = m.id\r\n"
					+ "	LEFT JOIN sys_productionprocess s ON s.id = p.processID\r\n"
					+ "	LEFT JOIN sys_industrialplant i ON i.id = p.plantID\r\n" + "WHERE\r\n" + "	1 = 1"
							+ "\r\n";
					
				if (!"-1".equals(plantID)) {
					sqlfilter += " and  p.plantID = '" + plantID + "' ";
				}
				if (!"-1".equals(processID)) {
					sqlfilter += " and p.processID = '" + processID + "' ";
				}
				sqlfilter += " AND p.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
						+ " AND p.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +
//					+ "AND p.plantID = '1001'\r\n" + "-- AND p.processID = '1011'\r\n"
//					+ "AND p.daytime >= DATE_FORMAT('2019-11-03', '%Y-%m-%d')\r\n" + "AND p.daytime <= DATE_FORMAT(\r\n"
//					+ "	'2019-11-23 23:59:59',\r\n" + "	'%Y-%m-%d'\r\n" + ")\r\n" + 
					" GROUP BY\r\n"
					+ "	p.materialName,p.classType1 )\r\n" + "\r\n" + 
					"UNION all \r\n" + "(\r\n" + "	SELECT\r\n" +
//                    		"		p.plantID,\r\n" + 
//                    		"		p.processID,\r\n" + 
					"		i.`name` AS '厂区',\r\n" + "		s.`name` AS '工序', s.ordinal,\r\n"
					+ "		CONCAT(s.`name`,p.classType1) as '班组' ,\r\n" + "	'2' as flag,	'合计'  AS '型号',\r\n" + "		\r\n"
					+ stb.toString() + "	FROM\r\n" + "		tb_dailyproductionsummaryline p\r\n"
					+ "	LEFT JOIN sys_material m ON p.materialID = m.id\r\n"
					+ "	LEFT JOIN sys_productionprocess s ON s.id = p.processID\r\n"
					+ "	LEFT JOIN sys_industrialplant i ON i.id = p.plantID\r\n" + "	WHERE\r\n" + "		1 = 1\r\n";
					
					if (!"-1".equals(plantID)) {
						sqlfilter += " and  p.plantID = '" + plantID + "' ";
					}
					if (!"-1".equals(processID)) {
						sqlfilter += " and p.processID = '" + processID + "' ";
					}
					sqlfilter += " AND p.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
							+ " AND p.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +
					
//					+ "	AND p.plantID = '1001'\r\n" + "-- 	AND p.processID = '1011'\r\n"
//					+ "	AND p.daytime >= DATE_FORMAT('2019-11-16', '%Y-%m-%d')\r\n"
//					+ "	AND p.daytime <= DATE_FORMAT(\r\n" + "		'2019-11-23 23:59:59',\r\n" + "		'%Y-%m-%d'\r\n"
//					+ "	)\r\n" +
//					
					
					"	GROUP BY\r\n" + "		processID,\r\n" + "		p.classType1\r\n" + ")\r\n"
					+ "UNION ALL\r\n" + "	(\r\n" + "		SELECT\r\n" +
//                    		"			p.plantID,\r\n" + 
//                    		"			p.processID,\r\n" + 
					"			i.`name` AS '厂区',\r\n" + "			s.`name` AS '工序',  s.ordinal,\r\n" +
//                    		"			'' AS '班组',\r\n" + 
					"		CONCAT(s.`name`,'夜班') as '班组' ,\r\n" + "	 '3' as flag,			' 总计'  AS '型号',\r\n" + "\r\n" + "\r\n"
					+ stb.toString() + "		FROM\r\n" + "			tb_dailyproductionsummaryline p\r\n"
					+ "		LEFT JOIN sys_material m ON p.materialID = m.id\r\n"
					+ "		LEFT JOIN sys_productionprocess s ON s.id = p.processID\r\n"
					+ "		LEFT JOIN sys_industrialplant i ON i.id = p.plantID\r\n" + "		WHERE\r\n"
					+ "			1 = 1\r\n";
					if (!"-1".equals(plantID)) {
						sqlfilter += " and  p.plantID = '" + plantID + "' ";
					}
					if (!"-1".equals(processID)) {
						sqlfilter += " and p.processID = '" + processID + "' ";
					}
					sqlfilter += " AND p.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
							+ " AND p.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +

//                    		"		AND p.plantID = '1001'\r\n" + 
//                    		"-- 		AND p.processID = '1011'\r\n" + 
//                    		"		AND p.daytime >= DATE_FORMAT('2019-11-16', '%Y-%m-%d')\r\n" + 
//                    		"		AND p.daytime <= DATE_FORMAT(\r\n" + 
//                    		"			'2019-11-23 23:59:59',\r\n" + 
//                    		"			'%Y-%m-%d'\r\n" + 
//                    		"		)\r\n" + 
					"		GROUP BY\r\n" + "			processID\r\n" + "	)\r\n" + "\r\n"
					+ "ORDER BY `厂区`  asc, ordinal asc,  `班组`  desc, flag, `型号` desc ) rsall";
			List<LinkedHashMap<Object, Object>> rlnmapList = dashboardMapper.queryDef(sqlfilter);
			result.setStatus(1);
			result.setData(JSONObject.toJSON(rlnmapList).toString());
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage("查询出错！" + ex.getMessage());
			return result;
		}
	}
	
	
	
	
	//已员工为基础查询条件报表汇总
	@Override
	public TNPYResponse getstaffproductAccountSummaryPlant(String plantID, String processID, String startTime,
			String endTime) {
		TNPYResponse result = new TNPYResponse();

		try {
			// 判断日期是否合法
			if (!DateUtilsDef.isDateCheck(startTime) && !DateUtilsDef.isDateCheck(endTime)) {
				result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
				result.setMessage("日期不合法");
				return result;
			}
			if ("-1".equals(plantID)||"-1".equals(processID)) {
				result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
				result.setMessage("工序不能为全部,请选择一个工序");
				return result;
			}
			// 获取日期范围天数
			List<String> rangdate = DateUtilsDef.getEveryday(startTime, endTime);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");
			String date = dateFormat.format(new Date());
			/////////////////////////////////////// 方法二/////////////////////
			StringBuilder stb1 = new StringBuilder();
			StringBuilder stb2 = new StringBuilder();
			StringBuilder stb3 = new StringBuilder();
			StringBuilder stb4 = new StringBuilder();
			StringBuilder stmp31 = new StringBuilder();
			StringBuilder stmp32 = new StringBuilder();
			for (String fdate : rangdate) {
				if (fdate.equals(rangdate.get(rangdate.size() - 1))) {
					stb1.append("convert( sum( CASE d.dayTime WHEN '" + fdate + "' THEN d.shelfProduction ELSE 0 END ), char) as  '" + fdate+ "日' ,");
					stb2.append("convert( MAX( CASE	"
							+ "WHEN t.classType2 = '全班' "
							+ "AND t.dayTime = '"+fdate+"' THEN	'1'	"
							+ "WHEN t.classType2 = '上半班'	AND t.dayTime = '"+fdate+"' THEN		'0.5'"
							+ "	WHEN t.classType2 = '下半班'	AND t.dayTime = '"+fdate+"' THEN	'0.5'		"
							+ "ELSE	0	END	) , char)  AS  '"+fdate+"日',");
					stb3.append("convert( MAX( CASE	"
							+ "WHEN t.classType1 = '白班' "+ "AND t.dayTime = '"+fdate+"' THEN	'白班'	"
							+ "WHEN t.classType1 = '夜班班'	AND t.dayTime = '"+fdate+"' THEN	'夜班'"
							
							+ "ELSE	''	END	) , char)  AS  '"+fdate+"日',");
					
					stb4.append("convert( MAX( CASE	"
							+ "WHEN t.classType2 = '全班' "
							+ "AND t.dayTime = '"+fdate+"' THEN	'1'	"
							+ "WHEN t.classType2 = '上半班'	AND t.dayTime = '"+fdate+"' THEN		'0.5'"
							+ "	WHEN t.classType2 = '下半班'	AND t.dayTime = '"+fdate+"' THEN	'0.5'		"
							+ "ELSE	0	END	) , char)  ");
					
					
					stmp31.append("tmp3.`" + fdate + "日`,");
					stmp32.append("tmp3.`" + fdate + "日`");
				} else {
					stb1.append("convert( sum( CASE d.dayTime WHEN '" + fdate + "' THEN d.shelfProduction ELSE 0 END ), char) as  '" + fdate+ "日', ");
					stb2.append("convert ( MAX( CASE	"
							+ "WHEN t.classType2 = '全班' "
							+ "AND t.dayTime = '"+fdate+"' THEN	'1'	"
							+ "WHEN t.classType2 = '上半班'	AND t.dayTime = '"+fdate+"' THEN		'0.5'"
							+ "	WHEN t.classType2 = '下半班'	AND t.dayTime = '"+fdate+"' THEN	'0.5'		"
							+ "ELSE	0	END	) , char) AS  '"+fdate+"日', ");
					
					
					stb3.append("convert( MAX( CASE	"
							+ "WHEN t.classType1 = '白班' "+ "AND t.dayTime = '"+fdate+"' THEN	'白班'	"
							+ "WHEN t.classType1 = '夜班班'	AND t.dayTime = '"+fdate+"' THEN	'夜班'"
							
							+ "ELSE	''	END	) , char)  AS  '"+fdate+"日',");
					
					stb4.append("convert( MAX( CASE	"
							+ "WHEN t.classType2 = '全班' "
							+ "AND t.dayTime = '"+fdate+"' THEN	'1'	"
							+ "WHEN t.classType2 = '上半班'	AND t.dayTime = '"+fdate+"' THEN		'0.5'"
							+ "	WHEN t.classType2 = '下半班'	AND t.dayTime = '"+fdate+"' THEN	'0.5'		"
							+ "ELSE	0	END	) , char) +  ");
					
					
					stmp31.append("tmp3.`" + fdate + "日` , ");
					stmp32.append("tmp3.`" + fdate + "日` +");
				}
			}
			StringBuilder sqlfilter = new StringBuilder();
					
			sqlfilter.append("SELECT\r\n" + 
					"	* "+
					"FROM\r\n" + 
					"	(\r\n" + 
					"		(\r\n" + 
					"			SELECT\r\n" + 
					"				s.`name` AS '厂区',\r\n" + 
					"				p.`name` AS '工序',\r\n" + 
//					"				d.staffID AS '工号',\r\n" + 
					"				d.staffName AS '姓名',\r\n" + 
//					"				d.classType1 AS '班别',\r\n" + 
					"				(CASE d.extd2 WHEN 'A' THEN 'A 班' WHEN 'B' THEN 'B 班' ELSE '无' END) as  '班组' ,\r\n"+
					"				d.verifierName AS '产量确认人(班长)',\r\n" + 
					"				d.materialName AS '型号', '1' as orderflag, \r\n" + stb1+
					"			CONVERT( sum(d.shelfProduction) , char) AS '合计', 	d.univalence AS '工价',  "
					+ "round(	sum(d.shelfProduction) * d.univalence,	2) AS '各型号工资',"
					+ "tmp2.`总产量`,\r\n" + 
					"	tmp2.`合计工资`\r\n" + 
					"			FROM\r\n" + 
					"				tb_dailyproductionandwagedetail d\r\n" + 
					"			LEFT JOIN sys_industrialplant s ON d.plantID = s.id\r\n" + 
					"			LEFT JOIN sys_productionprocess p ON d.processID = p.id\r\n"
					+ " LEFT JOIN (\r\n" + 
					"	SELECT\r\n" + 
					"		*, SUM(tmp.sshelfProduction) AS '总产量',\r\n" + 
					"		SUM(tmp.sushelfProduction) AS '合计工资'\r\n" + 
					"	FROM\r\n" + 
					"		(\r\n" + 
					"			SELECT\r\n" + 
					"				td.staffID,\r\n" + 
					"				td.staffName,\r\n" + 
					"				sum(td.shelfProduction) AS sshelfProduction,\r\n" + 
					"				ROUND(\r\n" + 
					"					sum(td.shelfProduction) * td.univalence,\r\n" + 
					"					2\r\n" + 
					"				) AS sushelfProduction\r\n" + 
					"			FROM\r\n" + 
					"				tb_dailyproductionandwagedetail td\r\n" + 
					"			WHERE\r\n" + 
					"				1 = 1\r\n" );
					if (!"-1".equals(plantID)) {
						sqlfilter.append( " and  td.plantID = '" + plantID + "' ");
					}
					if (!"-1".equals(processID)) {
						sqlfilter.append(" and td.processID = '" + processID + "' ");
					}
					sqlfilter .append( " AND td.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
							+ " AND td.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +

					"			GROUP BY\r\n" + 
					"				td.staffID,\r\n" + 
					"				td.materialID\r\n" + 
					"		) tmp\r\n" + 
					"	GROUP BY\r\n" + 
					"		tmp.staffID\r\n" + 
					") tmp2 ON d.staffID = tmp2.staffID" + 
					"			WHERE\r\n" + 
					"				1 = 1\r\n" );
					if (!"-1".equals(plantID)) {
						sqlfilter .append(" and  d.plantID = '" + plantID + "' ");
					}
					if (!"-1".equals(processID)) {
						sqlfilter .append(" and d.processID = '" + processID + "' ");
					}
					sqlfilter.append( " AND d.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
							+ " AND d.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +
					

					"			GROUP BY\r\n" + 
					"				d.plantID,\r\n" + 
					"				d.processID,\r\n" + 
					"				d.staffID,\r\n" + 
					"				d.staffName,\r\n" + 
					"				d.materialID\r\n" + 
					"		)\r\n" + 
					"		UNION ALL\r\n" + 
					"			(\r\n" + 
//					"				 select * from "
					"select tmp3.`厂区`,tmp3.`工序`,tmp3.`姓名`,"
//					+ "tmp3.`班别`,"
					+ "tmp3.`班组`,tmp3.`产量确认人(班长)`,tmp3.`型号`,tmp3.orderflag," + stmp31
//					+ " CONCAT("+stmp32+ ",'天') as '合计',"
							+ " tmp3.`合计` ,tmp3.`工价`,tmp3.`各型号工资`,tmp3.`总产量`,tmp3.`合计工资` from "
					+ "( "
					+ "( SELECT\r\n" + 
					"					s.`name` AS '厂区',\r\n" + 
					"					p.`name` AS '工序',\r\n" + 
//					"					t.staffID AS '工号',\r\n" + 
					"					t.staffName AS '姓名',\r\n" + 
//					"					t.classType1 AS '班别',\r\n" +
					"				(CASE t.extd2 WHEN 'A' THEN 'A 班' WHEN 'B' THEN 'B 班' ELSE '无' END) as  '班组' ,\r\n"+
					"					t.verifierName AS '产量确认人(班长)',\r\n" + 
					"					'考勤' AS '型号', '0' as orderflag , \r\n" +  stb2 +
					"					CONCAT(("+stb4+ "),'天') as '合计', '' AS '工价' , '' as '各型号工资', '' as '总产量' , '' as '合计工资'\r\n" +

					"		FROM\r\n" + 
					"			tb_staffattendancedetail t\r\n" + 
					"		LEFT JOIN sys_industrialplant s ON t.plantID = s.id\r\n" + 
					"		LEFT JOIN sys_productionprocess p ON t.processID = p.id\r\n" + 
					"		WHERE\r\n" + 
					"			1 = 1\r\n" + 
					"		AND t.`status` = '1'\r\n");
					if (!"-1".equals(plantID)) {
						sqlfilter .append(" and  t.plantID = '" + plantID + "' ");
					}
					if (!"-1".equals(processID)) {
						sqlfilter.append( " and t.processID = '" + processID + "' ");
					}
					sqlfilter.append( " AND t.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
							+ " AND t.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +
					"		GROUP BY\r\n" + 
					"			t.plantID,\r\n" + 
					"			t.processID,\r\n" + 
					"			t.staffID,\r\n" + 
					"			t.staffName\r\n  " + 
					
					"			) union all "
					+ "( SELECT\r\n" + 
					"					s.`name` AS '厂区',\r\n" + 
					"					p.`name` AS '工序',\r\n" + 
//					"					t.staffID AS '工号',\r\n" + 
					"					t.staffName AS '姓名',\r\n" + 
//					"					t.classType1 AS '班别',\r\n" +
					"				(CASE t.extd2 WHEN 'A' THEN 'A 班' WHEN 'B' THEN 'B 班' ELSE '无' END) as  '班组' ,\r\n"+
					"					t.verifierName AS '产量确认人(班长)',\r\n" + 
					"					'班别' AS '型号', '-1' as orderflag , \r\n" +  stb3 +
					"					'' as '合计', '' AS '工价' , '' as '各型号工资', '' as '总产量' , '' as '合计工资'\r\n" +

					"		FROM\r\n" + 
					"			tb_staffattendancedetail t\r\n" + 
					"		LEFT JOIN sys_industrialplant s ON t.plantID = s.id\r\n" + 
					"		LEFT JOIN sys_productionprocess p ON t.processID = p.id\r\n" + 
					"		WHERE\r\n" + 
					"			1 = 1\r\n" + 
					"		AND t.`status` = '1'\r\n");
					if (!"-1".equals(plantID)) {
						sqlfilter .append(" and  t.plantID = '" + plantID + "' ");
					}
					if (!"-1".equals(processID)) {
						sqlfilter.append( " and t.processID = '" + processID + "' ");
					}
					sqlfilter.append( " AND t.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
							+ " AND t.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +
					"		GROUP BY\r\n" + 
					"			t.plantID,\r\n" + 
					"			t.processID,\r\n" + 
					"			t.staffID,\r\n" + 
					"			t.staffName\r\n" +
					")  ) tmp3)\r\n" + 
					"	) rsall\r\n" +
//					"	) rsall where rsall.`姓名`='王自鑫' or rsall.`姓名`='韩立重' or rsall.`姓名`='靳业雷' or rsall.`姓名`='郭振垒'     \r\n" +
					"ORDER BY\r\n" + 
//					"rsall.`班别` desc,"
					 "rsall.`班组` ASC , rsall.`姓名` desc ,rsall.orderflag asc");
//					"	rsall.`工序`,\r\n" + 
//					"	rsall.`班别` ,rsall.`产量确认人(班长)` desc, rsall.`姓名` desc ,rsall.orderflag asc");
			//System.out.println(sqlfilter);
			List<LinkedHashMap<Object, Object>> rlnmapList = dashboardMapper.queryDef(sqlfilter.toString());
			result.setStatus(1);
			result.setData(JSONObject.toJSON(rlnmapList).toString());
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage("查询出错！" + ex.getMessage());
			return result;
		}
	}
	
	//工艺产品月报表
		@Override
		public TNPYResponse getprocessproductAccountSummaryPlant(String plantID, String processID, String startTime,
				String endTime) {
			TNPYResponse result = new TNPYResponse();

			try {
				// 判断日期是否合法
				if (!DateUtilsDef.isDateCheck(startTime) && !DateUtilsDef.isDateCheck(endTime)) {
					result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
					result.setMessage("日期不合法");
					return result;
				}
				/*String processsql = "select * from sys_productionprocess where status!='-1' ORDER BY ordinal";
				List<LinkedHashMap<Object, Object>> processList = dashboardMapper.queryDef(processsql);
				StringBuilder stb = new StringBuilder();
				for(int i=0;i<processList.size()-1;i++) {
					if(i==processList.size()-2) {
						stb.append("IFNULL( sum( case processID when '"+processList.get(i).get("id")+"' then td.production end ),0) as '"+processList.get(i).get("name")+"'");
					}else {
						stb.append("IFNULL(sum( case processID when '"+processList.get(i).get("id")+"' then td.production end ),0) as '"+processList.get(i).get("name")+"',");
					}
				}
				StringBuilder sqlfilter = new StringBuilder();
				sqlfilter.append("SELECT\r\n" + 
						"DATE_FORMAT(td.dayTime, '%Y-%m-%d') as '日期',\r\n" + stb +
						"FROM\r\n" + 
						"	tb_dailyproductionsummaryprocess td LEFT JOIN\r\n" + 
						"sys_productionprocess s on td.processID = s.id\r\n");
				sqlfilter.append("WHERE\r\n" + 
						"	1 = 1  AND td.`status` = '1'\r\n" );
				if (!"-1".equals(plantID)) {
					sqlfilter.append(" and  td.plantID = '" + plantID + "' ");
				}
				if (!"-1".equals(processID)) {
					sqlfilter.append( " and td.processID = '" + processID + "' ");
				}
				sqlfilter.append( " AND td.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
						+ " AND td.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +

						"GROUP BY td.plantID,td.dayTime\r\n" + 
						"ORDER BY td.dayTime asc, td.processID asc\r\n" );*/
//				System.out.println(sqlfilter);
				
				
				
				
				
				
				String processsql = "select * from sys_productionprocess where status!='-1' ORDER BY ordinal";
				List<LinkedHashMap<Object, Object>>  processList = dashboardMapper.queryDef(processsql);
				StringBuilder stb = new StringBuilder();
				StringBuilder stbunion = new StringBuilder();
				
				for(int i=0;i<processList.size()-1;i++) {
					
					//如果是全部工序
					if(processID.trim().equals(String.valueOf(ConfigParamEnum.BasicProcessEnum.ALLProcessID.getIndex()))) {
						if(i==processList.size()-2) {
							//计划产量
							stb.append("IFNULL(MAX( case processID when '"+processList.get(i).get("id")+"' then tp.tp_splanproduction end ),0) as '"+processList.get(i).get("name")+"计划产量',");
							//实际产量
							stb.append("IFNULL(sum( case processID when '"+processList.get(i).get("id")+"' then td.production end ),0) as '"+processList.get(i).get("name")+"实际产量',");
							//完成率
							stb.append("IFNULL( CONCAT( ROUND(( IFNULL( sum( CASE td.processID WHEN '"+processList.get(i).get("id")+"' then td.production end ),0) "
									+ " ) /( IFNULL( MAX( CASE td.processID WHEN '"+processList.get(i).get("id")+"' THEN tp.tp_splanproduction END ), 0 ) ) * 100, 2 ), '', '%' ), '100%' ) AS '"+processList.get(i).get("name")+"完成率'");
							
							
							
							stbunion.append("ifnull(sum(rs.`"+processList.get(i).get("name")+"计划产量`)  -  sum(rs.`"+processList.get(i).get("name")+"实际产量`)  , 0)    ,");
							stbunion.append("ifnull ( sum(rs.`"+processList.get(i).get("name")+"实际产量`) , 0)   ,  ");
							stbunion.append(" '##' ");
//							//未完成
//							stbunion.append("CASE processID WHEN '"+processList.get(i).get("id")+"' THEN sum(t.aaa) - sum(p.production) END AS '未完成',");
//							//实际
//							stbunion.append("sum(production) AS '实际',");
//							//空列
//							stbunion.append("''");
						}else {
							//计划产量
							stb.append("IFNULL(MAX( case processID when '"+processList.get(i).get("id")+"' then tp.tp_splanproduction end ),0) as '"+processList.get(i).get("name")+"计划产量',");
							//实际产量
							stb.append("IFNULL(sum( case processID when '"+processList.get(i).get("id")+"' then td.production end ),0) as '"+processList.get(i).get("name")+"实际产量',");
							//完成率
							stb.append("IFNULL( CONCAT( ROUND(( IFNULL( sum( CASE td.processID WHEN '"+processList.get(i).get("id")+"' then td.production end ),0) "
									+ " ) /( IFNULL( MAX( CASE td.processID WHEN '"+processList.get(i).get("id")+"' THEN tp.tp_splanproduction END ), 0 ) ) * 100, 2 ), '', '%' ), '100%' ) AS '"+processList.get(i).get("name")+"完成率', ");
							
							
							
							stbunion.append("ifnull ( sum(rs.`"+processList.get(i).get("name")+"计划产量`)  -  sum(rs.`"+processList.get(i).get("name")+"实际产量`)  , 0)   ,");
							stbunion.append("ifnull( sum(rs.`"+processList.get(i).get("name")+"实际产量`)  , 0)   ,  ");
							stbunion.append(" '##' ,");
							
							
							
							
//							//未完成
//							stbunion.append("CASE processID WHEN '"+processList.get(i).get("id")+"' THEN sum(t.aaa) - sum(p.production) END AS '未完成',");
//							//实际
//							stbunion.append("sum(production) AS '实际',");
//							//空列
//							stbunion.append("'',");
						}
					}
					
					//如果是指定工序
					if(processID.trim().equals(processList.get(i).get("id"))) {
							//计划产量
							stb.append("IFNULL(MAX( case processID when '"+processList.get(i).get("id")+"' then tp.tp_splanproduction end ),0) as '"+processList.get(i).get("name")+"计划产量',");
							//实际产量
							stb.append("IFNULL(sum( case processID when '"+processList.get(i).get("id")+"' then td.production end ),0) as '"+processList.get(i).get("name")+"实际产量',");
							//完成率
							stb.append("IFNULL( CONCAT( ROUND(( IFNULL( sum( CASE td.processID WHEN '"+processList.get(i).get("id")+"' then td.production end ),0) "
									+ " ) /( IFNULL( MAX( CASE td.processID WHEN '"+processList.get(i).get("id")+"' THEN tp.tp_splanproduction END ), 0 ) ) * 100, 2 ), '', '%' ), '100%' ) AS '"+processList.get(i).get("name")+"完成率'");
							
							
							
							stbunion.append("ifnull ( sum(rs.`"+processList.get(i).get("name")+"计划产量`)  -  sum(rs.`"+processList.get(i).get("name")+"实际产量` ) , 0 )   ,");
							stbunion.append("ifnull ( sum(rs.`"+processList.get(i).get("name")+"实际产量`) , 0 )   ,  ");
							stbunion.append(" '##' ");
							
							
//							//未完成
//							stbunion.append("CASE processID WHEN '"+processList.get(i).get("id")+"' THEN sum(t.aaa) - sum(p.production) END AS '未完成',");
//							//实际
//							stbunion.append("sum(production) AS '实际',");
//							//空列
//							stbunion.append("''");
					}
					
				}
				StringBuilder sqlfilter = new StringBuilder();
//				sqlfilter.append(" select * from ((SELECT\r\n" + 
						sqlfilter.append(" ( SELECT\r\n" + 
						"DATE_FORMAT(td.dayTime, '%Y-%m-%d') as '日期',\r\n" + stb +
						"FROM\r\n" + 
						"	tb_dailyproductionsummaryprocess td LEFT JOIN\r\n" + 
						"sys_productionprocess s on td.processID = s.id   LEFT JOIN (\r\n" + 
						"	SELECT\r\n" + 
						"		plantID AS tp_plantID,\r\n" + 
						"		processID AS tp_processID,\r\n" + 
						"		sum(\r\n" + 
						"			IFNULL(planDailyProduction, 0)\r\n" + 
						"		) AS tp_splanproduction,\r\n" + 
						"		planMonth AS tp_planmonth\r\n" + 
						"	FROM\r\n" + 
						"		tb_planproductionrecord\r\n" + 
						"	WHERE\r\n" + 
						"		1 = 1\r\n" + 
						"	AND plantID = '" + plantID + "' \r\n" + 
						"	AND `status` = '2'\r\n" + 
						"	AND planmonth >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"+
						"	AND planmonth <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d')"+
						"	GROUP BY\r\n" + 
						"		plantID,\r\n" + 
						"		processID,\r\n" + 
						"		planmonth\r\n" + 
						"	ORDER BY\r\n" + 
						"		plantID,\r\n" + 
						"		processID,\r\n" + 
						"		planmonth\r\n" + 
						") tp ON tp.tp_plantID = td.plantID\r\n" + 
						"AND tp.tp_processID = td.processID\r\n" + 
						"AND tp.tp_planmonth = td.dayTime\r\n");
				sqlfilter.append("WHERE\r\n" + 
						"	1 = 1  AND td.`status` = '1'\r\n" );
				if (!"-1".equals(plantID)) {
					sqlfilter.append(" and  td.plantID = '" + plantID + "' ");
				}
				if (!"-1".equals(processID)) {
					sqlfilter.append( " and td.processID = '" + processID + "' ");
				}
				sqlfilter.append( " AND td.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
						+ " AND td.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +

						"GROUP BY td.plantID,td.dayTime\r\n" + 
						"ORDER BY td.dayTime asc, td.processID asc )\r\n" );
				
				
				StringBuilder sqlfilter1 = new StringBuilder();
				
				sqlfilter1.append(" ( select 'other' as '日期'   ,   ");
				
				sqlfilter1.append(stbunion + "  from  ( ");
				sqlfilter1.append(sqlfilter + " ) as rs )");
				
				
				
				StringBuilder sqlfilter2 = new StringBuilder();
				sqlfilter2.append("select * from (" );
				sqlfilter2.append(sqlfilter);
				sqlfilter2.append("union all ");
				sqlfilter2.append(sqlfilter1);
				sqlfilter2.append(" ) as rsall order by rsall.`日期` ") ;
				List<LinkedHashMap<Object, Object>> rlnmapList = dashboardMapper.queryDef(sqlfilter2.toString());
				
				
//				sqlfilter.append("UNION ALL\r\n" + 
//						"	(\r\n" + 
//						"		SELECT\r\n" + 
//						"			'other',\r\n" + stbunion +
//						
//						"		FROM\r\n" + 
//						"			tb_dailyproductionsummaryprocess p\r\n" + 
//						"		LEFT JOIN (\r\n" + 
//						"			SELECT\r\n" + 
//						"				sum(IFNULL(planDailyProduction, 0)) aaa,\r\n" + 
//						"				planmonth\r\n" + 
//						"			FROM\r\n" + 
//						"				tb_planproductionrecord\r\n" );
//						sqlfilter.append("WHERE\r\n	1 = 1  " );
//						if (!"-1".equals(plantID)) {
//							sqlfilter.append(" and  plantID = '" + plantID + "'   ");
//						}
//						if (!"-1".equals(processID)) {
//							sqlfilter.append( " and processID = '" + processID + "' ");
//						}
//						sqlfilter.append( "		AND `status` = '2'  AND planmonth >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
//								+ " AND planmonth <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') GROUP BY\r\n" + 
//										"	planmonth  ORDER BY planmonth ) " +" t ON t.planmonth = p.dayTime\r\n" );
//
//						sqlfilter.append("WHERE\r\n	1 = 1  " );
//						if (!"-1".equals(plantID)) {
//							sqlfilter.append(" and  p.plantID = '" + plantID + "' ");
//						}
//						if (!"-1".equals(processID)) {
//							sqlfilter.append( " and p.processID = '" + processID + "' ");
//						}
//						sqlfilter.append( " AND p.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
//								+ " AND p.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') ) ) rsall order by rsall.`日期`" );
				
				
				
//				List<LinkedHashMap<Object, Object>> rlnmapList = dashboardMapper.queryDef(sqlfilter.toString());
				
				for(int i=0; i<rlnmapList.size(); i++) {
					rlnmapList.get(i).get(i);
					
					
					
					
					
				}
				System.out.println(rlnmapList.get(0).size());
				
				
				
				
				
				result.setStatus(1);
				result.setData(JSONObject.toJSON(rlnmapList).toString());
				return result;
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setMessage("查询出错！" + ex.getMessage());
				return result;
			}
		}

		@Override
		public TNPYResponse getmonthproductAccountSummaryPlant(String plantID, String processID, String startTime,
				String endTime) {
			TNPYResponse result = new TNPYResponse();

			try {
				// 判断日期是否合法
				if (!DateUtilsDef.isDateCheck(startTime) && !DateUtilsDef.isDateCheck(endTime)) {
					result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
					result.setMessage("日期不合法");
					return result;
				}
				StringBuilder sqlfilter = new StringBuilder();
				sqlfilter.append("select * from ( (\r\n" + 
						"	SELECT\r\n" + 
						"		DATE_FORMAT(p.dayTime, '%Y-%m-%d') AS '时间',\r\n" + 
						"		t.aaa AS '计划数量',\r\n" + 
						"		sum(IFNULL(p.production, 0)) AS '实际数量'  , IFNULL(CONCAT(ROUND(sum(IFNULL(p.production, 0))/t.aaa * 100 , 2),'','%'),'100%') as '完成率'\r\n" + 
						"	FROM\r\n" + 
						"		tb_dailyproductionsummaryprocess p\r\n" + 
						"	LEFT JOIN (\r\n" + 
						"		SELECT\r\n" + 
						"			sum(IFNULL(planDailyProduction, 0)) aaa," + 
						"			planmonth\r\n" + 
						"		FROM\r\n" + 
						"			tb_planproductionrecord\r\n") ;
						
						sqlfilter.append("WHERE\r\n	1 = 1  " );
						if (!"-1".equals(plantID)) {
							sqlfilter.append(" and  plantID = '" + plantID + "'   ");
						}
						if (!"-1".equals(processID)) {
							sqlfilter.append( " and processID = '" + processID + "' ");
						}
						sqlfilter.append( "		AND `status` = '2'  AND planmonth >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
								+ " AND planmonth <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d')  GROUP BY\r\n" + 
										"	planmonth  ORDER BY planmonth ) " +" t ON t.planmonth = p.dayTime\r\n" );
						sqlfilter.append("WHERE\r\n	1 = 1  " );
						if (!"-1".equals(plantID)) {
							sqlfilter.append(" and  p.plantID = '" + plantID + "' ");
						}
						if (!"-1".equals(processID)) {
							sqlfilter.append( " and p.processID = '" + processID + "' ");
						}
						sqlfilter.append( " AND p.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
								+ " AND p.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') " +
		
								"GROUP BY p.dayTime\r\n" + 
								" )\r\n " );
						sqlfilter.append("UNION ALL\r\n" + 
						"	(\r\n" + 
						"		SELECT\r\n" + 
						"			'other',\r\n" + 
						"			sum(t.aaa) - sum(p.production) AS '未完成',\r\n" + 
						"			sum(production) AS '实际' , ''\r\n" + 
						"		FROM\r\n" + 
						"			tb_dailyproductionsummaryprocess p\r\n" + 
						"		LEFT JOIN (\r\n" + 
						"			SELECT\r\n" + 
						"				sum(IFNULL(planDailyProduction, 0)) aaa,\r\n" + 
						"				planmonth\r\n" + 
						"			FROM\r\n" + 
						"				tb_planproductionrecord\r\n" );
						sqlfilter.append("WHERE\r\n	1 = 1  " );
						if (!"-1".equals(plantID)) {
							sqlfilter.append(" and  plantID = '" + plantID + "'   ");
						}
						if (!"-1".equals(processID)) {
							sqlfilter.append( " and processID = '" + processID + "' ");
						}
						sqlfilter.append( "		AND `status` = '2'  AND planmonth >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
								+ " AND planmonth <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') GROUP BY\r\n" + 
										"	planmonth  ORDER BY planmonth ) " +" t ON t.planmonth = p.dayTime\r\n" );

						sqlfilter.append("WHERE\r\n	1 = 1  " );
						if (!"-1".equals(plantID)) {
							sqlfilter.append(" and  p.plantID = '" + plantID + "' ");
						}
						if (!"-1".equals(processID)) {
							sqlfilter.append( " and p.processID = '" + processID + "' ");
						}
						sqlfilter.append( " AND p.daytime >= DATE_FORMAT('" + startTime + "',   '%Y-%m-%d')"
								+ " AND p.daytime <= DATE_FORMAT('" + endTime + "',   '%Y-%m-%d') ) ) rsall order by rsall.`时间`" );
		
//				System.out.println(sqlfilter);
				List<LinkedHashMap<Object, Object>> rlnmapList = dashboardMapper.queryDef(sqlfilter.toString());
				result.setStatus(1);
				result.setData(JSONObject.toJSON(rlnmapList).toString());
				return result;
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setMessage("查询出错！" + ex.getMessage());
				return result;
			}
		}
	
	
}
