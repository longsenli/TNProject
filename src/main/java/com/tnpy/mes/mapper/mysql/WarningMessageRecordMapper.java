package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WarningMessageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WarningMessageRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(WarningMessageRecord record);

    int insertSelective(WarningMessageRecord record);

    WarningMessageRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WarningMessageRecord record);

    int updateByPrimaryKey(WarningMessageRecord record);

    @Select("select * from tb_warningmessagerecord ${filter}")
    List<WarningMessageRecord> selectByFilter(@Param("filter") String filter);
}