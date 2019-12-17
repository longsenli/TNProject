package com.tnpy.mes.model.mysql;

public class DailyProcessScrapMaterialRecord {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String worklocationid;

    private String teamtype;

    private String classtype;

    private String daytime;

    private String scrapmaterialid;

    private String scrapmaterialname;

    private Double scrapmaterialnumber1;

    private Double scrapmaterialnumber2;

    private Double scrapmaterialnumber3;

    private String scrapreason;

    private String scraptype;

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

    public String getScrapmaterialid() {
        return scrapmaterialid;
    }

    public void setScrapmaterialid(String scrapmaterialid) {
        this.scrapmaterialid = scrapmaterialid == null ? null : scrapmaterialid.trim();
    }

    public String getScrapmaterialname() {
        return scrapmaterialname;
    }

    public void setScrapmaterialname(String scrapmaterialname) {
        this.scrapmaterialname = scrapmaterialname == null ? null : scrapmaterialname.trim();
    }

    public Double getScrapmaterialnumber1() {
        return scrapmaterialnumber1;
    }

    public void setScrapmaterialnumber1(Double scrapmaterialnumber1) {
        this.scrapmaterialnumber1 = scrapmaterialnumber1;
    }

    public Double getScrapmaterialnumber2() {
        return scrapmaterialnumber2;
    }

    public void setScrapmaterialnumber2(Double scrapmaterialnumber2) {
        this.scrapmaterialnumber2 = scrapmaterialnumber2;
    }

    public Double getScrapmaterialnumber3() {
        return scrapmaterialnumber3;
    }

    public void setScrapmaterialnumber3(Double scrapmaterialnumber3) {
        this.scrapmaterialnumber3 = scrapmaterialnumber3;
    }

    public String getScrapreason() {
        return scrapreason;
    }

    public void setScrapreason(String scrapreason) {
        this.scrapreason = scrapreason == null ? null : scrapreason.trim();
    }

    public String getScraptype() {
        return scraptype;
    }

    public void setScraptype(String scraptype) {
        this.scraptype = scraptype == null ? null : scraptype.trim();
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