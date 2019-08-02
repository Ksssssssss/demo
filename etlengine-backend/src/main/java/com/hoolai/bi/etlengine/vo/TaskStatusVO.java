package com.hoolai.bi.etlengine.vo;

import com.hoolai.bi.etlengine.model.entity.TaskStatus;
import com.hoolai.dao.pagination.AbstractObjectVO;

public class TaskStatusVO extends AbstractObjectVO<TaskStatus> {

	public TaskStatusVO() {
		super();
		this.entity=new TaskStatus();
	}

	public TaskStatusVO(TaskStatus entity) {
		super(entity);
	}

}
