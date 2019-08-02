package com.hoolai.bi.etlengine.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolai.bi.etlengine.dao.EngineEtlsDao;
import com.hoolai.bi.etlengine.model.entity.EngineEtls;
import com.hoolai.bi.etlengine.service.EngineEtlsService;
import com.hoolai.bi.etlengine.vo.EngineEtlsVO;
import com.hoolai.dao.GenericDao;
import com.jian.service.impl.GenericServiceImpl;

@Service
public class EngineEtlsServiceImpl extends GenericServiceImpl<EngineEtls, Long> implements EngineEtlsService {

	@Autowired
	private EngineEtlsDao entityDao;
	
	@Override
    public GenericDao<EngineEtls, Long> getGenricDao() {
            return this.entityDao;
    }

	@Override
	public List<EngineEtls> getAll() {
		try {
			EngineEtlsVO engineBaseetlVO=new EngineEtlsVO();
			return this.entityDao.selectMatchList(engineBaseetlVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@Override
	public List<EngineEtls> getEngineEtlsByType(String type) {
		try {
			EngineEtlsVO engineBaseetlVO=new EngineEtlsVO();
			engineBaseetlVO.getEntity().setType(type);
			return this.entityDao.selectMatchList(engineBaseetlVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
