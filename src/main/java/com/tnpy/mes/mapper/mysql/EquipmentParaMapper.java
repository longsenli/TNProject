package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.EquipmentPara;
import com.tnpy.mes.model.mysql.EquipmentParaKey;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component

public interface EquipmentParaMapper {
    int deleteByPrimaryKey(EquipmentParaKey key);

    int insert(EquipmentPara record);

    int insertSelective(EquipmentPara record);

    EquipmentPara selectByPrimaryKey(EquipmentParaKey key);

    int updateByPrimaryKeySelective(EquipmentPara record);

    int updateByPrimaryKey(EquipmentPara record);

    @Insert("delete from tb_equipmentparam where equipmentTypeID = #{equipTypeID}; ${insertData}")
    int updateEquipParams(@Param("insertData") String params,@Param("equipTypeID") String equipTypeID);

}