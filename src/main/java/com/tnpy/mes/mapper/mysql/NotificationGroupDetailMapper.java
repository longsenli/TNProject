package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.NotificationGroupDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NotificationGroupDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(NotificationGroupDetail record);

    int insertSelective(NotificationGroupDetail record);

    NotificationGroupDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotificationGroupDetail record);

    int updateByPrimaryKey(NotificationGroupDetail record);
}