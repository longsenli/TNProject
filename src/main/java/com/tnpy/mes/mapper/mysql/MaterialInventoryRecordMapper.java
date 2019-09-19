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
            "and grantTime >= #{startTime} and  grantTime <= #{endTime} group by batteryType) b on a.id =b.batteryType) m left join ( select materialID,number from tb_materialrecord \n" +
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
            "and grantTime >= #{startTime} and  grantTime <= #{endTime} group by batteryType) b on a.id =b.batteryType) m left join ( select materialID,number from tb_materialrecord \n" +
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
            "and grantTime >= #{startTime} and  grantTime <= #{endTime} group by batteryType) b on a.id =b.batteryType) m left join ( select materialID,number from tb_materialrecord \n" +
            "where subOrderID in ( select id from  tb_onlinematerialrecord where  updateTime >=  #{startTime}  and  updateTime <= #{endTime}  and  plantID = #{plantID} and processID = #{processID}) ) n on m.id = n.materialID ) c left join \n" +
            "( select materialID,sum(number) as productionNum from tb_materialrecord where  orderID in (select id from tb_workorder where  scheduledStartTime >=  #{startTime} \n" +
            " and  scheduledStartTime < #{endTime} and plantID = #{plantID} and processID = #{processID} and status < '6')  group by materialID ) d on c.id =d.materialID ) e left join  \n" +
            " ( select materialID,max(currentNum) as currentNum from tb_materialinventoryrecord where plantID = #{plantID} and processID = #{processID} and updateTime >= #{lastStatisTime} and updateTime <= #{startTime} group by  materialID  ) f on e.id = f.materialID ) g left join\n" +
            " ( select materialID,sum(value) as scrapNum from tb_workorderscrapinfo where orderID in (select id from tb_workorder where  scheduledStartTime >=  #{startTime} \n" +
            " and  scheduledStartTime < #{endTime} and plantID = #{plantID} and processID = #{nextProcessID} ) group by materialID ) h on g.id = h.materialID where (ifnull(currentNum,0) + ifnull(productionNum,0)  + ifnull(grantNum,0)   + ifnull(scrapNum,0) + ifnull(onlineInnum,0)) != 0")
    int insertTBInventoryStatistics( String startTime,String endTime,String plantID,String processID,String nextProcessID,String lastStatisTime);
}