package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.NotificationStaffDetail;

public interface NotificationStaffDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(NotificationStaffDetail record);

    int insertSelective(NotificationStaffDetail record);

    NotificationStaffDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotificationStaffDetail record);

    int updateByPrimaryKey(NotificationStaffDetail record);
}