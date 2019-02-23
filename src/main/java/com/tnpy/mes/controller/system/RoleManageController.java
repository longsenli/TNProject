package com.tnpy.mes.controller.system;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.model.mysql.TbRole;
import com.tnpy.mes.service.system.IRoleManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleManageController {
	@Autowired
//  private UserService userService;
	private IRoleManageService roleManageService;

	@RequestMapping(value = "/listRoles")
	public TNPYResponse listRoles() {
		//System.out.println(roleManageService.listRoles());
		return roleManageService.listRoles();
	}

	/**
	 * 新增角色
	 * 
	 * @param TbRole
	 */
	@ResponseBody
	@RequestMapping("/addRole")
	public TNPYResponse addRole(TbRole role) {
//		return roleManageService.addRole(role);
		return roleManageService.saveRole(role);
	}

	@ResponseBody
	@RequestMapping("/updateRole")
	public TNPYResponse updateRole(TbRole role) {
//		return roleManageService.updateRole(role);
		return roleManageService.saveRole(role);
	}

	@ResponseBody
	@RequestMapping("/deleteRole")
	public TNPYResponse deleteRole(String ids) {
		return roleManageService.deleteRole(ids);
	}
}