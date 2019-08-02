package com.hoolai.bi.etlengine.engine;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hoolai.bi.etlengine.core.Constant;
import com.hoolai.bi.etlengine.kafka.producer.ETLEngineFinishedProducer;
import com.hoolai.bi.etlengine.model.EngineEtlsAdapter.Levels;
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
import com.hoolai.bi.report.util.ReportDateUtils;
import com.jian.tools.util.JSONUtils;

public class GameETLEngineControllerThread implements Runnable{
	
	private static final Logger LOGGER=Logger.getLogger(GameETLEngineControllerThread.class.getSimpleName());
	
	private static final Logger ERROR_LOGGER=Logger.getLogger("exception");
	
	private static final HoolaiSmsSenderProxy SMS_SENDER_PROXY=new HoolaiSmsSenderProxy(Constant.SMS_PRODUCT_ID,Constant.SMS_PRODUCT_KEY);
	
	// 每层最多执行1个小时
	private static final long MAX_WAILT_MILLS=1*60*60*1000;

	private final String snid;
	
	private final String gameid;
	
	private final String ds;
	
	private final Integer maxThreadNum;
	
	private final Map<Integer, List<EngineEtls>> levelEngineEtlsMap;
	
	// 不同体系经济系统
	private final List<EngineEtls> economyEngineEtls;
	
	// 运行到第几步
	private final int step;
	
	private final SpringApplicationUtil springApplicationUtil;
	
	private final TaskStatusService taskStatusService;
	
	private final ETLEngineFinishedProducer etlEngineFinishedProducer;
	
	private Map<String,String> envParams = new HashMap<String, String>() ;
	
	private boolean isRetry=false;
	
	public GameETLEngineControllerThread(String snid, String gameid, String ds,Integer maxThreadNum, Map<Integer, List<EngineEtls>> levelEngineEtlsMap,List<EngineEtls> economyEngineEtls, int step,
			SpringApplicationUtil springApplicationUtil) {
		super();
		this.snid = snid;
		this.gameid = gameid;
		this.ds=ds;
		this.maxThreadNum = maxThreadNum;
		this.levelEngineEtlsMap = levelEngineEtlsMap;
		this.economyEngineEtls=economyEngineEtls;
		this.step = step;
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
		this.envParams.put("year", ReportDateUtils.getYear(ds));
		this.envParams.put("yearrefertoweek", ReportDateUtils.getYearReferToWeek(ds));
		this.envParams.put("month", ReportDateUtils.getMonthOfYear(ds));
		this.envParams.put("week", ReportDateUtils.getWeekOfYear(ds));
		this.envParams.put("week_start", ReportDateUtils.getWeekStartDate(ds));
		this.envParams.put("week_end", ReportDateUtils.getWeekEndDate(ds));
		this.envParams.put("month_start", ReportDateUtils.getMonthStartDate(ds));
		this.envParams.put("month_end", ReportDateUtils.getMonthEndDate(ds));
		this.envParams.put("monthid", ReportDateUtils.getYearMonth(ds));
		this.envParams.put("weekid", ReportDateUtils.getWeekRange(ds));
		
		this.envParams=Collections.unmodifiableMap(envParams);
	}
	
