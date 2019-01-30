package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    @Select("select c.id,c.typeID,c.name,c.status,c.shortname,d.number as description from \n" +
            "( select a.id,a.typeID,a.name,a.status,a.shortname  from sys_material  a left join sys_processmaterial b \n" +
            " on a.typeID = b.materialTypeID  where processID = #{processID} and inOrout =2 ) c left join sys_materialtype d on c.typeID = d.id" )
    List<Material>  selectOutByProcess(String processID);

    @Select("select * from sys_material where id in (select distinct inMaterialID from  sys_materialrelation  ${filter} )")
    List<Material> selectByFilter(@Param("filter") String filter);

    @Select("select * from\n" +
            "(\n" +
            " SELECT   a.proportionality,b.typeID FROM sys_materialrelation a left join sys_material b \n" +
            " on a.inMaterialID = b.id  where a.outMaterialID = #{outMaterialID} group by a.proportionality,b.typeID order by proportionality desc\n" +
            " ) c group by typeID ")
    List<Map<String, String>> selectProportionalityByOut( String outMaterialID);
}