package com.tnpy.mes.service.system;
/**
 * 系统管理下角色管理服务接口
 * 20190104 14:50
 * @author hzp
 * 
 */

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.model.mysql.TbRole;

public interface IRoleManageService {
	public TNPYResponse listRoles();
	public TNPYResponse addRole(TbRole role);
	public TNPYResponse updateRole(TbRole role);
	public TNPYResponse deleteRole(String ids);
}
