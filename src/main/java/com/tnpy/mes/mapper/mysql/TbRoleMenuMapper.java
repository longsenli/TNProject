package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TbRoleMenu;
import java.util.List;

/**
 * 角色与菜单关联表 数据层
 * 
 * @author hzp
 */
public interface TbRoleMenuMapper
{

    /**
     * 通过角色ID删除角色和菜单关联
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleMenuByRoleId(Integer roleId);
    
    /**
     * 批量删除角色菜单关联信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleMenu(Long[] ids);
    
    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    public int selectCountRoleMenuByMenuId(Long menuId);
    
    /**
     * 批量新增角色菜单信息
     * 
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    public int batchRoleMenu(List<TbRoleMenu> roleMenuList);

}
