package com.hoolai.bi.etlengine.engine.realtime.quasi;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.hoolai.bi.etlengine.core.Constant;
import com.hoolai.bi.etlengine.engine.GameETLExecutor;
import com.hoolai.bi.etlengine.kafka.producer.ETLEngineFinishedProducer;
import com.hoolai.bi.etlengine.model.TaskStatusAdapter;
import com.hoolai.bi.etlengine.model.entity.AbstractEngineEtlsAdapter;
import com.hoolai.bi.etlengine.model.entity.EngineEtls;
import com.hoolai.bi.etlengine.model.entity.TaskStatus;
import com.hoolai.bi.etlengine.service.TaskStatusService;
import com.hoolai.bi.etlengine.util.SpringApplicationUtil;
import com.hoolai.bi.notifyer.sms.HoolaiSmsSenderProxy;
import com.hoolai.bi.notifyer.sms.HoolaiSmsSenderProxy.SmsMess;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;
import com.jian.tools.util.JSONUtils;

public class GameQuasiETLEngineControllerThread implements Runnable{
	
	private static final Logger LOGGER=Logger.getLogger(GameQuasiETLEngineControllerThread.class.getSimpleName());
	
	private static final Logger ERROR_LOGGER=Logger.getLogger("exception");
	
	private static final HoolaiSmsSenderProxy SMS_SENDER_PROXY=new HoolaiSmsSenderProxy(Constant.SMS_PRODUCT_ID,Constant.SMS_PRODUCT_KEY);
	
	// 最多执行20分钟
	private static final long MAX_WAILT_MILLS=20*60*1000;

	private final String snid;
	
	private final String gameid;
	
	private final String ds;
	
	private final Integer maxThreadNum;
	
	private final Map<Integer,List<EngineEtls>> levelEtlsMap=new TreeMap<Integer, List<EngineEtls>>();
	
	// 运行到第几步
	private final int step;
	
	private final SpringApplicationUtil springApplicationUtil;
	
	private final TaskStatusService taskStatusService;
	
	private final ETLEngineFinishedProducer etlEngineFinishedProducer;
	
	private Map<String,String> envParams = new HashMap<String, String>() ;
	
	public GameQuasiETLEngineControllerThread(String snid, String gameid, String ds,Integer maxThreadNum,
			Map<Integer,List<EngineEtls>> levelEtlsMap, int step,
			SpringApplicationUtil springApplicationUtil) {
		super();
		this.snid = snid;
		this.gameid = gameid;
		this.ds=ds;
		this.maxThreadNum = maxThreadNum;
		this.step = step;
		this.levelEtlsMap.putAll(levelEtlsMap);
		this.springApplicationUtil=springApplicationUtil;
		this.taskStatusService=this.springApplicationUtil.getBean(TaskStatusService.class);
		this.etlEngineFinishedProducer=this.springApplicationUtil.getBean(ETLEngineFinishedProducer.class);
		
		this.init();
	}
	
	private void init(){
		this.prepareParams();
	}
	
	private void  prepareParams(){
		this.envParams.put("ds", ds);
		this.envParams.put("snid", this.snid);
		this.envParams.put("gameid", this.gameid);
		this.envParams.put("finish_time", this.getStepTime());
		this.envParams=Collections.unmodifiableMap(envParams);
	}
	
	private String getStepTime(){
		String prefix=this.step+"";
		if(this.step<10){
			prefix="0"+this.step;
		}
		prefix+=":59:59";
		return prefix;
	}
	
	@Override
	public void run() {
		
		boolean isSucc=this.runStep();
		
		try {
			ETLEngineGameInfo etlEngineGameInfo=new ETLEngineGameInfo(Type.QUASI_ETL_ENGINE_RUN.getDisplayName(),snid, gameid, ds, step);
			TaskStatus taskStatus=this.taskStatusService.getTaskStatusByCode(QuasiETLEngineProcesser.TASK_STATUS_NAME,etlEngineGameInfo.getIdentification());
			taskStatus.setEndTime(new Date());
			taskStatus.setContinueTimes(new Long((taskStatus.getEndTime().getTime()-taskStatus.getRuningTime().getTime())/1000).intValue());
			if(isSucc){
				taskStatus.setStatus(TaskStatusAdapter.Status.FINISHED.ordinal());
				
				this.etlEngineFinishedProducer.send(JSONUtils.toJSON(etlEngineGameInfo));
			}else{
				taskStatus.setStatus(TaskStatusAdapter.Status.ERROR.ordinal());
				SMS_SENDER_PROXY.send(new SmsMess(46,Constant.MONITOR_ETL_PHONES,new String[]{"准实时(日报)","失败",this.snid,this.gameid,this.ds,this.step+""}));
			}
			
			this.taskStatusService.modifyEntity(taskStatus);
			
		} catch (Exception e) {
			ERROR_LOGGER.error(e.getMessage(), e);
		}
		
		
	}
	
	/**
	 * 所有计算
	 */
	private boolean runStep(){
		boolean ret=false;
		
		ret = this.runLevelZero();
		if(!ret){
			return ret;
		}
		ret = this.runLevelOne();
		if(!ret){
			return ret;
		}
		ret = this.runLevelTwo();
		
		return ret;
	}
	
	private boolean runLevelZero(){
		int level=1;
		List<EngineEtls> elEngineEtls = this.levelEtlsMap.get(level);
		return this.runFor(level, elEngineEtls);
	}
	
	private boolean runLevelOne(){
		int level=2;
		List<EngineEtls> elEngineEtls = this.levelEtlsMap.get(level);
		return this.runFor(level, elEngineEtls);
	}
	
	private boolean runLevelTwo(){
		int level=3;
		List<EngineEtls> elEngineEtls = this.levelEtlsMap.get(level);
		return this.runFor(level, elEngineEtls);
	}
	
	private boolean runFor(int level,List<EngineEtls> elEngineEtls){
		
		if(elEngineEtls==null||elEngineEtls.isEmpty()){
			return true;
		}
		
		return this.runForEngineEtls("Game-Quasi level:"+level, elEngineEtls);
		
	}
	
	private boolean runForEngineEtls(String title,List<? extends AbstractEngineEtlsAdapter> engineEtlsAdapters){
		
		GameETLExecutor gameETLExecutor=new GameETLExecutor(this.snid, this.gameid, this.ds, maxThreadNum, envParams, title, engineEtlsAdapters, springApplicationUtil,MAX_WAILT_MILLS);
		
		return gameETLExecutor.execute();
		
	}
	
}
