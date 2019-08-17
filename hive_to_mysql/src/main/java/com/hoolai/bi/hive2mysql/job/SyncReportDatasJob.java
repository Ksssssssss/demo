package com.hoolai.bi.hive2mysql.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hoolai.bi.hive2mysql.datas.ConvertMapperDatas;
import com.hoolai.bi.hive2mysql.datas.ConvertMapperDatas.Mapper;
import com.hoolai.bi.hive2mysql.sync.SyncAllReportDatas;
import com.hoolai.bi.hive2mysql.sync.SyncConditions;
import com.hoolai.bi.hive2mysql.sync.SyncConditions.GameBiId;
import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;
import com.hoolai.bi.report.job.AbstractExecuteJob;
import com.hoolai.bi.report.model.entity.Games;
import com.hoolai.bi.report.service.GamesService;
import com.hoolai.bi.report.util.ReportDateUtils;
import com.hoolai.bi.report.vo.GamesVO;

public class SyncReportDatasJob extends AbstractExecuteJob {
	
	private Logger logger=Logger.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	private SyncAllReportDatas syncAllReportDatas;
	
	@Autowired
	private GamesService gamesService;
	
	private ConvertMapperDatas convertMapperDatas=ConvertMapperDatas.getInstance();
	
	private boolean isExecuting=false;
	
	@Override
	public Map<String, Object> executeJob() throws Exception {
		if(isExecuting==true){
			return Collections.emptyMap();
		}
		isExecuting=true;
		try{
			Date now=new Date();
			Date yesterday =DateUtils.addDays(now, -1);
			String statMonth=DateFormatUtils.format(yesterday, "yyyy-MM");
			String statDay=DateFormatUtils.format(yesterday, "yyyy-MM-dd");
			String statWeek=ReportDateUtils.getWeekRange(yesterday);
			List<GameBiId> gameBiIds=new ArrayList<GameBiId>();
			List<Games> gameList=this.gamesService.getMatch(new GamesVO());
			for (Games games : gameList) {
				gameBiIds.add(new GameBiId(games.getSnid(), games.getGameid()));
			}
			SyncConditions syncConditions=new SyncConditions(gameBiIds, statMonth, statWeek, statDay);
			syncConditions.setStatMonth(statMonth);
			syncConditions.setStatDay(statDay);
			syncConditions.setStatWeek(statWeek);
			List<Mapper> mapperList=convertMapperDatas.getTypeMapperList(Type.ETL_ENGINE_RUN.getDisplayName());
			syncAllReportDatas.sync(syncConditions,mapperList);
		}catch(Exception e){
			e.printStackTrace();
		}
		isExecuting=false;
		return Collections.emptyMap();
	}
	
}
