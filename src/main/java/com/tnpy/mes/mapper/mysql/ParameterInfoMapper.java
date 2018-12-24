package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ParameterInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component

public interface ParameterInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ParameterInfo record);

    int insertSelective(ParameterInfo record);

    ParameterInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ParameterInfo record);

    int updateByPrimaryKey(ParameterInfo record);

    @Select("select a.* from tb_parameterinfo a left join tb_equipmentparam b  on a.id = b.paramID where b.status = '1'  and b.equipmentTypeID = #{equipmentTypeID}")
    List<ParameterInfo> selectByEquipType(String equipmentTypeID);
}