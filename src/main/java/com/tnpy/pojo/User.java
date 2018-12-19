package com.tnpy.pojo;

public class User
{
    private int userid;
    private String name,password;
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "User [userid=" + userid + ", name=" + name + ", password=" + password + "]";
    }
}