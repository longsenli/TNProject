package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.customize.CustomMaterialRecord;
import com.tnpy.mes.model.mysql.MaterialRecord;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Component

public interface MaterialRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialRecord record);

    int insertSelective(MaterialRecord record);

    MaterialRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialRecord record);

    int updateByPrimaryKey(MaterialRecord record);

    @Select("select * from tb_materialrecord where subOrderID = #{id} order by inOrOut  limit 1")
    MaterialRecord selectBySuborderID(String id);

    @Select("select * from tb_materialrecord where subOrderID = #{id} and inOrOut = #{inoutStatus} limit 1")
    MaterialRecord selectBySuborderIDAndInOut(String id,String inoutStatus);

    @Select("select c.*,d.name as materialName from ( \n" +
            "SELECT a.*,b.orderSplitID as inSubOrderName,left(b.orderSplitID, length(b.orderSplitID)-3)  as inOrderName \n" +
            "FROM tb_materialrecord  a left join tb_ordersplit b on a.subOrderID = b.id where expendOrderID = #{expendOrder} \n" +
            ") c left join sys_material d on c.materialID = d.id  order by outputTime desc")
    List<CustomMaterialRecord> selectByExpendOrder(String expendOrder);


    @Select("( select '' as  id,'' as materialid,'' as inOrderName,'' as inSubOrderName,'' as outOrderName, materialNameInfo as materialName,'' as inputer,now() as inputtime,'总计' as outputer,now() as outputtime,sum(number) as number,'' as inorout " +
            " FROM tb_materialrecord  where expendOrderID = #{expendOrder} group by materialNameInfo limit 100 ) union all " +
            " ( SELECT id,materialid,orderid as inOrderName,subOrderID as inSubOrderName,expendorderid as outOrderName, materialNameInfo as materialName,inputer,inputtime,outputer,outputtime,number,inorout " +
            " FROM tb_materialrecord  where expendOrderID = #{expendOrder} order by outputTime desc  limit 1000)" )
    List<CustomMaterialRecord> selectByExpendOrder2(String expendOrder);

    @Select("( select '' as  id,'' as materialid,'' as inOrderName,'' as inSubOrderName,'' as outOrderName, materialNameInfo as materialName,'' as inputer,now() as inputtime,'总计' as outputer,now() as outputtime,sum(number) as number,'' as inorout " +
            " FROM tb_materialrecord  ${filter} group by materialNameInfo limit 100 ) union all " +
            " ( SELECT id,materialid,orderid as inOrderName,subOrderID as inSubOrderName,expendorderid as outOrderName, materialNameInfo as materialName,inputer,inputtime,outputer,outputtime,number,inorout " +
            "  FROM tb_materialrecord  ${filter} order by outputTime desc limit 1000 ) ")
    List<CustomMaterialRecord> selectByExpendLineIDFilter(@Param("filter") String filter);

    @Select("select c.*,d.name as materialName from ( \n" +
            "SELECT a.*,b.orderSplitID as inSubOrderName,left(b.orderSplitID, length(b.orderSplitID)-3)  as inOrderName \n" +
            "FROM tb_materialrecord  a left join tb_ordersplit b on a.subOrderID = b.id where a.subOrderID = #{subOrderID}  \n" +
            ") c left join sys_material d on c.materialID = d.id  order by inOrOut ")
    List<CustomMaterialRecord> selectBySubOrderID(String subOrderID);

