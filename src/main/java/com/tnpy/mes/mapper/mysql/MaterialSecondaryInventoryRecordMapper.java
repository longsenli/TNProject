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

    @Insert("insert into tb_materialsecondaryinventoryrecord \n" +
            "select UUID(),g.id,#{plantID},#{processID},(ifnull(currntNum,0) +ifnull(grantNum,0)  -ifnull(expendNum,0)   -ifnull(scrapNum,0) ) as newNum,g.currntNum,now(),g.grantNum,0,g.expendNum,h.scrapNum ,'system','1' from (\n" +
            "select e.*,f.currntNum from ( select c.id,c.name,c.grantNum,d.expendNum from ( select a.id,a.name,b.grantNum from (\n" +
            "select id,name from sys_material where typeID in ( select materialTypeID from sys_processmaterial where processID = #{processID} and inOrout = '1') )  a\n" +
            "left join ( select batteryType,sum(number) as grantNum from tb_grantmaterialrecord where plantID =#{plantID} and processID = #{lastProcessID} \n" +
            "and grantTime >= #{startTime} and  grantTime <= #{endTime} group by batteryType) b on a.id = b.batteryType ) c left join (\n" +
            "select materialID,sum(number) as expendNum from tb_materialrecord where  expendOrderID in (select id from tb_workorder where  scheduledStartTime >= #{startTime}\n" +
            " and  scheduledStartTime <= #{endTime} and plantID = #{plantID} and processID = #{processID} )  group by materialID ) d on c.id = d.materialID ) e left join\n" +
            "( select materialID,currntNum from tb_materialsecondaryinventoryrecord where plantID = #{plantID} and processID =  #{processID} order by updateTime desc limit 1 ) f on e.id = f.materialID ) g left join \n" +
            "( select materialID,sum(value) as scrapNum from tb_workorderscrapinfo where orderID in (select id from tb_workorder where  scheduledStartTime >= #{startTime}\n" +
            " and  scheduledStartTime <= #{endTime} and plantID = #{plantID} and processID = #{processID} ) group by materialID ) h on g.id = h.materialID")
    int insertJSSecondaryInventory( String startTime,String endTime,String plantID,String processID,String lastProcessID );
}