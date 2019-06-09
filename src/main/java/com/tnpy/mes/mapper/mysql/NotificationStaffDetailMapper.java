package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.NotificationStaffDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NotificationStaffDetailMapper {
    int deleteByPrimaryKey(String id);

    @Delete("delete from tb_notificationstaffdetail where  notificationGroupID = #{notificationGroupID}  ")
    int deleteByGroupID(String notificationGroupID );

    @Delete("delete from tb_notificationstaffdetail where  notificationGroupID = #{notificationGroupID} and staffID = #{staffID} ")
    int deleteByGroupIDStaffID(String notificationGroupID,String staffID);

    int insert(NotificationStaffDetail record);

    int insertSelective(NotificationStaffDetail record);

    NotificationStaffDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotificationStaffDetail record);

    int updateByPrimaryKey(NotificationStaffDetail record);

    @Select("select * from tb_notificationstaffdetail where notificationGroupID = #{notificationGroupID} order by staffName ")
    List<NotificationStaffDetail> selectByNotificationGroupID(String notificationGroupID);

    @Select(" select staffID from tb_notificationstaffdetail where notificationGroupID = #{notificationGroupID}" )
    List<String> selectNotificationTypeByNotificationGroupID(String notificationGroupID);
}