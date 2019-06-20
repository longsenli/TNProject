package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.WarningMessageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface WarningMessageRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(WarningMessageRecord record);

    int insertSelective(WarningMessageRecord record);

    WarningMessageRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WarningMessageRecord record);

    int updateByPrimaryKey(WarningMessageRecord record);

    @Select("select * from tb_warningmessagerecord ${filter}")
    List<WarningMessageRecord> selectByFilter(@Param("filter") String filter);

    @Select("  select email,userID,industrialplant_id,telephone from ( select b.staffID from ( SELECT notificationGroupID FROM tb_notificationgroupdetail where notificationType = #{notificationType}) a\n" +
            " left join tb_notificationstaffdetail b on a.notificationGroupID = b.notificationGroupID ) c left join tb_user d on c.staffID = d.userID")
    List<Map<java.lang.Object, Object>> selectUserInfoByWarning(String notificationType);
}