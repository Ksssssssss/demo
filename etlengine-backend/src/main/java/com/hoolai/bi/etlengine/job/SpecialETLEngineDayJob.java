package com.hoolai.bi.etlengine.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hoolai.bi.etlengine.engine.ETLEngineProcesser;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.hoolai.bi.report.job.AbstractExecuteJob;

public class SpecialETLEngineDayJob extends AbstractExecuteJob {
	
	private Logger logger=Logger.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	private ETLEngineProcesser engineProcesser;
	
	@Override
	public Map<String, Object> executeJob() throws Exception {
		List<ETLEngineGameInfo> etlEngineGameInfos=new ArrayList<ETLEngineGameInfo>();
		ETLEngineGameInfo etlEngineGameInfo0=new ETLEngineGameInfo("11", "85", "2015-03-27", 0);
		etlEngineGameInfos.add(etlEngineGameInfo0);
		this.engineProcesser.processGames(etlEngineGameInfos);
		return Collections.emptyMap();
	}
	
}
