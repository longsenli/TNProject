package com.tnpy.mes.mapper.mysql;

import org.apache.ibatis.annotations.Mapper;

import com.tnpy.mes.model.mysql.TbUser;

@Mapper
public interface TbUserMapper {
    int deleteByPrimaryKey(String userid);

    int insert(TbUser record);

    int insertSelective(TbUser record);

    TbUser selectByPrimaryKey(String userid);

    int updateByPrimaryKeySelective(TbUser record);

    int updateByPrimaryKey(TbUser record);
}