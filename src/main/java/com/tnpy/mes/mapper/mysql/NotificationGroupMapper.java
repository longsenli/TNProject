package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.NotificationGroup;

public interface NotificationGroupMapper {
    int deleteByPrimaryKey(String id);

    int insert(NotificationGroup record);

    int insertSelective(NotificationGroup record);

    NotificationGroup selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotificationGroup record);

    int updateByPrimaryKey(NotificationGroup record);
}