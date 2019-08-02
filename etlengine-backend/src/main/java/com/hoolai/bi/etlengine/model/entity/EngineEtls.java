package com.hoolai.bi.etlengine.model.entity;


public class EngineEtls  extends AbstractEngineEtlsAdapter{
    private String type;

    private Integer level;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

  
}