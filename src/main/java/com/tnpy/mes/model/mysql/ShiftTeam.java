package com.tnpy.mes.model.mysql;

public class ShiftTeam {
    private String id;

    private String plantid;

    private String processid;

    private String abshift;

    private String abshiftname;

    private String staffid;

    private String staffname;

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

    public String getAbshift() {
        return abshift;
    }

    public void setAbshift(String abshift) {
        this.abshift = abshift == null ? null : abshift.trim();
    }

    public String getAbshiftname() {
        return abshiftname;
    }

    public void setAbshiftname(String abshiftname) {
        this.abshiftname = abshiftname == null ? null : abshiftname.trim();
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid == null ? null : staffid.trim();
    }

    public String getStaffname() {
        return staffname;
    }

    public void setStaffname(String staffname) {
        this.staffname = staffname == null ? null : staffname.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}