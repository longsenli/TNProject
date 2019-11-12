package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DataProvenanceRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DataProvenanceRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(DataProvenanceRelation record);

    int insertSelective(DataProvenanceRelation record);

    DataProvenanceRelation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DataProvenanceRelation record);

    int updateByPrimaryKey(DataProvenanceRelation record);

    @Select(" select * from tb_dataprovenancerelation ${filter} ")
    List<DataProvenanceRelation> selectByFilter(@Param("filter") String filter);

    @Select(" select inSubOrderID from tb_dataprovenancerelation where outSubOrderID  in ( ${outOrderIDList} ) ")
    List<String> selectInOrderIDByOutOrderID(@Param("outOrderIDList") String outOrderIDList);

    @Select(" select outSubOrderID from tb_dataprovenancerelation where inSubOrderID  in ( ${inOrderIDList} ) ")
    List<String> selectOutOrderIDByInOrderID(@Param("inOrderIDList") String inOrderIDList);

    @Select(" select id as subOrderID,materialName as materialNameInfo,staffName as inputer,DATE_FORMAT(usedTime, '%Y-%m-%d %H:%i:%s') as inputTime,JQID,plantID as inputPlantID,'1015' as inputProcessID,lineID as inputLineID from tb_plasticusedrecord where id   = #{bottomQR} ")
    List<Map<Object, Object>> selectBatteryInfoByBottomQR(String bottomQR);

    @Select(" select id as subOrderID,materialName as materialNameInfo,staffName as inputer,DATE_FORMAT(usedTime, '%Y-%m-%d %H:%i:%s') as inputTime,JQID,plantID as inputPlantID,'1015' as inputProcessID,lineID as inputLineID from tb_plasticusedrecord where JQID   = #{JQCode} ")
    List<Map<Object, Object>> selectBatteryInfoByJQCode(String JQCode);
}