package com.hoolai.bi.hive2mysql.sync;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.hoolai.bi.hive2mysql.datas.ConvertMapperDatas;
import com.hoolai.bi.hive2mysql.datas.ConvertMapperDatas.Mapper;
import com.hoolai.bi.hive2mysql.datas.SqlFtlDatas;
import com.hoolai.bi.hive2mysql.sync.SyncConditions.GameBiId;
import com.hoolai.bi.report.dao.mapper.AbstractHiveParameterizedRowMapper;
import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;
import com.hoolai.bi.report.job.SystemThreadPoolExecutor;
import com.hoolai.bi.report.util.ReportDateUtils;

@Component
public class SyncAllReportDatas {
	
	private static final Logger logger=Logger.getLogger("syncdatas");
	
	@Autowired
	@Qualifier("hiveJdbcTemplate")
	private JdbcTemplate hiveJdbcTemplate;
	
	@Autowired
	@Qualifier("mysqlNamedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate mysqlNamedParameterJdbcTemplate;
	
	@Autowired
	private SqlFtlDatas sqlFtlDatas;
	
	@Autowired
	private SyncGamesInfoProcesser syncGamesInfoProcesser;
	
	private ConvertMapperDatas convertMapperDatas=ConvertMapperDatas.getInstance();
	
	private final SystemThreadPoolExecutor systemThreadPoolExecutor=new SystemThreadPoolExecutor("sync-report-data",convertMapperDatas.getMappers().size()+1);
	
	private boolean isAllDone=true;
	
	private int doneNum=0;
	
	private int failNum=0;
	
	private final BlockingQueue<SyncConditions> blockingQueue = new LinkedBlockingDeque<SyncConditions>(1000);
	
	private final BlockingQueue<SyncConditions> quasiBlockingQueue = new LinkedBlockingDeque<SyncConditions>(1000);
	
	private final BlockingQueue<SyncConditions> adTrackingQuasiBlockingQueue = new LinkedBlockingDeque<SyncConditions>(1000);
	
	/**
	 * 同步每天的
	 * @param gameBiIds
	 * @param beginDate
	 * @param endDate
	 */
	public void sync(List<GameBiId> gameBiIds,Date beginDate,Date endDate){
		if(gameBiIds==null || gameBiIds.isEmpty()||beginDate==null||endDate==null){
			return ;
		}
		Date statDate=beginDate;
		while(statDate.before(endDate)||DateUtils.isSameDay(statDate, endDate)){
			String statMonth=DateFormatUtils.format(statDate, "yyyy-MM");
			String statDay=DateFormatUtils.format(statDate, "yyyy-MM-dd");
			String statWeek=ReportDateUtils.getWeekRange(statDate);
			SyncConditions syncConditions=new SyncConditions(gameBiIds, statMonth, statWeek, statDay);
			this.blockingQueue.add(syncConditions);
			statDate=DateUtils.addDays(statDate, 1);
		}
		while(!this.blockingQueue.isEmpty()){
			List<Mapper> mapperList=convertMapperDatas.getTypeMapperList(Type.ETL_ENGINE_RUN.getDisplayName());
			try {
				SyncConditions syncConditions=this.blockingQueue.take();
				this.sync(syncConditions,mapperList);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 同步每个小时的
	 * @param gameBiIds
	 * @param beginDate
	 * @param endDate
	 */
	public void syncQuasi(List<GameBiId> gameBiIds,Date beginDate,Date endDate,String hour){
		if(gameBiIds==null || gameBiIds.isEmpty()||beginDate==null||endDate==null){
			return ;
		}
		Date statDate=beginDate;
		while(statDate.before(endDate)||DateUtils.isSameDay(statDate, endDate)){
			String statMonth=DateFormatUtils.format(statDate, "yyyy-MM");
			String statDay=DateFormatUtils.format(statDate, "yyyy-MM-dd");
			String statWeek=ReportDateUtils.getWeekRange(statDate);
			SyncConditions syncConditions=new SyncConditions(gameBiIds, statMonth, statWeek, statDay,hour);
			quasiBlockingQueue.add(syncConditions);
			statDate=DateUtils.addDays(statDate, 1);
		}
		while(!this.quasiBlockingQueue.isEmpty()){
			List<Mapper> mapperList=convertMapperDatas.getTypeMapperList(Type.QUASI_ETL_ENGINE_RUN.getDisplayName());
			try {
				SyncConditions syncConditions=this.quasiBlockingQueue.take();
				 this.sync(syncConditions,mapperList);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 同步Adtracking每个小时的
	 * @param gameBiIds
	 * @param beginDate
	 * @param endDate
	 */
	public void syncAdtrackingQuasi(List<GameBiId> gameBiIds,Date beginDate,Date endDate,String hour){
		if(gameBiIds==null || gameBiIds.isEmpty()||beginDate==null||endDate==null){
			return ;
		}
		Date statDate=beginDate;
		while(statDate.before(endDate)||DateUtils.isSameDay(statDate, endDate)){
			String statMonth=DateFormatUtils.format(statDate, "yyyy-MM");
			String statDay=DateFormatUtils.format(statDate, "yyyy-MM-dd");
			String statWeek=ReportDateUtils.getWeekRange(statDate);
			SyncConditions syncConditions=new SyncConditions(gameBiIds, statMonth, statWeek, statDay,hour);
			this.adTrackingQuasiBlockingQueue.add(syncConditions);
			statDate=DateUtils.addDays(statDate, 1);
		}
		while(!this.adTrackingQuasiBlockingQueue.isEmpty()){
			List<Mapper> mapperList=convertMapperDatas.getTypeMapperList(Type.ADTRACKING_QUASI_ETL_ENGINE_RUN.getDisplayName());
			try {
				SyncConditions syncConditions=this.adTrackingQuasiBlockingQueue.take();
				 this.sync(syncConditions,mapperList);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean sync(SyncConditions syncConditions,Collection<Mapper> mappers) {
		if(isAllDone==false){
			return true;
		}
		try {
			isAllDone=false;
			logger.info("begin sync... "+syncConditions.toString());
			long begin=System.currentTimeMillis();
			Map<Mapper,Future<Boolean>> resMapper=new HashMap<Mapper, Future<Boolean>>();
			for (Mapper mapper : mappers) {
				Future<Boolean> future=this.syncMapper(mapper,syncConditions);
				resMapper.put(mapper, future);
			}
		
			int futureSize=mappers.size();
			do{
				TimeUnit.MILLISECONDS.sleep(3000);
				Set<Mapper> keyMappers=new HashSet<ConvertMapperDatas.Mapper>();
				keyMappers.addAll(resMapper.keySet());
				Iterator<Mapper> iteratorMapper=keyMappers.iterator();
				while(iteratorMapper.hasNext()&&!isAllDone){
					Mapper mapper=iteratorMapper.next();
					
					logger.info(mapper.getSqlTableName()+" have doneNum:"+doneNum+"/"+futureSize+" failNum:"+failNum+" "+syncConditions.toString());
					
					Future<Boolean> future=resMapper.get(mapper);
					if(future.isDone()){
						if(future.get()){
							doneNum++;
						}else{
							failNum++;
						}
						resMapper.remove(mapper);
						iteratorMapper.remove();
						logger.info(mapper.getSqlTableName()+" succ one done! doneNum:"+doneNum+"/"+futureSize+" failNum:"+failNum+" "+syncConditions.toString());
					}
					
					if(doneNum>=futureSize||((doneNum+failNum)>=futureSize)){
						logger.info(mapper.getSqlTableName()+" is all done! doneNum:"+doneNum+"/"+futureSize+" failNum:"+failNum+" "+syncConditions.toString());
						isAllDone=true;
						doneNum=0;
						break;
					}
				}
			}while(!isAllDone);
			long end=System.currentTimeMillis();
			logger.info("end sync... spendTimeMills:"+(end-begin)+" "+syncConditions.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Future<Boolean> syncMapper(Mapper mapper,SyncConditions syncConditions){
		ProcessSyncReportDataCallable processSyncReportDataCallable=new ProcessSyncReportDataCallable(sqlFtlDatas, hiveJdbcTemplate, mysqlNamedParameterJdbcTemplate, mapper,syncConditions);
		Future<Boolean> future=this.systemThreadPoolExecutor.submit(processSyncReportDataCallable);
		return future;
	}
	
	private static class ProcessSyncReportDataCallable implements Callable<Boolean>{
		
		private static final Logger logger=Logger.getLogger("db_operator");
		
		private final SqlFtlDatas sqlFtlDatas;
		
		private final JdbcTemplate hiveJdbcTemplate;
		
		private final NamedParameterJdbcTemplate mysqlNamedParameterJdbcTemplate;
		
		private final Mapper mapper ;
		
		private final SyncConditions syncConditions;

		public ProcessSyncReportDataCallable(SqlFtlDatas sqlFtlDatas,
				JdbcTemplate hiveJdbcTemplate,
				NamedParameterJdbcTemplate mysqlNamedParameterJdbcTemplate, Mapper mapper,SyncConditions syncConditions) {
			super();
			this.sqlFtlDatas = sqlFtlDatas;
			this.hiveJdbcTemplate = hiveJdbcTemplate;
			this.mysqlNamedParameterJdbcTemplate = mysqlNamedParameterJdbcTemplate;
			this.mapper=mapper;
			this.syncConditions=syncConditions;
		}
		
		private void clearStatTimeDatas() throws Exception{
			StringBuilder sb=new StringBuilder("delete from ");
			sb.append(this.mapper.getSqlTableName());
			sb.append(" where ");
			sb.append(this.mapper.getStatIdentifyColumn());
			sb.append("=:statIdentify ");
			sb.append(this.syncConditions.getSnGameIdsSql());
			
			Object statIdentify=this.syncConditions.getFieldValue(this.mapper.getStatType().getStatName());
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("statIdentify", statIdentify);
			
			this.mysqlNamedParameterJdbcTemplate.update(sb.toString(), paramMap);
			
			logger.debug(this.mapper.getSqlTableName()+":"+sb.toString()+" "+statIdentify);
		}

		@Override
		public Boolean call() throws Exception {
			for(int i=0;i<3;i++){
				try {
					String hiveSql=this.sqlFtlDatas.condition(syncConditions,this.mapper.getHqlPath());
					//ParameterParsedSql parsedSql=NamedParameterUtils.parseSqlStatement(mysqlSql);
					
					long hiveBegin=System.currentTimeMillis();
					HiveParameterizedRowMapper hiveParameterizedRowMapper=new HiveParameterizedRowMapper(this.mapper);
					List<Map<String,Object>> hiveDatas=this.hiveJdbcTemplate.query(hiveSql,hiveParameterizedRowMapper);
					if(hiveDatas==null||hiveDatas.isEmpty()){
						logger.info(this.mapper.getSqlTableName()+" hive db no datas .");
						return true;
					}
					long hiveEnd=System.currentTimeMillis();
					logger.info(this.mapper.getSqlTableName()+" query hive spend mills:"+(hiveEnd-hiveBegin));
					
					this.clearStatTimeDatas();
					
					Map<String,?>[] batchValues=new Map[hiveDatas.size()];
					hiveDatas.toArray(batchValues);
					long mysqlBegin=System.currentTimeMillis();
					// 
					String mysqlSql=hiveParameterizedRowMapper.generateSql();
					logger.debug(this.mapper.getSqlTableName()+":"+mysqlSql);
					this.mysqlNamedParameterJdbcTemplate.batchUpdate(mysqlSql, batchValues);
					logger.debug(this.mapper.getSqlTableName()+":"+hiveParameterizedRowMapper.toStringBatchValues(hiveDatas));
					long mysqlEnd=System.currentTimeMillis();
					logger.info(this.mapper.getSqlTableName()+" to mysql db size:"+hiveDatas.size()+" spend mills:"+(mysqlEnd-mysqlBegin));
					return true;
				} catch (Exception e) {
					logger.error(this.syncConditions.toString(), e);
					TimeUnit.MILLISECONDS.sleep(2000);
				}
			}
			
			return false;
		}
	}
	


	private static class HiveParameterizedRowMapper extends AbstractHiveParameterizedRowMapper{
		
		private final Mapper mapper;

		public HiveParameterizedRowMapper(Mapper mapper) {
			super();
			this.mapper=mapper;
		}
		
		private String generateSql(){
			if(this.columnsList.isEmpty()){
				throw new RuntimeException("column list is empty!");
			}
			StringBuilder sb=new StringBuilder("insert into ");
			sb.append(this.mapper.getSqlTableName());
			sb.append("(");
			int idx=0;
			for (String column : columnsList) {
				sb.append(column);
				if(idx!=columnsList.size()-1){
					sb.append(",");
				}
				idx++;
			}
			sb.append(") values (");
			idx=0;
			for (String column : columnsList) {
				sb.append(":");
				sb.append(column);
				if(idx!=columnsList.size()-1){
					sb.append(",");
				}
				idx++;
			}
			sb.append(")");
			return sb.toString();
		}
		
	}

}
