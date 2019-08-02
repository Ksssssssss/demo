package com.hoolai.bi.etlengine.engine;

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
import com.hoolai.bi.etlengine.service.EngineBaseetlService;
import com.hoolai.bi.etlengine.service.EngineCumulativesetlService;
import com.hoolai.bi.etlengine.service.EngineCustomconceptionService;
import com.hoolai.bi.etlengine.service.EngineCustomlogicchainService;
import com.hoolai.bi.etlengine.service.EngineEtlsService;
import com.hoolai.bi.etlengine.service.EngineReportetlService;
import com.hoolai.bi.etlengine.service.TaskStatusService;
import com.hoolai.bi.etlengine.util.SpringApplicationUtil;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;
import com.hoolai.bi.report.job.SystemThreadPoolExecutor;
import com.hoolai.bi.report.util.Types.GameMobRunSystemType;
import com.hoolai.bi.report.util.Types.GamePcRunSystemType;
import com.hoolai.bi.report.util.Types.TerminalType;
import com.jian.tools.util.JSONUtils;

@Component
public class ETLEngineProcesser {
	
	private static final Logger logger=Logger.getLogger(ETLEngineProcesser.class.getSimpleName());
	
	private static final Logger ERROR_LOGGER=Logger.getLogger("exception");
	
	public static final String TASK_STATUS_NAME=Type.ETL_ENGINE_RUN.getDisplayName();

	@Autowired
	private SpringApplicationUtil springApplicationUtil;
	
	@Autowired
	private EngineBaseetlService engineBaseetlService;
	
	@Autowired
	private EngineCumulativesetlService engineCumulativesetlService;
	
	@Autowired
	private EngineCustomconceptionService engineCustomconceptionService;
	
	@Autowired
	private EngineCustomlogicchainService engineCustomlogicchainService;
	
	@Autowired
	private EngineReportetlService engineReportetlService;
	
	@Autowired
	private TaskStatusService taskStatusService;
	
	@Autowired
	private EngineEtlsService engineEtlsService;
	
	private final SystemThreadPoolExecutor asyncETLEngineThreadPoolExecutor=new SystemThreadPoolExecutor("async-etl-engine", 3);
	
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
		
		SystemThreadPoolExecutor gameETLEngineThreadPoolExecutor=new SystemThreadPoolExecutor("game-etl-engine", suitThreadNum);

		
		List<EngineEtls> engineEtls = this.engineEtlsService.getEngineEtlsByType(EngineEtlsAdapter.Types.DAILY.name().toLowerCase());
		if(null == engineEtls || engineEtls.size() <= 0){
			return ;
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
				
				Map<Integer, List<EngineEtls>> levelEngineEtlsMap = new HashMap<Integer, List<EngineEtls>>();
				
				TerminalType terminalType = TerminalType.instance(etlEngineGameInfo.getTerminalType());
				if(TerminalType.MOBILE_PHONE.equals(terminalType)){
					
					if(GameMobRunSystemType.IOS.getId()==etlEngineGameInfo.getSystemType()){
						//ios版
						levelEngineEtlsMap.putAll(getEengineEtls(engineEtls, TerminalType.MOBILE_PHONE.getId(), GameMobRunSystemType.IOS.getId()));
					}else{
						//levelEngineEtlsMap.putAll(getEengineEtls(engineEtls, TerminalType.PC.getId(), GamePcRunSystemType.DEFAULT.getId()));
						levelEngineEtlsMap.putAll(getEengineEtls(engineEtls, TerminalType.MOBILE_PHONE.getId(), GameMobRunSystemType.ANDROID.getId()));
					}
				}else{
					levelEngineEtlsMap.putAll(getEengineEtls(engineEtls, TerminalType.PC.getId(), GamePcRunSystemType.DEFAULT.getId()));
				}
				
		
				// 经济系统不同体系etl
				EngineEtlsAdapter.Types economyTypes=EngineEtlsAdapter.Types.getEconomyTypes(etlEngineGameInfo.getSnid()+"_"+etlEngineGameInfo.getGameid());
				List<EngineEtls> economyEngineEtls=this.engineEtlsService.getEngineEtlsByType(economyTypes.name().toLowerCase());
				
				GameETLEngineControllerThread gameETLEngineControllerThread = new GameETLEngineControllerThread(etlEngineGameInfo.getSnid(), etlEngineGameInfo.getGameid(), etlEngineGameInfo.getDs(), 
						Constant.ENGINE_HQL_THREAD_NUM, 
						levelEngineEtlsMap, economyEngineEtls,
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
			gameETLEngineThreadPoolExecutor.getExecutor().awaitTermination(12, TimeUnit.HOURS);
			
		} catch (InterruptedException e) {
			ERROR_LOGGER.error(e.getMessage(), e);
		}
		
		long end=System.currentTimeMillis();
		
		logger.info("all game etl engine processer final. spendTimes:"+(end-begin));
	}
	
