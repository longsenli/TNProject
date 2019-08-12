package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.MaterialScrapRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MaterialScrapRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MaterialScrapRecord record);

    int insertSelective(MaterialScrapRecord record);

    MaterialScrapRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MaterialScrapRecord record);

    int updateByPrimaryKey(MaterialScrapRecord record);
}