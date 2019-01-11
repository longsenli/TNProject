package com.tnpy.mes.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbRoleMapper;
import com.tnpy.mes.model.mysql.IndustrialPlant;
import com.tnpy.mes.model.mysql.TbRole;
/**
 * 
 * @author 2018122008
 *
 */
@Service("roleManageService")
public class RoleManageServiceImpl implements IRoleManageService {
	
	@Autowired
	private TbRoleMapper roleMapper;
	

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
//            List<TbRole> roles = roleMapper.listRoles();
        	int i = roleMapper.insert(role);
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
	
}
