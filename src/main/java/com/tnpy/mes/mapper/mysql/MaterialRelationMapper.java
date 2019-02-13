package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MaterialRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialRelation record);

    int insertSelective(MaterialRelation record);

    MaterialRelation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialRelation record);

    int updateByPrimaryKey(MaterialRelation record);

    @Select(" select  c.id,c.inMaterialID,d.name as outMaterialID,c.proportionality,c.status from (\n" +
            "SELECT a.id,b.name as inMaterialID ,a.outMaterialID,a.proportionality,a.status FROM sys_materialrelation a \n" +
            "left join sys_material b on a.inMaterialID = b.id  ${filter} ) c left join  sys_material d on c.outMaterialID = d.id order by inMaterialID" )
    List<MaterialRelation> selectByFilter(@Param("filter") String filter);
}