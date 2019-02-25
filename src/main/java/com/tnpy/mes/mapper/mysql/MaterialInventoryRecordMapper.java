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

    @Insert(" insert into tb_materialinventoryrecord\n" +
            " select  UUID(),g.id ,#{plantID},#{processID},(ifnull(currentNum,0) + ifnull(productionNum,0)  - ifnull(grantNum,0)   + ifnull(scrapNum,0) ) as newNum,ifnull(currentNum,0),now(),ifnull(productionNum,0),ifnull(scrapNum,0),ifnull(grantNum,0),0 ,'system','1' from (\n" +
            " select e.*,f.currentNum from ( select c.*,d.productionNum from ( select a.id,a.name,b.grantNum from (\n" +
            " select id,name from sys_material where typeID in ( select materialTypeID from sys_processmaterial where processID = #{processID} and inOrout = 2) )a left join \n" +
            " ( select batteryType,sum(number) as grantNum from tb_grantmaterialrecord where plantID = #{plantID} and processID = #{processID} \n" +
            "and grantTime >= #{startTime} and  grantTime <= #{endTime} group by batteryType) b on a.id =b.batteryType) c left join \n" +
            "( select materialID,sum(number) as productionNum from tb_materialrecord where  orderID in (select id from tb_workorder where  scheduledStartTime >=  #{startTime} \n" +
            " and  scheduledStartTime <= #{endTime} and plantID = #{plantID} and processID = #{processID} )  group by materialID ) d on c.id =d.materialID ) e left join  \n" +
            " ( select materialID,currentNum from tb_materialinventoryrecord where plantID = #{plantID} and processID = #{processID} order by updateTime desc limit 1 ) f on e.id = f.materialID ) g left join\n" +
            " ( select materialID,sum(value) as scrapNum from tb_workorderscrapinfo where orderID in (select id from tb_workorder where  scheduledStartTime >=  #{startTime} \n" +
            " and  scheduledStartTime <= #{endTime} and plantID = #{plantID} and processID = #{nextProcessID} ) group by materialID ) h on g.id = h.materialID")
    int insertZHInventoryStatistics( String startTime,String endTime,String plantID,String processID,String nextProcessID );
}