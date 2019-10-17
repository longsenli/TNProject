package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialScrapRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface MaterialScrapRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialScrapRecord record);

    int insertSelective(MaterialScrapRecord record);

    MaterialScrapRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialScrapRecord record);

       int updateByPrimaryKey(MaterialScrapRecord record);

    @Select("SELECT id,name FROM sys_material where id in ( ( SELECT distinct inMaterialID as materialID FROM sys_materialrelation where outMaterialID in \n" +
            "(SELECT materialID FROM tb_workorder where  lineID = #{lineID} and  scheduledStartTime = #{productTime} and status != '5') ) union all " +
            " (SELECT distinct materialID as materialID FROM tb_workorder where  lineID = #{lineID} and  scheduledStartTime = #{productTime} and status != '5') )")
    List<Map<Object, Object>> getUsedMaterialInfo(String lineID, String productTime);

    @Select("  SELECT materialID as id,materialNameInfo as name FROM tb_materialrecord where outputLineID = #{lineID} and outputTime > #{productTime} group by materialID,materialNameInfo ")
    List<Map<Object, Object>> getUsedMaterialInfoWithExpend(String lineID, String productTime);

    @Select("SELECT id,lineID,date_format(productDay, '%Y-%m-%d %H:%i:%s') as productDay ,classType,materialName,value,updateStaff,date_format(updateTime, '%Y-%m-%d %H:%i:%s') as updateTime,remark FROM tb_materialscraprecord" +
            "  where productDay >= #{startTime} and productDay <= #{endTime} ${filter}  order by productDay,classType,lineID")
    List<Map<Object, Object>> getMaterialScrapRecord(@Param("filter") String filter, String startTime, String endTime);

}