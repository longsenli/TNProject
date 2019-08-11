package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DryingKilnJZRecord;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Select(" select a.suborderID as orderID,a.inputer as status,date_format(a.inputTime, '%Y-%m-%d %H:%i:%s') as returnMessage  from ( select * from tb_materialrecord where inputPlantID = #{plantID} " +
            " and inputProcessID = #{processID} and  inputTime > #{startTime} and inputTime < #{endTime} and inOrOut = '0' ) a left join\n" +
            " tb_dryingkilnjzrecord b on a.subOrderID = b.suborderID where b.suborderID is  null")
    List<Map<Object, Object>>  orderOutOfDryingKiln( String plantID ,String processID,String startTime,String endTime );
}