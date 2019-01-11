package com.tnpy.mes.service.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.util.TreeNode.Node;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbMenuMapper;
import com.tnpy.mes.model.mysql.TbMenu;
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
	
	
	
	
    public Node getTreeJson() {
    	List<TbMenu> allMenu = menuMapper.listMenus();//从数据库获取所有资源
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
	
}
