package com.tnpy.wage.rz;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.mail.imap.protocol.Status;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbWageDetailRZMapper;
import com.tnpy.mes.model.mysql.TbWageDetailRZ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
            Date now = new Date();
            String filter = "where senddate = '"+formatter.format(now)+"' limit 1";
            List<LinkedHashMap<String, Object>> isexists = tbWageDetailRZMapper.selectByPrimaryKey1(filter);
            if (isexists.size()>0) {
                result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
                result.setMessage(formatter.format(now)+ "月份数据已经上传过啦!");
                return  result;
            }
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
            	if(lo.get(0).toString().equals(formatter.format(now))) {
            		tbwage.setSenddate(lo.get(0).toString());//发放月份
            	}else {
            		tbwage.setSenddate(formatter.format(now));//发放月份
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
            	tbWageDetailRZMapper.insert(tbwage);
            }
            result.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
            result.setMessage("上传成功!");
            return  result;
    		
		} catch (Exception e) {
			result.setMessage("上传出错！请联系管理员" + e.getMessage());
	        return  result;
		}
        
    }
    
    
  @RequestMapping(value = "/userwagequery")
  public TNPYResponse getEquipmentInfo(@RequestBody Map<String, String> reqMap) {
      TNPYResponse result = new TNPYResponse();
      String cardno = reqMap.get("cardno") == null?"":reqMap.get("cardno").toString();
      String senddate = reqMap.get("senddate") == null?"":reqMap.get("senddate").toString();
     try
     {
    	 //如果身份证号为空,则提示错误信息
    	 if(cardno.equals("")) {
    		 result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
    		 result.setMessage("身份证号不能为空");
             return  result;
    	 }
    	 //默认为当前月
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    	 Date nowmonth = new Date();
    	 if(senddate.equals("")) {
    		 senddate=sdf.format(nowmonth).toString();
    	 }
    	 String filter = "where cardno = '"+cardno+"' and senddate = '"+senddate+"'";
    	 List<LinkedHashMap<String, Object>> tbwage = tbWageDetailRZMapper.selectByPrimaryKey1(filter);
    	 if(tbwage.isEmpty()) {
    		 result.setStatus(StatusEnum.ResponseStatus.Fail.getIndex());
    		 result.setMessage("为找到数据");
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

}
