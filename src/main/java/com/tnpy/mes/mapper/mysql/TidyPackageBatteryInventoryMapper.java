package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TidyPackageBatteryInventory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TidyPackageBatteryInventoryMapper {
    int deleteByPrimaryKey(String id);

    int insert(TidyPackageBatteryInventory record);

    int insertSelective(TidyPackageBatteryInventory record);

    TidyPackageBatteryInventory selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TidyPackageBatteryInventory record);

    int updateByPrimaryKey(TidyPackageBatteryInventory record);

    @Select("select * from tb_tidypackagebatteryinventory ${filter}")
    List<TidyPackageBatteryInventory> selectByFilter(@Param("filter") String filter);

    @Insert("\n" +
            "insert into tb_tidypackagebatteryinventory  (id,plantID,materialID,materialName,currentTotalNum,onTidyingNum,pileTotalNum,backChargeNum,repairNewNum,backChargeNewNum,pipeNewNum,putonNum,packageNewNum,checkTime,remark,pulloffNum)\n" +
            "select uuid(),plantID,materialID,materialName,totalOnTidying +ifnull(pileTotalNum,0) + ifnull(pileNum,0) -ifnull(packageNum,0) + ifnull(backChargeNum,0) + ifnull(backChargeNewNum,0) - ifnull(putonBackNum,0),totalOnTidying,\n" +
            "ifnull(pileTotalNum,0) + ifnull(pileNum,0) -ifnull(packageNum,0) , ifnull(backChargeNum,0) + ifnull(backChargeNewNum,0) - ifnull(putonBackNum,0), ifnull(repairNewNum,0),ifnull(backChargeNewNum,0) ,ifnull(pileNum,0), \n" +
            "ifnull(putonBackNum,0),ifnull(packageNum,0),now(),'1',ifnull(pulloffNum,0) from (\n" +
            " select m.*, pulloffNum from  ( \n" +
            "select g.*,putonBackNum from (\n" +
            "select e.*,pileTotalNum,backChargeNum,backChargeNewNum,repairNewNum  from (\n" +
            "select c.*,d.packageNum from ( \n" +
            "select a.*,b.pileNum from (\n" +
            "select sum(currentNum) as totalOnTidying,plantID,materialID,materialName from tb_tidybatteryrecord where dayTime > #{onTidyingTime} group by plantID,materialID ) a left join \n" +
            "(select plantID,materialID,sum(productionNumber) as pileNum from tb_pilebatteryrecord where pileTime > #{startTime} and pileTime < #{endTime} group by plantID,materialID ) b on a.plantID = b.plantID and a.materialID = b.materialID ) c left join\n" +
            " ( select plantID,materialID,sum(productionNumber) as packageNum from tb_pilebatteryrecord where packageTime > #{startTime} and packageTime < #{endTime} group by plantID,materialID ) d on  c.plantID = d.plantID and c.materialID = d.materialID ) e left join\n" +
            " ( select plantID,materialID,pileTotalNum,backChargeNewNum,backChargeNum,repairNewNum from tb_tidypackagebatteryinventory where checkTime >  #{endTime} group by plantID,materialID ) f on e.plantID = f.plantID and e.materialID = f.materialID ) g left join \n" +
            " ( select plantID,materialID, sum(productionNumber) as putonBackNum from tb_chargingrackrecord  where putonDate >=  #{startTime} and putonDate <  #{endTime} and materialType > '2' group by plantID,materialID ) h on g.plantID = h.plantID and g.materialID = h.materialID  ) m left join\n" +
            " ( select plantID,materialID, sum(productionNumber) as pulloffNum from tb_chargingrackrecord  where pulloffDate >=  #{startTime} and pulloffDate <  #{endTime} group by plantID,materialID  ) n on m.plantID = n.plantID and m.materialID = n.materialID ) o")
    int insertInventoryRecord(String startTime,String endTime,String onTidyingTime);

    @Delete("delete from tb_tidypackagebatteryinventory where remark = '-1'")
    int deleteRemark();
}