package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ProcessMaterial;

public interface ProcessMaterialMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProcessMaterial record);

    int insertSelective(ProcessMaterial record);

    ProcessMaterial selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProcessMaterial record);

    int updateByPrimaryKey(ProcessMaterial record);
}