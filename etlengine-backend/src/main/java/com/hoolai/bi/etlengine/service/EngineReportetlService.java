package com.hoolai.bi.etlengine.service;

import java.util.List;

import com.hoolai.bi.etlengine.model.entity.EngineReportetl;
import com.jian.service.GenericService;

public interface EngineReportetlService extends GenericService<EngineReportetl, Long>{
	
	List<EngineReportetl> getAll();

}
