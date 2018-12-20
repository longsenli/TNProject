package com.tnpy.mes.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.service.ITbUserService;

@Service
public class TbUserServiceImpl implements ITbUserService {

	@Resource
	TbUserMapper tbUserMapper;

	@Override
	public TbUser getUserInfo(String userid) {
		return tbUserMapper.selectByPrimaryKey(userid);
	}
}
