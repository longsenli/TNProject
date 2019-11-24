package com.tnpy.mes.model.mysql;

import java.util.Date;

public class TbUser {

	private String userid;

	private String name;

	private String password;

	private String roleid;
	
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	private String sex;

	private Date birthday;

	private String dutyid;

	private String workplace;

	private Date createdate;

	private String createuser;

	private Date updatedate;

	private String updateuser;

	private Date deletedate;

	private String deleteuser;

	private Integer state;

	private String email;

	private String mobilephone;

	private String telephone;

	private String organid;

	private String industrialplant_id;

	private String industrialplant_name;

	private String productionline_id;

	public String getIndustrialplant_id() {
		return industrialplant_id;
	}

	public void setIndustrialplant_id(String industrialplant_id) {
		this.industrialplant_id = industrialplant_id;
	}

	public String getIndustrialplant_name() {
		return industrialplant_name;
	}

	public void setIndustrialplant_name(String industrialplant_name) {
		this.industrialplant_name = industrialplant_name;
	}

	public String getProductionline_id() {
		return productionline_id;
	}

	public void setProductionline_id(String productionline_id) {
		this.productionline_id = productionline_id;
	}

	public String getProductionline_name() {
		return productionline_name;
	}

	public void setProductionline_name(String productionline_name) {
		this.productionline_name = productionline_name;
	}

	public String getProductionprocess_id() {
		return productionprocess_id;
	}

	public void setProductionprocess_id(String productionprocess_id) {
		this.productionprocess_id = productionprocess_id;
	}

	public String getProductionprocess_name() {
		return productionprocess_name;
	}

	public void setProductionprocess_name(String productionprocess_name) {
		this.productionprocess_name = productionprocess_name;
	}

	private String productionline_name;

	private String productionprocess_id;

	private String productionprocess_name;

	private String worklocation_id;

	private String worklocation_name;
	
	private String cardno;

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid == null ? null : userid.trim();
	}

	public String getWorklocation_id() {
		return worklocation_id;
	}

	public void setWorklocation_id(String worklocation_id) {
		this.worklocation_id = worklocation_id;
	}

	public String getWorklocation_name() {
		return worklocation_name;
	}

	public void setWorklocation_name(String worklocation_name) {
		this.worklocation_name = worklocation_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid == null ? null : roleid.trim();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getDutyid() {
		return dutyid;
	}

	public void setDutyid(String dutyid) {
		this.dutyid = dutyid == null ? null : dutyid.trim();
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace == null ? null : workplace.trim();
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser == null ? null : createuser.trim();
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getUpdateuser() {
		return updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser == null ? null : updateuser.trim();
	}

	public Date getDeletedate() {
		return deletedate;
	}

	public void setDeletedate(Date deletedate) {
		this.deletedate = deletedate;
	}

	public String getDeleteuser() {
		return deleteuser;
	}

	public void setDeleteuser(String deleteuser) {
		this.deleteuser = deleteuser == null ? null : deleteuser.trim();
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone == null ? null : mobilephone.trim();
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone == null ? null : telephone.trim();
	}

	public String getOrganid() {
		return organid;
	}

	public void setOrganid(String organid) {
		this.organid = organid == null ? null : organid.trim();
	}

}