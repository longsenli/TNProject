package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WorkOrderTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface WorkOrderTemplateMapper {
    int deleteByPrimaryKey(String id);

    int insert(WorkOrderTemplate record);

    int insertSelective(WorkOrderTemplate record);

    WorkOrderTemplate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WorkOrderTemplate record);

    int updateByPrimaryKey(WorkOrderTemplate record);

    @Select(" SELECT  plantID,processID,lineID,batchNum,totalProduction,materialID,materialName,createStaff,createTime,id FROM tb_workordertemplate ${filter} order by lineID,materialName")
    List<Map<Object, Object>> selectWorkOrderTemplateByFilter(@Param("filter") String filter);
    
    @Select(" SELECT * FROM( \r\n" + 
    		"    (SELECT ' ' as '信息', IFNULL(m.materialNameInfo, '无') AS '物料型号', CONVERT (IFNULL(m.number, '无')  USING utf8) AS '数量', IFNULL(m.subOrderID, '无') AS '入库工单', IFNULL(m.inputer, '无') AS '入库人员姓名', IFNULL(pt1.`name`, '无') AS '入库厂区', IFNULL(pc1.`name`, '无') AS '入库工序', IFNULL(pl1.`name`, '无') AS '入库产线',"
    		+ " IFNULL(w1.`name`, '无') AS '入库工位', IFNULL(m.inputTime, '无') AS '入库时间',  IFNULL(m.expendOrderID, '无') AS '投料工单', IFNULL(m.outputer, '无') AS '投料人', IFNULL(m.outputerID, '无') AS '投料人工号', IFNULL(pt2.`name`, '无') AS '投料厂区', IFNULL(pc2.`name`, '无') AS '投料工序', IFNULL(pl2.`name`, '无') AS '投料产线', IFNULL(w2.`name`, '无') AS '投料工位', IFNULL(m.outputTime, '无') AS '投料时间'\r\n" + 
    		"    FROM tb_materialrecord m\r\n" + 
    		"    LEFT JOIN sys_industrialplant pt1\r\n" + 
    		"        ON m.inputPlantID = pt1.id\r\n" + 
    		"    LEFT JOIN sys_industrialplant pt2\r\n" + 
    		"        ON m.outputPlantID = pt2.id\r\n" + 
    		"    LEFT JOIN sys_productionprocess pc1\r\n" + 
    		"        ON m.inputProcessID = pc1.id\r\n" + 
    		"    LEFT JOIN sys_productionprocess pc2\r\n" + 
    		"        ON m.outputProcessID = pc2.id\r\n" + 
    		"    LEFT JOIN sys_productionline pl1\r\n" + 
    		"        ON m.inputLineID = pl1.id\r\n" + 
    		"    LEFT JOIN sys_productionline pl2\r\n" + 
    		"        ON m.outputLineID = pl2.id\r\n" + 
    		"    LEFT JOIN tb_worklocation w1\r\n" + 
    		"        ON m.inputWorkLocationID = w1.id\r\n" + 
    		"    LEFT JOIN tb_worklocation w2\r\n" + 
    		"        ON m.inputWorkLocationID = w2.id\r\n" + 
    		"    WHERE ${filter})\r\n" + 
    		"    UNION\r\n" + 
    		"    ALL \r\n" + 
    		"        (SELECT '总计' AS id, IFNULL(m.materialNameInfo, '无') AS '物料型号', CONVERT ( sum(m.number) USING utf8)  AS '数量', '' AS '入库工单', '' AS '入库人员姓名', '' AS '入库厂区', '' AS '入库工序', '' AS '入库产线', '' AS '入库工位', '' AS '入库时间', '' AS '投料工单', '' AS '投料人', '' AS '投料人工号', '' AS '投料厂区', '' AS '投料工序', '' AS '投料产线', '' AS '投料工位', '' AS '投料时间'\r\n" + 
    		"        FROM tb_materialrecord m\r\n" + 
    		"        WHERE ${filter}\r\n" + 
    		"        GROUP BY  m.materialID ) ) rsall\r\n" + 
    		"    ORDER BY  rsall.`投料时间`, rsall.`投料工单`, rsall.`入库工单` , rsall.`物料型号` limit 1000")
    List<LinkedHashMap<Object, Object>> workOrderPutIntoManage(@Param("filter") String filter);
}