package com.hoolai.bi.etlengine.dao.impl;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.etlengine.dao.EngineBaseetlDao;
import com.hoolai.bi.etlengine.model.entity.EngineBaseetl;
import com.hoolai.dao.impl.GenericDaoImpl;

@Repository
public class EngineBaseetlDaoImpl extends GenericDaoImpl<EngineBaseetl, Long> implements EngineBaseetlDao {

	@Override
	public String namespace() {
		return EngineBaseetl.class.getName();
	}
	
}
