package com.hoolai.bi.etlengine.service;

import java.util.List;

import com.hoolai.bi.etlengine.model.entity.EngineCustomconception;
import com.jian.service.GenericService;

public interface EngineCustomconceptionService extends GenericService<EngineCustomconception, Long>{
	
	List<EngineCustomconception> getAll();

}
