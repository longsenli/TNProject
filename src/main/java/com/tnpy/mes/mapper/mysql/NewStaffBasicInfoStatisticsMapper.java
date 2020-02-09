package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.NewStaffBasicInfoStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface NewStaffBasicInfoStatisticsMapper {
    int deleteByPrimaryKey(String id);

    int insert(NewStaffBasicInfoStatistics record);

    int insertSelective(NewStaffBasicInfoStatistics record);

    NewStaffBasicInfoStatistics selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NewStaffBasicInfoStatistics record);

    int updateByPrimaryKey(NewStaffBasicInfoStatistics record);

    @Select(" select * from tb_newStaffBasicInfoStatistics where name = #{name} and status = '1'")
    List<Map<Object, Object>> getNewStaffBasicInfo(String name);
}