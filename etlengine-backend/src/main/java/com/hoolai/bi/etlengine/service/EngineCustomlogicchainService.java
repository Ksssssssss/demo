package com.hoolai.bi.etlengine.service;

import java.util.List;

import com.hoolai.bi.etlengine.model.entity.EngineCustomlogicchain;
import com.jian.service.GenericService;

public interface EngineCustomlogicchainService extends GenericService<EngineCustomlogicchain, Long>{
	
	List<EngineCustomlogicchain> getAll();

}
