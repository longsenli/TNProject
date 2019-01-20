package com.tnpy.mes.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.TreeNode.Node;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbMenuMapper;
import com.tnpy.mes.model.mysql.TbMenu;
import com.tnpy.mes.model.mysql.TbRole;
/**
 * 
 * @author 2018122008
 *
 */
@Service("menuManageService")
public class MenuManageServiceImpl implements IMenuManageService {
	
	@Autowired
	private TbMenuMapper menuMapper;
	

	@Override
	public List<TbMenu> listMenus() {
		TNPYResponse result = new TNPYResponse();
        try
        {
            List<TbMenu> menus = menuMapper.listMenus();
//            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
//            result.setData(JSONObject.toJSON(menus).toString());
            return  menus;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  null;
        }
        
	}

	@Override
	public TNPYResponse addMenu(TbMenu menu) {
		TNPYResponse result = new TNPYResponse();
        try
        {
//            List<TbMenu> Menus = MenuMapper.listMenus();
        	int i = menuMapper.insert(menu);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
//          result.setData(JSONObject.toJSON(Menus).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            ex.printStackTrace();
            return  result;
        }
	}

	@Override
	public TNPYResponse updateMenu(TbMenu menu) {
		TNPYResponse result = new TNPYResponse();
        try
        {
//            List<TbMenu> Menus = MenuMapper.listMenus();
        	int i = menuMapper.updateByPrimaryKey(menu);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
//            result.setData(JSONObject.toJSON(Menus).toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            ex.printStackTrace();
            return  result;
        }
	}

	@Override
	public TNPYResponse deleteMenu(String ids) {
		TNPYResponse result = new TNPYResponse();
        try
        {	
        	int i = menuMapper.deleteByPrimaryKey(Integer.parseInt(ids));
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
	public List<TbMenu> listFolders() {
		try
        {
            List<TbMenu> menus = menuMapper.listFolders();
            return  menus;
        }
        catch (Exception ex)
        {
            return  null;
        }
	}

	@Override
	public TNPYResponse selectById(String menuId) {
		TNPYResponse result = new TNPYResponse();
        try
        {
        	menuId = menuId==null?"":menuId;
            TbMenu menu = menuMapper.selectByPrimaryKey(Integer.parseInt(menuId));
            List<TbMenu> menus = new ArrayList<TbMenu>();
            menus.add(menu);
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(menus).toString());
            System.out.println("返回TNResponse: " + result.toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
	}
	/**
	 * bootstrap treegrid
	 */
	@Override
	public TNPYResponse getAllMenuList() {
		TNPYResponse result = new TNPYResponse();
        try
        {
//        	List<TbMenu> allMenu = menuMapper.getAllParentMenuList();
//        	for(TbMenu m:allMenu){
//    			List<TbMenu> child = menuMapper.getSubMenuByParentId(m.getMenuId());
//    			m.setChildren(child);
//    		}
        	
        	List<Node> nodes = new ArrayList<Node>();
        	nodes.add(getTreeJson());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(nodes).toString());
            System.out.println("返回TNResponse: " + result.getData().toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
	}
	
	/**
	 * bootstrap treegrid
	 */
	@Override
	public TNPYResponse getUserMenuList() {
		TNPYResponse result = new TNPYResponse();
        try
        {
//        	List<TbMenu> allMenu = menuMapper.getAllParentMenuList();
//        	for(TbMenu m:allMenu){
//    			List<TbMenu> child = menuMapper.getSubMenuByParentId(m.getMenuId());
//    			m.setChildren(child);
//    		}
        	
        	List<Node> nodes = new ArrayList<Node>();
        	nodes.add(getTreeJson());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(nodes).toString());
            System.out.println("返回TNResponse: " + result.getData().toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
	}
	
	
	
    public Node getTreeJson() {
    	List<TbMenu> allMenu = menuMapper.listMenus();//从数据库获取所有资源
//    	List<TbMenu> allMenu = menuMapper.listUserMenus();//从数据库获取当前所有菜单
        List<Node> nodes = new ArrayList<Node>();//把所有资源转换成树模型的节点集合，此容器用于保存所有节点
        for(TbMenu tbmenu : allMenu){
            Node node = new Node();
            node.setHref(tbmenu.getUrl());
            node.setIcon(tbmenu.getIcon());
            node.setNodeId(tbmenu.getMenuId().toString());
            node.setPid(tbmenu.getParentId().toString());
            node.setText(tbmenu.getMenuName());
            nodes.add(node);//添加到节点容器
        }
        Node tree = new Node();//重要插件，创建一个树模型
        Node mt = tree.createTree(nodes);//Node类里面包含了一个创建树的方法。这个方法就是通过节点的信息（nodes）来构建一颗多叉树manytree->mt。
        //System.out.println(tree.iteratorTree(mt));
        return mt;
    }
	
    /**
     * 根据角色ID查询菜单
     * 
     * @param role 角色对象
     * @return 菜单列表
     */
    @Override
    public TNPYResponse roleMenuTreeData(TbRole role)
    {
    	TNPYResponse result = new TNPYResponse();
        try
        {
        	 Integer roleId = role.getRoleId();
             List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
             List<TbMenu> menuList = menuMapper.listMenus();
             if (roleId!=null)
             {
                 List<String> roleMenuList = menuMapper.selectMenuTree(roleId);
                 trees = getTrees(menuList, true, roleMenuList, true);
             }
             else
             {
                 trees = getTrees(menuList, false, null, true);
             }
//        	List<TbMenu> allMenu = menuMapper.getAllParentMenuList();
//        	for(TbMenu m:allMenu){
//    			List<TbMenu> child = menuMapper.getSubMenuByParentId(m.getMenuId());
//    			m.setChildren(child);
//    		}
        	
//        	List<Node> nodes = new ArrayList<Node>();
//        	nodes.add(getTreeJson());
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setData(JSONObject.toJSON(trees).toString());
            System.out.println("返回TNResponse: " + result.getData().toString());
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }
    /**
     * 对象转菜单树
     * 
     * @param menuList 菜单列表
     * @param isCheck 是否需要选中
     * @param roleMenuList 角色已存在菜单列表
     * @param permsFlag 是否需要显示权限标识
     * @return
     */
    public List<Map<String, Object>> getTrees(List<TbMenu> menuList, boolean isCheck, List<String> roleMenuList,
            boolean permsFlag)
    {
        List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
        for (TbMenu menu : menuList)
        {
            Map<String, Object> deptMap = new HashMap<String, Object>();
            deptMap.put("id", menu.getMenuId());
            deptMap.put("pId", menu.getParentId());
            deptMap.put("name", menu.getMenuName());
            if (isCheck)
            {
                deptMap.put("checked", roleMenuList.contains(menu.getMenuId() + menu.getPerms()));
            }
            else
            {
                deptMap.put("checked", false);
            }
            trees.add(deptMap);
        }
        return trees;
    }
}
