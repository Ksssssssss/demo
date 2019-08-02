package com.hoolai.bi.etlengine.service;

import java.util.List;

import com.hoolai.bi.etlengine.model.entity.EngineCumulativesetl;
import com.jian.service.GenericService;

public interface EngineCumulativesetlService extends GenericService<EngineCumulativesetl, Long>{
	
	List<EngineCumulativesetl> getAll();

}
