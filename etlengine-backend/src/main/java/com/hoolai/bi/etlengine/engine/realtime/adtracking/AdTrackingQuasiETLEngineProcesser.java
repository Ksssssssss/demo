package com.hoolai.bi.etlengine.engine.realtime.adtracking;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hoolai.bi.etlengine.core.Constant;
import com.hoolai.bi.etlengine.model.EngineEtlsAdapter;
import com.hoolai.bi.etlengine.model.TaskStatusAdapter;
import com.hoolai.bi.etlengine.model.entity.EngineEtls;
import com.hoolai.bi.etlengine.model.entity.TaskStatus;
import com.hoolai.bi.etlengine.service.EngineEtlsService;
import com.hoolai.bi.etlengine.service.TaskStatusService;
import com.hoolai.bi.etlengine.util.SpringApplicationUtil;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;
import com.hoolai.bi.report.job.SystemThreadPoolExecutor;
import com.jian.tools.util.JSONUtils;

@Component
public class AdTrackingQuasiETLEngineProcesser {
	
	private static final Logger logger=Logger.getLogger(AdTrackingQuasiETLEngineProcesser.class.getSimpleName());
	
	private static final Logger ERROR_LOGGER=Logger.getLogger("exception");
	
	public static final String TASK_STATUS_NAME=Type.ADTRACKING_QUASI_ETL_ENGINE_RUN.getDisplayName();

	@Autowired
	private SpringApplicationUtil springApplicationUtil;
	
	@Autowired
	private EngineEtlsService engineEtlsService;
	
	@Autowired
	private TaskStatusService taskStatusService;
	
	private final SystemThreadPoolExecutor asyncETLEngineThreadPoolExecutor=new SystemThreadPoolExecutor(TASK_STATUS_NAME, 1);
	
	/**
	 * 异步执行
	 * @param etlEngineGameInfo
	 */
	public void asyncProcessGame(ETLEngineGameInfo etlEngineGameInfo){
		if(etlEngineGameInfo==null){
			return ;
		}
		List<ETLEngineGameInfo> etlEngineGameInfos=new ArrayList<ETLEngineGameInfo>();
		etlEngineGameInfos.add(etlEngineGameInfo);
		this.asyncProcessGame(etlEngineGameInfos);
	}
	/**
	 * 异步执行
	 * @param etlEngineGameInfos
	 */
	public void asyncProcessGame(List<ETLEngineGameInfo> etlEngineGameInfos){
		
		if(etlEngineGameInfos==null||etlEngineGameInfos.isEmpty()){
			return ;
		}
		
		List<List<ETLEngineGameInfo>> daysPrepareEtlEngineGameInfos=new ArrayList<List<ETLEngineGameInfo>>();
		daysPrepareEtlEngineGameInfos.add(etlEngineGameInfos);
		
		this.asyncProcessGameDays(daysPrepareEtlEngineGameInfos);
		
	}
	/**
	 * 异步执行
	 * @param etlEngineGameInfos
	 */
	public void asyncProcessGameDays(List<List<ETLEngineGameInfo>> etlEngineGameInfos){
		
		if(etlEngineGameInfos==null||etlEngineGameInfos.isEmpty()){
			return ;
		}
		
		AsyncETLEngineTrigger asyncETLEngineTrigger=new AsyncETLEngineTrigger(this, etlEngineGameInfos);
		
		this.asyncETLEngineThreadPoolExecutor.submit(asyncETLEngineTrigger);
		
	}
	
	/**
	 * 同步执行
	 * @param etlEngineGameInfo
	 */
	public void processGame(ETLEngineGameInfo etlEngineGameInfo){
		if(etlEngineGameInfo==null){
			return ;
		}
		List<ETLEngineGameInfo> etlEngineGameInfos=new ArrayList<ETLEngineGameInfo>();
		etlEngineGameInfos.add(etlEngineGameInfo);
		this.processGames(etlEngineGameInfos);
	}
	/**
	 * 外围集合串行，内部集合并行方式执行
	 * @param etlEngineGameInfos
	 */
	public void processGameDays(List<List<ETLEngineGameInfo>> etlEngineGameInfos){
		if(etlEngineGameInfos==null||etlEngineGameInfos.isEmpty()){
			return ;
		}
		for (List<ETLEngineGameInfo> list : etlEngineGameInfos) {
			this.processGames(list);
		}
	}
	
