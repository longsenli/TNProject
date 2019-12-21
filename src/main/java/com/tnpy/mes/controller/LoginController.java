package com.tnpy.mes.controller;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.Enum.StatusEnum;
import com.tnpy.common.utils.encryption.Encryption;
import com.tnpy.common.utils.token.Token;
import com.tnpy.common.utils.token.TokenUtil;
import com.tnpy.common.utils.web.TNPYResponse;
import com.tnpy.mes.mapper.mysql.LoginRecordMapper;
import com.tnpy.mes.mapper.mysql.SoftwareVersionMapper;
import com.tnpy.mes.mapper.mysql.StaffAttendanceDetailMapper;
import com.tnpy.mes.mapper.mysql.TokenMapper;
import com.tnpy.mes.model.mysql.LoginRecord;
import com.tnpy.mes.model.mysql.StaffAttendanceDetail;
import com.tnpy.mes.model.mysql.TbUser;
import com.tnpy.mes.service.userService.ITbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
public class LoginController {

	@Resource
	ITbUserService tbUserService;
	@Autowired
	private TokenMapper tokenmapper;

	@Autowired
	private LoginRecordMapper loginRecordMapper;

	@Autowired
	private SoftwareVersionMapper softwareVersionMapper;

	@Autowired
	private StaffAttendanceDetailMapper staffAttendanceDetailMapper;

	@RequestMapping(value = "/getappversion")
	public TNPYResponse getAppVersion( ) {
		TNPYResponse response = new TNPYResponse();
		try
		{

			response.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
			response.setMessage("1.0.5");
			return  response;
		}
		catch (Exception ex)
		{
			response.setMessage("0");
			return  response;
		}
	}

	@RequestMapping(value = "/getAPPVersionWithParam")
	public TNPYResponse getAPPVersionWithParam(String clientType,String userID,String plantID,String processID ) {
		TNPYResponse response = new TNPYResponse();
		try
		{
			String version = softwareVersionMapper.selectLatestVersion(clientType);
			response.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
			response.setMessage(version);
			return  response;
		}
		catch (Exception ex)
		{
			response.setMessage("0");
			return  response;
		}
	}

