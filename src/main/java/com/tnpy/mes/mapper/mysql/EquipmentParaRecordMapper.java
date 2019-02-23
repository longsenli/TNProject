package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.customize.EquipParamLatestRecord;
import com.tnpy.mes.model.mysql.EquipmentParaRecord;
import com.tnpy.mes.model.mysql.EquipmentParaRecordKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

/*
*  <select id="selectLatestRecord" resultMap="ParamLatestRecordResultMap">
    select a.equipmentID as equipmentID,b.name as equipName,b.location as equipLocation , a.recordTime as recordTime,a.value as value,a.status as status,a.recorder as recorder from ( (
    select x.equipmentID,x.recordTime,x.status,x.value,x.recorder from tb_equipmentparamrecord x where x.paramID=#{paramID} and x.equipmentTypeID =#{equipType} and  recordTime =
    (select max(recordTime) from tb_equipmentparamrecord y where  y.equipmentID=x.equipmentID)
      ) as a left join tb_equipmentinfo b on a.equipmentID = b.id) where b.plantID = #{plantID} order by equipName desc
  </select>
  */
    List<EquipParamLatestRecord> selectLatestRecord(String plantID, String equipType, String paramID);

    @Select("select * from tb_equipmentparamrecord where equipmentID = #{equipID} and paramID = #{paramID} and recordTime >= #{startTime} " +
            "and recordTime <= #{endTime}  order by recordTime asc limit 1000")
    List<EquipParamLatestRecord> selectOneEquipParamRecord( String startTime,String endTime,String equipID,String paramID);

    @Select("select * from tb_equipmentparamrecord where equipmentID = #{equipID} and recordTime >= #{startTime} " +
            "and recordTime <= #{endTime}  order by recordTime desc limit 1000")
    List<EquipmentParaRecord> selectRecordByTime(  String startTime,String endTime,String equipID);

    @Select(" \n" +
            "select d.*,concat(ifnull(e.name,''),'：' ,ifnull(value,''),ifnull(e.units,'')) as showName  from (\n" +
            "select p.id,p.paramID,equipName,equipLocation,value,recordTime,status,recorder from (\n" +
            "select m.id,m.name as equipName, m.location as equipLocation,n.paramID from tb_equipmentinfo m left join tb_equipmentparam n on m.typeID = n.equipmentTypeID where typeID = #{equipType} and plantID =  #{plantID} ) p\n" +
            "left join (select a.equipmentID as equipmentID,a.paramID, a.recordTime as recordTime,a.value as value,a.status as status,a.recorder as recorder \n" +
            " from (select * from ( select ROW_NUMBER() over(partition by equipmentID,paramID order by recordTime desc) RowNum,tb_equipmentparamrecord.* \n" +
            " from tb_equipmentparamrecord where  paramID in ( select paramID from tb_equipmentparam where equipmentTypeID = #{equipType})  and equipmentTypeID =#{equipType} and  recordTime >= #{recentTime} ) as t1  where RowNum = 1) a\n" +
            ") c on p.id = c.equipmentID and p.paramID = c.paramID) d left join tb_parameterinfo e on d.paramID = e.ID  order by id asc")
    List<Map<Object,Object>> selectRecentAllParamPecord(String plantID, String equipType,String recentTime);
}