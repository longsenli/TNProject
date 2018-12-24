package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.EquipmentParaRecord;
import com.tnpy.mes.model.mysql.EquipmentParaRecordKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component

public interface EquipmentParaRecordMapper {
    int deleteByPrimaryKey(EquipmentParaRecordKey key);

    int insert(EquipmentParaRecord record);

    int insertSelective(EquipmentParaRecord record);

    EquipmentParaRecord selectByPrimaryKey(EquipmentParaRecordKey key);

    int updateByPrimaryKeySelective(EquipmentParaRecord record);

    int updateByPrimaryKey(EquipmentParaRecord record);
}