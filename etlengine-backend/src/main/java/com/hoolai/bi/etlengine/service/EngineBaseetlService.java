package com.hoolai.bi.etlengine.service;

import java.util.List;

import com.hoolai.bi.etlengine.model.entity.EngineBaseetl;
import com.jian.service.GenericService;

public interface EngineBaseetlService extends GenericService<EngineBaseetl, Long>{
	
	List<EngineBaseetl> getAll();

}
