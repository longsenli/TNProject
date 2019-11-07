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

    @Select("select status from tb_pilebatteryrecord where id = #{id}")
    String selectPileRecordStatus(String id);

    @Update("update tb_pilebatteryrecord set status = '1' , finishplantID = null,finishprocessID = null," +
            " finishlineID=null,finishpileStaffID=null,finishpileStaffName = null,finishpileTime=null,finishpileNum = null,partpileID = null  where id = #{id}")
    int cancelPileRecordSuborder(String id);


    @Select( "(select   materialID as materialid,materialName, sum(fnishpackageNum)  as number,'' as outputtime,'' as outputer,\n" +
            "'' as orderid,'' as inOrderName,'' as inSubOrderName,'' as suborderid,'' as inputer,'' as inputtime,'' as id,'' as expendOrderid\n" +
            "from tb_pilebatteryrecord ${filter} group by materialid )\n" +
            "union all\n" +
            "(select   materialID as materialid,materialName, fnishpackageNum  as number,date_format(packageTime,'%Y-%m-%d %h:%i') as outputtime,finishpackageStaffName as outputer,\n" +
            "id as orderid,id as inOrderName,partpackageID as inSubOrderName,id as suborderid,finishpileStaffName as inputer,date_format(finishpileTime,'%Y-%m-%d %h:%i')  as inputtime,id,id as expendOrderid\n" +
            "from tb_pilebatteryrecord ${filter} order by packageTime desc limit 1000)")
    List<Map<Object, Object>> selectLineInputDetail(@Param("filter") String filter);

    @Select( "(select '总计' as inputer,materialName as materialNameInfo,'' as subOrderID,sum(finishpileNum) as number,'' as inputTime\n" +
            " from tb_pilebatteryrecord where finishpileTime >#{startTime} and  finishpileTime < #{endTime} and lineID = #{lineID} group by materialNameInfo)\n" +
            "union all\n" +
            "(select finishpileStaffName as inputer,materialName as materialNameInfo,id as subOrderID,finishpileNum as number,finishpileTime as inputTime\n" +
            " from tb_pilebatteryrecord where finishpileTime >#{startTime} and  finishpileTime < #{endTime} and lineID = #{lineID} order by finishpileTime)\n")
    List<Map<Object, Object>> selectLinePileDetail( String lineID,String startTime,String endTime);

    @Select("select id as ordersplitid,materialName,materialID as materialid,productionNumber as productionnum , case status when '1' then '已下单' else '已打堆扫码' end as statusName,\n" +
            " status as status,id,id as orderid  from tb_pilebatteryrecord where id = #{id}")
    List<Map<String, String>> selectToOrderFinishInfo(String id);
}