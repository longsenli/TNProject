package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.EquipmentType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component

public interface EquipmentTypeMapper {
    int deleteByPrimaryKey(String id);

    int insert(EquipmentType record);

    int insertSelective(EquipmentType record);

    EquipmentType selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EquipmentType record);

    int updateByPrimaryKey(EquipmentType record);

    @Select("select * from tb_equipmenttype")
    List<EquipmentType> selectAllType();
}