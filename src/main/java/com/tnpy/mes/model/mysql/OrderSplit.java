package com.tnpy.mes.model.mysql;

public class OrderSplit {
    private String id;

    private String orderid;

    private String ordersplitid;

    private Double productionnum;

    private Integer status;

    private String materialid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getOrdersplitid() {
        return ordersplitid;
    }

    public void setOrdersplitid(String ordersplitid) {
        this.ordersplitid = ordersplitid == null ? null : ordersplitid.trim();
    }

    public Double getProductionnum() {
        return productionnum;
    }

    public void setProductionnum(Double productionnum) {
        this.productionnum = productionnum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }
}