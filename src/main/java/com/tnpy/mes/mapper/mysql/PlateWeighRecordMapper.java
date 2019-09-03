package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PlateWeighRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PlateWeighRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PlateWeighRecord record);

    int insertSelective(PlateWeighRecord record);

    PlateWeighRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PlateWeighRecord record);

    int updateByPrimaryKey(PlateWeighRecord record);

    @Select("select * from ${filter}")
    List<PlateWeighRecord> selectByFilter(@Param("filter") String filter);

    @Select(" ${filter}")
    List<Map<Object,Object>> selectBasicData(@Param("filter") String filter);
}