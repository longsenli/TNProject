package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TbRoleMenu;

public interface TbRoleMenuMapper {
    int deleteByPrimaryKey(Integer roleMenuId);

    int insert(TbRoleMenu record);

    int insertSelective(TbRoleMenu record);

    TbRoleMenu selectByPrimaryKey(Integer roleMenuId);

    int updateByPrimaryKeySelective(TbRoleMenu record);

    int updateByPrimaryKey(TbRoleMenu record);
}