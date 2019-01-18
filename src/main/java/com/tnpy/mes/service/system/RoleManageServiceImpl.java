package com.tnpy.mes.service.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbRoleMapper;
import com.tnpy.mes.mapper.mysql.TbRoleMenuMapper;
import com.tnpy.mes.model.mysql.TbRole;
import com.tnpy.mes.model.mysql.TbRoleMenu;
/**
 * 
 * @author 2018122008
 *
 */
@Service("roleManageService")
public class RoleManageServiceImpl implements IRoleManageService {
	
	@Autowired
	private TbRoleMapper roleMapper;
	@Autowired
	private TbRoleMenuMapper roleMenuMapper;

	@Override
	public TNPYResponse listRoles() {
		TNPYResponse result = new TNPYResponse();
        try
        {
            List<TbRole> roles = roleMapper.listRoles();
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(roles).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
	}

	@Override
	public TNPYResponse addRole(TbRole role) {
		TNPYResponse result = new TNPYResponse();
        try
        {
        	int i = roleMapper.insert(role);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
	}

	@Override
	public TNPYResponse updateRole(TbRole role) {
		TNPYResponse result = new TNPYResponse();
        try
        {
//            List<TbRole> roles = roleMapper.listRoles();
        	int i = roleMapper.updateByPrimaryKey(role);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
//            result.setData(JSONObject.toJSON(roles).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
	}

	@Override
	public TNPYResponse deleteRole(String ids) {
		TNPYResponse result = new TNPYResponse();
        try
        {	
        	int i = roleMapper.deleteByPrimaryKey(Integer.parseInt(ids));
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
	}
	
    /**
              *保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
//    @Override
    public TNPYResponse saveRole(TbRole role)
    {
    	
    	
    	TNPYResponse result = new TNPYResponse();
        try
        {	
        	Integer roleId = role.getRoleId();
            if (roleId!=null)
            {
//                role.setUpdateBy(ShiroUtils.getLoginName());
                // 修改角色信息
//                roleMapper.updateRole(role);
//                // 删除角色与菜单关联
//                roleMenuMapper.deleteRoleMenuByRoleId(roleId);
            }else
            {
//              role.setCreateBy(ShiroUtils.getLoginName());
              // 新增角色信息
              roleMapper.insert(role);
              insertRoleMenu(role);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
        
    }
	
    
    /**
     * 新增角色菜单信息
     * 
     * @param user 角色对象
     */
    public int insertRoleMenu(TbRole role)
    {
        int rows = 1;
        // 新增用户与角色管理
        List<TbRoleMenu> list = new ArrayList<TbRoleMenu>();
        for (Long menuId : role.getMenuIds())
        {
        	TbRoleMenu rm = new TbRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId.toString());
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }
}
