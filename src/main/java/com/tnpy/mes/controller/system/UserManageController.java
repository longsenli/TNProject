package com.tnpy.mes.controller.system;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tnpy.common.utils.encryption.Encryption;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.model.mysql.EquipmentInfo;
//import com.tnpy.mes.mapper.mysql.TbUserMapper;
//import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.model.mysql.TbUser;



@RestController
@RequestMapping("/user")
public class UserManageController {
  @Autowired
//  private UserService userService;
  private TbUserMapper userService;
  
  
//  @RequestMapping("/insert")
//  public void insert(TbUser user) {
//	System.out.println("before");
//	userService.addUser(user);
//	System.out.println("after");
//  }
//  @RequestMapping("/index")
//  public String index(TbUser user) {
//	return "user1";
//  }
//  @ResponseBody
//  @RequestMapping(value="getUser",produces="html/text;charset=UTF-8")
//  public  String queryUserList() {
//	List<TbUser> users =userService.getUserList();
//	System.out.println();
//	JSONObject result = new JSONObject();
//	result.put("rows", users);
//	System.out.println(result.toJSONString());
//	return result.toJSONString();
//  }
//  @ResponseBody
//  @RequestMapping("/pageInfo")
//  public  TNPYResponse pageInfo(@RequestParam int pageNumber,int pageSize) {
//	  TNPYResponse rs = new TNPYResponse();
//	  try {
//		  List<TbUser> users =userService.userList();
//		  int total = users.size();
//		  PageHelper.startPage(pageNumber,pageSize);
////		List<TbUser> pageInfo=userService.userList();
//		  PageInfo<TbUser> pageInfo = new PageInfo<>(users);
//		  JSONObject result = new JSONObject();
//		  result.put("rows",pageInfo);
//		  result.put("total",total);
//		  System.out.println(result.toJSONString());
//		  rs.setStatus(1);
//		  rs.setData(result.toJSONString());
//	  }catch(Exception ex)
//	     {
//		  rs.setMessage("查询出错！" + ex.getMessage());
//	      return  rs;
//	     }
//		return rs;
//  }
  
  
  
  
  
  @RequestMapping(value = "/pageInfo")
  public TNPYResponse getEquipmentInfo(String typeID,String plantID) {
      TNPYResponse result = new TNPYResponse();
     try
     {
    	 List<TbUser> users =userService.userList();
         result.setStatus(1);
         result.setData(JSONObject.toJSONString(users, SerializerFeature.WriteMapNullValue).toString());
         System.out.println(result);
         return  result;
     }
     catch (Exception ex)
     {
         result.setMessage("查询出错！" + ex.getMessage());
         return  result;
     }
  }

  
  /**
 * 新增用户
 * @param TbUser
 */
  @ResponseBody
  @RequestMapping("/addUser")
  public TNPYResponse addUser(TbUser user) {
	 TNPYResponse result = new TNPYResponse();
	 Encryption encryption = new Encryption();
     try
     {
//         List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectByType(typeID,plantID);
    	 //uuid随机生成userId
//    	 String uuid = UUID.randomUUID().toString(); 
//    	 user.setUserid(uuid);
    	 user.setPassword(encryption.encrypt(user.getPassword(),null));
    	 int i = userService.insert(user);
         result.setStatus(1);
//         result.setData(JSONObject.toJSONString(equipmentInfoList, SerializerFeature.WriteMapNullValue).toString());
         return  result;
     }
     catch (Exception ex)
     {
    	 ex.printStackTrace();
         result.setMessage("查询出错！" + ex.getMessage());
         return  result;
     }
  }
  @ResponseBody
  @RequestMapping("/updateUser")
  public TNPYResponse updateUser(TbUser user) {
	TNPYResponse result = new TNPYResponse();
	Encryption encryption = new Encryption();
	try
    {
	user.setPassword(encryption.encrypt(user.getPassword(),null));
   	 int i = userService.updateByPrimaryKey(user);
        result.setStatus(1);
//        result.setData(JSONObject.toJSONString(equipmentInfoList, SerializerFeature.WriteMapNullValue).toString());
        return  result;
    }
    catch (Exception ex)
    {
   	 	ex.printStackTrace();
        result.setMessage("查询出错！" + ex.getMessage());
        return  result;
    }
  }
  @ResponseBody
  @RequestMapping("/deleteUser")
  public JSONObject deleteUser(HttpServletRequest request) {
	String[] list=request.getParameterValues("ids");
	System.out.println(list);
	for (int i = 0; i < list.length; i++) {
	    String id = list[i];
	    System.out.println(id);
//	    userService.deleteUser(id);
	    userService.deleteByPrimaryKey(id);
	    
	}
	
	JSONObject result = new JSONObject();
	result.put("state", "success");
	return result;
  }
}
