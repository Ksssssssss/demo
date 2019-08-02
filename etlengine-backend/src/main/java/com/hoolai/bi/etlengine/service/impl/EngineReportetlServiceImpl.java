package com.hoolai.bi.etlengine.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolai.bi.etlengine.dao.EngineReportetlDao;
import com.hoolai.bi.etlengine.model.entity.EngineReportetl;
import com.hoolai.bi.etlengine.service.EngineReportetlService;
import com.hoolai.bi.etlengine.vo.EngineReportetlVO;
import com.hoolai.dao.GenericDao;
import com.jian.service.impl.GenericServiceImpl;

@Service
public class EngineReportetlServiceImpl extends GenericServiceImpl<EngineReportetl, Long> implements EngineReportetlService {

	@Autowired
	private EngineReportetlDao entityDao;
	
	@Override
    public GenericDao<EngineReportetl, Long> getGenricDao() {
            return this.entityDao;
    }

	@Override
	public List<EngineReportetl> getAll() {
		try {
			EngineReportetlVO engineBaseetlVO=new EngineReportetlVO();
			return this.entityDao.selectMatchList(engineBaseetlVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
