package com.hoolai.bi.etlengine.dao.impl;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.etlengine.dao.EngineCustomconceptionDao;
import com.hoolai.bi.etlengine.model.entity.EngineCustomconception;
import com.hoolai.dao.impl.GenericDaoImpl;

@Repository
public class EngineCustomconceptionDaoImpl extends GenericDaoImpl<EngineCustomconception, Long> implements EngineCustomconceptionDao {

	@Override
	public String namespace() {
		return EngineCustomconception.class.getName();
	}
	
}
