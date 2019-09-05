
package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TbUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface TbUserMapper {
	int deleteByPrimaryKey(String userid);

	int insert(TbUser record);

	int insertSelective(TbUser record);

	TbUser selectByPrimaryKey(String userid);

	@Select("select * from tb_user where userID = #{userid} and state != -1 ")
	TbUser selectByPrimaryKeyWithStatus(String userid);
	
	@Select("select * from tb_user where name like '%${name}%'")
	List<TbUser> selectByName(@Param(value = "name") String name);

	@Select("update  tb_user set state = -1 where userID = #{userid}  ")
	TbUser deleteByPrimaryKeyWithStatus(String userid);
//	@Select("select u.*,(select r.role_name from tb_role r where r.role_id=u.role_id) as roleName from tb_user u ")
	List<TbUser> userList();

	int updateByPrimaryKeySelective(TbUser record);

	int updateByPrimaryKey(TbUser record);

	@Select("select userID,name,mobilephone,industrialplant_name,productionprocess_name from tb_user where state = '1' order by name ")
	List<Map<Object, Object>> selectAllBasicUserInfo();

	@Select("select ${columnList} from tb_user ${filter} ")
	List<Map<Object, Object>> selecUserInfoByfilter(@Param("columnList") String columnList,@Param("filter")String filter);
}