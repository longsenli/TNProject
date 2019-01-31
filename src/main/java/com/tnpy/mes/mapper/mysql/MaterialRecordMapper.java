package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.customize.CustomMaterialRecord;
import com.tnpy.mes.model.mysql.MaterialRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

   // @Select("select * from tb_materialrecord where expendOrderID = #{expendOrder}")
    @Select("select c.*,d.name as materialName from ( \n" +
            "SELECT a.*,b.orderSplitID as inSubOrderName,left(b.orderSplitID, length(b.orderSplitID)-3)  as inOrderName \n" +
            "FROM tb_materialrecord  a left join tb_ordersplit b on a.subOrderID = b.id where expendOrderID = #{expendOrder} \n" +
            ") c left join sys_material d on c.materialID = d.id  order by outputTime desc")
    List<CustomMaterialRecord> selectByExpendOrder(String expendOrder);

    @Select("select e.*,f.name as materialName from ( select c.*,d.orderSplitID as inSubOrderName,\n" +
            "left(d.orderSplitID, length(d.orderSplitID)-3)  as inOrderName from ( \n" +
            "select a.* from ( SELECT * FROM tb_materialrecord where materialID in(\n" +
            "select inMaterialID from sys_materialrelation where outMaterialID = #{materialID}) and inOrOut = '1'\n" +
            ") a left join tb_workorder b on a.orderID = b.id  \n" +
            "where b.plantID = #{plantID} ) c left join tb_ordersplit d on c.subOrderID = d.id \n" +
            ") e left join sys_material f on e.materialID = f.id  order by inputTime asc ")
    List<CustomMaterialRecord> selectUsableMaterial(String plantID,String materialID );


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
    List<Map<String, String>> selectBatchChargingByOrder(String outOrderID);

    @Select("select sum(number) from tb_materialrecord where orderID = #{orderID}")
    String getProductionByOrderID(String orderID);

}