package com.tnpy.mes.model.mysql;

public class DailyLineProductionDetailRecord {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String worklocationid;

    private String teamtype;

    private String classtype;

    private String daytime;

    private String materialid;

    private String materialname;

    private Integer productionnumber;

    private Double productiontransition1;

    private Double productiontransition2;

    private Double productiontransition3;

    private String usedmaterialid;

    private String usedmaterialname;

    private Integer usednumber;

    private Double usednumbertransition1;

    private Double usednumbertransition2;

    private String scrapmaterialid;

    private String scrapmaterialname;

    private Integer scrapnumber;

    private Double weightnumber;

    private Double scrapnumbertransition1;

    private Double scrapnumbertransition2;

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

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname == null ? null : materialname.trim();
    }

    public Integer getProductionnumber() {
        return productionnumber;
    }

    public void setProductionnumber(Integer productionnumber) {
        this.productionnumber = productionnumber;
    }

    public Double getProductiontransition1() {
        return productiontransition1;
    }

    public void setProductiontransition1(Double productiontransition1) {
        this.productiontransition1 = productiontransition1;
    }

    public Double getProductiontransition2() {
        return productiontransition2;
    }

    public void setProductiontransition2(Double productiontransition2) {
        this.productiontransition2 = productiontransition2;
    }

    public Double getProductiontransition3() {
        return productiontransition3;
    }

    public void setProductiontransition3(Double productiontransition3) {
        this.productiontransition3 = productiontransition3;
    }

    public String getUsedmaterialid() {
        return usedmaterialid;
    }

    public void setUsedmaterialid(String usedmaterialid) {
        this.usedmaterialid = usedmaterialid == null ? null : usedmaterialid.trim();
    }

    public String getUsedmaterialname() {
        return usedmaterialname;
    }

    public void setUsedmaterialname(String usedmaterialname) {
        this.usedmaterialname = usedmaterialname == null ? null : usedmaterialname.trim();
    }

    public Integer getUsednumber() {
        return usednumber;
    }

    public void setUsednumber(Integer usednumber) {
        this.usednumber = usednumber;
    }

    public Double getUsednumbertransition1() {
        return usednumbertransition1;
    }

    public void setUsednumbertransition1(Double usednumbertransition1) {
        this.usednumbertransition1 = usednumbertransition1;
    }

    public Double getUsednumbertransition2() {
        return usednumbertransition2;
    }

    public void setUsednumbertransition2(Double usednumbertransition2) {
        this.usednumbertransition2 = usednumbertransition2;
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

    public Integer getScrapnumber() {
        return scrapnumber;
    }

    public void setScrapnumber(Integer scrapnumber) {
        this.scrapnumber = scrapnumber;
    }

    public Double getWeightnumber() {
        return weightnumber;
    }

    public void setWeightnumber(Double weightnumber) {
        this.weightnumber = weightnumber;
    }

    public Double getScrapnumbertransition1() {
        return scrapnumbertransition1;
    }

    public void setScrapnumbertransition1(Double scrapnumbertransition1) {
        this.scrapnumbertransition1 = scrapnumbertransition1;
    }

    public Double getScrapnumbertransition2() {
        return scrapnumbertransition2;
    }

    public void setScrapnumbertransition2(Double scrapnumbertransition2) {
        this.scrapnumbertransition2 = scrapnumbertransition2;
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