package com.hoolai.bi.etlengine.service;

import java.util.List;

import com.hoolai.bi.etlengine.model.entity.EngineEtls;
import com.jian.service.GenericService;

public interface EngineEtlsService extends GenericService<EngineEtls, Long>{
	
	List<EngineEtls> getAll();
	
	List<EngineEtls> getEngineEtlsByType(String type);

}
