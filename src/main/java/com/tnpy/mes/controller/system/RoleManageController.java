package com.tnpy.mes.controller.system;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.model.mysql.TbRole;
import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.service.system.IRoleManageService;

@RestController
@RequestMapping("/role")
public class RoleManageController {
	@Autowired
//  private UserService userService;
	private IRoleManageService roleManageService;

	@RequestMapping(value = "/listRoles")
	public TNPYResponse listRoles() {
		System.out.println(roleManageService.listRoles());
		return roleManageService.listRoles();
//		TNPYResponse result = new TNPYResponse();
//		try {
//			List<TbUser> users = userService.userList();
//			result.setStatus(1);
//			result.setData(JSONObject.toJSONString(users, SerializerFeature.WriteMapNullValue).toString());
//			return result;
//		} catch (Exception ex) {
//			result.setMessage("查询出错！" + ex.getMessage());
//			return result;
//		}
	}

	/**
	 * 新增角色
	 * 
	 * @param TbRole
	 */
	@ResponseBody
	@RequestMapping("/addRole")
	public TNPYResponse addRole(TbRole role) {
		return roleManageService.addRole(role);
	}

	@ResponseBody
	@RequestMapping("/updateRole")
	public TNPYResponse updateRole(TbRole role) {
		return roleManageService.updateRole(role);
	}

	@ResponseBody
	@RequestMapping("/deleteRole")
	public TNPYResponse deleteRole(String ids) {
		return roleManageService.deleteRole(ids);
//		return null;
	}
}