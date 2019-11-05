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

    //包板二级库存 ： 库存 = 上次结余 + 固化出库 + 借调 - 报废 -借出 - 极群消耗，统一规整为小片型号
    @Insert("insert into tb_materialsecondaryinventoryrecord (id,materialID,plantID,processID,currentNum,lastStorage,updateTime,gainNum,inNum,expendNum,outNum,todayRepair,operator,status)\n" +
            "select uuid(),materialID,plantID,'1006',currentNum +outNumber -scrapNumber + borrowInNumber-borrowOutNumber - expendNumber,currentNum,now(),borrowInNumber,outNumber,expendNumber,borrowOutNumber,scrapNumber,'system','1' from (\n" +
            "select materialID,plantID,sum(currentNum) as currentNum,sum(outNumber) as outNumber, sum(scrapNumber) as scrapNumber,sum(borrowInNumber) as borrowInNumber,sum(borrowOutNumber) as borrowOutNumber,sum(expendNumber) as expendNumber from (\n" +
            "select ifnull(c.outMaterialID,b.materialID ) as materialID,b.plantID,b.currentNum, ifnull(c.rate,1) * b.outNumber as outNumber , ifnull(c.rate,1) * b.scrapNumber as scrapNumber,\n" +
            " ifnull(c.rate, 1) *b.borrowInNumber as borrowInNumber, ifnull(c.rate,1) *b.borrowOutNumber as borrowOutNumber, ifnull(c.rate,1) *b.expendNumber as expendNumber from ( \n" +
            "(select materialID,plantID,currentNum,0 as outNumber,0 as scrapNumber ,0 as borrowInNumber,0 as borrowOutNumber,0 as expendNumber from tb_materialsecondaryinventoryrecord \n" +
            "where updateTime > #{lastInventoryTime} and updateTime <  #{startTime} and processID = '1006' group by materialID,plantID)\n" +
            "union all\n" +
            "(select materialID,plantID, 0 as currentNum,sum(productionNum)  as outNumber,0 as scrapNumber ,0 as borrowInNumber,0 as borrowOutNumber,0 as expendNumber   from tb_solidifyrecord  \n" +
            "where endtime3 > #{startTime} and endtime3 < #{endTime} group by materialID,plantID)\n" +
            "union all\n" +
            "(select materialID,plantID, 0 as currentNum,0 as outNumber,sum(value) as scrapNumber,0 as borrowInNumber,0 as borrowOutNumber,0 as expendNumber  from tb_materialscraprecord \n" +
            "where  updateTime > #{startTime} and updateTime <#{endTime} and processID in ('1004','1005','1006') group by materialID,plantID)\n" +
            "union all\n" +
            "(SELECT batteryType as materialID,acceptPlantID as plantID, 0 as currentNum, 0 as outNumber,0 as scrapNumber, sum(number) as borrowInNumber,0 as borrowOutNumber,0 as expendNumber   FROM tb_grantmaterialrecord \n" +
            "where grantTime > #{startTime} and grantTime < #{endTime} and acceptPlantID != plantID and processID in ('1004','1005','1003') group by acceptPlantID,batteryType )\n" +
            "union all\n" +
            "(select materialID,inputPlantID as plantID,0 as currentNum, 0 as outNumber,0 as scrapNumber,0 as borrowInNumber,0 as borrowOutNumber,sum(number) as expendNumber  \n" +
            "from tb_materialrecord where inputProcessID = '1006' and orderID like #{dayString}  and status = '1' group by materialID,inputPlantID)\n" +
            "union all\n" +
            "(SELECT  batteryType as materialID,acceptPlantID as plantID, 0 as currentNum,0 as outNumber,0 as scrapNumber, 0 as borrowInNumber,sum(number) as borrowOutNumber,0 as expendNumber  FROM tb_grantmaterialrecord \n" +
            "where grantTime > #{startTime} and grantTime < #{endTime} and acceptPlantID != plantID and processID in ('1004','1005','1003') group by plantID,batteryType) \n" +
            " ) b left join tb_materialchangerelation c on b.materialID = c.inMaterialID and b.plantID = c.plantID\n" +
            " ) a group by  plantID,materialID ) d ")
    int insertBBSecondaryInventory(  String startTime,String endTime,String dayString,String lastInventoryTime);
}