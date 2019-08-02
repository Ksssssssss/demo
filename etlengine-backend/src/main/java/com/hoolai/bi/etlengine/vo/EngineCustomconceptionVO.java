package com.hoolai.bi.etlengine.vo;

import com.hoolai.bi.etlengine.model.entity.EngineCustomconception;
import com.hoolai.dao.pagination.AbstractObjectVO;

public class EngineCustomconceptionVO extends AbstractObjectVO<EngineCustomconception> {

	public EngineCustomconceptionVO() {
		super();
		this.entity=new EngineCustomconception();
	}

	public EngineCustomconceptionVO(EngineCustomconception entity) {
		super(entity);
	}

}