	@RequestMapping(value = "/getappversionbyclienttype")
	public TNPYResponse getAppVersionByClientType( String clientType) {
		TNPYResponse response = new TNPYResponse();
		try
		{
			String version = softwareVersionMapper.selectLatestVersion(clientType);
			response.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
			response.setMessage(version);
			return  response;
		}
		catch (Exception ex)
		{
			response.setMessage("0");
			return  response;
		}
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TNPYResponse login(HttpServletRequest request,@RequestParam(value = "username") String username,
							  @RequestParam(value = "password") String password) {
		//System.out.println("======="+ request.getRemoteAddr());
		TNPYResponse response = new TNPYResponse();
		Encryption encryption = new Encryption();
		//判断用户信息为空
		if ("".equals(username.trim()) || "".equals(password.trim())) {
			response.setMessage("传入的用户名/密码为空！");
			return response;
		}

		//根据客户用户名查找数据库的usre对象
		TbUser myUser = tbUserService.getUserInfo(username);

		//判断用户不存在
		if (null == myUser) {
			response.setMessage("用户不存在");
			return response;
		}
		//判断用户不存在
		if (!password.trim().equals(myUser.getPassword()) && !encryption.encrypt(password.trim(),null).equals(myUser.getPassword())) {
			response.setMessage("密码不正确");
			return response;
		}

		//根据数据库的用户信息查询Token
		Token token =  tokenmapper.findByUserId(myUser.getUserid());

		try
		{
			LoginRecord loginRecord = new LoginRecord();
			loginRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
			loginRecord.setUserid(myUser.getUserid());
			loginRecord.setUsername(myUser.getName());
			loginRecord.setLoginip(request.getRemoteAddr());
			loginRecord.setLogintime(new   Date ());
			loginRecordMapper.insert(loginRecord);

			TokenUtil tokenUtil = new  TokenUtil();
			//生成Token
			if (null == token) {

				//第一次登陆
				token =tokenUtil.creatToken(username) ;
				tokenmapper.insertToken(token);

			}else{
				//登陆就更新Token信息


				token = tokenUtil.creatToken(username) ;

				tokenmapper.updateToken(token);
			}
		}
		catch (Exception ex)
		{
			response.setMessage("登录失败" + ex.getMessage());
			return  response;
		}



		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if (calendar.get(Calendar.HOUR_OF_DAY) < 6) {
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();   //这个时间就是日期往后推一天的结果
		}

		StaffAttendanceDetail staffAttendanceDetail = staffAttendanceDetailMapper.selectCurrentUsableRecord(username,dateFormat.format(date));

		if(staffAttendanceDetail != null)
		{
			response.setData("1");  //表示已经扫过工作位置
			response.setMessage(myUser.getName() + " ### " + myUser.getRoleid() + " ### " + staffAttendanceDetail.getPlantid()
					+ " ### " +  staffAttendanceDetail.getProcessid() + " ### " + staffAttendanceDetail.getLineid() + "###" + staffAttendanceDetail.getWorklocationid()
					+ "###" + staffAttendanceDetail.getExtd1()+ "###" + staffAttendanceDetail.getExtd2());

		}
		else
		{
			response.setData("2");//表示未扫过工作位置
			response.setMessage(myUser.getName() + " ### " + myUser.getRoleid() + " ### " + myUser.getIndustrialplant_id()
					+ " ### " +  myUser.getProductionprocess_id() + " ### " + myUser.getProductionline_id() + "###" + myUser.getWorklocation_id() + "###-1###-1" );

		}
		response.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
		response.setToken(JSONObject.toJSON(token).toString());

		return response;
	}

	@RequestMapping(value = "/appLogin")
	public TNPYResponse login( HttpServletRequest request,String username,String password,String appVersion) {
//System.out.println(username + "===" +appVersion +"===="+request  );
		TNPYResponse response = new TNPYResponse();
		Encryption encryption = new Encryption();
		//判断用户信息为空
		if ("".equals(username.trim()) || "".equals(password.trim())) {
			response.setMessage("传入的用户名/密码为空！");
			return response;
		}

		//根据客户用户名查找数据库的usre对象
		TbUser myUser = tbUserService.getUserInfo(username);

		//判断用户不存在
		if (null == myUser) {
			response.setMessage("用户不存在");
			return response;
		}
		//判断用户不存在
		if (!password.trim().equals(myUser.getPassword()) && !encryption.encrypt(password.trim(),null).equals(myUser.getPassword())) {
			response.setMessage("密码不正确");
			return response;
		}

		//根据数据库的用户信息查询Token
		Token token =  tokenmapper.findByUserId(myUser.getUserid());

		try
		{
			LoginRecord loginRecord = new LoginRecord();
			loginRecord.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
			loginRecord.setUserid(myUser.getUserid());
			loginRecord.setUsername(myUser.getName());
			loginRecord.setLoginip(request.getRemoteAddr());
			loginRecord.setLogintime(new   Date ());
			loginRecordMapper.insert(loginRecord);

			TokenUtil tokenUtil = new  TokenUtil();
			//生成Token
			if (null == token) {

				//第一次登陆
				token =tokenUtil.creatToken(username) ;
				tokenmapper.insertToken(token);

			}else{
				//登陆就更新Token信息


				token = tokenUtil.creatToken(username) ;

				tokenmapper.updateToken(token);
			}
		}
		catch (Exception ex)
		{
			response.setMessage("登录失败" + ex.getMessage());
			return  response;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if (calendar.get(Calendar.HOUR_OF_DAY) < 6) {
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();   //这个时间就是日期往后推一天的结果
		}

		StaffAttendanceDetail staffAttendanceDetail = staffAttendanceDetailMapper.selectCurrentUsableRecord(username,dateFormat.format(date));

		if(staffAttendanceDetail != null)
		{
			response.setData("1");  //表示已经扫过工作位置
			response.setMessage(myUser.getName() + " ### " + myUser.getRoleid() + " ### " + staffAttendanceDetail.getPlantid()
					+ " ### " +  staffAttendanceDetail.getProcessid() + " ### " + staffAttendanceDetail.getLineid() + "###" + staffAttendanceDetail.getWorklocationid()
					+ "###" + staffAttendanceDetail.getExtd1());
		}
		else
		{
			response.setData("2");//表示未扫过工作位置
			response.setMessage(myUser.getName() + " ### " + myUser.getRoleid() + " ### " + myUser.getIndustrialplant_id()
					+ " ### " +  myUser.getProductionprocess_id() + " ### " + myUser.getProductionline_id() + "###" + myUser.getWorklocation_id() + "###-1" );
		}
		response.setStatus(StatusEnum.ResponseStatus.Success.getIndex());
		response.setToken(JSONObject.toJSON(token).toString());

		return response;
	}
}
