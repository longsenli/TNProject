package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PlasticUsedRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PlasticUsedRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PlasticUsedRecord record);

    int insertSelective(PlasticUsedRecord record);

    PlasticUsedRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PlasticUsedRecord record);

    int updateByPrimaryKey(PlasticUsedRecord record);

    @Select("select * from tb_plasticUsedRecord ${filter}")
    List<PlasticUsedRecord> selectByParam(@Param("filter") String filter);
}