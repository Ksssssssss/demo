package com.hoolai.bi.etlengine.model.entity;

import java.util.Date;

public abstract class AbstractEngineEtlsAdapter {
	
    private Integer id;

    private String title;

    private Integer terminalType;

    private Integer systemType;
    
    private String equipMark;

    private Date addedAt;

    private Date modifyAt;

    private String description;

    private String template;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public String getEquipMark() {
		return equipMark;
	}

	public void setEquipMark(String equipMark) {
		this.equipMark = equipMark;
	}

	public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}