@Select("select materialid,materialName,finishpileNum as number,id as orderid,id as inOrderName,id as inSubOrderName,id as suborderid,finishpileStaffName as inputer,date_format(finishpileTime,'%Y-%m-%d %h:%i')  as inputtime,id\n" +
        "from tb_pilebatteryrecord where partpileID = #{subOrderID} order by status asc")
    List<Map<Object, Object>> selectPilePackageRecord(String subOrderID);

    /* @Select("select g.* from \n" +
             "( select e.*,f.name as materialName from ( select c.*,d.orderSplitID as inSubOrderName,\n" +
             "left(d.orderSplitID, length(d.orderSplitID)-3)  as inOrderName from ( \n" +
             "select a.* from ( SELECT * FROM tb_materialrecord where materialID in(\n" +
             "select inMaterialID from sys_materialrelation where outMaterialID = #{materialID}) and inOrOut = '1'\n" +
             ") a left join tb_workorder b on a.orderID = b.id  \n" +
             "where b.plantID = #{plantID} ) c left join tb_ordersplit d on c.subOrderID = d.id \n" +
             ") e left join sys_material f on e.materialID = f.id  ) g left join  tb_batchrelationcontrol m\n" +
             " on g.orderID = m.relationOrderID where m.tbBatch = #{batchID} order by inputTime asc ")*/
    List<CustomMaterialRecord> selectUsableMaterial(String plantID,String materialID,String batchID );


    int updateGainMaterialRecord(List<String> materialIDList, String expendOrderID, String outputer , Date outputTime, int status);

    @Select("select count(*) from tb_materialrecord where subOrderID = #{qrCode} and inOrOut =#{status}")
    int checkMaterialRecordUsed(String qrCode, int status);

    @Select("SELECT count(*) FROM tb_materialrecord where subOrderID = #{qrCode}  and materialID in(\n" +
            "select distinct inMaterialID from sys_materialrelation where outMaterialID in (select materialID from tb_workorder where id = #{expendOrderID}) )\n")
    int checkMaterialRelation(String qrCode,String expendOrderID);

    @Update("  update tb_materialrecord\n" +
            "    set\n" +
            "    expendOrderID = #{expendOrderID},\n" +
            "    inOrOut=#{status},\n" +
            "    outputTime=#{outputTime},\n" +
            "    outputer=#{outputer} where subOrderID= #{qrCode}")
    int updateGainMaterialByQR(String qrCode, String expendOrderID, String outputer , Date outputTime, int status);

    @Select("select b.typeID,sum(a.number) as sum  from \n" +
            "(\n" +
            "SELECT materialID,number FROM ilpsdb.tb_materialrecord where expendOrderID = #{outOrderID}\n" +
            ") a left join sys_material b on a.materialID = b.id group by b.typeID ")
    List<Map<Object, Object>> selectBatchChargingByOrder(String outOrderID);

    @Select("select sum(number) from tb_materialrecord where orderID = #{orderID}")
    String getProductionByOrderID(String orderID);

    @Select("select distinct orderID from tb_materialrecord where expendOrderID = #{expendOrderID}")
    List<String> getOrderIDByExpendID(String expendOrderID);


    List<CustomMaterialRecord> selectByExpendIDList(@Param("expendIDList") List<String> expendIDList);

    /*  @Select("select d.outputTotal,e.name from ( select sum(number) as outputTotal,materialID from \n" +
              "(\n" +
              "select a.materialID,b.plantID,b.lineID,b.processID,a.number,a.inputTime,a.inOrOut from ( select * from tb_materialrecord where inputTime > #{startTime} and inputTime < #{endTime} ) a\n" +
              " left join tb_workorder b on a.orderID = b.id where plantID  = #{plantID} and processID = #{processID} ${lineIDFilter} \n" +
              " ) c group by materialID ) d left join sys_material e on d.materialID = e.id ")*/
    @Select("  (select * from (select e.*, f.name as lineName from (  select plantID,processID,lineID,materialID,scheduledStartTime,outputTotal,orderDay,orderHour,classType ,d.name as materialName from (\n" +
            " select a.plantID,a.processID,a.lineID,a.materialID,sum(b.number) as outputTotal,a.scheduledStartTime,orderDay,orderHour, case  when orderHour < '10' then '白班'  when orderHour > '16' then '夜班' end as classType from (  " +
            " select id,plantID,processID,lineID,materialID,scheduledStartTime,date_format(scheduledStartTime,'%Y-%m-%d') as orderDay,date_format(scheduledStartTime,'%H') as orderHour  \n" +
            " from tb_workorder  where plantID  = #{plantID} and processID = #{processID} and scheduledStartTime  >= #{startTime} and scheduledStartTime < #{endTime} ) a \n" +
            " left join  tb_materialrecord b on a.id = b.orderID group by lineID,materialID,scheduledStartTime ) c left join sys_material d on c.materialID = d.id ) e left join sys_productionline f on e.lineID = f.id ) g order by g.scheduledStartTime,g.lineName limit 1000)\n" +
            " UNION ALL\n" +
            " ( select 'plantID' as plantID,'processID' as processID,'lineID' as lineID,'materialID' as materialID,'' as scheduledStartTime, sum(g.outputTotal) as outputTotal,'' as orderDay,'' as orderHour , '' as classType,g.materialName,'总计' as lineName from (\n" +
            "  select e.*, f.name as lineName from (  select plantID,processID,lineID,materialID,outputTotal ,d.name as materialName from (\n" +
            " select a.plantID,a.processID,a.lineID,a.materialID,sum(b.number) as outputTotal from (  select id,plantID,processID,lineID,materialID  \n" +
            " from tb_workorder  where plantID  = #{plantID} and processID = #{processID} and scheduledStartTime  >= #{startTime} and scheduledStartTime < #{endTime} ) a \n" +
            " left join  tb_materialrecord b on a.id = b.orderID group by lineID,materialID ) c left join sys_material d on c.materialID = d.id ) e left join sys_productionline f on e.lineID = f.id ) g  group by g.materialName )\n" )
    List<Map<String, String>> orderOutputStatistics(String startTime,String endTime,String plantID,String processID,@Param("lineIDFilter") String lineID );

    @Select("select d.remnantTotalNum,e.name from ( select sum(number) as remnantTotalNum,materialID from \n" +
            "(\n" +
            "select a.materialID,b.plantID,b.lineID,b.processID,a.number,a.inputTime,a.inOrOut from ( select * from tb_materialrecord where inputTime > #{startTime} and inputTime < #{endTime} ) a\n" +
            " left join tb_workorder b on a.orderID = b.id where inOrOut = '1' and plantID  = #{plantID} and processID = #{processID} ${lineIDFilter} \n" +
            " ) c group by materialID ) d left join sys_material e on d.materialID = e.id ")
    List<Map<String, String>> orderRemnantProductStatistics(String startTime,String endTime,String plantID,String processID,@Param("lineIDFilter") String lineID );

    /*
    select a.materialID,a.plantID,b.number,a.scheduledStartTime
    from ( SELECT * FROM ilpsdb.tb_workorder where plantID = '3' and scheduledStartTime > '2019-02-02' and scheduledStartTime <= '2019-02-20' )
    a left join tb_materialrecord b on a.id = b.orderID
    */
    @Select("select sum(b.number) \n" +
            "    from ( SELECT * FROM ilpsdb.tb_workorder where plantID = #{plantID} and materialID = #{batteryType} and scheduledStartTime >= #{startTime} and scheduledStartTime <=  #{endTime} and processID = #{processID} )\n" +
            "    a left join tb_materialrecord b on a.id = b.orderID")
    Object getJSProcessBatteryProduction(String startTime,String endTime,String plantID,String processID,String batteryType);

    @Select("SELECT sum(shipmentNum) FROM tb_batteryshipmentnumrecord  where plantID = #{plantID} and materialID = #{batteryType}" +
            " and shipmentTime >= #{startTime} and shipmentTime <=  #{endTime}")
    Object getBatteryShipmentNum(String startTime,String endTime,String plantID,String batteryType);


    @Select("(select 'id' as id,p.lineID,q.name,'' as grantNum, p.expendNum from (select m.lineID,n.materialID,sum(n.number) as expendNum from ( select id,lineID from tb_workorder where  scheduledStartTime >= #{startTime}\n" +
            "    and  scheduledStartTime <=  #{endTime} and plantID = #{plantID} and processID = #{processID} and status < '5' ) m left join tb_materialrecord n on m.id = n.expendOrderID group by m.lineID,n.materialID) p \n" +
            "    left join sys_material q on p.materialID = q.id limit 1000) UNION ALL  " +
            " ( select c.id,'总计' as lineID,c.name,c.grantNum,d.expendNum from (\n" +
            "select a.id,a.name,b.grantNum from (\n" +
            "select id,name from sys_material where typeID in (\n" +
            "select materialTypeID from sys_processmaterial where processID = #{processID} and inOrout = '1') )  a\n" +
            "left join ( select batteryType,sum(number) as grantNum from tb_grantmaterialrecord where plantID = #{plantID} and processID = #{lastProcessID} \n" +
            "and grantTime >= #{startTime} and  grantTime <= #{endTime} group by batteryType) b on a.id = b.batteryType ) c left join (\n" +
            "select materialID,sum(number) as expendNum from tb_materialrecord where  expendOrderID in (select id from tb_workorder where  scheduledStartTime >= #{startTime} \n" +
            " and  scheduledStartTime <= #{endTime} and plantID = #{plantID} and processID = #{processID} )  group by materialID ) d on c.id = d.materialID where c.grantNum + d.expendNum >0 )")
    List<Map<Object, Object>> grantAndExpendStatistics(  String startTime,String endTime,String plantID,String processID,String lastProcessID );

