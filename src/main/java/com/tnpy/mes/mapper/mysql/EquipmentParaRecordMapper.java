package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.customize.EquipParamLatestRecord;
import com.tnpy.mes.model.mysql.EquipmentParaRecord;
import com.tnpy.mes.model.mysql.EquipmentParaRecordKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
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
    List<EquipmentParaRecord> selectRecord(String equipID);

/*
*  <select id="selectLatestRecord" resultMap="ParamLatestRecordResultMap">
    select a.equipmentID as equipmentID,b.name as equipName,b.location as equipLocation , a.recordTime as recordTime,a.value as value,a.status as status,a.recorder as recorder from ( (
    select x.equipmentID,x.recordTime,x.status,x.value,x.recorder from tb_equipmentparamrecord x where x.paramID=#{paramID} and x.equipmentTypeID =#{equipType} and  recordTime =
    (select max(recordTime) from tb_equipmentparamrecord y where  y.equipmentID=x.equipmentID)
      ) as a left join tb_equipmentinfo b on a.equipmentID = b.id) where b.plantID = #{plantID} order by equipName desc
  </select>
  */
    List<EquipParamLatestRecord> selectLatestRecord(String plantID, String equipType, String paramID, @Param("tableName") String tableName);

    @Select("select * from ${DBName} where equipmentID = #{equipID} and paramID = #{paramID} and recordTime >= #{startTime} " +
            "and recordTime <= #{endTime}  order by recordTime asc limit 1000")
    List<EquipParamLatestRecord> selectOneEquipParamRecord(@Param("DBName") String dbName, String startTime,String endTime,String equipID,String paramID);

    @Select("select * from tb_equipmentparamrecord where equipmentID = #{equipID} and recordTime >= #{startTime} " +
            "and recordTime <= #{endTime}  order by recordTime desc limit 1000")
    List<EquipmentParaRecord> selectRecordByTime(  String startTime,String endTime,String equipID);

    @Select(" \n" +
            "select d.*,concat(ifnull(e.name,''),'：' ,ifnull(value,''),ifnull(e.units,'')) as showName  from (\n" +
            "select p.id,p.paramID,equipName,equipLocation,p.ordernum,value,recordTime,status,recorder from (\n" +
            "select m.id,m.name as equipName, m.location as equipLocation,n.paramID,m.ordernum from ( select * from tb_equipmentinfo where typeID = #{equipType} and plantID =  #{plantID}  and ifnull(status,1) != '-1'  ${processIDFilter}) m left join tb_equipmentparam n on m.typeID = n.equipmentTypeID ) p\n" +
            "left join (select a.equipmentID as equipmentID,a.paramID, a.recordTime as recordTime,a.value as value,a.status as status,a.recorder as recorder \n" +
            " from (select * from ( select ROW_NUMBER() over(partition by equipmentID,paramID order by recordTime desc) RowNum,${tableName}.* \n" +
            " from ${tableName} where  paramID in ( select paramID from tb_equipmentparam where equipmentTypeID = #{equipType})  and equipmentTypeID =#{equipType} and  recordTime >= #{recentTime} ) as t1  where RowNum = 1) a\n" +
            ") c on p.id = c.equipmentID and p.paramID = c.paramID) d left join tb_parameterinfo e on d.paramID = e.ID  order by ordernum asc,paramID asc")
    List<Map<Object,Object>> selectRecentAllParamPecord(String plantID, String equipType,String recentTime, @Param("tableName") String tableName, @Param("processIDFilter") String processIDFilter);

    @Select("select typeID from tb_equipmentinfo where id = #{equipID} limit 1")
    String selectEquipType(String equipID);

    @Select(" \n" +
            "select m.totalElectric,n.totalProduction,ROUND(n.totalProduction/m.totalElectric,0) as prodPerElc,m.timeStr from (\n" +
            "  select sum(a.value) as totalElectric,a.timeStr from (\n" +
            "select equipmentID,ROUND(value,1) as value,date_format(date_add(recordTime, interval -1 day), '%Y-%m-%d') as timeStr \n" +
            "from tb_equipmentparamrecord_10012 where recordTime > #{startTime}  and recordTime < #{endTime}) a \n" +
            "left join tb_equipmentinfo b on a.equipmentID = b.id where b.processID = #{processID} and b.plantID = #{plantID} group by timeStr ) m left join (\n" +
            "SELECT sum(productionNum) totalProduction,processID,date_format(date_add(updateTime, interval -1 day), '%Y-%m-%d') as timeStr \n" +
            "FROM tb_materialinventoryrecord where updateTime > #{startTime}   and updateTime < #{endTime} and processID = #{processID} and plantID = #{plantID} group by timeStr ) n on m.timeStr = n.timeStr order by timeStr ")
    List<Map<Object,Object>> getElectricProductionRelation(String plantID, String processID,String startTime,String endTime);
    
    
    @Select("SELECT e.location AS '位置',( CASE t.runningStatus WHEN 1 THEN '运行' WHEN 2 THEN '运行' WHEN 0 THEN '停止' ELSE '未知' END) AS '运行状态', IFNULL(t.realtimeTemperature,'') AS '实时温度', IFNULL(t.settingTemperature,'') AS '设定温度', IFNULL(t.statusTemperature,'') AS '温度状态', IFNULL(t.realtimeHumidity,'') AS '实时湿度', IFNULL(t.settingHumidity,'') AS '设定湿度', IFNULL(t.statusHumidity,'') AS '湿度状态', IFNULL(t.solidificationHour,'') AS '固化时长小时', IFNULL(t.solidificationMinute,'') AS '固化时长分钟', IFNULL(t.solidificationSecond,'') AS '固化时长秒' FROM tb_equipmentinfo e LEFT JOIN(SELECT * FROM ${tableName} s WHERE s.acquisitionTime >= "
    		+ "DATE_SUB( DATE_FORMAT( #{recentTime}, '%Y-%m-%d %H:%i:%s'), INTERVAL 5 MINUTE ) GROUP BY s.equipmentID ) t ON e.id = t.equipmentID "
    		+ "WHERE e.plantID = #{plantID} AND e.typeID = #{equipType} AND e. STATUS = '1' ORDER BY e.location")
    List<LinkedHashMap<Object,Object>> selectSolidificationoperatingparametersacquisition(String plantID, String equipType,String recentTime, @Param("tableName") String tableName);


}