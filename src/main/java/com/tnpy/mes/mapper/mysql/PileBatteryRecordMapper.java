package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PileBatteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PileBatteryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PileBatteryRecord record);

    int insertSelective(PileBatteryRecord record);

    PileBatteryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PileBatteryRecord record);

    int updateByPrimaryKey(PileBatteryRecord record);

    @Select("select * from tb_pilebatteryrecord ${filter} ")
    List<PileBatteryRecord> selectByFilter(@Param("filter") String filter);
}