//    @Select("( select '' as id, '' as plantID,'' as processID,'' as materialID,'总计' as currentNum,'' as lastStorage,sum(gainNum) as gainNum,sum(inNum) as inNum,sum(expendNum) as expendNum,sum(outNum) as outNum," +
//            "'' as updateTime,sum(onlineNum) as onlineNum,sum(todayRepair) as todayRepair,name,'' as remark\n" +
//            "from ( select plantID,processID,materialID,currentNum,lastStorage,gainNum,inNum,expendNum,outNum,date_format(updateTime,'%Y-%m-%d %H:%i') as updateTime,onlineNum,todayRepair,name from (\n" +
//            "  select plantID,processID,materialID,currentNum,lastStorage,gainNum,inNum,expendNum,outNum,updateTime,onlineNum,todayRepair  from tb_materialsecondaryinventoryrecord \n" +
//            "   where plantID = #{plantID} and processID = #{processID} and updateTime >= #{startTime} and updateTime < #{endTime}\n" +
//            "    ) a left join  sys_material b on a.materialID = b.id order by updateTime desc,name ) d group by materialID limit 1000)\n" +
//            " union all\n" +
//            " (select  a.id, plantID,processID,materialID,currentNum,lastStorage,gainNum,inNum,expendNum,outNum,date_format(updateTime,'%Y-%m-%d %H:%i') as updateTime,onlineNum,todayRepair,name ,a.remark from (\n" +
//            "select id, plantID,processID,materialID,currentNum,lastStorage,gainNum,inNum,expendNum,outNum,updateTime,onlineNum,todayRepair,remark  from tb_materialsecondaryinventoryrecord \n" +
//            "where plantID = #{plantID} and processID = #{processID} and updateTime >= #{startTime} and updateTime < #{endTime}\n" +
//            ") a left join  sys_material b on a.materialID = b.id order by updateTime desc,name)")
//    List<Map<Object, Object>> getSecondaryMaterialInventoryStatistics(  String startTime,String endTime,String plantID,String processID);

    @Select( " (select  a.id, plantID,processID,materialID,currentNum,lastStorage,gainNum,inNum,expendNum,outNum,date_format(updateTime,'%Y-%m-%d %H:%i') as updateTime,onlineNum,todayRepair,name ,a.remark,a.extend1,a.extend2 from (\n" +
            "select id, plantID,processID,materialID,currentNum,lastStorage,gainNum,inNum,expendNum,outNum,updateTime,onlineNum,todayRepair,remark,extend1,extend2  from tb_materialsecondaryinventoryrecord \n" +
            "where plantID = #{plantID} and processID = #{processID} and updateTime >= #{startTime} and updateTime < #{endTime}\n" +
            ") a left join  sys_material b on a.materialID = b.id order by updateTime desc,name)")
    List<Map<Object, Object>> getSecondaryMaterialInventoryStatistics(  String startTime,String endTime,String plantID,String processID);

    @Select("select a.id,plantID,processID,materialID,currentNum,lastStorage,productionNum,inNum,expendNum,outNum,date_format(updateTime,'%Y-%m-%d %H:%i')  as updateTime,name,a.remark, a.extend1,a.extend2,a.extend5  from (\n" +
            "select id,plantID,processID,materialID,currentNum,lastStorage,productionNum,inNum,expendNum,outNum,updateTime,remark,extend1,extend2,extend5  from tb_materialinventoryrecord \n" +
            "where plantID = #{plantID} and processID = #{processID} and updateTime >= #{startTime} and updateTime <= #{endTime}\n" +
            ") a left join  sys_material b on a.materialID = b.id order by updateTime desc,name")
    List<Map<Object, Object>> getMaterialInventoryStatistics(  String startTime,String endTime,String plantID,String processID);

    @Update("update ${tableName} set ${updateStr} where id= #{id}")
    int updateMaterialInventoryData(@Param("tableName") String tableName,@Param("updateStr")String updateStr,String id);

    @Select("(select  '总计' as orderName,sum(number) as number,CONCAT('二维码数量',count(1)) as operator ,'' as grantTime ,name,'' as usedstatus,acceptPlantID from (  select orderName,number,operator,date_format(grantTime,'%Y-%m-%d %H:%i') as grantTime,name,acceptPlantID from (\n" +
            "select batteryType,orderName,number,operator,grantTime,acceptPlantID from tb_grantmaterialrecord \n" +
            "where plantID = #{plantID} and processID = #{processID}  and grantTime >= #{startTime}  and grantTime <= #{endTime} ) \n" +
            " a left join  sys_material b on a.batteryType = b.id  ) c group by name,acceptPlantID limit 100)" +
            " union all " +
            " ( select orderName,number,operator,grantTime,name,case inOrOut when '2' then '已投料' else '未投料' end as usedstatus ,acceptPlantID from ( select orderName,number,operator,date_format(grantTime,'%Y-%m-%d %H:%i') as grantTime,name,acceptPlantID from (\n" +
            "select batteryType,orderName,number,operator,grantTime,acceptPlantID from tb_grantmaterialrecord \n" +
            "where plantID = #{plantID} and processID = #{processID}  and grantTime >= #{startTime}  and grantTime <= #{endTime} ) \n" +
            " a left join  sys_material b on a.batteryType = b.id )  m left join ( select id,inOrOut from tb_materialrecord where outputTime > #{startTime}) n on m.orderName = n.id order by grantTime desc limit 1000 )")
    List<Map<Object, Object>> getGrantMaterialRecord(  String startTime,String endTime,String plantID,String processID);

    @Select("select a.*,b.grantTime,b.operator as grantOperator from ( select  *  from tb_materialrecord where subOrderID like '%${subOrderID}%') a left join tb_grantmaterialrecord b on a.subOrderID = b.orderID")
    List<Map<Object, Object>> getMaterialRecordDetailBySubOrderID(@Param("subOrderID") String subOrderID);

    int updateCancelInputSuborder(MaterialRecord record);

    @Update( "update tb_materialrecord set inputWorkLocationID = inputWorkLocationID + 1 where id = #{orderID}")
    int updateJQByBottomScrapNumber(String orderID );
    /**
   	 * 批量出窑使用
     * @param dryingkilnid
     * @return
     */
