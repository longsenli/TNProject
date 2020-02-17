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


    @Select("( select  distinct department as id ,'' as name,'1' as type " +
            " from tb_epidemiccontrolstaffinfo ${filter}  order by CONVERT(department using gbk)  limit 1000 ) union all" +
            " (   select  role_id as id,role_name as name ,'2' as type  from  tb_role  where role_id like '7%' ${filter2} order by CONVERT(role_name using gbk) limit 200) ")
    List<Map<Object, Object>> getStaffEpidemicBasicDepartmentInfo(@Param( "filter") String filter,@Param( "filter2") String filter2 );

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