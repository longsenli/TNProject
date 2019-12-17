package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProductionDetailRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DailyProductionDetailRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProductionDetailRecord record);

    int insertSelective(DailyProductionDetailRecord record);

    DailyProductionDetailRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProductionDetailRecord record);

    int updateByPrimaryKey(DailyProductionDetailRecord record);
    @Select("SELECT distinct units FROM tb_workorder where plantID = #{plantID} and processID = #{processID} and  id like  ${orderString} and units is not null limit 1;")
    String getTeamType(String plantID, String processID,@Param("orderString") String orderString);
    @Select("select c.*,d.scrapNumber,d.weightNumber ,#{teamType} as teamType from (\n" +
            "select a.plantID,a.processID,a.lineID,a.materialID,a.materialName,a.number as productionNumber, usedMaterialID,usedMaterialName, usedNumber  from \n" +
            "(\n" +
            "SELECT inputPlantID as plantID,inputProcessID as processID ,inputLineID as lineID ,materialID,materialNameInfo as materialName,sum(number)   as number\n" +
            "FROM tb_materialrecord where inputPlantID =#{plantID} and inputProcessID=  #{processID} and orderID like ${orderString} and status = '1' group by inputPlantID,inputProcessID,inputLineID,materialID\n" +
            ") a left join \n" +
            " ( SELECT outputPlantID as plantID,outputProcessID as processID,outputLineID as lineID,materialID as usedMaterialID,materialNameInfo as usedMaterialName,sum(number)   as usedNumber\n" +
            "FROM tb_materialrecord where outputPlantID = #{plantID} and outputProcessID=  #{processID} and orderID like ${orderString}  group by outputPlantID,outputProcessID,outputLineID,materialID )\n" +
            "b on a.plantID = b.plantID and a.processID = b.processID and a.lineID = b.lineID\n" +
            ") c left join \n" +
            "( SELECT plantID,processID,lineID,materialID,materialName,sum(value) as scrapNumber ,sum(weight) as weightNumber\n" +
            "FROM tb_materialscraprecord where plantID = #{plantID} and processID= #{processID} and productDay = #{dayTime} and classType =#{classType}   and status = '1' group by plantID,processID,lineID,materialID )\n" +
            "d on  c.plantID = d.plantID and c.processID = d.processID and c.lineID = d.lineID and c.usedMaterialID = d.materialID")
    List<Map<Object,Object>> getTMPDailyProductionDetailRecord(String plantID, String processID, @Param("orderString") String orderString, String dayTime, String classType,String teamType);
}