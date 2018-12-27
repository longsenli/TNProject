package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.IndustrialPlant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface IndustrialPlantMapper {
    int deleteByPrimaryKey(String id);

    int insert(IndustrialPlant record);

    int insertSelective(IndustrialPlant record);

    IndustrialPlant selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(IndustrialPlant record);

    int updateByPrimaryKey(IndustrialPlant record);

    @Select("select * from sys_industrialPlant")
    List<IndustrialPlant> selectAll();
}