//    @Update("UPDATE tb_materialrecord t1 SET t1.inOrOut = #{inOrOut} WHERE t1.id IN ( SELECT a.id FROM ( SELECT id FROM tb_materialrecord t WHERE t.suborderID IN "
//            + "( SELECT d.suborderID FROM tb_dryingkilnjzrecord d WHERE d.dryingKilnID = #{dryingkilnid} ) ) AS a )")
//    int updateByDryingkilnid(@Param("dryingkilnid")  String dryingkilnid, @Param("inOrOut") int inOrOut);
    @Update("update tb_materialrecord set  inOrOut = #{inOrOut}  where  inOrOut = '0' and  subOrderID in ( select id from tb_dryingkilnjzrecord where dryingKilnID =  #{dryingkilnid} and status = '1' )" )
    int updateByDryingkilnid(  String dryingkilnid,  int inOrOut);

    @Select("select b.* from ( \n" +
            "(select subOrderID,inputer,DATE_FORMAT(inputTime, '%Y-%m-%d %H:%i:%s') as inputTime,materialNameInfo,inputPlantID,inputProcessID,inputLineID from tb_materialrecord\n" +
            " where subOrderID in ( ${orderList} ) )\n" +
            " union all  \n" +
            " (select id as subOrderID ,recorder1 as inputer,CONCAT( DATE_FORMAT(starttime1, '%Y-%m-%d %H:%i:%s') ,' ——' ,DATE_FORMAT(endtime3, '%Y-%m-%d %H:%i:%s')) as inputTime,\n" +
            " materialName as materialNameInfo,plantID as inputPlantID,'1004' as inputProcessID ,solidifyRoomID as inputLineID from tb_solidifyrecord  \n" +
            " where id in ( ${orderList} ) and endtime3 is not null  order by subOrderID limit 1000 )\n" +
            " ) b left join sys_productionprocess c on b.inputProcessID = c.id order by c.ordinal asc,subOrderID, inputTime")
    List<Map<Object, Object>>  selectByOrderIDList(@Param("orderList") String orderList);

    @Select("select ${columnList} from tb_materialrecord ${filter}")
    List<Map<Object, Object>>  selectByFilter(@Param("columnList") String columnList,@Param("filter") String filter);

    @Select(" select a.inputer,a.subOrderID,a.number,a.inputTime,a.materialNameInfo,round(a.number * b.wage,2) as wage from (\n" +
            "( select inputer,'总计' as subOrderID ,sum(number) as number ,'-' as inputTime,materialNameInfo,materialID from tb_materialrecord  \n" +
            " where inputerID =  #{staffID} and inputTime >= #{startTime} and inputTime < #{endTime} group by materialNameInfo  limit 100) \n" +
            "  union all  " +
            "  ( select inputer,subOrderID,number,inputTime,materialNameInfo,materialID from tb_materialrecord  \n" +
            "  where inputerID = #{staffID} and inputTime >= #{startTime} and inputTime <  #{endTime} order by subOrderID   limit 1000)\n" +
            "  ) a left join sys_material b on a.materialID = b.id")
    List<Map<Object, Object>>  selectSelfProductionRecord( String staffID,  String startTime,String endTime);

    @Select("select subOrderID from tb_materialrecord where inputTime > '2019-07-23'  and orderID like '%BB20190723'")
    List<String> selectByID();

    @Update(" update tb_materialrecord set inOrOut = #{inOrOutStatus} where id =#{recordID}" )
    int updateFinishStatusByID(String recordID,String inOrOutStatus);

    @Update("update tb_materialrecord set inputWorkLocationID = #{numberRemain} where id =#{id} ")
    int updateJQNumber(String id,String numberRemain );
}




