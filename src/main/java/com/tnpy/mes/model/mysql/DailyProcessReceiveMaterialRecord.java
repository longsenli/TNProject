package com.tnpy.mes.model.mysql;

public class DailyProcessReceiveMaterialRecord {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String worklocationid;

    private String teamtype;

    private String classtype;

    private String daytime;

    private String receivematerialid;

    private String receivematerialname;

    private Integer receivematerialnumber1;

    private Integer receivematerialnumber2;

    private Integer receivematerialnumber3;

    private String reveivetype;

    private String extend1;

    private String extend2;

    private String extend3;

    private String remark;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPlantid() {
        return plantid;
    }

    public void setPlantid(String plantid) {
        this.plantid = plantid == null ? null : plantid.trim();
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid == null ? null : processid.trim();
    }

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid == null ? null : lineid.trim();
    }

    public String getWorklocationid() {
        return worklocationid;
    }

    public void setWorklocationid(String worklocationid) {
        this.worklocationid = worklocationid == null ? null : worklocationid.trim();
    }

    public String getTeamtype() {
        return teamtype;
    }

    public void setTeamtype(String teamtype) {
        this.teamtype = teamtype == null ? null : teamtype.trim();
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype == null ? null : classtype.trim();
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime == null ? null : daytime.trim();
    }

    public String getReceivematerialid() {
        return receivematerialid;
    }

    public void setReceivematerialid(String receivematerialid) {
        this.receivematerialid = receivematerialid == null ? null : receivematerialid.trim();
    }

    public String getReceivematerialname() {
        return receivematerialname;
    }

    public void setReceivematerialname(String receivematerialname) {
        this.receivematerialname = receivematerialname == null ? null : receivematerialname.trim();
    }

    public Integer getReceivematerialnumber1() {
        return receivematerialnumber1;
    }

    public void setReceivematerialnumber1(Integer receivematerialnumber1) {
        this.receivematerialnumber1 = receivematerialnumber1;
    }

    public Integer getReceivematerialnumber2() {
        return receivematerialnumber2;
    }

    public void setReceivematerialnumber2(Integer receivematerialnumber2) {
        this.receivematerialnumber2 = receivematerialnumber2;
    }

    public Integer getReceivematerialnumber3() {
        return receivematerialnumber3;
    }

    public void setReceivematerialnumber3(Integer receivematerialnumber3) {
        this.receivematerialnumber3 = receivematerialnumber3;
    }

    public String getReveivetype() {
        return reveivetype;
    }

    public void setReveivetype(String reveivetype) {
        this.reveivetype = reveivetype == null ? null : reveivetype.trim();
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1 == null ? null : extend1.trim();
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2 == null ? null : extend2.trim();
    }

    public String getExtend3() {
        return extend3;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3 == null ? null : extend3.trim();
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
}