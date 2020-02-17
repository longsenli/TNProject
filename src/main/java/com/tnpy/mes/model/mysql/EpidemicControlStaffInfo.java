package com.tnpy.mes.model.mysql;

import java.util.Date;

public class EpidemicControlStaffInfo {
    private String identityno;

    private String name;

    private String sex;

    private String age;

    private String department;

    private String telephonenumber;

    private String familylocation;

    private Date updatetime;

    private String remark;

    private String status;

    private String extd1;

    private String extd2;

    private String extd3;

    private String updator;

    private String compony;

    private String extd4;

    private String extd5;

    public String getIdentityno() {
        return identityno;
    }

    public void setIdentityno(String identityno) {
        this.identityno = identityno == null ? null : identityno.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber == null ? null : telephonenumber.trim();
    }

    public String getFamilylocation() {
        return familylocation;
    }

    public void setFamilylocation(String familylocation) {
        this.familylocation = familylocation == null ? null : familylocation.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getExtd1() {
        return extd1;
    }

    public void setExtd1(String extd1) {
        this.extd1 = extd1 == null ? null : extd1.trim();
    }

    public String getExtd2() {
        return extd2;
    }

    public void setExtd2(String extd2) {
        this.extd2 = extd2 == null ? null : extd2.trim();
    }

    public String getExtd3() {
        return extd3;
    }

    public void setExtd3(String extd3) {
        this.extd3 = extd3 == null ? null : extd3.trim();
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public String getCompony() {
        return compony;
    }

    public void setCompony(String compony) {
        this.compony = compony == null ? null : compony.trim();
    }

    public String getExtd4() {
        return extd4;
    }

    public void setExtd4(String extd4) {
        this.extd4 = extd4 == null ? null : extd4.trim();
    }

    public String getExtd5() {
        return extd5;
    }

    public void setExtd5(String extd5) {
        this.extd5 = extd5 == null ? null : extd5.trim();
    }
}