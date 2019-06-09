package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.NotificationTypeDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NotificationTypeDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(NotificationTypeDetail record);

    int insertSelective(NotificationTypeDetail record);

    NotificationTypeDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(NotificationTypeDetail record);

    int updateByPrimaryKey(NotificationTypeDetail record);

    @Select("select * from tb_notificationtypedetail where status != '-1' order by name")
    List<NotificationTypeDetail> selectAll();
}