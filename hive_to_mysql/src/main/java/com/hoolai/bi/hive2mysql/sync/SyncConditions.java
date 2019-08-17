package com.hoolai.bi.hive2mysql.sync;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.hoolai.bi.report.model.Conditions;

public class SyncConditions implements Conditions{
	
	private final List<GameBiId> gameBiIds=new ArrayList<GameBiId>();
	
	private final String snGameIdsSql;
	
	private String statMonth;
	
	private String statWeek;

	private String statDay;
	
	private String hour;
	
	public SyncConditions(List<GameBiId> gameBiIds, String statMonth,
			String statWeek, String statDay) {
		super();
		this.gameBiIds.addAll(gameBiIds);
		this.snGameIdsSql=this.prepareSnGameIdsSql();
		this.statMonth = statMonth;
		this.statWeek = statWeek;
		this.statDay = statDay;
	}
	
	public SyncConditions(List<GameBiId> gameBiIds, String statMonth,
			String statWeek, String statDay,String hour) {
		super();
		this.gameBiIds.addAll(gameBiIds);
		this.snGameIdsSql=this.prepareSnGameIdsSql();
		this.statMonth = statMonth;
		this.statWeek = statWeek;
		this.statDay = statDay;
		this.hour=hour;
	}
	

	private String prepareSnGameIdsSql(){
		StringBuilder sb=new StringBuilder(" and (");
		
		int idx=1;
		for (GameBiId gameBiId : gameBiIds) {
			sb.append("(");
			sb.append(" snid=").append(gameBiId.getSnid()).append(" and gameid=").append(gameBiId.getGameid());
			sb.append(")");
			if(idx++<this.gameBiIds.size()){
				sb.append(" or ");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	public List<GameBiId> getGameBiIds() {
		return gameBiIds;
	}
	
	public String getStatMonth() {
		return statMonth;
	}

	public void setStatMonth(String statMonth) {
		this.statMonth = statMonth;
	}

	public String getStatWeek() {
		return statWeek;
	}

	public void setStatWeek(String statWeek) {
		this.statWeek = statWeek;
	}
	
	public String getStatDay() {
		return statDay;
	}

	public void setStatDay(String statDay) {
		this.statDay = statDay;
	}
	
	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getSnGameIdsSql(){
		return this.snGameIdsSql;
	}

	public Object getFieldValue(String fieldName) throws Exception{
		 Field field = this.getClass().getDeclaredField(fieldName); 
		 field.setAccessible(true);
		 return field.get(this);
	}

	@Override
	public String toString() {
		return "statMonth:"+this.statMonth+" statWeek:"+this.statWeek+" statDay:"+this.statDay+" snGameIdsSql:"+this.snGameIdsSql;
	}
	
	
	public static class GameBiId{
		
		private final String snid;
		
		private final String gameid;

		public GameBiId(String snid, String gameid) {
			super();
			this.snid = snid;
			this.gameid = gameid;
		}

		public String getSnid() {
			return snid;
		}

		public String getGameid() {
			return gameid;
		}
	}
	
}
