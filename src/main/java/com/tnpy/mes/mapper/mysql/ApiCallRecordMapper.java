package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.ApiCallRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ApiCallRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ApiCallRecord record);

    int insertSelective(ApiCallRecord record);

    ApiCallRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ApiCallRecord record);

    int updateByPrimaryKey(ApiCallRecord record);
}