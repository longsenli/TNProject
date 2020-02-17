package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.EpidemicControlTMPTRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
@Component
public interface EpidemicControlTMPTRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(EpidemicControlTMPTRecord record);

    int insertSelective(EpidemicControlTMPTRecord record);

    EpidemicControlTMPTRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EpidemicControlTMPTRecord record);

    int updateByPrimaryKey(EpidemicControlTMPTRecord record);


    @Select(" select  distinct department as department" +
            " from tb_epidemiccontrolstaffinfo order by CONVERT(department using gbk) ")
    List<Map<Object, Object>> getStaffEpidemicBasicDepartmentInfo( );

    @Select(" select * " +
            " from tb_epidemiccontrolstaffinfo where identityNO = #{identityID}   ")
    List<Map<Object, Object>> getStaffEpidemicBasicInfo(String identityID);

    @Select(" select * " +
            " from tb_epidemiccontrolstaffinfo where department = #{department}  order by CONVERT(name using gbk)   ")
    List<Map<Object, Object>> getStaffEpidemicBasicInfoByDepartment(String department);

    @Select(" select name,sex,department,concat(left(identityNO,6),' ',right(identityNO,12)) as identityNO,extd1,telephoneNumber,familyLocation,temperature,date_format(updateTime,'%Y-%m-%d %H:%i:%s') as updateTime,remark  " +
            " from tb_epidemiccontroltmptrecord ${filter} order by department, CONVERT(name using gbk),updateTime desc ")
    List<Map<Object, Object>> getStaffEpidemicTMPTRecord(@Param("filter") String filter);


    @Select(" select name,sex,department,temperature,date_format(updateTime,'%Y-%m-%d %H:%i:%s') as updateTime,concat(left(identityNO,6),' ',right(identityNO,12)) as identityNO,extd1,telephoneNumber,familyLocation,remark  " +
            " from tb_epidemiccontroltmptrecord ${filter} order by department, CONVERT(name using gbk),updateTime desc limit 2 ")
    List<Map<Object, Object>> getStaffLatestEpidemicTMPTRecord(@Param("filter") String filter);
}