package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.NotificationGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface NotificationGroupMapper {
    int deleteByPrimaryKey(String id);

    int insert(NotificationGroup record);

    int insertSelective(NotificationGroup record);

    NotificationGroup selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotificationGroup record);

    int updateByPrimaryKey(NotificationGroup record);

    @Select("select * from tb_notificationgroup where status != '-1' order by sortNum")
    List<NotificationGroup> selectAllGroup();

    @Select("select name from tb_notificationgroup where id = #{id}")
    String selectGroupNameByID(String id);
}