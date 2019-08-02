package com.hoolai.bi.etlengine.dao.impl;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.etlengine.dao.EngineReportetlDao;
import com.hoolai.bi.etlengine.model.entity.EngineReportetl;
import com.hoolai.dao.impl.GenericDaoImpl;

@Repository
public class EngineReportetlDaoImpl extends GenericDaoImpl<EngineReportetl, Long> implements EngineReportetlDao {

	@Override
	public String namespace() {
		return EngineReportetl.class.getName();
	}
	
}
