package com.hoolai.bi.etlengine.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolai.bi.etlengine.dao.EngineBaseetlDao;
import com.hoolai.bi.etlengine.model.entity.EngineBaseetl;
import com.hoolai.bi.etlengine.service.EngineBaseetlService;
import com.hoolai.bi.etlengine.vo.EngineBaseetlVO;
import com.hoolai.dao.GenericDao;
import com.jian.service.impl.GenericServiceImpl;

@Service
public class EngineBaseetlServiceImpl extends GenericServiceImpl<EngineBaseetl, Long> implements EngineBaseetlService {

	@Autowired
	private EngineBaseetlDao entityDao;
	
	@Override
    public GenericDao<EngineBaseetl, Long> getGenricDao() {
            return this.entityDao;
    }

	@Override
	public List<EngineBaseetl> getAll() {
		try {
			EngineBaseetlVO engineBaseetlVO=new EngineBaseetlVO();
			return this.entityDao.selectMatchList(engineBaseetlVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
