package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Content {
    @JsonProperty(value = "NULL")
    private String id;
    @JsonProperty(value = "-1")
    private Integer type;
    @JsonProperty(value = "NULL")
    private String title;
    @JsonProperty(value = "NULL")
    private String context;
    @JsonProperty(value = "NULL")
    private String creator;
    @JsonProperty(value = "NULL")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    @JsonProperty(value = "NULL")
    private String feedback;
    @JsonProperty(value = "NULL")
    private String feedbackor;
    @JsonProperty(value = "NULL")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date feedbacktime;
    @JsonProperty(value = "-1")
    private Integer status;
    @JsonProperty(value = "NULL")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback == null ? null : feedback.trim();
    }

    public String getFeedbackor() {
        return feedbackor;
    }

    public void setFeedbackor(String feedbackor) {
        this.feedbackor = feedbackor == null ? null : feedbackor.trim();
    }

    public Date getFeedbacktime() {
        return feedbacktime;
    }

    public void setFeedbacktime(Date feedbacktime) {
        this.feedbacktime = feedbacktime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}