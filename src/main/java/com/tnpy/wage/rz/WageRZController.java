package com.tnpy.wage.rz;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.encryption.Encryption;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbUserMapper;
import com.tnpy.mes.mapper.mysql.TbWageDetailRZMapper;
import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.model.mysql.TbWageDetailRZ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: curry
 * @Date: 2018/8/16
 */
@RestController
public class WageRZController {

    @Autowired
    private WageRZService importService;
    @Autowired
    private TbWageDetailRZMapper tbWageDetailRZMapper;
    @Autowired
    private TbUserMapper tbUserMapper;
    
    @PostMapping(value = "/rzwageupload")
    @ResponseBody
    public TNPYResponse uploadExcel(HttpServletRequest request) throws Exception {
    	TNPYResponse result = new TNPYResponse();
    	try {
    		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("filename");
            if (file.isEmpty()) {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage("文件不能为空");
                return  result;
            }
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MONTH, -1);
            Date m = c.getTime();
            String mon = format.format(m);
            System.out.println("过去一个月："+mon);
            
            String filter = "where senddate = '"+mon+"' limit 1";
            List<LinkedHashMap<String, String>> isexists = tbWageDetailRZMapper.selectByPrimaryKey1(filter);
            if (isexists.size()>0) {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage(mon+ "月份工资数据已经上传过啦!");
                return  result;
            }
            
            //获取所有user表记录
            List<TbUser> ltbuser = tbUserMapper.selectAllUser();
            
            
            InputStream inputStream = file.getInputStream();
            List<List<Object>> list = importService.getBankListByExcel(inputStream, file.getOriginalFilename());
            inputStream.close();
            for (int i = 0; i < list.size(); i++) {
                List<Object> lo = list.get(i);
            	String uuid = UUID.randomUUID().toString();
            	uuid = uuid.replace("-", "");
            	TbWageDetailRZ tbwage = new TbWageDetailRZ();
            	tbwage.setId(uuid);//主键
//                	tbwage.setSenddate(lo.get(0).toString());//发放月份
            	if(lo.get(0).toString().equals(mon)) {
            		tbwage.setSenddate(lo.get(0).toString());//发放月份
            	}else {
            		tbwage.setSenddate(mon);//发放月份
            	}
            	tbwage.setName(lo.get(1).toString());//姓名 
            	tbwage.setCardno(lo.get(2).toString());//身份证号
            	tbwage.setDepartment(lo.get(3).toString());//结算部门
            	tbwage.setWage(lo.get(4).toString());//工资
            	tbwage.setOvertimepay(lo.get(5).toString());//加班工资
            	tbwage.setPerformancebonus(lo.get(6).toString());//绩效奖金
            	tbwage.setSubsidyincome(lo.get(7).toString());//补贴收入
            	tbwage.setMilitarytrainingincome(lo.get(8).toString());//军训收入
            	tbwage.setWagesupplement(lo.get(9).toString());//工资补差
            	tbwage.setUtilities(lo.get(10).toString());//水电气费补贴
            	tbwage.setHousingallowance(lo.get(11).toString());//住房补贴
            	tbwage.setHightemperaturesubsidy(lo.get(12).toString());//高温补贴
            	tbwage.setSeniorityallowance(lo.get(13).toString());//厂龄补贴
            	tbwage.setShouldsendsalary(lo.get(14).toString());//应发工资
            	tbwage.setSocialinsurance(lo.get(15).toString());//减：社会保险
            	tbwage.setReservedfunds(lo.get(16).toString());//减：公积金
            	tbwage.setOthercutpayment(lo.get(17).toString());//其他扣款
            	tbwage.setUtilitiescut(lo.get(18).toString());//扣：水电气费
            	tbwage.setLovedonation(lo.get(19).toString());//爱心费
            	tbwage.setIndividualincometax(lo.get(20).toString());//代扣个人所得税
            	tbwage.setTakehomepay(lo.get(21).toString());//实发工资
            	tbwage.setUserid(lo.get(22).toString());
            	
            	
            	if((lo.get(22).toString()==null||lo.get(22).toString().equals(""))) {
        			result.setMessage("上传出错, 有员工工号为空");
        	        return  result;
        		}
            	if(!isexistsUser(ltbuser, lo.get(22).toString())) {
        			TbUser tbuserfu = new TbUser();
        			tbuserfu.setCardno(lo.get(2).toString());
        			tbuserfu.setUserid(lo.get(22).toString());
        			tbuserfu.setName(lo.get(1).toString());
        			tbuserfu.setPassword(lo.get(2).toString().substring(lo.get(2).toString().length() - 6));
        			tbUserMapper.insertSelective(tbuserfu);
        		}
            	if(isexistsUser(ltbuser, lo.get(22).toString())) {
        			for(int j=0;j<ltbuser.size();j++) {
	            		//如果用户存在则不插入新记录
	            		if(lo.get(2).toString().equals(ltbuser.get(j).getCardno())&&lo.get(22).toString().equals(ltbuser.get(j).getUserid())) {
	            			continue;
	            		}
            			//如果userid存在, 但身份证号不存在则更新
                		if((ltbuser.get(j).getCardno()== null || ltbuser.get(j).getCardno().equals("")) &&lo.get(22).toString().equals(ltbuser.get(j).getUserid())) {
                			TbUser tbuserfu = ltbuser.get(j);
                			tbuserfu.setCardno(lo.get(2).toString());
                			tbUserMapper.updateByPrimaryKeySelective(tbuserfu);
                		}
	            	}
        		}
	            	tbWageDetailRZMapper.insert(tbwage);
            	
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("上传成功!");
            return  result;
    		
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("上传出错！请联系管理员" + e.getMessage());
	        return  result;
		}
        
    }
    
    
  @RequestMapping(value = "/userwagequery")
  public TNPYResponse getEquipmentInfo(@RequestBody Map<String, String> reqMap) {
      TNPYResponse result = new TNPYResponse();
      String cardno = reqMap.get("cardno") == null?"":reqMap.get("cardno").toString();
      String password = reqMap.get("password") == null?"":reqMap.get("password").toString();
      String senddate = reqMap.get("senddate") == null?"":reqMap.get("senddate").toString();

     try
     {
    	 //如果身份证号为空,则提示错误信息
    	 if(StringUtils.isEmpty(cardno)) {
    		 result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
    		 result.setMessage("身份证号不能为空");
             return  result;
    	 }

    	 Date nowmonth = new Date();
    	 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
         Calendar c = Calendar.getInstance();
         c.setTime(nowmonth);
         if(c.get(Calendar.DAY_OF_MONTH) < 26)
         {
             c.add(Calendar.MONTH, -1);
         }
         nowmonth = c.getTime();
    	 if(StringUtils.isEmpty(senddate)) {
    		 senddate=format.format(nowmonth);
    	 }

//    	 String queryfilter = "select password, cardno from tb_user where cardno ='"+cardno+"'";
    	 List<LinkedHashMap<String, Object>> verify = tbWageDetailRZMapper.queryDef(cardno);
    	
    	 
    	//判断密码不正确
 		if (verify.size()==0) {
 			result.setMessage("未找到该用户");
 			return result;
 		}
 		 String qpassword =verify.get(0).get("password")==null?"":verify.get(0).get("password").toString().trim();
    	 Encryption encryption = new Encryption();
 		//判断密码不正确
		if (!password.trim().equals(qpassword) && !encryption.encrypt(password.trim(),null).equals(qpassword)) {
			result.setMessage("密码不正确");
			return result;
		}
    	 String filter = "where cardno = '"+cardno+"' and senddate = '"+senddate+"'";
    	 List<LinkedHashMap<String, String>> tbwage = tbWageDetailRZMapper.selectByPrimaryKey1(filter);
    	 if(tbwage.isEmpty()) {
    		 result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
    		 result.setMessage("未找到该用户当月工资数据");
             return  result;
    	 }
         result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
         result.setData(JSONObject.toJSONString(tbwage, SerializerFeature.WriteMapNullValue).toString());
         return  result;
     }
     catch (Exception ex)
     {
         result.setMessage("查询出错！" + ex.getMessage());
         return  result;
     }
  }
  
  public boolean isexistsUser(List<TbUser> lsuser, String excelUserid) {
	  boolean flag = false;
	  for(int i = 0; i< lsuser.size(); i++) {
		  if(lsuser.get(i).getUserid().equals(excelUserid)) {
			  flag = true;
		  }
	  }
	  return flag;
  }
  
  public boolean isexistsWageData(List<TbUser> lsuser, String excelUserid) {
	  boolean flag = false;
	  for(int i = 0; i< lsuser.size(); i++) {
		  if(lsuser.get(i).getUserid().equals(excelUserid)) {
			  flag = true;
		  }
	  }
	  return flag;
  }

}
