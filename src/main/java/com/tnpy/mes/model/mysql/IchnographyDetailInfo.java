package com.tnpy.mes.model.mysql;

import java.util.Date;

public class IchnographyDetailInfo {
    private String id;

    private String mainregionid;

    private String mainregionname;

    private String regionid;

    private String regionname;

    private String showname;

    private Integer drawnumber;

    private String fillcolor;

    private String outlinecolor;

    private String shapetype;

    private String shapedrawparam;

    private String updater;

    private Date updatetime;

    private String status;

    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMainregionid() {
        return mainregionid;
    }

    public void setMainregionid(String mainregionid) {
        this.mainregionid = mainregionid == null ? null : mainregionid.trim();
    }

    public String getMainregionname() {
        return mainregionname;
    }

    public void setMainregionname(String mainregionname) {
        this.mainregionname = mainregionname == null ? null : mainregionname.trim();
    }

    public String getRegionid() {
        return regionid;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid == null ? null : regionid.trim();
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname == null ? null : regionname.trim();
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname == null ? null : showname.trim();
    }

    public Integer getDrawnumber() {
        return drawnumber;
    }

    public void setDrawnumber(Integer drawnumber) {
        this.drawnumber = drawnumber;
    }

    public String getFillcolor() {
        return fillcolor;
    }

    public void setFillcolor(String fillcolor) {
        this.fillcolor = fillcolor == null ? null : fillcolor.trim();
    }

    public String getOutlinecolor() {
        return outlinecolor;
    }

    public void setOutlinecolor(String outlinecolor) {
        this.outlinecolor = outlinecolor == null ? null : outlinecolor.trim();
    }

    public String getShapetype() {
        return shapetype;
    }

    public void setShapetype(String shapetype) {
        this.shapetype = shapetype == null ? null : shapetype.trim();
    }

    public String getShapedrawparam() {
        return shapedrawparam;
    }

    public void setShapedrawparam(String shapedrawparam) {
        this.shapedrawparam = shapedrawparam == null ? null : shapedrawparam.trim();
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater == null ? null : updater.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}