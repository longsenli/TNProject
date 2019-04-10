package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DryingKilnJZRecord;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DryingKilnJZRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DryingKilnJZRecord record);

    int insertSelective(DryingKilnJZRecord record);

    DryingKilnJZRecord selectByPrimaryKey(String id);
    
    @Select("select * from tb_dryingkilnjzrecord t where t.suborderid=#{suborderid}")
    List<DryingKilnJZRecord> selectBySuborderid(@Param("suborderid") String suborderid);

    int updateByPrimaryKeySelective(DryingKilnJZRecord record);

    int updateByPrimaryKey(DryingKilnJZRecord record);
}