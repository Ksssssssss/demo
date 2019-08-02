package com.hoolai.bi.etlengine.dao.impl;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.etlengine.dao.EngineEtlsDao;
import com.hoolai.bi.etlengine.model.entity.EngineEtls;
import com.hoolai.dao.impl.GenericDaoImpl;

@Repository
public class EngineEtlsDaoImpl extends GenericDaoImpl<EngineEtls, Long> implements EngineEtlsDao {

	@Override
	public String namespace() {
		return EngineEtls.class.getName();
	}
	
}
