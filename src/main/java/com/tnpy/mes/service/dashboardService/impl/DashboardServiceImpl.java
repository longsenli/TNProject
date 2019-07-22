package com.tnpy.mes.service.dashboardService.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.DashboardMapper;
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
    private WageDetailMapper wageDetailMapper ;

    @Override
    public TNPYResponse getDailyProduction(String plantID ,String processID,String queryTypeID,String startTime,String endTime) {
        /*
        queryTypeID 前后台统一：
        <option value='byLine'>产线</option>
		<option value='byMaterial'>物料</option>
		<option value='byWorkingLocation'>工位</option>
		<option value='byStaff'>人员</option>
		<option value='byClassType'>班次</option>
		<option value='byStaffAndMaterial'>人员物料</option>
		<option value='byWage'>人员工资</option>
		<option value='byOrderDetail'>工单详情</option>
         */

        TNPYResponse result = new TNPYResponse();
        try
        {
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
            if("byWage".equals(queryTypeID))
            {
                return getWageDetail(plantID,processID,startTime.split(" ")[0],endTime.split(" ")[0]);
            }
            if("byLine".equals(queryTypeID))
            {
                querySQL = "(select inputLineID,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
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
                querySQL = "select m.* from ((select inputWorkLocationID,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
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
                querySQL = "(select inputer,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                        "select b.inputer,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputer,orderDay,orderHour order by orderDay,orderHour,inputer desc ) a group by inputer limit 1000)\n" +
                        "union all\n" +
                        "(select b.inputer,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputer,orderDay,orderHour order by orderDay,orderHour desc,inputer limit 1000)" ;

            }

            if("byMaterial".equals(queryTypeID))
            {
                querySQL = "(select materialNameInfo,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
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
                querySQL = "(select inputer,materialNameInfo,'' as orderDay,'总计' as orderHour,sum(sumProduction)  as sumProduction from (\n" +
                        "select b.inputer,b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        " where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputer,materialNameInfo,orderDay,orderHour order by orderDay,orderHour desc,inputer,materialNameInfo ) a group by inputer,materialNameInfo limit 1000)\n" +
                        "union all\n" +
                        "(select b.inputer,b.materialNameInfo,a.orderDay,a.orderHour,sum(number) as sumProduction from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null group by inputer,materialNameInfo,orderDay,orderHour order by orderDay,orderHour desc,inputer,materialNameInfo desc limit 1000)" ;

            }
            if("byClassType".equals(queryTypeID))
            {
                querySQL = "(select '总计' as orderDay, orderHour,sum(sumProduction)  as sumProduction from (\n" +
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

            if("byOrderDetail".equals(queryTypeID))
            {
                querySQL = "(select a.orderDay,a.orderHour,b.subOrderID,b.materialNameInfo,b.number,b.inputer,b.inputTime from (select id ,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,\n" +
                        " case  when date_format(scheduledStartTime,'%H') < '10' then '白班'  when date_format(scheduledStartTime,'%H') > '16' then '夜班' end  as orderHour from tb_workorder\n" +
                        "where plantID = '" + plantID + "' and processID =  '" + processID + "'  and scheduledStartTime > '" + startTime + "' and scheduledStartTime <'" + endTime + "' ) a \n" +
                        "left join tb_materialrecord  b on a.id = b.orderID where inputLineID is not null  order by orderDay,orderHour desc,inputTime limit 1000)" ;

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
				"	     a.plantID = '"+plantID+"'" + 
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
        return  result;
    }
    catch (Exception ex)
    {
        result.setMessage("查询出错！" + ex.getMessage());
        ex.printStackTrace();
        return  result;
    }
	}
    // 1 在窑数据查询  2入窑记录查询 3 出窑记录查询
    @Override
    public TNPYResponse getDryingKilnInfo( String plantID ,String equipmentID,String queryTypeID,String startTime,String endTime) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String equipmentIDFilter = "";
            if(!"-1".equals(equipmentID))
            {
                equipmentIDFilter = " and d.dryingKilnID = '" + equipmentID + "' ";
            }
            String sql = "";
            if("1".equals(queryTypeID))
            {
                sql =   "(\r\n" +
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
                        "		d.plantID = p.id\r\n" + equipmentIDFilter +
                        "	AND d.lineID = o.id\r\n" +
                        "	AND d.plantID = '"+plantID+"'" +
                        "	AND d.outputerName IS NULL\r\n" +
                        ")\r\n" +
                        "UNION ALL\r\n" +
                        "	(\r\n" +
                        "		SELECT\r\n" +
                        "			d.id,\r\n" +
                        "			'总计' AS dryingKilnID,\r\n" +
                        "			d.dryingKilnName,\r\n" +
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
                        "			sum(d.materialQuantity),\r\n" +
                        "			'拖数共计: ',\r\n" +
                        "			COUNT(d.id) AS num\r\n" +
                        "		FROM\r\n" +
                        "			tb_dryingkilnjzrecord d\r\n" +
                        "		WHERE\r\n" +
                        "	     d.plantID = '"+plantID+"'" +  equipmentIDFilter +
                        "		AND d.outputerName IS NULL\r\n" +
                        "		GROUP BY\r\n" +
                        "			d.dryingKilnID\r\n" +
                        "	)\r\n" +
                        "ORDER BY\r\n" +
                        "	dryingKilnID DESC,inputTime";
            }

            if("2".equals(queryTypeID))
            {
                sql =   "(\r\n" +
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
                        "		d.plantID = p.id\r\n" + equipmentIDFilter +
                        "	AND d.lineID = o.id\r\n" +
                        "	AND d.plantID = '"+plantID+"'" +
                        "	AND d.inputTime  >= '"  + startTime+ "' AND d.inputTime  < '"  + endTime+ "'\r\n" +
                        ")\r\n" +
                        "UNION ALL\r\n" +
                        "	(\r\n" +
                        "		SELECT\r\n" +
                        "			d.id,\r\n" +
                        "			'总计' AS dryingKilnID,\r\n" +
                        "			d.dryingKilnName,\r\n" +
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
                        "			sum(d.materialQuantity),\r\n" +
                        "			'拖数共计: ',\r\n" +
                        "			COUNT(d.id) AS num\r\n" +
                        "		FROM\r\n" +
                        "			tb_dryingkilnjzrecord d\r\n" +
                        "		WHERE\r\n" +
                        "	     d.plantID = '"+plantID+"'" +  equipmentIDFilter +
                        "	AND d.inputTime  >= '"  + startTime+ "' AND d.inputTime  < '"  + endTime+ "'\r\n" +

                        "		GROUP BY\r\n" +
                        "			d.dryingKilnID\r\n" +
                        "	)\r\n" +
                        "ORDER BY\r\n" +
                        "	dryingKilnID DESC,inputTime";
            }

            if("3".equals(queryTypeID))
            {
                sql =   "(\r\n" +
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
                        "		d.plantID = p.id\r\n" + equipmentIDFilter +
                        "	AND d.lineID = o.id\r\n" +
                        "	AND d.plantID = '"+plantID+"'" +
                        "	AND d.outputTime  >= '"  + startTime+ "' AND d.outputTime  < '"  + endTime+ "'\r\n" +
                        ")\r\n" +
                        "UNION ALL\r\n" +
                        "	(\r\n" +
                        "		SELECT\r\n" +
                        "			d.id,\r\n" +
                        "			'总计' AS dryingKilnID,\r\n" +
                        "			d.dryingKilnName,\r\n" +
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
                        "			sum(d.materialQuantity),\r\n" +
                        "			'拖数共计: ',\r\n" +
                        "			COUNT(d.id) AS num\r\n" +
                        "		FROM\r\n" +
                        "			tb_dryingkilnjzrecord d\r\n" +
                        "		WHERE\r\n" +
                        "	     d.plantID = '"+plantID+"'" +  equipmentIDFilter +
                        "	AND d.outputTime  >= '"  + startTime+ "' AND d.outputTime  < '"  + endTime+ "'\r\n" +
                        "		GROUP BY\r\n" +
                        "			d.dryingKilnID\r\n" +
                        "	)\r\n" +
                        "ORDER BY\r\n" +
                        "	dryingKilnID DESC,inputTime";
            }

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

    public TNPYResponse getWageDetail(String plantID ,String processID,String startTime,String endTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            List<Map<Object, Object>> mapList = wageDetailMapper.selectByFilter(plantID,processID,startTime,endTime);
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

    public TNPYResponse getInventoryInfo(String plantID ,String processID,String dayTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String sqlFilter = " select materialID,plantID,processID,currentNum from tb_materialinventoryrecord where status != '-1' " ;
            if(!"-1".equals(plantID))
            {
                sqlFilter += " and plantID ='" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                sqlFilter += " and processID ='" + processID + "' ";
            }
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if( calendar.get(Calendar.HOUR_OF_DAY)<7)
            {
                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            }

            sqlFilter += " and updateTime > '" + format1.format(date) + "' ";
            sqlFilter = " select a.*,b.name from ( " + sqlFilter + " ) a left join sys_material b on a.materialID = b.id";

            List<Map<Object, Object>> mapList = dashboardMapper.getInventoryInfo(sqlFilter);
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


    public TNPYResponse getProductionAndGrantInfo(String plantID ,String processID,String dayTime)
    {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String sqlFilter = " select materialID,plantID,processID,productionNum,expendNum, date_format(updateTime, '%Y-%m-%d') as updateTime from tb_materialinventoryrecord where status != '-1' " ;
            if(!"-1".equals(plantID))
            {
                sqlFilter += " and plantID ='" + plantID + "' ";
            }
            if(!"-1".equals(processID))
            {
                sqlFilter += " and processID ='" + processID + "' ";
            }
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();//取时间

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);

            calendar.add(Calendar.DATE, -7);

            date = calendar.getTime();   //这个时间就是日期往后推一天的结果

            sqlFilter += " and updateTime > '" + format1.format(date) + "'";

            sqlFilter = " select a.*,b.name from ( " + sqlFilter + " ) a left join sys_material b on a.materialID = b.id order by a.updateTime asc,b.name";
            List<Map<Object, Object>> mapList = dashboardMapper.getInventoryInfo(sqlFilter);
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
