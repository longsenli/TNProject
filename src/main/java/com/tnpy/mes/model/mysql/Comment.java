package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Comment {
    @JsonProperty(value = "NULL")
    private String id;
    @JsonProperty(value = "NULL")
    private String commentor;
    @JsonProperty(value = "NULL")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @JsonProperty(value = "NULL")
    private String text;
    @JsonProperty(value = "NULL")
    private String contentid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCommentor() {
        return commentor;
    }

    public void setCommentor(String commentor) {
        this.commentor = commentor == null ? null : commentor.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid == null ? null : contentid.trim();
    }
}