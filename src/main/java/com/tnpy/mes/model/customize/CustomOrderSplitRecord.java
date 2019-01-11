package com.tnpy.mes.model.customize;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/6 16:51
 */
public class CustomOrderSplitRecord {
    private String id;

    private String orderid;

    private String ordersplitid;

    private Double productionnum;

    private String status;

    private String materialid;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    private String statusName;

    private String materialName;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }
}
