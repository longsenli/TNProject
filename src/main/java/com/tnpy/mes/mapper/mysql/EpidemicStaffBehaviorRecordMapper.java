package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.EpidemicStaffBehaviorRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface EpidemicStaffBehaviorRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(EpidemicStaffBehaviorRecord record);

    int insertSelective(EpidemicStaffBehaviorRecord record);

    EpidemicStaffBehaviorRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EpidemicStaffBehaviorRecord record);

    int updateByPrimaryKey(EpidemicStaffBehaviorRecord record);

    @Select(" select count(1) from  tb_epidemicstaffbehaviorrecord where identityID = #{identityID} and daytime = #{dayTime} ")
    int selectRecordCount(String identityID,String dayTime);

    @Select(" select date_format(daytime,'%Y-%m-%d') as daytime, stayLocation,contactSeverity,abnormalShelf,abnormalPartner,quarantine,remark,name,extd1 as telephone" +
            " from tb_epidemicstaffbehaviorrecord where identityID = #{identityID}  order by daytime limit 1000 ")
    List<Map<Object, Object>> getShelfFilloutEpidemicRecord(String identityID);

//    @Select(" select date_format(daytime,'%Y-%m-%d') as daytime, stayLocation,contactSeverity,abnormalShelf,abnormalPartner,quarantine,remark,name,extd1 as telephone \n" +
//            "from tb_epidemicstaffbehaviorrecord order by CONVERT(name using gbk), daytime ")
//    List<Map<Object, Object>> getShelfFilloutEpidemicRecord(String identityID);
}