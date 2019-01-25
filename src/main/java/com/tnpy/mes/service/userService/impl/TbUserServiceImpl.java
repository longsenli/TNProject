package com.tnpy.mes.service.userService.impl;

import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.service.userService.ITbUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TbUserServiceImpl implements ITbUserService {

	@Resource
	TbUserMapper tbUserMapper;

	@Override
	public TbUser getUserInfo(String userid) {
		return tbUserMapper.selectByPrimaryKey(userid);
	}
}
