package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.token.Token;
import com.tnpy.common.utils.token.TokenUtil;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TokenMapper;
import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.service.ITbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api")
public class LoginController {

	@Resource
	ITbUserService tbUserService;
	@Autowired
	private TokenMapper tokenmapper;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TNPYResponse login(@RequestParam(value = "username") String username,
							  @RequestParam(value = "password") String password) {
		TNPYResponse response = new TNPYResponse();

		//判断用户信息为空
		if ("".equals(username.trim()) || "".equals(password.trim())) {
			response.setMessage("传入的用户名/密码为空！");
			return response;
		}

		//根据客户用户名查找数据库的usre对象
		TbUser myUser = tbUserService.getUserInfo(username);

		//判断用户不存在
		if (null == myUser) {
			response.setMessage("用户不存在");
			return response;
		}
		//判断用户不存在
		if (!password.trim().equals(myUser.getPassword())) {
			response.setMessage("密码不正确");
			return response;
		}

		//根据数据库的用户信息查询Token
		Token token =  tokenmapper.findByUserId(myUser.getUserid());

		try
		{
			TokenUtil tokenUtil = new  TokenUtil();
			//生成Token
			if (null == token) {

				//第一次登陆
				token =tokenUtil.creatToken(username) ;
				tokenUtil.InsertToken(token);
			}else{
				//登陆就更新Token信息

				token = tokenUtil.creatToken(username) ;

				tokenUtil.UpdateToken(token);
			}
		}
		catch (Exception ex)
		{

			response.setMessage("登录失败" + ex.getMessage());
			return  response;
		}
		//返回Token信息给客户端

		response.setStatus(1);
		response.setMessage(myUser.getName());
		response.setToken(JSONObject.toJSON(token).toString());
		return response;
	}
}
