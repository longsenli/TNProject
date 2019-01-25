package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class SalesOrder {
    private String id;

    private String plantid;

    private String materialid;

    private String workshopid;

    private String workshopname;

    private String salesorder;

    private String productorder;

    private String zonecode;

    private String zonename;

    private String customer;

    private String productinfo;

    private Float production;

    private String unitproduct;

    private Float salevolume;

    private String unitsale;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date productioninputtime;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date salecreatetime;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date producttime;

    private String orderstatus;

    private String batterymodel;

    private Float packingquantity;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date deliverytime;

    private Float deliveryquality;

    private String remark;

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

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }

    public String getWorkshopid() {
        return workshopid;
    }

    public void setWorkshopid(String workshopid) {
        this.workshopid = workshopid == null ? null : workshopid.trim();
    }

    public String getWorkshopname() {
        return workshopname;
    }

    public void setWorkshopname(String workshopname) {
        this.workshopname = workshopname == null ? null : workshopname.trim();
    }

    public String getSalesorder() {
        return salesorder;
    }

    public void setSalesorder(String salesorder) {
        this.salesorder = salesorder == null ? null : salesorder.trim();
    }

    public String getProductorder() {
        return productorder;
    }

    public void setProductorder(String productorder) {
        this.productorder = productorder == null ? null : productorder.trim();
    }

    public String getZonecode() {
        return zonecode;
    }

    public void setZonecode(String zonecode) {
        this.zonecode = zonecode == null ? null : zonecode.trim();
    }

    public String getZonename() {
        return zonename;
    }

    public void setZonename(String zonename) {
        this.zonename = zonename == null ? null : zonename.trim();
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer == null ? null : customer.trim();
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo == null ? null : productinfo.trim();
    }

    public Float getProduction() {
        return production;
    }

    public void setProduction(Float production) {
        this.production = production;
    }

    public String getUnitproduct() {
        return unitproduct;
    }

    public void setUnitproduct(String unitproduct) {
        this.unitproduct = unitproduct == null ? null : unitproduct.trim();
    }

    public Float getSalevolume() {
        return salevolume;
    }

    public void setSalevolume(Float salevolume) {
        this.salevolume = salevolume;
    }

    public String getUnitsale() {
        return unitsale;
    }

    public void setUnitsale(String unitsale) {
        this.unitsale = unitsale == null ? null : unitsale.trim();
    }

    public Date getProductioninputtime() {
        return productioninputtime;
    }

    public void setProductioninputtime(Date productioninputtime) {
        this.productioninputtime = productioninputtime;
    }

    public Date getSalecreatetime() {
        return salecreatetime;
    }

    public void setSalecreatetime(Date salecreatetime) {
        this.salecreatetime = salecreatetime;
    }

    public Date getProducttime() {
        return producttime;
    }

    public void setProducttime(Date producttime) {
        this.producttime = producttime;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus == null ? null : orderstatus.trim();
    }

    public String getBatterymodel() {
        return batterymodel;
    }

    public void setBatterymodel(String batterymodel) {
        this.batterymodel = batterymodel == null ? null : batterymodel.trim();
    }

    public Float getPackingquantity() {
        return packingquantity;
    }

    public void setPackingquantity(Float packingquantity) {
        this.packingquantity = packingquantity;
    }

    public Date getDeliverytime() {
        return deliverytime;
    }

    public void setDeliverytime(Date deliverytime) {
        this.deliverytime = deliverytime;
    }

    public Float getDeliveryquality() {
        return deliveryquality;
    }

    public void setDeliveryquality(Float deliveryquality) {
        this.deliveryquality = deliveryquality;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}