	@Override
	public void run() {
		
		boolean isSucc=false;
		
		switch (this.step) {
			case 0:
				isSucc=this.runZeroStep();
				break;
			case 1:
				isSucc=this.runOneStep();
				break;
			case 2:
				isSucc=this.runTwoStep();
				break;
			case 3:
				isSucc=this.runThreeStep();
				break;
			case 4:
				isSucc=this.runThreeStep();
				break;
			default:
				break;
		}
		
		try {
			ETLEngineGameInfo etlEngineGameInfo=new ETLEngineGameInfo(Type.ETL_ENGINE_RUN.getDisplayName(),snid, gameid, ds, step);
			TaskStatus taskStatus=this.taskStatusService.getTaskStatusByCode(ETLEngineProcesser.TASK_STATUS_NAME,etlEngineGameInfo.getIdentification());
			if(taskStatus!=null){
				taskStatus.setEndTime(new Date());
				taskStatus.setContinueTimes(new Long((taskStatus.getEndTime().getTime()-taskStatus.getRuningTime().getTime())/1000).intValue());
				if(isSucc){
					taskStatus.setStatus(TaskStatusAdapter.Status.FINISHED.ordinal());
					
					this.etlEngineFinishedProducer.send(JSONUtils.toJSON(etlEngineGameInfo));
					
					this.taskStatusService.modifyEntity(taskStatus);
				}else{
					// 已经失败过一次，再失败，则发送短信提醒
					if(isRetry){
						taskStatus.setStatus(TaskStatusAdapter.Status.ERROR.ordinal());
						SMS_SENDER_PROXY.send(new SmsMess(46,Constant.MONITOR_ETL_PHONES,new String[]{"每天(重算)","失败",this.snid,this.gameid,this.ds,this.step+""}));
						this.taskStatusService.modifyEntity(taskStatus);
					}else{
						// 第一次失败，更新状态。重新计算一次
						SMS_SENDER_PROXY.send(new SmsMess(46,Constant.MONITOR_ETL_PHONES,new String[]{"每天(重算)","开始",this.snid,this.gameid,this.ds,this.step+""}));
						
						taskStatus.setStatus(TaskStatusAdapter.Status.RUNNING.ordinal());
						taskStatus.setRunTimes(taskStatus.getRunTimes()+1);
						taskStatus.setRuningTime(new Date());
						taskStatus.setEndTime(null);
						this.taskStatusService.modifyEntity(taskStatus);
						
						isRetry=true;
						// 重新算一次
						this.run();
					}
					
				}
				
				
			}
			
		} catch (Exception e) {
			ERROR_LOGGER.error(e.getMessage(), e);
		}
		
		
	}
	
	/**
	 * 所有计算
	 */
	private boolean runZeroStep(){
		boolean ret=false;
		
		ret = this.runForBaseETL();
		if(!ret){
			return ret;
		}
		ret = this.runForCumulativesETL();
		if(!ret){
			return ret;
		}
		ret = this.runForConceptionETL();
		if(!ret){
			return ret;
		}
		ret = this.runForCustomLogicChain();
		if(!ret){
			return ret;
		}
		ret=this.runForEconomyAdapterETL();
		
		return ret;
	}
	
	private boolean runOneStep(){
		boolean ret=false;
		
		ret = this.runForCumulativesETL();
		if(!ret){
			return ret;
		}
		ret = this.runForConceptionETL();
		if(!ret){
			return ret;
		}
		ret = this.runForCustomLogicChain();
		if(!ret){
			return ret;
		}
		ret=this.runForEconomyAdapterETL();
		
		return ret;
	}
	
	private boolean runTwoStep(){
		boolean ret=false;
		
		ret = this.runForConceptionETL();
		if(!ret){
			return ret;
		}
		ret = this.runForCustomLogicChain();
		if(!ret){
			return ret;
		}
		ret=this.runForEconomyAdapterETL();
		
		return ret;
	}
	
	private boolean runThreeStep(){
		boolean ret=false;
		
		ret = this.runForCustomLogicChain();
		if(!ret){
			return ret;
		}
		ret=this.runForEconomyAdapterETL();
		
		return ret;
	}
	
	private boolean runForBaseETL(){
		return this.runForEngineEtls("run-base-etl", this.levelEngineEtlsMap.get(Levels.ONE.getLevelValue()));
	}
	
	private boolean runForCumulativesETL(){
		
		return this.runForEngineEtls("run-cumulatives-etl", this.levelEngineEtlsMap.get(Levels.TWO.getLevelValue()));
	}
	
	private boolean runForConceptionETL(){
		
		return this.runForEngineEtls("run-conceptionETLConfs-etl", this.levelEngineEtlsMap.get(Levels.THREE.getLevelValue()));
		
		
	}
	@Deprecated
	private boolean runForReportETL(){
		
		return this.runForEngineEtls("run-report-etl", this.levelEngineEtlsMap.get(Levels.FOUR.getLevelValue()));
		
		
	}
	/**
	 * 经济系统兼容运行
	 * @return
	 */
	private boolean runForEconomyAdapterETL(){
		
		return this.runForEngineEtls("run-economy-etl", this.economyEngineEtls);
		
	}
	
	private boolean runForCustomLogicChain() {
		return true;
	}
	
	private boolean runForEngineEtls(String title,List<? extends AbstractEngineEtlsAdapter> engineEtlsAdapters){
		
		GameETLExecutor gameETLExecutor=new GameETLExecutor(this.snid, this.gameid, this.ds, this.maxThreadNum, this.envParams, title, engineEtlsAdapters, this.springApplicationUtil,MAX_WAILT_MILLS);
		
		return gameETLExecutor.execute();
		
	}
	
	
	
}
