package com.hoolai.bi.etlengine.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class EngineEtlsAdapter {
	
	private static final String[] SHENQU_IDS=new String[]{"11_120","11_73","34_73","11_94","34_135","11_91","11_120","11_108","11_113","11_34"};
	
	private static final String[] FENTIAN_IDS=new String[]{"1_71","1_115","31_124","45_71","15_71","15_145"};
	
	public enum Types{
		// 准实时
		QUASI(null),
		// 经济系统标准体系
		ECONOMY_STANDARD(null),
		// 经济系统神曲体系
		ECONOMY_SHENQU(SHENQU_IDS),
		// 经济系统焚天体系
		ECONOMY_FENTIAN(FENTIAN_IDS),
		// ad_tracking准实时
		ADTRACKING_QUASI(null),
		DAILY(null);
		
		private Set<String> matchGameIds=new HashSet<String>();
		
		Types(String[] matchIds){
			this.init(matchIds);
		}
		
		private void init(String[] matchIds){
			if(matchIds==null||matchIds.length<1){
				return ;
			}
			for (String matchId : matchIds) {
				this.matchGameIds.add(matchId);
			}
		}
		
		public boolean isContains(String snGameId){
			return this.matchGameIds.contains(snGameId);
		}
		
		
		public static Types getEconomyTypes(String snGameId){
			for (Types type : Types.values()) {
				if(!Types.ECONOMY_STANDARD.equals(type)&&!Types.ECONOMY_SHENQU.equals(type)&&!Types.ECONOMY_FENTIAN.equals(type)){
					continue;
				}
				if(type.isContains(snGameId)){
					return type;
				}
			}
			return Types.ECONOMY_STANDARD;
		}
	}
	
	
	//根据不同的level获取不同的的etl
	public enum Levels {
		ONE(1), 
		TWO(2), 
		THREE(3), 
		FOUR(4);

		private Integer levelValue;

		private Levels(Integer leveValue) {
			this.levelValue = leveValue;
		}

		public Integer getLevelValue() {
			return levelValue;
		}

		public void setLevelValue(Integer levelValue) {
			this.levelValue = levelValue;
		}

		public static Levels getLevelByVal(Integer val) {
			return Levels.map.get(val);
		}

		private static Map<Integer, Levels> map = new HashMap<Integer, Levels>();

		static {
			for (Levels p : Levels.values()) {
				Levels.map.put(p.levelValue, p);
			}
		}

		public static Levels getLevelByKey(String key) {
			for (Levels p : Levels.values()) {
				if (p.name().equals(key)) {
					return p;
				}
			}
			return null;
		}
	}
	
}