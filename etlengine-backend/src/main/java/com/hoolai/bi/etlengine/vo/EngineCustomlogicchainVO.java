package com.hoolai.bi.etlengine.vo;

import com.hoolai.bi.etlengine.model.entity.EngineCustomlogicchain;
import com.hoolai.dao.pagination.AbstractObjectVO;

public class EngineCustomlogicchainVO extends AbstractObjectVO<EngineCustomlogicchain> {

	public EngineCustomlogicchainVO() {
		super();
		this.entity=new EngineCustomlogicchain();
	}

	public EngineCustomlogicchainVO(EngineCustomlogicchain entity) {
		super(entity);
	}

}
