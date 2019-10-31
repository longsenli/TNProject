package com.tnpy.wage.rz;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.TbWageDetailRZMapper;
import com.tnpy.mes.model.mysql.TbWageDetailRZ;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
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

    @PostMapping(value = "/upload")
    @ResponseBody
    public String uploadExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        MultipartFile file = multipartRequest.getFile("filename");
        if (file.isEmpty()) {
            return "文件不能为空";
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
            	tbwage.setSenddate(lo.get(0).toString());//发放月份
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
            //TODO 随意发挥
//            System.out.println(lo);

        }
        return "上传成功";
    }
    
    
    @RequestMapping(value = "/userwagequery")
  public TNPYResponse getEquipmentInfo(String typeID,String plantID) {
      TNPYResponse result = new TNPYResponse();
     try
     {
    	 TbWageDetailRZ key = new TbWageDetailRZ();
    	 key.setId("06615b0f29a9409981dca38d476931ed");
    	 key.setCardno("410922198701153115");
    	 List<LinkedHashMap<String, Object>> tbwage = tbWageDetailRZMapper.selectByPrimaryKey1("06615b0f29a9409981dca38d476931ed");
    	 
         result.setStatus(1);
         result.setData(JSONObject.toJSONString(tbwage, SerializerFeature.WriteMapNullValue).toString());
        // System.out.println(result);
         return  result;
     }
     catch (Exception ex)
     {
         result.setMessage("查询出错！" + ex.getMessage());
         return  result;
     }
  }

}
