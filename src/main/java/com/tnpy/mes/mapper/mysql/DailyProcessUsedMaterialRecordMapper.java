package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProcessUsedMaterialRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DailyProcessUsedMaterialRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProcessUsedMaterialRecord record);

    int insertSelective(DailyProcessUsedMaterialRecord record);

    DailyProcessUsedMaterialRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProcessUsedMaterialRecord record);

    int updateByPrimaryKey(DailyProcessUsedMaterialRecord record);
}