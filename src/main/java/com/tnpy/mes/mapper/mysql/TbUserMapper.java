
package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TbUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TbUserMapper {
	int deleteByPrimaryKey(String userid);

	int insert(TbUser record);

	int insertSelective(TbUser record);

	TbUser selectByPrimaryKey(String userid);

	@Select("select * from tb_user where userID = #{userid} and state != -1 ")
	TbUser selectByPrimaryKeyWithStatus(String userid);

	@Select("update  tb_user set state = -1 where userID = #{userid}  ")
	TbUser deleteByPrimaryKeyWithStatus(String userid);
//	@Select("select u.*,(select r.role_name from tb_role r where r.role_id=u.role_id) as roleName from tb_user u ")
	List<TbUser> userList();

	int updateByPrimaryKeySelective(TbUser record);

	int updateByPrimaryKey(TbUser record);
}