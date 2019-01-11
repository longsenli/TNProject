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
//    @Select("select * from tb_menu where menu_type='M' ")
	public List<TbMenu> listFolders();
//  @Select("select * from tb_menu where menu_type='M' ")
	public List<TbMenu> getAllParentMenuList();
	public List<TbMenu> getSubMenuByParentId(long ParentId);
}