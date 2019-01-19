package com.tnpy.mes.controller.system;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.model.mysql.IndustrialPlant;
import com.tnpy.mes.model.mysql.TbMenu;
import com.tnpy.mes.model.mysql.TbRole;
import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.service.system.IMenuManageService;

@RestController
//@Controller
@RequestMapping("/menu")
public class MenuManageController {
	@Autowired
	private IMenuManageService menuManageService;

	@RequestMapping(value = "/listMenus")
	public JSONArray listMenus() {
		System.out.println("listmenus");
		List<TbMenu> list = menuManageService.listMenus();
//		1、使用JSONObject
//        JSONObject listObject=JSONObject.toJavaObject(list);
//        2、使用JSONArray
        JSONArray listArray=JSONArray.parseArray(JSON.toJSONString(list));
//        JSONObject.parse
        //System.out.println("listObject:"+listObject.toString());
        System.out.println(listArray);
		
        return listArray;
//		System.out.println(menuManageService.listMenus());
//		return menuManageService.listMenus();
//		TNPYResponse result = new TNPYResponse();
//		try {
//			List<TbUser> users = userService.userList();
//			result.setStatus(1);
//			result.setData(JSONObject.toJSONString(users, SerializerFeature.WriteMapNullValue).toString());
//			return result;
//		} catch (Exception ex) {
//			result.setMessage("查询出错！" + ex.getMessage());
//			return result;
//		}
	}
	@RequestMapping(value = "/listFolders")
	public JSONArray listFolders() {
		System.out.println("listFolders");
		List<TbMenu> list = menuManageService.listFolders();
        JSONArray listArray=JSONArray.parseArray(JSON.toJSONString(list));
//        JSONObject.parse
        //System.out.println("listObject:"+listObject.toString());
        System.out.println(listArray);
		
        return listArray;
//		System.out.println(menuManageService.listMenus());
//		return menuManageService.listMenus();
//		TNPYResponse result = new TNPYResponse();
//		try {
//			List<TbUser> users = userService.userList();
//			result.setStatus(1);
//			result.setData(JSONObject.toJSONString(users, SerializerFeature.WriteMapNullValue).toString());
//			return result;
//		} catch (Exception ex) {
//			result.setMessage("查询出错！" + ex.getMessage());
//			return result;
//		}
	}
	
	
	/**
	 * 按照主键查找
	 * 
	 * @param String menuId
	 */
	
	@ResponseBody
	@RequestMapping(value = "/listByMenuId")
	public TNPYResponse listByMenuId(String menuId) {
		System.out.println("listByMenuId 被调用了");
		return menuManageService.selectById(menuId);
	}

	/**
	 * 新增菜单
	 * 
	 * @param TbMenu
	 */
	@ResponseBody
	@RequestMapping("/addMenu")
	public TNPYResponse addMenu(TbMenu Menu) {
		return menuManageService.addMenu(Menu);
	}

	@ResponseBody
	@RequestMapping("/updateMenu")
	public TNPYResponse updateMenu(TbMenu Menu) {
		return menuManageService.updateMenu(Menu);
	}

	@ResponseBody
	@RequestMapping("/deleteMenu")
	public TNPYResponse deleteMenu(String menuId) {
		return menuManageService.deleteMenu(menuId);
//		return null;
	}
	@ResponseBody
	@RequestMapping("/getAllMenuList")
	public TNPYResponse getAllMenuList(String menuId) {
		return menuManageService.getAllMenuList();
//		return null;
	}
	@ResponseBody
	@RequestMapping("/roleMenuTreeData")
	public TNPYResponse roleMenuTreeData(TbRole role) {
		System.out.println(role.getRoleId());
		return menuManageService.roleMenuTreeData(role);
//		return null;
	}
//	@RequestMapping("/getAllMenuList")
//	public String getAllMenuList(Model model) {
//		TNPYResponse tnrs = menuManageService.getAllMenuList();
//		model.addAttribute("menus", tnrs);
//      model.addAttribute("user", "test");
//      model.addAttribute("copyrightYear", "2018-02-02");
//		return "/system/menu/myindex2";
////		return null;
//	}
	
	
	
	
	
	
	
//	// 系统首页
//    @GetMapping("/index")
//    public String index(Model model)
//    {
//        // 取身份信息
//        User user = getUser();
//        // 根据用户id取出菜单
//        List<Menu> menus = menuService.selectMenusByUserId(user.getUserId());
//        model.addAttribute("menus", menus);
//        model.addAttribute("user", user);
//        model.addAttribute("copyrightYear", ruoYiConfig.getCopyrightYear());
//        return "index";
//    }
	
	
	
	
	
	
	
	
	
	

//	    @RequestMapping("menu")
//	    public void getMenu(HttpServletRequest request,HttpServletResponse response) throws Exception{
//	        json.setResult("no");
//	        Node tree = getTreeJson();//获得一棵树模型的数据
//	        //json.setData(tree);
//	        json.setList(tree.getNodes());
//	        json.setResult("ok");
//	        response.getWriter().println(mapper.writeValueAsString(json));//把json数据写回前台
//	    }
//	    
//
//	     public Node getTreeJson() {
//	         List<Resource> reslist = resourceService.loadAll();//从数据库获取所有资源
//	         List<Node> nodes = new ArrayList<Node>();//把所有资源转换成树模型的节点集合，此容器用于保存所有节点
//	         for(Resource res : reslist){
//	             Node node = new Node();
//	             node.setHref(res.getUrl());
//	             node.setIcon(res.getIcon());
//	             node.setNodeId(res.getNodeId());
//	             node.setPid(res.getPid());
//	             node.setText(res.getName());
//	             nodes.add(node);//添加到节点容器
//	         }
//	         Node tree = new Node();//重要插件，创建一个树模型
//	         Node mt = tree.createTree(nodes);//Node类里面包含了一个创建树的方法。这个方法就是通过节点的信息（nodes）来构建一颗多叉树manytree->mt。
//	         //System.out.println(tree.iteratorTree(mt));
//	         return mt;
//	     }
	    
}




