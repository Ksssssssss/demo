package com.hoolai.bi.etlengine.job;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hoolai.bi.etlengine.engine.ETLEngineProcesser;
import com.hoolai.bi.report.job.AbstractExecuteJob;

public class ETLEngineDayJob extends AbstractExecuteJob {
	
	@Autowired
	private ETLEngineProcesser engineProcesser;
	
	private boolean isExecute=false;
	
	@Override
	public Map<String, Object> executeJob() throws Exception {

		if(this.isExecute){
			super.info(this.getClass().getSimpleName()+" already executed.");
			return Collections.emptyMap();
		}
		
		this.isExecute=true;
		
//		List<ETLEngineGameInfo> etlEngineGameInfos=new ArrayList<ETLEngineGameInfo>();
//		ETLEngineGameInfo etlEngineGameInfo0=new ETLEngineGameInfo("32", "117", "2015-03-31", 0);
//		etlEngineGameInfos.add(etlEngineGameInfo0);
//		this.engineProcesser.processGames(etlEngineGameInfos);
		super.info(this.getClass().getSimpleName()+" finiched.");
		return Collections.emptyMap();
	}
	
}
