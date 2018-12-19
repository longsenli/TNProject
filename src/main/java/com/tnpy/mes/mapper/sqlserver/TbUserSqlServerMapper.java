package com.tnpy.mes.mapper.sqlserver;

import com.tnpy.mes.model.sqlserver.TbUserSqlServer;

public interface TbUserSqlServerMapper {
    int deleteByPrimaryKey(String id);

    int insert(TbUserSqlServer record);

    int insertSelective(TbUserSqlServer record);

    TbUserSqlServer selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TbUserSqlServer record);

    int updateByPrimaryKey(TbUserSqlServer record);
}