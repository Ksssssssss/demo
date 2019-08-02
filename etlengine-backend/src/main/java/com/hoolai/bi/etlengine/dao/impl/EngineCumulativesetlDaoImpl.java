package com.hoolai.bi.etlengine.dao.impl;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.etlengine.dao.EngineCumulativesetlDao;
import com.hoolai.bi.etlengine.model.entity.EngineCumulativesetl;
import com.hoolai.dao.impl.GenericDaoImpl;

@Repository
public class EngineCumulativesetlDaoImpl extends GenericDaoImpl<EngineCumulativesetl, Long> implements EngineCumulativesetlDao {

	@Override
	public String namespace() {
		return EngineCumulativesetl.class.getName();
	}
	
}
