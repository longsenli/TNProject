package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TidyBatteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TidyBatteryRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(TidyBatteryRecord record);

    int insertSelective(TidyBatteryRecord record);

    TidyBatteryRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TidyBatteryRecord record);

    int updateByPrimaryKey(TidyBatteryRecord record);

    @Select("select * from tb_tidybatteryrecord ${filter} ")
    List<TidyBatteryRecord> selectByFilter(@Param("filter") String filter);
    
    @Select(" ${filter} ")
    List<TidyBatteryRecord> selectByFilter1(@Param("filter") String filter);

    @Select("select * from tb_tidybatteryrecord ${filter} ")
    TidyBatteryRecord selectLatestRecordByFilter(@Param("filter") String filter);

    @Update(" update tb_tidybatteryrecord set currentNum = currentNum - ${pileNum},pileNum = pileNum + ${pileNum} where id = #{id}")
    int updateCurrentNumAfterPile(String id,@Param("pileNum") String pileNum);
}