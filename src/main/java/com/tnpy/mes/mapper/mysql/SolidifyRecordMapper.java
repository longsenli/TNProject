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
    List<SolidifyRecord> selectByRoomID(String plantID,String roomID);

    @Update("update tb_solidifyrecord set  ${value} where id = #{id}")
    int addSolidifyRecord(String id,@Param("value") String value);
}