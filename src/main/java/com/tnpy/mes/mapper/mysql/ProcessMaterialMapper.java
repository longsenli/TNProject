package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ProcessMaterial;
import com.tnpy.mes.model.mysql.ProcessMaterialKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ProcessMaterialMapper {
    int deleteByPrimaryKey(ProcessMaterialKey key);

    int insert(ProcessMaterial record);

    int insertSelective(ProcessMaterial record);

    ProcessMaterial selectByPrimaryKey(ProcessMaterialKey key);

    int updateByPrimaryKeySelective(ProcessMaterial record);

    int updateByPrimaryKey(ProcessMaterial record);
}