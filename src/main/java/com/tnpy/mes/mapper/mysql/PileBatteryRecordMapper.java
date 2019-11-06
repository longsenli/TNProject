package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PileBatteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PileBatteryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PileBatteryRecord record);

    int insertSelective(PileBatteryRecord record);

    PileBatteryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PileBatteryRecord record);

    int updateByPrimaryKey(PileBatteryRecord record);
    @Select("select * from tb_pilebatteryrecord ${filter} ")
    List<PileBatteryRecord> selectByFilter(@Param("filter") String filter);
    
    @Select("${filter} ")
    List<PileBatteryRecord> selectAllByFilter(@Param("filter") String filter);

    @Update(" update tb_pilebatteryrecord set status = #{status} ,packageTime = #{packageTime} where id = #{id}")
    int updateStatusByPrimaryKey(String id,String status,String packageTime);

    @Update("update tb_pilebatteryrecord set status = '3' , finishpackageplantID = null,finishpackageprocessID = null," +
            " finishopackagelineID=null,finishpackageStaffID=null,finishpackageStaffName = null,packageTime=null  where id = #{id}")
    int cancelInputSuborder(String id);

    @Select( "(select   materialID as materialid,materialName, sum(fnishpackageNum)  as number,'' as outputtime,'' as outputer,\n" +
            "'' as orderid,'' as inOrderName,'' as inSubOrderName,'' as suborderid,'' as inputer,'' as inputtime,'' as id,'' as expendOrderid\n" +
            "from tb_pilebatteryrecord ${filter} group by materialid )\n" +
            "union all\n" +
            "(select   materialID as materialid,materialName, fnishpackageNum  as number,packageTime as outputtime,finishpackageStaffName as outputer,\n" +
            "id as orderid,id as inOrderName,partpackageID as inSubOrderName,id as suborderid,finishpileStaffName as inputer,finishpileTime as inputtime,id,id as expendOrderid\n" +
            "from tb_pilebatteryrecord ${filter} order by packageTime desc limit 1000)")
    List<Map<Object, Object>> selectLineInputDetail(@Param("filter") String filter);
}