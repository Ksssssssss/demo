package com.hoolai.bi.etlengine.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolai.bi.etlengine.dao. EngineCumulativesetlDao;
import com.hoolai.bi.etlengine.model.entity. EngineCumulativesetl;
import com.hoolai.bi.etlengine.service. EngineCumulativesetlService;
import com.hoolai.bi.etlengine.vo. EngineCumulativesetlVO;
import com.hoolai.dao.GenericDao;
import com.jian.service.impl.GenericServiceImpl;

@Service
public class EngineCumulativesetlServiceImpl extends GenericServiceImpl< EngineCumulativesetl, Long> implements  EngineCumulativesetlService {

	@Autowired
	private  EngineCumulativesetlDao entityDao;
	
	@Override
    public GenericDao< EngineCumulativesetl, Long> getGenricDao() {
            return this.entityDao;
    }

	@Override
	public List< EngineCumulativesetl> getAll() {
		try {
			 EngineCumulativesetlVO engineBaseetlVO=new  EngineCumulativesetlVO();
			return this.entityDao.selectMatchList(engineBaseetlVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
