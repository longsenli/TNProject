package com.tnpy.mes.model.mysql;

public class Material {
    private String id;

    private String typeid;

    private String name;

    private String description;

    private Integer status;

    private String shortname;

    private Double eachbatchnumber;

    private Double wage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid == null ? null : typeid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname == null ? null : shortname.trim();
    }

    public Double getEachbatchnumber() {
        return eachbatchnumber;
    }

    public void setEachbatchnumber(Double eachbatchnumber) {
        this.eachbatchnumber = eachbatchnumber;
    }

    public Double getWage() {
        return wage;
    }

    public void setWage(Double wage) {
        this.wage = wage;
    }
}