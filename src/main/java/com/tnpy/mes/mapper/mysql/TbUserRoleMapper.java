package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TbUserRole;

public interface TbUserRoleMapper {
    int deleteByPrimaryKey(Integer userRoleId);

    int insert(TbUserRole record);

    int insertSelective(TbUserRole record);

    TbUserRole selectByPrimaryKey(Integer userRoleId);

    int updateByPrimaryKeySelective(TbUserRole record);

    int updateByPrimaryKey(TbUserRole record);
}