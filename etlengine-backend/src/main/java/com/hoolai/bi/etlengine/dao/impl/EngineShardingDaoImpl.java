package com.hoolai.bi.etlengine.dao.impl;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.etlengine.dao.EngineShardingDao;
import com.hoolai.bi.etlengine.model.entity.EngineSharding;
import com.hoolai.dao.impl.GenericDaoImpl;

@Repository
public class EngineShardingDaoImpl extends GenericDaoImpl<EngineSharding, Long> implements EngineShardingDao {

	@Override
	public String namespace() {
		return EngineSharding.class.getName();
	}
	
}
