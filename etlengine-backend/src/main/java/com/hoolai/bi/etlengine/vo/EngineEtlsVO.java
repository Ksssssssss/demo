package com.hoolai.bi.etlengine.vo;

import com.hoolai.bi.etlengine.model.entity.EngineEtls;
import com.hoolai.dao.pagination.AbstractObjectVO;

public class EngineEtlsVO extends AbstractObjectVO<EngineEtls> {

	public EngineEtlsVO() {
		super();
		this.entity=new EngineEtls();
	}

	public EngineEtlsVO(EngineEtls entity) {
		super(entity);
	}

}
