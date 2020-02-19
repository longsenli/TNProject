package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.EpidemicControlTMPTRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
            " from tb_epidemiccontrolstaffinfo where department = #{department} and compony = #{compony}  order by CONVERT(name using gbk)   ")
    List<Map<Object, Object>> getStaffEpidemicBasicInfoByDepartment(String department,String compony);

    @Select(" select id,name,sex,department,concat(left(identityNO,6),' ',right(identityNO,12)) as identityNO,extd1,telephoneNumber,familyLocation,temperature,updator, date_format(updateTime,'%Y-%m-%d %H:%i:%s') as updateTime,remark  " +
            " from tb_epidemiccontroltmptrecord ${filter} order by department, CONVERT(name using gbk),updateTime desc ")
    List<Map<Object, Object>> getStaffEpidemicTMPTRecord(@Param("filter") String filter);


    @Select(" select name,sex,department,temperature,date_format(updateTime,'%Y-%m-%d %H:%i:%s') as updateTime,concat(left(identityNO,6),' ',right(identityNO,12)) as identityNO,updator,extd1,telephoneNumber,familyLocation,remark  " +
            " from tb_epidemiccontroltmptrecord ${filter} order by department, CONVERT(name using gbk),updateTime desc limit 2 ")
    List<Map<Object, Object>> getStaffLatestEpidemicTMPTRecord(@Param("filter") String filter);

    @Update(" update tb_epidemiccontroltmptrecord set status = '-1' where id = #{id}")
    int deleteByChangeStatus(String id);

    @Select(" (\n" +
            "select count(1) as number ,'天能集团(河南)能源科技有限公司' as name,'检测总人数' as type from (\n" +
            "select identityNO  from  tb_epidemiccontroltmptrecord \n" +
            " ${filter1} group by identityNO\n" +
            ")  a\n" +
            ")union all (\n" +
            "select count(1) as number, '天能集团(河南)能源科技有限公司' as name,'新增人数' as type from (\n" +
            "select identityNO,min(updateTime) as minTime from  tb_epidemiccontroltmptrecord \n" +
            " ${filter3} group by identityNO\n" +
            ") b  ${filter2} \n" +
            " ) union all (\n" +
            "select count(1) as number ,updator,'各监测点检测人数' as type from (\n" +
            "select identityNO,updator from  tb_epidemiccontroltmptrecord \n" +
            "  ${filter1} group by identityNO,updator\n" +
            ")  c group by updator  order by CONVERT(updator using gbk) limit 1000\n" +
            ") union all (\n" +
            "select  count(1) as number,updator,'各监测点检测人次' as type from  tb_epidemiccontroltmptrecord \n" +
            " ${filter1} group by updator\n" +
            "order by CONVERT(updator using gbk) limit 1000 )" +" union all (\n" +
            "select count(1) as number ,department,'各部门检测人数' as type from (\n" +
            "select identityNO,department from  tb_epidemiccontroltmptrecord \n" +
            "  ${filter1} group by identityNO,department\n" +
            ")  c group by department order by CONVERT(department using gbk) limit 1000\n" +
            ") " )
    //filter1 时间和公司过滤，filter2 时间过滤 filter3 公司过滤
    List<Map<Object, Object>> getStaffEpidemicTMPTRecordSummary(@Param("filter1") String filter1,@Param("filter2") String filter2,@Param("filter3") String filter3);


}