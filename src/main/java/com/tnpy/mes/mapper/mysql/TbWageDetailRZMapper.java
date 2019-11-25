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
//    		"	round(wage,2) as '工资',\r\n" + 
    		"	CONVERT(round(wage,2),char)  as '工资', " +
    		"	CONVERT(round(overtimepay,2),char) as '加班工资',\r\n" + 
    		"	CONVERT(round(performancebonus,2),char) as '绩效奖金',\r\n" + 
    		"	CONVERT(round(subsidyincome,2),char) as '值班补贴',\r\n" + 
    		"	CONVERT(round(militarytrainingincome,2),char) as '军训补贴',\r\n" + 
    		"	CONVERT(round(wagesupplement,2),char) as '工资补差',\r\n" + 
    		"	CONVERT(round(utilities,2),char) as '水电气费补贴',\r\n" + 
    		"	CONVERT(round(housingallowance,2),char) as '住房补贴',\r\n" + 
    		"	CONVERT(round(hightemperaturesubsidy,2),char) as '高温补贴',\r\n" + 
    		"	CONVERT(round(seniorityallowance,2),char) as '厂龄补贴',\r\n" + 
    		"	CONVERT(round(shouldsendsalary,2),char) as '应发工资',\r\n" + 
    		"	CONVERT(round(socialinsurance,2),char) as '减：社会保险',\r\n" + 
    		"	CONVERT(round(reservedfunds,2),char) as '减：公积金',\r\n" + 
    		"	CONVERT(round(othercutpayment,2),char) as '其他扣款',\r\n" + 
    		"	CONVERT(round(utilitiescut,2),char) as '扣水电气费',\r\n" + 
    		"	CONVERT(round(lovedonation,2),char) as '爱心费',\r\n" + 
    		"	CONVERT(round(individualincometax,2),char) as '代扣个人所得税',\r\n" + 
    		"	CONVERT(round(takehomepay,2),char) as '实发工资'\r\n" + 
    		"FROM\r\n" + 
    		"	tb_wagedetail_rz ${filter}")
    List<LinkedHashMap<String, String>> selectByPrimaryKey1(@Param("filter") String filter);
    
    @Select("select password, cardno from tb_user where cardno = ${filter} ")
    List<LinkedHashMap<String, Object>> queryDef(@Param("filter") String filter);

    int updateByPrimaryKeySelective(TbWageDetailRZ record);

    int updateByPrimaryKey(TbWageDetailRZ record);
    
    @Select("select * from tb_wagedetail_rz where senddate = ${filter} ")
    List<TbWageDetailRZ> selectAllBySendDate(@Param("filter") String filter);
}