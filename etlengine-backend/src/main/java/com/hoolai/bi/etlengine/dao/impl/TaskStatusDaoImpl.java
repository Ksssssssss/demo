package com.hoolai.bi.etlengine.dao.impl;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.etlengine.dao.TaskStatusDao;
import com.hoolai.bi.etlengine.model.entity.TaskStatus;
import com.hoolai.dao.impl.GenericDaoImpl;

@Repository
public class TaskStatusDaoImpl extends GenericDaoImpl<TaskStatus, Long> implements TaskStatusDao {

	@Override
	public String namespace() {
		return TaskStatus.class.getName();
	}
	
}
