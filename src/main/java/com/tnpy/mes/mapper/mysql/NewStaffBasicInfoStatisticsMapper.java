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

    @Select(" select date_format(updateTime,'%Y-%m-%d') as updateTime,name,sex,age, telephoneNumber, familyLocation, educationLevel, " +
            "            employmentObjective,  remark from tb_newStaffBasicInfoStatistics where name = #{name} and status = '1'")
    List<Map<Object, Object>> getNewStaffBasicInfo(String name);

    @Select(" select date_format(updateTime,'%Y-%m-%d') as updateTime,name,sex,age, telephoneNumber, familyLocation, educationLevel," +
            " employmentObjective,  remark from tb_newStaffBasicInfoStatistics where  status = '1' order by  CONVERT(name using gbk)")
    List<Map<Object, Object>> getAllNewStaffBasicInfo();
}