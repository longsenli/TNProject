package com.tnpy.mes.model.mysql;

public class DailyProcessProductionDetailRecord {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String worklocationid;

    private String teamtype;

    private String classtype;

    private String daytime;

    private Double attendancenumber;

    private Double actualattendancenumber;

    private Double attendanceratio;

    private Double machinenumber;

    private Double actualmachinenumber;

    private Double productionmachineratio;

    private String productionmaterialid;

    private String productionmaterialname;

    private Integer productionnumber;

    private Double productiontransition1;

    private Double productiontransition2;

    private Double productiontransition3;

    private Integer plandailyproduction;

    private Double ratiofinish;

    private Integer lastinventory;

    private Integer currentinventory;

    private Double inventorytransition1;

    private Double inventorytransition2;

    private Double inventorytransition3;

    private Integer dailyconsumenumber;

    private Double periodnumber;

    private String receivematerialid;

    private String receivematerialname;

    private Double receivenumber;

    private Integer receivematerialnumber1;

    private Double receivematerialnumber2;

    private Double receivematerialnumber3;

    private String reveivetype;

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

    private Double scrapnumbertransition3;

    private String grantmaterialid;

    private String grantmaterialname;

    private Integer grantnumber;

    private Double grantnumbertransition1;

    private Double grantnumbertransition2;

    private Double grantnumbertransition3;

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

    public Double getAttendancenumber() {
        return attendancenumber;
    }

    public void setAttendancenumber(Double attendancenumber) {
        this.attendancenumber = attendancenumber;
    }

    public Double getActualattendancenumber() {
        return actualattendancenumber;
    }

    public void setActualattendancenumber(Double actualattendancenumber) {
        this.actualattendancenumber = actualattendancenumber;
    }

    public Double getAttendanceratio() {
        return attendanceratio;
    }

    public void setAttendanceratio(Double attendanceratio) {
        this.attendanceratio = attendanceratio;
    }

    public Double getMachinenumber() {
        return machinenumber;
    }

    public void setMachinenumber(Double machinenumber) {
        this.machinenumber = machinenumber;
    }

    public Double getActualmachinenumber() {
        return actualmachinenumber;
    }

    public void setActualmachinenumber(Double actualmachinenumber) {
        this.actualmachinenumber = actualmachinenumber;
    }

    public Double getProductionmachineratio() {
        return productionmachineratio;
    }

    public void setProductionmachineratio(Double productionmachineratio) {
        this.productionmachineratio = productionmachineratio;
    }

    public String getProductionmaterialid() {
        return productionmaterialid;
    }

    public void setProductionmaterialid(String productionmaterialid) {
        this.productionmaterialid = productionmaterialid == null ? null : productionmaterialid.trim();
    }

    public String getProductionmaterialname() {
        return productionmaterialname;
    }

    public void setProductionmaterialname(String productionmaterialname) {
        this.productionmaterialname = productionmaterialname == null ? null : productionmaterialname.trim();
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

    public Integer getPlandailyproduction() {
        return plandailyproduction;
    }

    public void setPlandailyproduction(Integer plandailyproduction) {
        this.plandailyproduction = plandailyproduction;
    }

    public Double getRatiofinish() {
        return ratiofinish;
    }

    public void setRatiofinish(Double ratiofinish) {
        this.ratiofinish = ratiofinish;
    }

    public Integer getLastinventory() {
        return lastinventory;
    }

    public void setLastinventory(Integer lastinventory) {
        this.lastinventory = lastinventory;
    }

    public Integer getCurrentinventory() {
        return currentinventory;
    }

    public void setCurrentinventory(Integer currentinventory) {
        this.currentinventory = currentinventory;
    }

    public Double getInventorytransition1() {
        return inventorytransition1;
    }

    public void setInventorytransition1(Double inventorytransition1) {
        this.inventorytransition1 = inventorytransition1;
    }

    public Double getInventorytransition2() {
        return inventorytransition2;
    }

    public void setInventorytransition2(Double inventorytransition2) {
        this.inventorytransition2 = inventorytransition2;
    }

    public Double getInventorytransition3() {
        return inventorytransition3;
    }

    public void setInventorytransition3(Double inventorytransition3) {
        this.inventorytransition3 = inventorytransition3;
    }

    public Integer getDailyconsumenumber() {
        return dailyconsumenumber;
    }

    public void setDailyconsumenumber(Integer dailyconsumenumber) {
        this.dailyconsumenumber = dailyconsumenumber;
    }

    public Double getPeriodnumber() {
        return periodnumber;
    }

    public void setPeriodnumber(Double periodnumber) {
        this.periodnumber = periodnumber;
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

    public Double getReceivenumber() {
        return receivenumber;
    }

    public void setReceivenumber(Double receivenumber) {
        this.receivenumber = receivenumber;
    }

    public Integer getReceivematerialnumber1() {
        return receivematerialnumber1;
    }

    public void setReceivematerialnumber1(Integer receivematerialnumber1) {
        this.receivematerialnumber1 = receivematerialnumber1;
    }

    public Double getReceivematerialnumber2() {
        return receivematerialnumber2;
    }

    public void setReceivematerialnumber2(Double receivematerialnumber2) {
        this.receivematerialnumber2 = receivematerialnumber2;
    }

    public Double getReceivematerialnumber3() {
        return receivematerialnumber3;
    }

    public void setReceivematerialnumber3(Double receivematerialnumber3) {
        this.receivematerialnumber3 = receivematerialnumber3;
    }

    public String getReveivetype() {
        return reveivetype;
    }

    public void setReveivetype(String reveivetype) {
        this.reveivetype = reveivetype == null ? null : reveivetype.trim();
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

    public Double getScrapnumbertransition3() {
        return scrapnumbertransition3;
    }

    public void setScrapnumbertransition3(Double scrapnumbertransition3) {
        this.scrapnumbertransition3 = scrapnumbertransition3;
    }

    public String getGrantmaterialid() {
        return grantmaterialid;
    }

    public void setGrantmaterialid(String grantmaterialid) {
        this.grantmaterialid = grantmaterialid == null ? null : grantmaterialid.trim();
    }

    public String getGrantmaterialname() {
        return grantmaterialname;
    }

    public void setGrantmaterialname(String grantmaterialname) {
        this.grantmaterialname = grantmaterialname == null ? null : grantmaterialname.trim();
    }

    public Integer getGrantnumber() {
        return grantnumber;
    }

    public void setGrantnumber(Integer grantnumber) {
        this.grantnumber = grantnumber;
    }

    public Double getGrantnumbertransition1() {
        return grantnumbertransition1;
    }

    public void setGrantnumbertransition1(Double grantnumbertransition1) {
        this.grantnumbertransition1 = grantnumbertransition1;
    }

    public Double getGrantnumbertransition2() {
        return grantnumbertransition2;
    }

    public void setGrantnumbertransition2(Double grantnumbertransition2) {
        this.grantnumbertransition2 = grantnumbertransition2;
    }

    public Double getGrantnumbertransition3() {
        return grantnumbertransition3;
    }

    public void setGrantnumbertransition3(Double grantnumbertransition3) {
        this.grantnumbertransition3 = grantnumbertransition3;
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