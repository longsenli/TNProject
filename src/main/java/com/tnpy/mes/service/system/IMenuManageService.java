package com.tnpy.mes.service.system;
/**
 * 系统管理下菜单管理服务接口
 * 20190104 14:50
 * @author hzp
 * 
 */

import java.util.List;
import java.util.Map;

import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.model.mysql.TbMenu;
import com.tnpy.mes.model.mysql.TbRole;

public interface IMenuManageService {
	public List<TbMenu> listMenus();
	public List<TbMenu> listFolders();
	public TNPYResponse selectById(String menuId);
	public TNPYResponse addMenu(TbMenu Menu);
	public TNPYResponse updateMenu(TbMenu Menu);
	public TNPYResponse deleteMenu(String ids);
	public TNPYResponse getAllMenuList();
	public TNPYResponse roleMenuTreeData(TbRole role);
	TNPYResponse getUserMenuList();
}
