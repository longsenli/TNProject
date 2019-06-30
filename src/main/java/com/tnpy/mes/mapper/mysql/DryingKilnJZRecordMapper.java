package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DryingKilnJZRecord;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface DryingKilnJZRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DryingKilnJZRecord record);

    int insertSelective(DryingKilnJZRecord record);

    DryingKilnJZRecord selectByPrimaryKey(String id);
    
    @Select("select * from tb_dryingkilnjzrecord t where t.suborderid=#{suborderid}")
    List<DryingKilnJZRecord> selectBySuborderid(@Param("suborderid") String suborderid);

    @Select("select * from tb_dryingkilnjzrecord t where t.dryingKilnID = #{dryingKilnID} and t.status = '1' limit 1")
    List<DryingKilnJZRecord> selectByDryingKilnIDAndStatus(@Param("dryingKilnID") String dryingKilnID);
    
    int updateByPrimaryKeySelective(DryingKilnJZRecord record);

    int updateByPrimaryKey(DryingKilnJZRecord record);
    
    @Update("update tb_dryingkilnjzrecord t set t.outputerID =#{outputerID} ,t.outputerName = #{outputerName} , t.outputtime = #{outputtime} , t.status =  #{changestatus} where "
    		+ "t.dryingKilnID = #{dryingKilnID} and t.status = #{status} ")
    int updateByDryingKilnIDAndStatus(@Param("outputerID") String outputerID,@Param("outputerName") String outputerName,
    		@Param("outputtime") Date date, @Param("changestatus") int changestatus, @Param("dryingKilnID") String dryingKilnID, @Param("status") int status);
    
    
    /**
  	  * 查询当前窑中有多少工单
     */
    @Select("select count(1) from tb_dryingkilnjzrecord t where t.dryingKilnID = #{dryingKilnID} and t.outputTime is null")
    int selectExitsInDryRecord(@Param("dryingKilnID") String dryingKilnID);
    
    /**
 	  *取消入窑, 按  suborderid 删除 
    */
   @Delete("delete from tb_dryingkilnjzrecord\r\n" + 
   		"    where suborderID = #{suborderID} ")
   int deleteBySubOrderId(@Param("suborderID") String suborderID);


}