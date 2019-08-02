package com.hoolai.bi.etlengine.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolai.bi.etlengine.dao.TaskStatusDao;
import com.hoolai.bi.etlengine.model.entity.TaskStatus;
import com.hoolai.bi.etlengine.service.TaskStatusService;
import com.hoolai.bi.etlengine.vo.TaskStatusVO;
import com.hoolai.dao.GenericDao;
import com.jian.service.impl.GenericServiceImpl;

@Service
public class TaskStatusServiceImpl extends GenericServiceImpl<TaskStatus, Long> implements TaskStatusService {

	@Autowired
	private TaskStatusDao entityDao;
	
	@Override
    public GenericDao<TaskStatus, Long> getGenricDao() {
            return this.entityDao;
    }

	@Override
	public TaskStatus getTaskStatusByCode(String name,String code) {
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(code)){
			return null;
		}
		try {
			TaskStatusVO taskStatusVO=new TaskStatusVO();
			taskStatusVO.getEntity().setName(name);
			taskStatusVO.getEntity().setCode(code);
			return this.entityDao.selectOne(taskStatusVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
