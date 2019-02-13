package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ProcessMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ProcessMaterialMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProcessMaterial record);

    int insertSelective(ProcessMaterial record);

    ProcessMaterial selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProcessMaterial record);

    int updateByPrimaryKey(ProcessMaterial record);

    @Select("select * from sys_processmaterial order by processID")
    List<ProcessMaterial> selectAll();

    @Select("select * from sys_processmaterial ${filter}")
    List<ProcessMaterial> selectByFilter(@Param("filter") String filter);
}