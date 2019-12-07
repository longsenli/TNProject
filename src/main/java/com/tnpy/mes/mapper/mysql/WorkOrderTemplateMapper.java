package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WorkOrderTemplate;

public interface WorkOrderTemplateMapper {
    int deleteByPrimaryKey(String id);

    int insert(WorkOrderTemplate record);

    int insertSelective(WorkOrderTemplate record);

    WorkOrderTemplate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WorkOrderTemplate record);

    int updateByPrimaryKey(WorkOrderTemplate record);
}