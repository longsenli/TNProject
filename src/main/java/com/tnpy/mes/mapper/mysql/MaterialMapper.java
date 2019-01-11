package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MaterialMapper {
    int deleteByPrimaryKey(String id);

    int insert(Material record);

    int insertSelective(Material record);

    Material selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Material record);

    int updateByPrimaryKey(Material record);

    @Select("select * from sys_material where typeID = #{typeID} order by name ")
    List<Material> selectByType(String typeID);

    @Select("select * from sys_material")
    List<Material> selectAll();

    @Select("select * from sys_material where typeID in( select distinct materialTypeID from sys_processmaterial where processID = #{processID} and inOrout =2)")
    List<Material>  selectOutByProcess(String processID);

}