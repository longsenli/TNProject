package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DryingKilnJZRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DryingKilnJZRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DryingKilnJZRecord record);

    int insertSelective(DryingKilnJZRecord record);

    DryingKilnJZRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DryingKilnJZRecord record);

    int updateByPrimaryKey(DryingKilnJZRecord record);
}