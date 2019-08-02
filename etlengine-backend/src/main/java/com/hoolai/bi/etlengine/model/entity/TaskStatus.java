package com.hoolai.bi.etlengine.model.entity;

import java.util.Date;

public class TaskStatus {
    private Long id;

    private String name;

    private String code;

    private Date runingTime;

    private Date endTime;

    private Integer continueTimes;

    private Integer status;

    private Integer runTimes;

    private Integer currFailTimes;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getRuningTime() {
        return runingTime;
    }

    public void setRuningTime(Date runingTime) {
        this.runingTime = runingTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getContinueTimes() {
        return continueTimes;
    }

    public void setContinueTimes(Integer continueTimes) {
        this.continueTimes = continueTimes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(Integer runTimes) {
        this.runTimes = runTimes;
    }

    public Integer getCurrFailTimes() {
        return currFailTimes;
    }

    public void setCurrFailTimes(Integer currFailTimes) {
        this.currFailTimes = currFailTimes;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}