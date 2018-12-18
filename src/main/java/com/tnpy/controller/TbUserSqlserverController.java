package com.tnpy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tnpy.domain.mysql.Store;
import com.tnpy.domain.sqlserver.TbUserSqlServer;
import com.tnpy.mapper.mysql.StoreMapper;
import com.tnpy.mapper.sqlserver.TbUserSqlServerMapper;

@RestController
@RequestMapping(value="/sqlserver")
public class TbUserSqlserverController {
	@Autowired
	private TbUserSqlServerMapper tbUserSqlServerMapper;
	@Autowired
	private StoreMapper storemapper;
	@RequestMapping(value="/getsqlserverTbuser",method=RequestMethod.GET)
	public TbUserSqlServer getsqlserverTbuser(String userId) {
		return tbUserSqlServerMapper.selectByPrimaryKey(userId);
	}
	@RequestMapping(value="/store",method=RequestMethod.GET)
	public Store store(Byte storeId) {
		return storemapper.selectByPrimaryKey(storeId);
	}
}
