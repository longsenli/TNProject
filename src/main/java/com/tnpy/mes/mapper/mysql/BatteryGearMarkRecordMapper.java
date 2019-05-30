package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.BatteryGearMarkRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface BatteryGearMarkRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(BatteryGearMarkRecord record);

    int insertSelective(BatteryGearMarkRecord record);

    BatteryGearMarkRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BatteryGearMarkRecord record);

    int updateByPrimaryKey(BatteryGearMarkRecord record);

    @Select("select distinct(lineID) as id from tb_batterygearmarkrecord ${filter}")
    List<Map<Object, Object>> selectLineInfo(@Param("filter") String filter);

    @Select("select distinct(locationID) as id from tb_batterygearmarkrecord ${filter}")
    List<Map<Object, Object>> selectLineLocationInfo(@Param("filter") String filter);

    @Select("select * from tb_batterygearmarkrecord ${filter}")
    List<BatteryGearMarkRecord> selectBatteryGearRecordInfo(@Param("filter") String filter);
}