package com.hoolai.bi.etlengine.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.hoolai.bi.etlengine.engine.AbstractGameETLEngine.GameETLEngineResult;
import com.hoolai.bi.etlengine.model.entity.AbstractEngineEtlsAdapter;
import com.hoolai.bi.etlengine.util.SpringApplicationUtil;
import com.hoolai.bi.report.job.SystemThreadPoolExecutor;

public class GameETLExecutor {
	
	private static final Logger LOGGER=Logger.getLogger(GameETLExecutor.class.getSimpleName());
	
	private static final Logger ERROR_LOGGER=Logger.getLogger("exception");
	
	private static final long SLEEP_MILLS=5000;
	
	private final String snid;
	
	private final String gameid;
	
	private final String ds;
	
	private final Integer maxThreadNum;
	
	private final Map<String,String> envParams;
	
	private final String title;
	
	private final List<? extends AbstractEngineEtlsAdapter> engineEtlsAdapters;
	
	private final SpringApplicationUtil springApplicationUtil;
	
	private final long maxWaitMills;
	
	private final SystemThreadPoolExecutor systemThreadPoolExecutor;
	
	private final String stepInfo;
	
	private final List<GameETLEngineThreadMess> gameETLEngineThreadMessList=new ArrayList<GameETLEngineThreadMess>();
	
	private boolean isAllDone=false;
	
	private int doneNum=0;
	
	private AtomicLong runMills=new AtomicLong(0);

	public GameETLExecutor(String snid, String gameid, String ds, Integer maxThreadNum, Map<String, String> envParams, String title,
			List<? extends AbstractEngineEtlsAdapter> engineEtlsAdapters,SpringApplicationUtil springApplicationUtil,long maxWaitMills) {
		super();
		this.snid = snid;
		this.gameid = gameid;
		this.ds = ds;
		this.maxThreadNum = maxThreadNum;
		this.envParams = envParams;
		this.title = title;
		this.engineEtlsAdapters = engineEtlsAdapters;
		this.springApplicationUtil=springApplicationUtil;
		this.maxWaitMills=maxWaitMills;
		this.systemThreadPoolExecutor=new SystemThreadPoolExecutor(this.title, this.getSuitableThreadNum(this.engineEtlsAdapters.size()));
		this.stepInfo=this.title+":"+this.snid+"_"+this.gameid+"_"+this.ds;
	}

	public boolean execute(){
		if(this.engineEtlsAdapters==null||this.engineEtlsAdapters.isEmpty()){
			LOGGER.info(this.stepInfo+":have not elt conf.");
			return true;
		}
		
		LOGGER.info(this.stepInfo+" begin.");
		long begin=System.currentTimeMillis();
		try {
			
			this.prepare();
			
			boolean isSucc=this.monitor();
			
			long end=System.currentTimeMillis();
			LOGGER.info(this.stepInfo+" end. spendMills:"+(end-begin));
			
			return isSucc;
		} catch (Exception e) {
			ERROR_LOGGER.error(e.getMessage(), e);
			return false;
		}finally{
			// 释放资源
			this.releaseResource(gameETLEngineThreadMessList);
		}
	
	}
	
	private void prepare(){
		try {
			for (AbstractEngineEtlsAdapter engineEtlsAdapter : engineEtlsAdapters) {
				GameETLEngineThread gameETLEngineThread=new GameETLEngineThread(this.snid, this.gameid,this.envParams, engineEtlsAdapter,this.springApplicationUtil);
				Future<GameETLEngineResult> future=this.systemThreadPoolExecutor.submit(gameETLEngineThread);
				this.gameETLEngineThreadMessList.add(new GameETLEngineThreadMess(future, gameETLEngineThread));
			}
			this.systemThreadPoolExecutor.getExecutor().shutdown();
		} catch (Exception e) {
			ERROR_LOGGER.error(e.getMessage(), e);
		}
	}
	
