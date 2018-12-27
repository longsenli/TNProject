
package com.tnpy.mes.mapper.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.tnpy.mes.model.mysql.TbUser;

@Mapper
@Component
public interface TbUserMapper {
	int deleteByPrimaryKey(String userid);

	int insert(TbUser record);

	int insertSelective(TbUser record);

	TbUser selectByPrimaryKey(String userid);

	@Select("select * from tb_user")
	List<TbUser> userList();

	int updateByPrimaryKeySelective(TbUser record);

	int updateByPrimaryKey(TbUser record);
}