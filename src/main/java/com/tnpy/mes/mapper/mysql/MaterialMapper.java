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
    @Select("select * from sys_material")
    List<Material> selectAll();
}