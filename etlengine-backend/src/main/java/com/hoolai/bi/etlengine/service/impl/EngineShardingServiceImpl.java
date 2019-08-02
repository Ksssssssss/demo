package com.hoolai.bi.etlengine.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolai.bi.etlengine.dao.EngineShardingDao;
import com.hoolai.bi.etlengine.model.entity.EngineSharding;
import com.hoolai.bi.etlengine.service.EngineShardingService;
import com.hoolai.bi.etlengine.vo.EngineShardingVO;
import com.hoolai.dao.GenericDao;
import com.jian.service.impl.GenericServiceImpl;

@Service
public class EngineShardingServiceImpl extends GenericServiceImpl<EngineSharding, Long> implements EngineShardingService {

	@Autowired
	private EngineShardingDao entityDao;
	
	@Override
    public GenericDao<EngineSharding, Long> getGenricDao() {
            return this.entityDao;
    }

	@Override
	public List<EngineSharding> getAll() {
		try {
			EngineShardingVO engineBaseetlVO=new EngineShardingVO();
			return this.entityDao.selectMatchList(engineBaseetlVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
