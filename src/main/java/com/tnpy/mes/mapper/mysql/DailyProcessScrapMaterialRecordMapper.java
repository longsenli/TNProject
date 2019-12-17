package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.DailyProcessScrapMaterialRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DailyProcessScrapMaterialRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DailyProcessScrapMaterialRecord record);

    int insertSelective(DailyProcessScrapMaterialRecord record);

    DailyProcessScrapMaterialRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DailyProcessScrapMaterialRecord record);

    int updateByPrimaryKey(DailyProcessScrapMaterialRecord record);
}