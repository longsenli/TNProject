package com.tnpy.mes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.mapper.sqlserver.TbUserSqlServerMapper;
import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.model.sqlserver.TbUserSqlServer;

@RestController
@RequestMapping(value="/sqlserver")
public class TbUserSqlserverController {
	@Autowired
	private TbUserSqlServerMapper tbUserSqlServerMapper;
	@Autowired
	private TbUserMapper tbuserMapper;
	
	
	@RequestMapping(value="/user")
	public TbUser user(String id) {
		return tbuserMapper.selectByPrimaryKey(id);
	}
	
	
	@RequestMapping(value="/getsqlserverTbuser",method=RequestMethod.GET)
	public TbUserSqlServer getsqlserverTbuser(String userId) {
		return tbUserSqlServerMapper.selectByPrimaryKey(userId);
	}
}
