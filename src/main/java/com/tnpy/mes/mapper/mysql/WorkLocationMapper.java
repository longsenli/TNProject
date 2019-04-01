package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WorkLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WorkLocationMapper {
    int deleteByPrimaryKey(String id);

    int insert(WorkLocation record);

    int insertSelective(WorkLocation record);

    WorkLocation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WorkLocation record);

    int updateByPrimaryKey(WorkLocation record);

    @Select("select * from tb_worklocation ${filter}")
    List<WorkLocation> selectByFilter(@Param("filter") String filter);
}