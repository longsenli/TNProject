package com.tnpy.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tnpy.dao.TbUserMapper;
import com.tnpy.pojo.TbUser;
import com.tnpy.service.ITbUserService;

@Service
public class TbUserServiceImpl implements ITbUserService {

	@Resource
	TbUserMapper tbUserMapper;

	@Override
	public TbUser getUserInfo(String userid) {
		return tbUserMapper.selectByPrimaryKey(userid);
	}
}
