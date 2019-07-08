package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.SolidifyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SolidifyRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(SolidifyRecord record);

    int insertSelective(SolidifyRecord record);

    SolidifyRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SolidifyRecord record);

    int updateByPrimaryKey(SolidifyRecord record);


    @Select("select a.* from (select * from tb_solidifyrecord where solidifyRoomID = #{roomID} or solidifyRoomID is null) a" +
            " left join tb_workorder b on a.orderID = b.id  where b.plantID =  #{plantID} order by starttime1 desc limit 1000")
    List<SolidifyRecord> selectByRoomID(String plantID, String roomID);

    @Update("update tb_solidifyrecord set  ${value} where id = #{id}")
    int addSolidifyRecord(String id,@Param("value") String value);

    @Select("select * from tb_solidifyrecord  ${filter} limit 1000")
    List<SolidifyRecord> getSolidifyRecordByFilter(@Param("filter") String filter);

    @Update(" update tb_solidifyrecord  ${value} where ${filter} ")
    int changeSolidifyStatus(@Param("value") String value,@Param("filter") String filter);

    @Select("select * from tb_solidifyrecord  where orderID = #{orderID} ")
    List<SolidifyRecord>  selectByOrderID(String orderID);


    @Select(" (select '' as id,'' as orderID,sum( productionNum) as productionNum,'' as  plantID,'' as  materialID, materialName,'' as  solidifyRoomID, '' as recorder1, \n" +
            "    '' as starttime1,'' as  endtime1, '' as recorder2, '' as starttime2,'' as  endtime2,'' as  recorder3, '' as starttime3, '' as endtime3, \n" +
            "   '' as  status,'' as  remark, solidifyRoomName,'' as  outOperator from tb_solidifyrecord  ${filter} group by solidifyRoomID,materialName order by solidifyRoomID limit 1000) union all ( select * from tb_solidifyrecord  ${filter} order by solidifyRoomID limit 1000 ) ")
    List<SolidifyRecord>  selectInSolidifyRecord(@Param("filter") String filter);
}