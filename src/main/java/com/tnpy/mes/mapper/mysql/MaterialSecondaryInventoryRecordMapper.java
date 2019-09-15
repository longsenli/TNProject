package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialSecondaryInventoryRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MaterialSecondaryInventoryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialSecondaryInventoryRecord record);

    int insertSelective(MaterialSecondaryInventoryRecord record);

    MaterialSecondaryInventoryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialSecondaryInventoryRecord record);

    int updateByPrimaryKey(MaterialSecondaryInventoryRecord record);

    @Insert("insert into tb_materialsecondaryinventoryrecord (id, materialID, plantID, processID, currentNum, lastStorage, updateTime, gainNum, \n" +
            "    inNum, expendNum, outNum, operator, status, onlineNum, todayRepair,remark ) \n" +
            "select UUID(),g.id,#{plantID},#{processID},(ifnull(currentNum,0) +ifnull(grantNum,0)  -ifnull(expendNum,0)   +ifnull(todayAdd,0) + ifnull(mergeNum,0)  - ifnull(repairNum,0) ) as newNum,ifnull(currentNum,0),now(),ifnull(grantNum,0) ,0," +
            "ifnull(expendNum,0),ifnull(repairNum,0) ,'system','1',ifnull(onlineNum,0),ifnull(mergeNum,0),''  from (\n" +
            "select e.*,f.currentNum from ( select c.id,c.name,c.grantNum,d.expendNum from ( select a.id,a.name,b.grantNum from (\n" +
            "select id,name from sys_material where typeID in ( select materialTypeID from sys_processmaterial where processID = #{processID} and inOrout = '1') )  a\n" +
            "left join ( select batteryType,sum(number) as grantNum from tb_grantmaterialrecord where plantID =#{plantID} and processID = #{lastProcessID} \n" +
            "and grantTime >= #{startTime} and  grantTime < #{endTime} group by batteryType) b on a.id = b.batteryType ) c left join (\n" +
            "select materialID,sum(number) as expendNum from tb_materialrecord where  expendOrderID in (select id from tb_workorder where  scheduledStartTime >= #{startTime}\n" +
            " and  scheduledStartTime < #{endTime} and plantID = #{plantID} and processID = #{processID} )  group by materialID ) d on c.id = d.materialID ) e left join\n" +
            "( select materialID,max(currentNum) as currentNum from tb_materialsecondaryinventoryrecord where plantID = #{plantID} and processID =  #{processID}  and updateTime >= #{lastStatisTime} and updateTime < #{startTime} group by  materialID  ) f on e.id = f.materialID ) g left join \n" +
            "( select materialID,sum(CASE status WHEN '1' THEN sumNum ELSE 0 END ) 'onlineNum', sum(CASE status WHEN '2' THEN sumNum ELSE 0 END ) 'mergeNum', sum(CASE status WHEN '3' THEN sumNum ELSE 0 END ) 'repairNum',sum(CASE status WHEN '5' THEN sumNum ELSE 0 END ) 'todayAdd' from (\n" +
            " (select materialID,sum(materialNum) as sumNum,CASE status WHEN '1' THEN '5' ELSE status END as status from tb_onlinematerialrecord where  plantID = #{plantID} and processID = #{processID} and updateTime >= #{startTime} and updateTime < #{endTime}  group by status,materialID )" +
            "union all ( select materialID,sum(materialNum) as sumNum,status from tb_onlinematerialrecord where  plantID = #{plantID} and processID = #{processID}  and status = '1' group by status,materialID  ) ) a GROUP BY materialID\n" +
            " ) h on g.id = h.materialID where (ifnull(currentNum,0) + ifnull(expendNum,0)  + ifnull(grantNum,0)   + + ifnull(onlineNum,0)  + ifnull(mergeNum,0)  + ifnull(repairNum,0)) != 0")
    int insertJSSecondaryInventory( String startTime,String endTime,String plantID,String processID,String lastProcessID ,String lastStatisTime);

}