package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PlasticUsedRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PlasticUsedRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PlasticUsedRecord record);

    int insertSelective(PlasticUsedRecord record);

    PlasticUsedRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PlasticUsedRecord record);

    int updateByPrimaryKey(PlasticUsedRecord record);

    @Select("select count(1) as productionNumb,lineID,workLocation,materialName from tb_plasticUsedRecord ${filter} ")
    List<Map<Object,Object>> selectByParam(@Param("filter") String filter);

    @Select("select count(*) from tb_plasticUsedRecord  where JQID = #{orderID} and status != '9'")
    int selectJQUsedNumber(String orderID);


    @Select("SELECT count(*) FROM tb_materialrecord where subOrderID = #{inOrderID}  and materialID in(\n" +
            "select distinct inMaterialID from sys_materialrelation where outMaterialID = #{outMaterialID} )\n")
    int checkMaterialUsable(String inOrderID,String outMaterialID);

    @Select("select count(*) from tb_plasticUsedRecord  where workLocation = #{locationID} and usedTime > #{startTime} and usedTime < #{endTime} and status != '9'")
    int selectPlasticUsedNumber(String locationID,String startTime,String endTime);

    @Select("select count(*) from tb_plasticUsedRecord  where workLocation = #{locationID} and usedOrderID like ${orderFilter} and status != '9'")
    int selectPlasticUsedNumberByOrder(String locationID,@Param("orderFilter") String orderFilter);

    @Update("update tb_plasticUsedRecord set status = #{status} ,extend1 = #{repairReason} where id = #{id}")
    int updateScrapInfo(String id,String status,String repairReason);
}