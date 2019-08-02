package com.hoolai.bi.etlengine.vo;

import com.hoolai.bi.etlengine.model.entity.EngineBaseetl;
import com.hoolai.dao.pagination.AbstractObjectVO;

public class EngineBaseetlVO extends AbstractObjectVO<EngineBaseetl> {

	public EngineBaseetlVO() {
		super();
		this.entity=new EngineBaseetl();
	}

	public EngineBaseetlVO(EngineBaseetl entity) {
		super(entity);
	}

}
