package com.tnpy.mes.mapper.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tnpy.mes.model.mysql.TbRole;

public interface TbRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(TbRole record);

    int insertSelective(TbRole record);

    TbRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(TbRole record);

    int updateByPrimaryKey(TbRole record);
    
//    @Select("select * from tb_role ")
	List<TbRole> listRoles();
}