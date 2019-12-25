
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
	
	@Select("select u.*,(select r.role_name from tb_role r where r.role_id=u.roleID) as roleName from tb_user u where u.name like '%${name}%'")
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
	
	@Select("SELECT\r\n" + 
			"	ifnull(userID,' ') userID,\r\n" + 
			"	ifnull(NAME,' ') NAME,\r\n" + 
			"	ifnull(PASSWORD,' ') PASSWORD,\r\n" + 
			"	ifnull(roleID,' ') roleID,\r\n" + 
			"	ifnull(sex,' ') sex,\r\n" + 
			"	 birthday,\r\n" + 
			"	ifnull(dutyID,' ') dutyID,\r\n" + 
			"	ifnull(workplace,' ') workplace,\r\n" + 
			"	createdate,\r\n" + 
			"	ifnull(createuser,' ') createuser,\r\n" + 
			"	updatedate,\r\n" + 
			"	ifnull(updateuser,' ') updateuser,\r\n" + 
			"	 deletedate,\r\n" + 
			"	ifnull(deleteuser,' ') deleteuser,\r\n" + 
			"	 state,\r\n" + 
			"	ifnull(email,' ') email,\r\n" + 
			"	ifnull(mobilephone,' ') mobilephone,\r\n" + 
			"	ifnull(telephone,' ') telephone,\r\n" + 
			"	ifnull(organID,' ') organID,\r\n" + 
			"	ifnull(industrialplant_id,' ') industrialplant_id,\r\n" + 
			"	ifnull(industrialplant_name,' ') industrialplant_name,\r\n" + 
			"	ifnull(productionline_id,' ') productionline_id,\r\n" + 
			"	ifnull(productionline_name,' ') productionline_name,\r\n" + 
			"	ifnull(productionprocess_id,' ') productionprocess_id,\r\n" + 
			"	ifnull(productionprocess_name,' ') productionprocess_name,\r\n" + 
			"	ifnull(worklocation_id,' ') worklocation_id,\r\n" + 
			"	ifnull(worklocation_name,' ') worklocation_name,\r\n" + 
			"	ifnull(cardno,' ') cardno\r\n" + 
			"FROM\r\n" + 
			"	tb_user")
	List<TbUser> selectAllUser();
}