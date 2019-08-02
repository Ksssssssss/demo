package com.hoolai.bi.etlengine.service;

import java.util.List;

import com.hoolai.bi.etlengine.model.entity.EngineSharding;
import com.jian.service.GenericService;

public interface EngineShardingService extends GenericService<EngineSharding, Long>{
	
	List<EngineSharding> getAll();

}
