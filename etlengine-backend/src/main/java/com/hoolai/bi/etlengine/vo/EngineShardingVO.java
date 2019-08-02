package com.hoolai.bi.etlengine.vo;

import com.hoolai.bi.etlengine.model.entity.EngineSharding;
import com.hoolai.dao.pagination.AbstractObjectVO;

public class EngineShardingVO extends AbstractObjectVO<EngineSharding> {

	public EngineShardingVO() {
		super();
		this.entity=new EngineSharding();
	}

	public EngineShardingVO(EngineSharding entity) {
		super(entity);
	}

}
