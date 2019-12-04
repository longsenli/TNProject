package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialCirculationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface MaterialCirculationRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialCirculationRecord record);

    int insertSelective(MaterialCirculationRecord record);

    MaterialCirculationRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialCirculationRecord record);

    int updateByPrimaryKey(MaterialCirculationRecord record);

    @Select( "(SELECT '' as id, originalPlantID,processID,destinationPlantID, materialName,sum(number) as number , '' as sender,'' as sendTime ,'' as saccepter,\n" +
            "''  as acceptTime ,'总计' as circulationType,'' as circulationDescription FROM tb_materialcirculationrecord  ${filter}  group by originalPlantID,processID,destinationPlantID, materialName \n" +
            "order by originalPlantID,processID,destinationPlantID, materialName)\n" +
            "union all\n" +
            "( SELECT id,originalPlantID,processID,destinationPlantID, materialName,number,sender,date_format(sendTime, '%Y-%m-%d %H:%i:%s') as sendTime ,accepter,\n" +
            "date_format(acceptTime, '%Y-%m-%d %H:%i:%s') as acceptTime ,circulationType,circulationDescription FROM tb_materialcirculationrecord ${filter} order by sendTime)")
    List<Map<Object, Object>> getMaterialCirculationRecord(@Param("filter") String filter);

    @Update("update tb_materialcirculationrecord set status = '-1' where id = #{id} ")
    int deleteByChangeStatusWithPrimaryKey(String id);

}