	private Map<Integer,List<EngineEtls>> getEengineEtls(List<EngineEtls> engineEtls, Integer terminalType, Integer systemType){
		
		//第一层计算ETL区分PC，MOBILE  其他不区分
		//如果是第一次，判断TerminalType和SystemType
		Map<Integer,List<EngineEtls>> levelEngineEtlsMap = new HashMap<Integer,List<EngineEtls>>();
		
		for (EngineEtls engineEtl : engineEtls) {
			List<EngineEtls> tempList = null;
			Integer level = engineEtl.getLevel();
			
			//在第一层做过区分，需要在每一层判断如果是安卓就需要拿equip模板，pc不需要equip模板
			if(1 == level.intValue()){
				// TerminalTpe systemType  pc（0，0），ios(1,0), 安卓（1，1）
				if(null != engineEtl.getTerminalType() && terminalType.intValue() == TerminalType.MOBILE_PHONE.getId()
						&& systemType.intValue() ==GameMobRunSystemType.IOS.getId() && engineEtl.getTerminalType()==TerminalType.MOBILE_PHONE.getId() &&
						null != engineEtl.getSystemType()){
					//第一层的ios  模板
					if(levelEngineEtlsMap.containsKey(level)){
						tempList = levelEngineEtlsMap.get(level);
					}else{
						tempList = new ArrayList<EngineEtls>();
						levelEngineEtlsMap.put(level, tempList);
					}
					
					tempList.add(engineEtl);
		
				}else if(null != engineEtl.getTerminalType() && terminalType.intValue() == TerminalType.MOBILE_PHONE.getId()
						&& systemType.intValue() == GameMobRunSystemType.ANDROID.getId() && engineEtl.getTerminalType()==TerminalType.PC.getId() &&
						null != engineEtl.getSystemType()){
					//第一层的安卓模板
					if(levelEngineEtlsMap.containsKey(level)){
						tempList = levelEngineEtlsMap.get(level);
					}else{
						tempList = new ArrayList<EngineEtls>();
						levelEngineEtlsMap.put(level, tempList);
					}
					
					tempList.add(engineEtl);
					
				}else if(null != engineEtl.getTerminalType() && terminalType.intValue() == TerminalType.PC.getId()
						&& systemType.intValue() ==GamePcRunSystemType.DEFAULT.getId() && engineEtl.getTerminalType()==TerminalType.PC.getId() &&
						null != engineEtl.getSystemType() && (!(engineEtl.getEquipMark().equals("equip")))){
					
					if(levelEngineEtlsMap.containsKey(level)){
						tempList = levelEngineEtlsMap.get(level);
					}else{
						tempList = new ArrayList<EngineEtls>();
						levelEngineEtlsMap.put(level, tempList);
					}
					
					tempList.add(engineEtl);
				}

			}else{
				
				if(levelEngineEtlsMap.containsKey(level)){
					tempList = levelEngineEtlsMap.get(level);
				}else{
					tempList = new ArrayList<EngineEtls>();
					levelEngineEtlsMap.put(level, tempList);
				}
				
//				if(!(terminalType.intValue()==TerminalType.PC.getId() && engineEtl.getEquipMark().equals("equip") )){
//					tempList.add(engineEtl);
//				}
				
				//如果是pc 且这条etl包含equip字段，那么就不添加etl
				
				if(!(terminalType.intValue() == TerminalType.PC.getId() && 
						systemType.intValue() == GamePcRunSystemType.DEFAULT.getId() &&
						engineEtl.getEquipMark().equals("equip"))){
					
					tempList.add(engineEtl);
				}
				
			}
			
			
			
		}
		return levelEngineEtlsMap;
	}
	
	private static class AsyncETLEngineTrigger implements Runnable{
		
		private final ETLEngineProcesser engineProcesser;
		
		private final List<List<ETLEngineGameInfo>> etlEngineGameInfos;

		public AsyncETLEngineTrigger(ETLEngineProcesser engineProcesser, List<List<ETLEngineGameInfo>> etlEngineGameInfos) {
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
