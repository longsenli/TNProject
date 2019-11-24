package com.tnpy.mes.mapper.mysql;

import com.tnpy.mes.model.mysql.TbWageDetailRZ;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
@Component
public interface TbWageDetailRZMapper {
    int deleteByPrimaryKey(String id);

    int insert(TbWageDetailRZ record);

    int insertSelective(TbWageDetailRZ record);

    TbWageDetailRZ selectByPrimaryKey(String id);
    
    @Select("SELECT\r\n" +
    		"	id AS 'id',\r\n" + 
    		"	senddate as '工资月份',\r\n" + 
    		"	NAME as '姓名',\r\n" + 
    		"	cardno as '身份证号',\r\n" + 
    		"	department as '结算部门',\r\n" + 
    		"	round(wage,2) as '工资',\r\n" + 
    		"	round(overtimepay,2) as '加班工资',\r\n" + 
    		"	round(performancebonus,2) as '绩效奖金',\r\n" + 
    		"	round(subsidyincome,2) as '值班补贴',\r\n" + 
    		"	round(militarytrainingincome,2) as '军训补贴',\r\n" + 
    		"	round(wagesupplement,2) as '工资补差',\r\n" + 
    		"	round(utilities,2) as '水电气费补贴',\r\n" + 
    		"	round(housingallowance,2) as '住房补贴',\r\n" + 
    		"	round(hightemperaturesubsidy,2) as '高温补贴',\r\n" + 
    		"	round(seniorityallowance,2) as '厂龄补贴',\r\n" + 
    		"	round(shouldsendsalary,2) as '应发工资',\r\n" + 
    		"	round(socialinsurance,2) as '减：社会保险',\r\n" + 
    		"	round(reservedfunds,2) as '减：公积金',\r\n" + 
    		"	round(othercutpayment,2) as '其他扣款',\r\n" + 
    		"	round(utilitiescut,2) as '扣水电气费',\r\n" + 
    		"	round(lovedonation,2) as '爱心费',\r\n" + 
    		"	round(individualincometax,2) as '代扣个人所得税',\r\n" + 
    		"	round(takehomepay,2) as '实发工资'\r\n" + 
    		"FROM\r\n" + 
    		"	tb_wagedetail_rz ${filter}")
    List<LinkedHashMap<String, Object>> selectByPrimaryKey1(@Param("filter") String filter);
    
    @Select(" ${filter}")
    List<LinkedHashMap<String, Object>> queryDef(@Param("filter") String filter);

    int updateByPrimaryKeySelective(TbWageDetailRZ record);

    int updateByPrimaryKey(TbWageDetailRZ record);
}