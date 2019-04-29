package com.tnpy.mes.service.dashboardService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.*;
import com.tnpy.mes.service.dashboardService.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Override
    public TNPYResponse getDailyProduction(String plantID ,String processID,String queryTypeID,String startTime,String endTime) {
        /*
        queryTypeID 前后台统一：
        <option value='byLine'>产线</option>
		<option value='byMaterial'>物料</option>
		<option value='byWorkingLocation'>工位</option>
        <option value='byStaff'>人员</option>
         */
        TNPYResponse result = new TNPYResponse();
        try
        {
            String querySQL = "";
            if("byLine".equals(queryTypeID))
            {
                querySQL = "(select inputLineID,'' as orderDay,'' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                        "select b.inputLineID,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputLineID,orderDay,orderHour order by inputLineID,orderDay,orderHour desc ) a group by inputLineID limit 1000)\n" +
                        "union all\n" +
                        "(select b.inputLineID,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputLineID,orderDay,orderHour order by inputLineID,orderDay,orderHour desc limit 1000)" ;

            }
            if("byWorkingLocation".equals(queryTypeID))
            {
                querySQL = "select m.* from ((select inputWorkLocationID,'' as orderDay,'' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                        "select b.inputWorkLocationID,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputWorkLocationID,orderDay,orderHour order by inputWorkLocationID,orderDay,orderHour desc ) a group by inputWorkLocationID limit 1000)\n" +
                        "union all\n" +
                        "(select b.inputWorkLocationID,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputWorkLocationID,orderDay,orderHour order by inputWorkLocationID,orderDay,orderHour desc limit 1000) \n" +
                         " ) m left join tb_worklocation n on m.inputWorkLocationID = n.id order by m.orderDay, n.ordinal "    ;

            }

            if("byStaff".equals(queryTypeID))
            {
                querySQL = "(select inputer,'' as orderDay,'' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                        "select b.inputer,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputer,orderDay,orderHour order by inputer,orderDay,orderHour desc ) a group by inputer limit 1000)\n" +
                        "union all\n" +
                        "(select b.inputer,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputer,orderDay,orderHour order by inputer,orderDay,orderHour desc limit 1000)" ;

            }

            if("byMaterial".equals(queryTypeID))
            {
                querySQL = "(select materialNameInfo,'' as orderDay,'' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                        "select b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by materialNameInfo,orderDay,orderHour order by materialNameInfo,orderDay,orderHour desc ) a group by materialNameInfo limit 1000)\n" +
                        "union all\n" +
                        "(select b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by materialNameInfo,orderDay,orderHour order by materialNameInfo,orderDay,orderHour desc limit 1000)" ;

            }

            if("byStaffAndMaterial".equals(queryTypeID))
            {
                querySQL = "(select inputer,materialNameInfo,'' as orderDay,'' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                        "select b.inputer,b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputer,materialNameInfo,orderDay,orderHour order by inputer,materialNameInfo,orderDay,orderHour desc ) a group by inputer,materialNameInfo limit 1000)\n" +
                        "union all\n" +
                        "(select b.inputer,b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputer,materialNameInfo,orderDay,orderHour order by inputer,materialNameInfo,orderDay,orderHour desc limit 1000)" ;

            }
           // System.out.println(querySQL);
            List<Map<Object, Object>> mapList = dashboardMapper.getDailyProduction(querySQL);
            result.setStatus(1);
            result.setData(JSONObject.toJSON(mapList).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }

    }
}
