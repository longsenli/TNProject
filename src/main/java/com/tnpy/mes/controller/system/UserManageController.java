package com.tnpy.mes.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.model.mysql.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//import com.tnpy.mes.mapper.mysql.TbUserMapper;
//import com.tnpy.mes.model.mysql.TbUser;



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


    @RequestMapping(value = "/getUserInfoByParam")
    public TNPYResponse getUserInfoByParam(String plantID,String processID) {
        TNPYResponse result = new TNPYResponse();
        try
        {
            String filter = " where state = '1'  ";
            String columnList = " userID,name ";
            if(!"-1".equals(plantID))
            {
                filter += " and industrialplant_id ='" + plantID + "'";
            }
            if(!"-1".equals(processID))
            {
                filter += " and productionprocess_id ='" + processID + "'";
            }

            filter += " order by  name" ;
            List<Map<Object,Object>> users =userService.selecUserInfoByfilter(columnList,filter);
            result.setStatus(1);
            result.setData(JSONObject.toJSONString(users, SerializerFeature.WriteMapNullValue).toString());
            // System.out.println(result);
            return  result;
        }
        catch (Exception ex)
        {
            result.setMessage("查询出错！" + ex.getMessage());
            return  result;
        }
    }



    @RequestMapping(value = "/pageInfo")
  public TNPYResponse getEquipmentInfo(String typeID,String plantID) {
      TNPYResponse result = new TNPYResponse();
     try
     {
    	 List<TbUser> users =userService.userList();
         result.setStatus(1);
         result.setData(JSONObject.toJSONString(users, SerializerFeature.WriteMapNullValue).toString());
        // System.out.println(result);
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
     try
     {
//         List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectByType(typeID,plantID);
    	 //uuid随机生成userId
//    	 String uuid = UUID.randomUUID().toString(); 
//    	 user.setUserid(uuid);
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
	try
    {
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
	   // System.out.println(id);
//	    userService.deleteUser(id);
	    userService.deleteByPrimaryKey(id);
	}
	JSONObject result = new JSONObject();
	result.put("state", "success");
	return result;
  }
  
  
  @PostMapping(value = "/selectAll", headers = "Accept=application/json")
  public TNPYResponse selectAll(@RequestBody PageInfo pageInfo) {
	  TNPYResponse result = new TNPYResponse();
	     try
	     {
	         PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
//	         Page page = new Page();
//	         List<Page> list = pageMapper.selectAll(page);
	         List<TbUser> users =userService.userList();
	         PageInfo pageInfo1 = new PageInfo(users);
	         result.setStatus(1);
	         result.setData(JSONObject.toJSONString(pageInfo1, SerializerFeature.WriteMapNullValue).toString());
	         //System.out.println(result);
	         return  result;
	     }
	     catch (Exception ex)
	     {
	         result.setMessage("查询出错！" + ex.getMessage());
	         return  result;
	     }
  }
  
  /**
	 * 按用户名搜索
	 * @param TbUser
	 */
  @ResponseBody
  @RequestMapping("/searchUser")
  public TNPYResponse searchUser(String name) {
	 TNPYResponse result = new TNPYResponse();
     try
     {
    	 List<TbUser> users = userService.selectByName(name);
         result.setStatus(1);
         result.setData(JSONObject.toJSONString(users, SerializerFeature.WriteMapNullValue).toString());
         return  result;
     }
     catch (Exception ex)
     {
    	 ex.printStackTrace();
         result.setMessage("查询出错！" + ex.getMessage());
         return  result;
     }
  }
}
