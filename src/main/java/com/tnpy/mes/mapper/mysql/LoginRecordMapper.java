package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.LoginRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface LoginRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(LoginRecord record);

    int insertSelective(LoginRecord record);

    LoginRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(LoginRecord record);

    int updateByPrimaryKey(LoginRecord record);
}