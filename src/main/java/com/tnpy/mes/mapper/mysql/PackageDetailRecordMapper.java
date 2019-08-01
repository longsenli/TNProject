package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PackageDetailRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PackageDetailRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PackageDetailRecord record);

    int insertSelective(PackageDetailRecord record);

    PackageDetailRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PackageDetailRecord record);

    int updateByPrimaryKey(PackageDetailRecord record);

    @Select("( select '' as plantID,'' as equipmentID ,lineName,materialName ,'总计' as packageName ,sum(planNumber)  as planNumber,sum(totalNumber) as totalNumber, dayTime from tb_packagedetailrecord ${groupFilter} limit 1000 )" +
            " UNION ALL " +
            " ( select plantID,equipmentID,lineName,materialName,packageName,planNumber,totalNumber,dayTime from tb_packagedetailrecord ${filter} limit 1000 )")
    List<Map<Object, Object>> selectRecordWithSum(@Param("groupFilter") String groupFilter,@Param("filter") String filter);
}