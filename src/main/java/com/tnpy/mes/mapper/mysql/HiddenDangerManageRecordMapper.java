package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.HiddenDangerManageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface HiddenDangerManageRecordMapper {
    int deleteByPrimaryKey(String id);

    @Update("update  tb_hiddendangermanagerecord set status = '-1' where id = #{id}  ")
    int deleteByChangeStatus(String id);

    int insert(HiddenDangerManageRecord record);

    int insertSelective(HiddenDangerManageRecord record);

    HiddenDangerManageRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(HiddenDangerManageRecord record);

    int updateByPrimaryKey(HiddenDangerManageRecord record);

    @Update("update  tb_hiddendangermanagerecord set dealinfo = #{dealinfo},dealstaff = #{dealstaff},remark = #{remark},dealPicture = #{dealPicture},dealTime = #{dealTime},status = '2' where id = #{id}  ")
    int dealHiddenDangerManageRecord(String id, String  dealinfo, String  dealstaff, String  remark, String  dealPicture, Date dealTime);

    @Select("select * from tb_hiddendangermanagerecord ${filter}")
    List<HiddenDangerManageRecord> selectByFilter(@Param("filter") String filter);

    @Select("select a.status,a.number,b.name from (  select plantID,status,count(1) as number from tb_hiddendangermanagerecord ${filter}) a \n" +
            " left join sys_industrialplant b on a.plantID = b.id order by b.id")
    List<Map<Object, Object>> selectRecordSummaryByFilter(@Param("filter") String filter);
}