package com.tnpy.mes.mapper.mysql;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tnpy.mes.model.mysql.TbWageDetailRZ;

public interface TbWageDetailRZMapper {
    int deleteByPrimaryKey(String id);

    int insert(TbWageDetailRZ record);

    int insertSelective(TbWageDetailRZ record);

    TbWageDetailRZ selectByPrimaryKey(String id);
    
    @Select("SELECT\r\n" +
    		"	id AS 'id',\r\n" + 
    		"	senddate as '发放日期',\r\n" + 
    		"	NAME as '姓名',\r\n" + 
    		"	cardno as '身份证号',\r\n" + 
    		"	department as '结算部门',\r\n" + 
    		"	wage as '工资',\r\n" + 
    		"	overtimepay as '加班工资',\r\n" + 
    		"	performancebonus as '绩效奖金',\r\n" + 
    		"	subsidyincome as '值班补贴',\r\n" + 
    		"	militarytrainingincome as '军训补贴',\r\n" + 
    		"	wagesupplement as '工资补差',\r\n" + 
    		"	utilities as '水电气费补贴',\r\n" + 
    		"	housingallowance as '住房补贴',\r\n" + 
    		"	hightemperaturesubsidy as '高温补贴',\r\n" + 
    		"	seniorityallowance as '厂龄补贴',\r\n" + 
    		"	shouldsendsalary as '应发工资',\r\n" + 
    		"	socialinsurance as '减：社会保险',\r\n" + 
    		"	reservedfunds as '减：公积金',\r\n" + 
    		"	othercutpayment as '其他扣款',\r\n" + 
    		"	utilitiescut as '扣水电气费',\r\n" + 
    		"	lovedonation as '爱心费',\r\n" + 
    		"	individualincometax as '代扣个人所得税',\r\n" + 
    		"	takehomepay as '实发工资'\r\n" + 
    		"FROM\r\n" + 
    		"	tb_wagedetail_rz where id = #{orderID}")
    List<LinkedHashMap<String, Object>> selectByPrimaryKey1(String id);

    int updateByPrimaryKeySelective(TbWageDetailRZ record);

    int updateByPrimaryKey(TbWageDetailRZ record);
}