package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.NotificationGroupDetail;
import com.tnpy.mes.model.mysql.NotificationTypeDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NotificationGroupDetailMapper {
    int deleteByPrimaryKey(String id);

    @Delete("delete from tb_notificationgroupdetail where notificationGroupID = #{notificationGroupID}")
    int deleteByGroupID(String notificationGroupID);

    @Delete("delete from tb_notificationgroupdetail where notificationGroupID = #{notificationGroupID} and notificationType = #{typeID}")
    int deleteByGroupIDTypeID(String notificationGroupID,String typeID);

    int insert(NotificationGroupDetail record);

    int insertSelective(NotificationGroupDetail record);

    NotificationGroupDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotificationGroupDetail record);

    int updateByPrimaryKey(NotificationGroupDetail record);

    @Select(" select b.* from ( select notificationType from tb_notificationgroupdetail where notificationGroupID = #{notificationGroupID}) a left join tb_notificationtypedetail b" +
            " on a.notificationType = b.id ")
    List<NotificationTypeDetail> selectByNotificationGroupID(String notificationGroupID);

    @Select(" select notificationType from tb_notificationgroupdetail where notificationGroupID = #{notificationGroupID}" )
    List<String> selectNotificationTypeByNotificationGroupID(String notificationGroupID);
}