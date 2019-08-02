package com.hoolai.bi.etlengine.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolai.bi.etlengine.dao.EngineCustomlogicchainDao;
import com.hoolai.bi.etlengine.model.entity.EngineCustomlogicchain;
import com.hoolai.bi.etlengine.service.EngineCustomlogicchainService;
import com.hoolai.bi.etlengine.vo.EngineCustomlogicchainVO;
import com.hoolai.dao.GenericDao;
import com.jian.service.impl.GenericServiceImpl;

@Service
public class EngineCustomlogicchainServiceImpl extends GenericServiceImpl<EngineCustomlogicchain, Long> implements EngineCustomlogicchainService {

	@Autowired
	private EngineCustomlogicchainDao entityDao;
	
	@Override
    public GenericDao<EngineCustomlogicchain, Long> getGenricDao() {
            return this.entityDao;
    }

	@Override
	public List<EngineCustomlogicchain> getAll() {
		try {
			EngineCustomlogicchainVO engineBaseetlVO=new EngineCustomlogicchainVO();
			return this.entityDao.selectMatchList(engineBaseetlVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
