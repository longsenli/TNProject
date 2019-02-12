package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MaterialTypeMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialType record);

    int insertSelective(MaterialType record);

    MaterialType selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialType record);

    int updateByPrimaryKey(MaterialType record);

    @Select("select * from sys_materialtype order by id asc")
    List<MaterialType> selectAll();

    @Select("select name from sys_materialtype where id = #{id}")
    String getTypeNameByID(String id);
}