	private boolean monitor(){
		try {
			
			int futureSize=this.gameETLEngineThreadMessList.size();

			List<GameETLEngineThreadMess> gameETLEngineThreadMessListClone=new ArrayList<GameETLEngineThreadMess>();
			gameETLEngineThreadMessListClone.addAll(this.gameETLEngineThreadMessList);
			
			do{
				
				Iterator<GameETLEngineThreadMess> iteratorThreadMess=gameETLEngineThreadMessListClone.iterator();
				
				this.sleep(SLEEP_MILLS);
				
				this.runMills.addAndGet(SLEEP_MILLS);
				
				if(this.runMills.get()>this.maxWaitMills){
					this.processFailThread();
					return false;
				}
				
				LOGGER.info(this.stepInfo+" is executing! doneNum:"+this.doneNum+"/"+futureSize+" alreadySpendMills:"+this.runMills.get()+" forceFinishMills:"+(this.maxWaitMills-this.runMills.get()));
				
				while(iteratorThreadMess.hasNext()&&!this.isAllDone){
					GameETLEngineThreadMess gameETLEngineThreadMess=iteratorThreadMess.next();
					Future<GameETLEngineResult> future=gameETLEngineThreadMess.getFuture();
					if(future.isDone()){
						GameETLEngineResult gameETLEngineResult=future.get();
						if(gameETLEngineResult.isSucc()){
							this.doneNum++;
							LOGGER.info(this.stepInfo+" of "+gameETLEngineThreadMess.getGameETLEngineThread().title+ " succ one done! doneNum:"+this.doneNum+"/"+futureSize+" alreadySpendMills:"+this.runMills.get()+" forceFinishMills:"+(this.maxWaitMills-this.runMills.get()));
						}else{
							this.processFailThread();
							LOGGER.error(this.stepInfo+" of "+gameETLEngineThreadMess.getGameETLEngineThread().title+" fail one done! doneNum:"+this.doneNum+"/"+futureSize);
							return false;
						}
						iteratorThreadMess.remove();
					}
					
					if(this.doneNum>=futureSize){
						LOGGER.info(this.stepInfo+" is all done! doneNum:"+this.doneNum+"/"+futureSize);
						this.isAllDone=true;
						break;
					}
				}
				
			}while(!this.isAllDone);
			
			return true;
			
		} catch (Exception e) {
			ERROR_LOGGER.error(e.getMessage(), e);
			return false;
		}
	}
	
	
	
	private int getSuitableThreadNum(int requireSize){
		if(this.maxThreadNum>=requireSize){
			return requireSize;
		}
		return this.maxThreadNum;
	}
	
	/**
	 * 释放hive数据库连接（预防超时线程，没有释放成功的情况）
	 */
	private void releaseResource(List<GameETLEngineThreadMess> gameETLEngineThreadMessList){
		if(gameETLEngineThreadMessList==null||gameETLEngineThreadMessList.isEmpty()){
			return ;
		}
		try {
			for (GameETLEngineThreadMess gameETLEngineThreadMess : gameETLEngineThreadMessList) {
				gameETLEngineThreadMess.getGameETLEngineThread().destory();
			}
		} catch (Exception e1) {
			ERROR_LOGGER.error(e1.getMessage(), e1);
		}
	}
	
	private void sleep(long mills){
		try {
			TimeUnit.MILLISECONDS.sleep(mills);
		} catch (InterruptedException e) {
			ERROR_LOGGER.error(e.getMessage(), e);
		}
	}
	
	private void processFailThread(){
		// 如果失败，则该层全部失败
		List<Runnable> errorRunnableList=systemThreadPoolExecutor.getExecutor().shutdownNow();
		StringBuilder sb=new StringBuilder(this.title);
		for (Runnable runnable : errorRunnableList) {
			if(!(runnable instanceof GameETLEngineThread)){
				continue;
			}
			GameETLEngineThread gameETLEngineThread=(GameETLEngineThread)runnable;
			sb.append(gameETLEngineThread.title).append("\r\n");
		}
		LOGGER.error(this.stepInfo+" process-fail-thread mess:"+sb.toString());
	}
	
	private static class GameETLEngineThreadMess{
		
		private final Future<GameETLEngineResult> future;
		
		private final GameETLEngineThread gameETLEngineThread;

		public GameETLEngineThreadMess(Future<GameETLEngineResult> future, GameETLEngineThread gameETLEngineThread) {
			super();
			this.future = future;
			this.gameETLEngineThread = gameETLEngineThread;
		}

		public Future<GameETLEngineResult> getFuture() {
			return future;
		}

		public GameETLEngineThread getGameETLEngineThread() {
			return gameETLEngineThread;
		}
		
	}
	
	
	

}
