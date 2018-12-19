package com.tnpy.mes.model.mysql;

public class Token {
    private String tokenid;

    private Integer userid;

    private String token;

    private Double buildtime;

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid == null ? null : tokenid.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public double getBuildtime() {
        return buildtime;
    }

    public void setBuildtime(Long buildtime) {
        this.buildtime = buildtime*1.0;
    }
}