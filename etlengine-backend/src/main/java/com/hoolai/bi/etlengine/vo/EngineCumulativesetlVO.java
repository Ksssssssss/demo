package com.hoolai.bi.etlengine.vo;

import com.hoolai.bi.etlengine.model.entity.EngineCumulativesetl;
import com.hoolai.dao.pagination.AbstractObjectVO;

public class EngineCumulativesetlVO extends AbstractObjectVO<EngineCumulativesetl> {

	public EngineCumulativesetlVO() {
		super();
		this.entity=new EngineCumulativesetl();
	}

	public EngineCumulativesetlVO(EngineCumulativesetl entity) {
		super(entity);
	}

}
