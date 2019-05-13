package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ChargingRackRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ChargingRackRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChargingRackRecord record);

    int insertSelective(ChargingRackRecord record);

    ChargingRackRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChargingRackRecord record);

    int updateByPrimaryKey(ChargingRackRecord record);

    @Select("select * from tb_chargingrackrecord ${filter} ")
    List<ChargingRackRecord> selectByFilter(@Param("filter") String filter);
}