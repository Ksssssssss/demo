package com.hoolai.bi.etlengine.vo;

import com.hoolai.bi.etlengine.model.entity.EngineReportetl;
import com.hoolai.dao.pagination.AbstractObjectVO;

public class EngineReportetlVO extends AbstractObjectVO<EngineReportetl> {

	public EngineReportetlVO() {
		super();
		this.entity=new EngineReportetl();
	}

	public EngineReportetlVO(EngineReportetl entity) {
		super(entity);
	}

}
