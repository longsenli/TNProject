package com.tnpy.mes.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.service.ITbUserService;

@RestController
@RequestMapping(value = "/api")
public class LoginController {

	@Resource
	ITbUserService tbUserService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password) {
		TbUser tbUser = tbUserService.getUserInfo(username);
		if (tbUser != null && tbUser.getPassword().equals(password)) {
			return "right";
		} else {
			return "wrong";
		}
	}
}
