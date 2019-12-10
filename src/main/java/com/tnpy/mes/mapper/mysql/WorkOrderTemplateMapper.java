package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WorkOrderTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

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
}