	/**
	 * 同步执行
	 * @param etlEngineGameInfos
	 */
	public void processGames(List<ETLEngineGameInfo> etlEngineGameInfos){
		
		if(etlEngineGameInfos==null||etlEngineGameInfos.isEmpty()){
			return ;
		}
		
		long begin=System.currentTimeMillis();
		
		int suitThreadNum=Constant.ENGINE_THREAD_NUM>etlEngineGameInfos.size()?etlEngineGameInfos.size():Constant.ENGINE_THREAD_NUM;
		
		SystemThreadPoolExecutor gameETLEngineThreadPoolExecutor=new SystemThreadPoolExecutor(TASK_STATUS_NAME, suitThreadNum);
		
		List<EngineEtls> engineEtls=this.engineEtlsService.getEngineEtlsByType(EngineEtlsAdapter.Types.ADTRACKING_QUASI.name().toLowerCase());
		if(engineEtls==null){
			return ;
		}
		Map<Integer,List<EngineEtls>> levelEngineEtlsMap=new HashMap<Integer,List<EngineEtls>>();
		for (EngineEtls engineEtl : engineEtls) {
			Integer level=engineEtl.getLevel();
			List<EngineEtls> tempList=null;
			if(levelEngineEtlsMap.containsKey(level)){
				tempList=levelEngineEtlsMap.get(level);
			}else{
				tempList=new ArrayList<EngineEtls>();
				levelEngineEtlsMap.put(level, tempList);
			}
			tempList.add(engineEtl);
		}
		
		for (ETLEngineGameInfo etlEngineGameInfo : etlEngineGameInfos) {
			try {
				if(etlEngineGameInfo==null){
					continue ;
				}
				TaskStatus taskStatus=this.taskStatusService.getTaskStatusByCode(TASK_STATUS_NAME,etlEngineGameInfo.getIdentification());
				if(!TaskStatusAdapter.canExecute(taskStatus)){
					continue;
				}
				
				AdTrackingQuasiETLEngineControllerThread gameETLEngineControllerThread=new AdTrackingQuasiETLEngineControllerThread(etlEngineGameInfo.getSnid(), etlEngineGameInfo.getGameid(), etlEngineGameInfo.getDs(), 
						Constant.ENGINE_HQL_THREAD_NUM, levelEngineEtlsMap, 
						etlEngineGameInfo.getStep(), springApplicationUtil);	
				gameETLEngineThreadPoolExecutor.submit(gameETLEngineControllerThread);
				
				if(taskStatus==null){
					taskStatus=new TaskStatus();
					taskStatus.setName(TASK_STATUS_NAME);
					taskStatus.setCode(etlEngineGameInfo.getIdentification());
					taskStatus.setRuningTime(new Date());
					taskStatus.setStatus(TaskStatusAdapter.Status.RUNNING.ordinal());
					taskStatus.setRunTimes(1);
					Map<String,Object> remarkInfo=new HashMap<String,Object>();
					remarkInfo.put("etlEngineGameInfo", etlEngineGameInfo);
					taskStatus.setRemark(JSONUtils.toJSON(remarkInfo));
					this.taskStatusService.saveEntity(taskStatus);
				}else{
					taskStatus.setStatus(TaskStatusAdapter.Status.RUNNING.ordinal());
					taskStatus.setRunTimes(taskStatus.getRunTimes()+1);
					taskStatus.setRuningTime(new Date());
					taskStatus.setEndTime(null);
					this.taskStatusService.modifyEntity(taskStatus);
				}
				
				
			} catch (Exception e) {
				ERROR_LOGGER.error(e.getMessage(), e);
			}
		}

		
		try {
			
			gameETLEngineThreadPoolExecutor.getExecutor().shutdown();
			gameETLEngineThreadPoolExecutor.getExecutor().awaitTermination(1, TimeUnit.HOURS);
			
		} catch (InterruptedException e) {
			ERROR_LOGGER.error(e.getMessage(), e);
		}
		
		long end=System.currentTimeMillis();
		
		logger.info("all game etl engine processer final. spendTimes:"+(end-begin));
	}
	
	private static class AsyncETLEngineTrigger implements Runnable{
		
		private final AdTrackingQuasiETLEngineProcesser engineProcesser;
		
		private final List<List<ETLEngineGameInfo>> etlEngineGameInfos;

		public AsyncETLEngineTrigger(AdTrackingQuasiETLEngineProcesser engineProcesser, List<List<ETLEngineGameInfo>> etlEngineGameInfos) {
			super();
			this.engineProcesser = engineProcesser;
			this.etlEngineGameInfos=etlEngineGameInfos;
		}

		@Override
		public void run() {
			this.engineProcesser.processGameDays(etlEngineGameInfos);
		}
		
	}
	
}
