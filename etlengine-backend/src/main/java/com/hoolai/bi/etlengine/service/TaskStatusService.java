package com.hoolai.bi.etlengine.service;

import com.hoolai.bi.etlengine.model.entity.TaskStatus;
import com.jian.service.GenericService;

public interface TaskStatusService extends GenericService<TaskStatus, Long>{
	
	TaskStatus getTaskStatusByCode(String name,String code);
	
}
