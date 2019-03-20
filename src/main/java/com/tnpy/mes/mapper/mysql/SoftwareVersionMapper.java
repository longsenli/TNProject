package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.SoftwareVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SoftwareVersionMapper {
    int deleteByPrimaryKey(String id);

    int insert(SoftwareVersion record);

    int insertSelective(SoftwareVersion record);

    SoftwareVersion selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SoftwareVersion record);

    int updateByPrimaryKey(SoftwareVersion record);

    @Select("select version from sys_softwareversion where clientType = #{clientType} order by updateTime desc limit 1")
    String selectLatestVersion(String clientType);
}