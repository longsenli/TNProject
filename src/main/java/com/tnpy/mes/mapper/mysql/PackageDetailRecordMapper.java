package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.PackageDetailRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface PackageDetailRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(PackageDetailRecord record);

    int insertSelective(PackageDetailRecord record);

    PackageDetailRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PackageDetailRecord record);

    int updateByPrimaryKey(PackageDetailRecord record);
}