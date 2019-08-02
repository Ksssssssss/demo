package com.hoolai.bi.etlengine.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolai.bi.etlengine.dao.EngineCustomconceptionDao;
import com.hoolai.bi.etlengine.model.entity.EngineCustomconception;
import com.hoolai.bi.etlengine.service.EngineCustomconceptionService;
import com.hoolai.bi.etlengine.vo.EngineCustomconceptionVO;
import com.hoolai.dao.GenericDao;
import com.jian.service.impl.GenericServiceImpl;

@Service
public class EngineCustomconceptionServiceImpl extends GenericServiceImpl<EngineCustomconception, Long> implements EngineCustomconceptionService {

	@Autowired
	private EngineCustomconceptionDao entityDao;
	
	@Override
    public GenericDao<EngineCustomconception, Long> getGenricDao() {
            return this.entityDao;
    }

	@Override
	public List<EngineCustomconception> getAll() {
		try {
			EngineCustomconceptionVO engineBaseetlVO=new EngineCustomconceptionVO();
			return this.entityDao.selectMatchList(engineBaseetlVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
