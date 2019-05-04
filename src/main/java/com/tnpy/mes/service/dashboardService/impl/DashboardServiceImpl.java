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
            if("byClassType".equals(queryTypeID))
            {
                querySQL = "(select '' as orderDay, orderHour,sum(sumProduction)  as sumProduction from (\n" +
                        "select a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by orderDay,orderHour order by orderDay,orderHour desc ) a group by orderHour limit 1000)\n" +
                        "union all\n" +
                        "(select a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by orderDay,orderHour order by orderDay,orderHour desc limit 1000)" ;

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

	@Override
	public TNPYResponse nowInDryingKilnjz(String plantID, String processID, String queryTypeID, String startTime,
			String endTime) {
		TNPYResponse result = new TNPYResponse();
        try
        {
		String sql ="(\r\n" + 
				"	SELECT\r\n" + 
				"		d.id,\r\n" + 
				"		d.dryingKilnID,\r\n" + 
				"		d.dryingKilnName,\r\n" + 
				"		d.suborderID,\r\n" + 
				"		d.plantID,\r\n" + 
				"		p.`name` AS plantname,\r\n" + 
				"		d.lineID,\r\n" + 
				"		o.`name` AS linename,\r\n" + 
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
				"	AND d.plantID = '"+plantID+"'" + 
				"	AND d.outputerID IS NULL\r\n" + 
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
				"	     a.plantID = '"+plantID+"'" + 
				"		AND a.outputerID IS NULL\r\n" + 
				"		GROUP BY\r\n" + 
				"			a.dryingKilnID\r\n" + 
				"	)\r\n" + 
				"ORDER BY\r\n" + 
				"	dryingKilnID DESC";
//		System.out.println(sql);
		List<Map<Object, Object>> mapList = dashboardMapper.getNowInDryingKilnjz(sql);
        result.setStatus(1);
        result.setData(JSONObject.toJSON(mapList).toString());
        return  result;
    }
    catch (Exception ex)
    {
        result.setMessage("查询出错！" + ex.getMessage());
        ex.printStackTrace();
        return  result;
    }
	}
}
