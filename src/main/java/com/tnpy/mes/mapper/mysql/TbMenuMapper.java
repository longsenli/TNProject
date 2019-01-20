package com.tnpy.mes.mapper.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tnpy.mes.model.mysql.TbMenu;

public interface TbMenuMapper {
	public int deleteByPrimaryKey(Integer menuId);

	public int insert(TbMenu record);

	public int insertSelective(TbMenu record);

	public TbMenu selectByPrimaryKey(Integer menuId);

	public int updateByPrimaryKeySelective(TbMenu record);

	public int updateByPrimaryKey(TbMenu record);
    
//  @Select("select * from tb_role ")
	public List<TbMenu> listMenus();
	public List<TbMenu> listUserMenus(String userID);
//    @Select("select * from tb_menu where menu_type='M' ")
	public List<TbMenu> listFolders();
//  @Select("select * from tb_menu where menu_type='M' ")
	public List<TbMenu> getAllParentMenuList();
	public List<TbMenu> getSubMenuByParentId(long ParentId);
    /**
     * 根据角色ID查询菜单
     * 
     * @param roleId 角色ID
     * @return 菜单列表
     */
    public List<String> selectMenuTree(Integer roleId);

}