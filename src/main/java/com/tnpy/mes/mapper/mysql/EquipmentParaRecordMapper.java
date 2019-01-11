package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.customize.EquipParamLatestRecord;
import com.tnpy.mes.model.mysql.EquipmentParaRecord;
import com.tnpy.mes.model.mysql.EquipmentParaRecordKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component

public interface EquipmentParaRecordMapper {
    int deleteByPrimaryKey(EquipmentParaRecordKey key);

    int insert(EquipmentParaRecord record);

    int insertSelective(EquipmentParaRecord record);

    EquipmentParaRecord selectByPrimaryKey(EquipmentParaRecordKey key);

    int updateByPrimaryKeySelective(EquipmentParaRecord record);

    int updateByPrimaryKey(EquipmentParaRecord record);
@Select("select * from tb_equipmentparamrecord where equipmentID = #{equipID} order by recordTime desc,paramID asc limit 1000")
    List<EquipmentParaRecord> selectRecord( String equipID);

    List<EquipParamLatestRecord> selectLatestRecord(String plantID, String equipType, String paramID);

    @Select("select * from tb_equipmentparamrecord where equipmentID = #{equipID} and paramID = #{paramID} and recordTime >= #{startTime} " +
            "and recordTime <= #{endTime}  order by recordTime asc limit 1000")
    List<EquipParamLatestRecord> selectOneEquipParamRecord( String startTime,String endTime,String equipID,String paramID);

    @Select("select * from tb_equipmentparamrecord where equipmentID = #{equipID} and recordTime >= #{startTime} " +
            "and recordTime <= #{endTime}  order by recordTime desc limit 1000")
    List<EquipmentParaRecord> selectRecordByTime(  String startTime,String endTime,String equipID);
}