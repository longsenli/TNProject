package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.NotificationTypeDetail;

public interface NotificationTypeDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(NotificationTypeDetail record);

    int insertSelective(NotificationTypeDetail record);

    NotificationTypeDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotificationTypeDetail record);

    int updateByPrimaryKey(NotificationTypeDetail record);
}