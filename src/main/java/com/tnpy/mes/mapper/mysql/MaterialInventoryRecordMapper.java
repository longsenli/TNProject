package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialInventoryRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MaterialInventoryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialInventoryRecord record);

    int insertSelective(MaterialInventoryRecord record);

    MaterialInventoryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialInventoryRecord record);

    int updateByPrimaryKey(MaterialInventoryRecord record);

    @Insert(" insert into tb_materialinventoryrecord  (id, materialID, plantID, processID, currentNum, lastStorage, updateTime, productionNum,\n" +
            "    inNum, expendNum, outNum, operator, status,onlineStorageInNum,remark)\n" +
            " select  UUID(),g.id ,#{plantID},#{processID},(ifnull(currentNum,0) + ifnull(productionNum,0) + ifnull(onlineInnum,0) - ifnull(grantNum,0)   + ifnull(scrapNum,0) ) as newNum," +
            " ifnull(currentNum,0),now(),ifnull(productionNum,0),ifnull(scrapNum,0),ifnull(grantNum,0),0 ,'system','1',ifnull(onlineInnum,0),'' from (\n" +
            " select e.*,f.currentNum from ( select c.*,d.productionNum from ( select m.*  ,n.number as onlineInnum from ( select a.id,a.name,b.grantNum from (\n" +
            " select id,name from sys_material where typeID in ( select materialTypeID from sys_processmaterial where processID = #{processID} and inOrout = 2) )a left join \n" +
            " ( select batteryType,sum(number) as grantNum from tb_grantmaterialrecord where plantID = #{plantID} and processID = #{processID} \n" +
            "and grantTime > #{lastStatisTime} and  grantTime <= #{endTime} group by batteryType) b on a.id =b.batteryType) m left join ( select materialID,number from tb_materialrecord \n" +
            "where subOrderID in ( select id from  tb_onlinematerialrecord where  updateTime >=  #{startTime}  and  updateTime <= #{endTime}  and  plantID = #{plantID} and processID = #{processID}) ) n on m.id = n.materialID ) c left join \n" +
            "( select materialID,sum(number) as productionNum from tb_materialrecord where  orderID in (select id from tb_workorder where  scheduledStartTime >=  #{startTime} \n" +
            " and  scheduledStartTime < #{endTime} and plantID = #{plantID} and processID = #{processID} and status < '6' )  group by materialID ) d on c.id =d.materialID ) e left join  \n" +
            " ( select materialID,max(currentNum) as currentNum from tb_materialinventoryrecord where plantID = #{plantID} and processID = #{processID} and updateTime >= #{lastStatisTime} and updateTime <= #{startTime} group by  materialID  ) f on e.id = f.materialID ) g left join\n" +
            " ( select materialID,sum(value) as scrapNum from tb_workorderscrapinfo where orderID in (select id from tb_workorder where  scheduledStartTime >=  #{startTime} \n" +
            " and  scheduledStartTime < #{endTime} and plantID = #{plantID} and processID = #{nextProcessID} ) group by materialID ) h on g.id = h.materialID where (ifnull(currentNum,0) + ifnull(productionNum,0)  + ifnull(grantNum,0)   + ifnull(scrapNum,0) + ifnull(onlineInnum,0)) != 0")
    int insertZHInventoryStatistics( String startTime,String endTime,String plantID,String processID,String nextProcessID,String lastStatisTime);

    @Insert(" insert into tb_materialinventoryrecord (id, materialID, plantID, processID, currentNum, lastStorage, updateTime, productionNum,\n" +
            "    inNum, expendNum, outNum, operator, status, onlineStorageInNum,remark)\n" +
            " select  UUID(),g.id ,#{plantID},#{processID},(ifnull(currentNum,0) + ifnull(productionNum,0) + ifnull(onlineInnum,0) - ifnull(grantNum,0)   + ifnull(scrapNum,0) ) as newNum,ifnull(currentNum,0)," +
            " now(),ifnull(productionNum,0),ifnull(scrapNum,0),ifnull(grantNum,0),0 ,'system','1',ifnull(onlineInnum,0),'' from (\n" +
            " select e.*,f.currentNum from ( select c.*,d.productionNum from ( select m.*  ,n.number as onlineInnum from ( select a.id,a.name,b.grantNum from (\n" +
            " select id,name from sys_material where typeID in ( select materialTypeID from sys_processmaterial where processID = #{processID} and inOrout = 2) )a left join \n" +
            " ( select batteryType,sum(number) as grantNum from tb_grantmaterialrecord where plantID = #{plantID} and processID = #{processID} \n" +
            "and grantTime > #{lastStatisTime} and  grantTime <= #{endTime} group by batteryType) b on a.id =b.batteryType) m left join ( select materialID,number from tb_materialrecord \n" +
            "where subOrderID in ( select id from  tb_onlinematerialrecord where  updateTime >=  #{startTime}  and  updateTime <= #{endTime}  and  plantID = #{plantID} and processID = #{processID}) ) n on m.id = n.materialID ) c left join \n" +
            "( select materialID,sum(number) as productionNum from tb_materialrecord where  orderID in (select id from tb_workorder where  scheduledStartTime >=  #{startTime} \n" +
            " and  scheduledStartTime < #{endTime} and plantID = #{plantID} and processID = #{processID}  and status < '6' )  group by materialID ) d on c.id =d.materialID ) e left join  \n" +
            " ( select materialID,max(currentNum) as currentNum from tb_materialinventoryrecord where plantID = #{plantID} and processID = #{processID} and updateTime >= #{lastStatisTime} and updateTime <= #{startTime} group by  materialID  ) f on e.id = f.materialID ) g left join\n" +
            " ( select materialID,sum(materialNumber) as scrapNum from tb_unqualifiedmaterialreturn where returnTime >  #{startTime} \n" +
            " and  returnTime < #{endTime} and inputPlantID = #{plantID} and inputProcessID = #{processID} group by materialID ) h on g.id = h.materialID where (ifnull(currentNum,0) + ifnull(productionNum,0)  + ifnull(grantNum,0)   + ifnull(scrapNum,0) + ifnull(onlineInnum,0)) != 0")
    int insertJZInventoryStatistics( String startTime,String endTime,String plantID,String processID,String nextProcessID,String lastStatisTime);


    @Insert(" insert into tb_materialinventoryrecord  (id, materialID, plantID, processID, currentNum, lastStorage, updateTime, productionNum,\n" +
            "    inNum, expendNum, outNum, operator, status,onlineStorageInNum,remark)\n" +
            " select  UUID(),g.id ,#{plantID},#{processID},(ifnull(currentNum,0) + ifnull(productionNum,0) + ifnull(onlineInnum,0) - ifnull(grantNum,0)   + ifnull(scrapNum,0) ) as newNum," +
            " ifnull(currentNum,0),now(),ifnull(productionNum,0),ifnull(scrapNum,0),ifnull(grantNum,0),0 ,'system','1',ifnull(onlineInnum,0),'' from (\n" +
            " select e.*,f.currentNum from ( select c.*,d.productionNum from ( select m.*  ,n.number as onlineInnum from ( select a.id,a.name,b.grantNum from (\n" +
            " select id,name from sys_material where typeID in ( select materialTypeID from sys_processmaterial where processID = #{processID} and inOrout = 2) )a left join \n" +
            " ( select batteryType,sum(number) as grantNum from tb_grantmaterialrecord where plantID = #{plantID} and processID = #{processID} \n" +
            "and grantTime >= #{lastStatisTime} and  grantTime <= #{endTime} group by batteryType) b on a.id =b.batteryType) m left join ( select materialID,number from tb_materialrecord \n" +
            "where subOrderID in ( select id from  tb_onlinematerialrecord where  updateTime >=  #{startTime}  and  updateTime <= #{endTime}  and  plantID = #{plantID} and processID = #{processID}) ) n on m.id = n.materialID ) c left join \n" +
            "( select materialID,sum(number) as productionNum from tb_materialrecord where  orderID in (select id from tb_workorder where  scheduledStartTime >=  #{startTime} \n" +
            " and  scheduledStartTime < #{endTime} and plantID = #{plantID} and processID = #{processID} and status < '6')  group by materialID ) d on c.id =d.materialID ) e left join  \n" +
            " ( select materialID,max(currentNum) as currentNum from tb_materialinventoryrecord where plantID = #{plantID} and processID = #{processID} and updateTime >= #{lastStatisTime} and updateTime <= #{startTime} group by  materialID  ) f on e.id = f.materialID ) g left join\n" +
            " ( select materialID,sum(value) as scrapNum from tb_workorderscrapinfo where orderID in (select id from tb_workorder where  scheduledStartTime >=  #{startTime} \n" +
            " and  scheduledStartTime < #{endTime} and plantID = #{plantID} and processID = #{nextProcessID} ) group by materialID ) h on g.id = h.materialID where (ifnull(currentNum,0) + ifnull(productionNum,0)  + ifnull(grantNum,0)   + ifnull(scrapNum,0) + ifnull(onlineInnum,0)) != 0")
    int insertTBInventoryStatistics( String startTime,String endTime,String plantID,String processID,String nextProcessID,String lastStatisTime);

    //  只有入库和消耗，对于不发料的工序，productionNum = 产量，expendNum = 投料消耗掉的，
    @Insert(" insert into tb_materialinventoryrecord (id,materialID,plantID,processID,currentNum,lastStorage,updateTime,productionNum, inNum, expendNum, outNum, operator, status,extend6) \n" +
            " select uuid(),id,#{plantID},#{processID},currentNum + totalIn - totalOut,totalOut,now(),totalIn,0,totalOut,0,'system','1',name from (\n" +
            " select p.id,p.name,ifnull(p.totalIn,0) as totalIn ,ifnull(p.totalOut,0) as totalOut,ifnull(q.currentNum,0) as currentNum from (\n" +
            " select m.id,m.name,sum(productionNumber) as totalIn,sum(outputNumber) as totalOut from (\n" +
            " select id,name from sys_material where typeID in ( select materialTypeID from sys_processmaterial where processID = #{processID} and inOrout = 2) ) m left join  \n" +
            "(  ( select  materialID,  sum(number) as productionNumber,0 as outputNumber  from (\n" +
            " select id from tb_workorder where  scheduledStartTime >=  #{startTime} and  scheduledStartTime <#{endTime} and plantID = #{plantID} and processID = #{processID} and status < '6' ) \n" +
            " a left join tb_materialrecord b on a.id = b.orderID where materialID is not null  group by materialID ) union all\n" +
            " ( select  materialID, 0 as productionNumber, sum(number) as outputNumber   from tb_materialrecord where\n" +
            " outputTime >=  #{startTime}  and  outputTime < #{endTime}  and inputPlantID = #{plantID} and inputProcessID = #{processID} group by materialID )\n" +
            " )  n on m.id = n.materialID    group by m.id ) p left join ( select materialID,max(currentNum) as currentNum from tb_materialinventoryrecord\n" +
            " where plantID = #{plantID} and processID = #{processID} and updateTime >=  #{lastStatisTime}  \n" +
            "and updateTime <=  #{startTime} group by  materialID) q on p.id = q.materialID ) y where totalIn + totalOut +currentNum <> 0")
    int insertFBInventoryStatistics( String startTime,String endTime,String plantID,String processID,String nextProcessID,String lastStatisTime);

    //涂板只计算产量
    @Insert(" insert into tb_materialinventoryrecord (id,materialID,plantID,processID,updateTime,productionNum, operator, status,extend6) \n" +
            "select uuid(),id,#{plantID}, #{processID},now(),totalIn,'system','1',name from (\n" +
            "select m.id,m.name,sum(productionNumber) as totalIn,sum(outputNumber) as totalOut from (\n" +
            "select id,name from sys_material where typeID in ( select materialTypeID from sys_processmaterial where processID =   #{processID} and inOrout = 2) ) m left join  \n" +
            "  ( select  materialID,  sum(number) as productionNumber,0 as outputNumber  from (\n" +
            "select id from tb_workorder where  scheduledStartTime >=    #{startTime}  and  scheduledStartTime < #{endTime} and plantID = #{plantID} and processID = #{processID} and status < '6' ) \n" +
            "a left join tb_materialrecord b on a.id = b.orderID where materialID is not null  group by materialID ) n on m.id = n.materialID group by m.id ) y where totalIn + totalOut  <> 0   ")
    int insertTBNewInventoryStatistics( String startTime,String endTime,String plantID,String processID,String nextProcessID,String lastStatisTime);


    //固化室显示当前在库数，当日入库数、当日出库数
    @Insert(" insert into tb_materialinventoryrecord (id,materialID,plantID,processID,currentNum,lastStorage,updateTime,productionNum, inNum, expendNum, outNum, operator, status,extend6)\n" +
            "select uuid(),materialID,#{plantID}, #{processID},remainNumber,remainTS,now(),totalInNumber,inTS,totalOutNumber,outTS,'system','1',materialName from (\n" +
            "select materialID,materialName,sum(remainNumber) as remainNumber,sum(remainTS) as remainTS,sum(totalInNumber) as totalInNumber,sum(inTS) as inTS,sum(totalOutNumber) as totalOutNumber,sum(outTS) as outTS from (\n" +
            "( SELECT materialID,materialName,sum(productionNum) as remainNumber,count(1) as remainTS,0 as totalInNumber, 0 as inTS,0 as totalOutNumber,0 as outTS  \n" +
            "FROM tb_solidifyrecord where plantID = #{plantID} and status < '9' group by materialID,materialName) union all\n" +
            "(SELECT materialID,materialName,0 as remainNumber,0 as remainTS, sum(productionNum) as totalInNumber,count(1) as inTS ,0 as totalOutNumber,0 as outTS \n" +
            " FROM tb_solidifyrecord where plantID = #{plantID} and  starttime1> #{startTime} and  starttime1 < #{endTime} group by materialID,materialName) union all\n" +
            "(SELECT materialID,materialName,0 as remainNumber,0 as remainTS,0 as totalInNumber, 0 as inTS,sum(productionNum) as totalOutNumber,count(1) as outTS \n" +
            "FROM tb_solidifyrecord where plantID =#{plantID} and  endtime3> #{startTime} \n" +
            "and  endtime3 <  #{endTime} group by materialID,materialName)  ) a group by materialID,materialName ) b where remainNumber + totalInNumber + totalOutNumber > 0  ")
    int insertGHNewInventoryStatistics( String startTime,String endTime,String plantID,String processID,String nextProcessID,String lastStatisTime);

    //更新周期浇铸、固化室、极群、装配；分板的在包板二级库存中查看，电池在包装二级库存
    @Insert("update tb_materialinventoryrecord t1,tb_planproductionrecord t2 set t1.extend1 = t2.planDailyProduction,t1.extend2 = t1.currentNum/ifnull(t2.planDailyProduction,1) \n" +
            "where t1.materialID = t2.materialID and t1.plantID = t2.plantID and t1.updateTime > #{startTime} and t2.planMonth = #{monthStr} and t1.processID in ('1011','1006','1007','1004') ")
    int updateZQData( String startTime,String monthStr);

    //更新周期浇铸、固化室、极群、装配；分板的在包板二级库存中查看，电池在包装二级库存
    @Insert("update tb_materialsecondaryinventoryrecord t1,tb_planproductionrecord t2 set t1.extend1 = t2.planDailyProduction,t1.extend2 = t1.currentNum/ifnull(t2.planDailyProduction,1) ,t1.extend6 = t2.materialName \n" +
            "where t1.materialID = t2.materialID and t1.plantID = t2.plantID and t1.updateTime > #{startTime} and t2.planMonth = #{monthStr} and t1.processID in ('1005','1006','1012') ")
    int updateEJKCZQData( String startTime,String monthStr);


}