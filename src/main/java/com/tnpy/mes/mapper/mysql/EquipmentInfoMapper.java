package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.EquipmentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component

public interface EquipmentInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(EquipmentInfo record);

    int insertSelective(EquipmentInfo record);

    EquipmentInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EquipmentInfo record);

    int updateByPrimaryKey(EquipmentInfo record);

    @Select("select * from tb_equipmentinfo where typeID = #{typeID} and plantID = #{plantID} and IFNULL(status,'') <> '-1'")
    List<EquipmentInfo> selectByType(String typeID,String plantID);

    @Update("update tb_equipmentinfo set status = -1 where id = #{id}")
    int deleteByChangeStatus(String id);

}