package com.hoolai.bi.hive2mysql.sync;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hoolai.bi.hive2mysql.sync.SyncConditions.GameBiId;
import com.hoolai.bi.report.model.entity.Games;
import com.hoolai.bi.report.model.entity.MonthReport;
import com.hoolai.bi.report.service.GamesService;
import com.hoolai.bi.report.service.MonthReportService;

@Component
public class SyncGamesInfoProcesser {
	
	private static final Logger logger=Logger.getLogger("syncdatas");
	
	@Autowired
	private MonthReportService monthReportService;
	
	@Autowired
	private GamesService gamesService;
	
	public void sync(SyncConditions syncConditions){
		if(syncConditions==null || syncConditions.getGameBiIds()==null){
			return ;
		}
		for (GameBiId gameBiId : syncConditions.getGameBiIds()) {
			this.syncGameInfo(gameBiId,syncConditions);
		}
	}
	
	private void syncGameInfo(GameBiId gameBiId,SyncConditions syncConditions){
		
		try {
			MonthReport monthReport=this.monthReportService.get(gameBiId.getSnid(), gameBiId.getGameid(), syncConditions.getStatMonth());
			
			Games games=this.gamesService.getGames(new Long(gameBiId.getSnid()), new Long(gameBiId.getGameid()));
			
			if(games==null){
				return ;
			}
			Games updateGames=new Games();
			updateGames.setId(games.getId());
			
			updateGames.setInstall(monthReport.getInstall());
			updateGames.setPu(new Long(monthReport.getPu()));
			updateGames.setPayAmount(monthReport.getPaymentAmount());
			
			this.gamesService.modifyEntitySelective(updateGames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
