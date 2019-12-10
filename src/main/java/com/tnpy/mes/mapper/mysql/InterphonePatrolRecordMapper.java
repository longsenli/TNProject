package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.InterphonePatrolRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface InterphonePatrolRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(InterphonePatrolRecord record);

    int insertSelective(InterphonePatrolRecord record);

    InterphonePatrolRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(InterphonePatrolRecord record);

    int updateByPrimaryKey(InterphonePatrolRecord record);

    @Update("update tb_interphonepatrolrecord set status = '-1' where id =#{id} ")
    int deleteByUpdateStatus(String id);


    @Select("select count(1) from  tb_interphonepatrolrecord where dayTime = #{dayTime} and ordinal = #{ordinal}")
    int checkRecordExit(String dayTime,int ordinal);


    @Select(" SELECT id,plantID,patrolLocationName,dayTime,ordinal, case  when onlineFlag=1 then CONCAT( '在岗 ', content) else CONCAT('不在岗 ',content) end as onlineFlag ," +
            " DATE_FORMAT( patrolTime,'%Y-%m-%d %H:%i:%s') as patrolTime,patrolStaff FROM tb_interphonepatrolrecord ${filter} order by dayTime,ordinal,patrolLocationID")
    List<Map<Object,Object>> getInterphonePatrolRecordDetail(@Param("filter") String filter);

    @Select(" select plantID,patrolLocationID,patrolLocationName,dayTime,max(onlineFlag1)  as onlineFlag1 ,max(patrolTime1) as patrolTime1 ,max(patrolStaff1)  as patrolStaff1,max(onlineFlag2)  as onlineFlag2 ,max(patrolTime2) as patrolTime2 ,max(patrolStaff2)  as patrolStaff2,\n" +
            "max(onlineFlag3)  as onlineFlag3 ,max(patrolTime3) as patrolTime3 ,max(patrolStaff3)  as patrolStaff3,max(onlineFlag4)  as onlineFlag4 ,max(patrolTime4) as patrolTime4 ,max(patrolStaff4)  as patrolStaff4\n" +
            "from (\n" +
            "(SELECT plantID,patrolLocationID,patrolLocationName,dayTime,ordinal as ordinal1,  case  when onlineFlag=1 then CONCAT( '在岗 ', content) else CONCAT('不在岗 ',content) end as onlineFlag1,\n" +
            " DATE_FORMAT( patrolTime,'%Y-%m-%d %H:%i:%s') as patrolTime1,patrolStaff as patrolStaff1 , '' as onlineFlag2, '' as patrolTime2 ,'' as patrolStaff2, '' as onlineFlag3, '' as patrolTime3 ,'' as patrolStaff3 , '' as onlineFlag4, '' as patrolTime4 ,'' as patrolStaff4 \n" +
            "FROM tb_interphonepatrolrecord where dayTime = #{time} and ordinal = 1 and status != '-1' )\n" +
            "union all\n" +
            "(SELECT plantID,patrolLocationID,patrolLocationName,dayTime,ordinal as ordinal1,  '' as onlineFlag1, '' as patrolTime1 ,'' as patrolStaff1, case  when onlineFlag=1 then CONCAT( '在岗 ', content) else CONCAT('不在岗 ',content) end as onlineFlag2,\n" +
            " DATE_FORMAT( patrolTime,'%Y-%m-%d %H:%i:%s') as patrolTime2,patrolStaff as patrolStaff2, '' as onlineFlag3, '' as patrolTime3 ,'' as patrolStaff3 , '' as onlineFlag4, '' as patrolTime4 ,'' as patrolStaff4 \n" +
            "  FROM tb_interphonepatrolrecord where dayTime = #{time} and ordinal = 2 and status != '-1' )\n" +
            " union all \n" +
            "(SELECT plantID,patrolLocationID,patrolLocationName,dayTime,ordinal as ordinal1,  '' as onlineFlag1, '' as patrolTime1 ,'' as patrolStaff1,'' as onlineFlag2, '' as patrolTime2 ,'' as patrolStaff2, \n" +
            " case  when onlineFlag=1 then CONCAT( '在岗 ', content) else CONCAT('不在岗 ',content) end as onlineFlag3,\n" +
            "DATE_FORMAT( patrolTime,'%Y-%m-%d %H:%i:%s') as patrolTime3,patrolStaff as patrolStaff3, '' as onlineFlag4, '' as patrolTime4 ,'' as patrolStaff4  FROM tb_interphonepatrolrecord where dayTime = #{time} and ordinal = 3 and status != '-1' )\n" +
            "union all\n" +
            "(SELECT plantID,patrolLocationID,patrolLocationName,dayTime,ordinal as ordinal1, '' as onlineFlag1, '' as patrolTime1 ,'' as patrolStaff1,'' as onlineFlag2, '' as patrolTime2 ,'' as patrolStaff2, \n" +
            "'' as onlineFlag3, '' as patrolTime3 ,'' as patrolStaff3 ,  case  when onlineFlag=1 then CONCAT( '在岗 ', content) else CONCAT('不在岗 ',content) end as onlineFlag4,\n" +
            "DATE_FORMAT( patrolTime,'%Y-%m-%d %H:%i:%s') as patrolTime4,patrolStaff as patrolStaff4  FROM tb_interphonepatrolrecord where dayTime = #{time} and ordinal = 4 and status != '-1') ) a group by patrolLocationID order by patrolLocationID")
    List<Map<Object,Object>> getInterphonePatrolRecordReport( String time);
}