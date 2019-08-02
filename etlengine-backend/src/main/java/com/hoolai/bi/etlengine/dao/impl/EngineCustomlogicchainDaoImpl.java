package com.hoolai.bi.etlengine.dao.impl;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.etlengine.dao.EngineCustomlogicchainDao;
import com.hoolai.bi.etlengine.model.entity.EngineCustomlogicchain;
import com.hoolai.dao.impl.GenericDaoImpl;

@Repository
public class EngineCustomlogicchainDaoImpl extends GenericDaoImpl<EngineCustomlogicchain, Long> implements EngineCustomlogicchainDao {

	@Override
	public String namespace() {
		return EngineCustomlogicchain.class.getName();
	}
	
}
