package com.hoolai.bi.hive2mysql.datas;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;

public final class ConvertMapperDatas {
	
	private static final ConvertMapperDatas CONVERT_MAPPER_DATAS=new ConvertMapperDatas();
	
	public static ConvertMapperDatas getInstance(){
		return CONVERT_MAPPER_DATAS;
	}
	
	private Collection<Mapper> mappers=null;
	
	private Map<String,Mapper> mapperMap=null;
	
	private Map<String,List<Mapper>> typeMapperMap=null;
	
	private ConvertMapperDatas() {
		super();
		init();
	}

	private void init(){
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream dataInput=loader.getResourceAsStream("hive_mappers/mappers.xml");
			SAXReader reader = new SAXReader();
	        Document doc = reader.read(dataInput);
	        List<Element> elements = doc.getRootElement().elements();
	        mappers=new ArrayList<Mapper>();
	        mapperMap=new HashMap<String, Mapper>();
	        typeMapperMap=new HashMap<String, List<Mapper>>();
	        for (Element element : elements) {
	        	String type=element.elementText("type");
	        	if(type==null){
	        		type=Type.ETL_ENGINE_RUN.getDisplayName();
	        	}
	        	String name=element.elementText("name");
	        	String sqlTableName=element.elementText("sqlTableName");
	        	String hqlPath=element.elementText("hqlPath");
	        	StatType statType=StatType.instanceByName(element.elementText("statType"));
	        	String statIdentifyColumn=element.elementText("statIdentifyColumn");
	        	Mapper mapper=new Mapper(name, sqlTableName, hqlPath,statType,statIdentifyColumn);
	        	if(mapperMap.containsKey(name)){
	        		throw new RuntimeException("mappers.xml have same name mapper.");
	        	}
	        	mappers.add(mapper);
	        	mapperMap.put(name, mapper);
	        	
	        	List<Mapper> typeMapperList=null;
	        	if(typeMapperMap.containsKey(type)){
	        		typeMapperList=typeMapperMap.get(type);
	        	}else{
	        		typeMapperList=new ArrayList<Mapper>();
	        		typeMapperMap.put(type, typeMapperList);
	        	}
	        	typeMapperList.add(mapper);
	        }
	        mappers = Collections.unmodifiableCollection(mappers);
	        mapperMap=Collections.unmodifiableMap(mapperMap);
	        typeMapperMap=Collections.unmodifiableMap(typeMapperMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Collection<Mapper> getMappers() {
		return mappers;
	}

	public Map<String, Mapper> getMapperMap() {
		return mapperMap;
	}
	
	public List<Mapper> getTypeMapperList(String type){
		return this.typeMapperMap.get(type);
	}
	
	public enum StatType{
		
		MONTH("statMonth"),
		WEEK("statWeek"),
		DAY("statDay"),
		HOUR("hour");
		
		private final String statName;
		
		private StatType(String statName){
			this.statName=statName;
		}

		public String getStatName() {
			return statName;
		}
		
		public static StatType instanceByName(String name){
			for (StatType t : StatType.values()) {
                if (t.name().equalsIgnoreCase(name)) {
                        return t;
                }
            }
            throw new RuntimeException(" stattype instance meet error. errorName:"+name);
		}
		
	}

	public static class Mapper{
		
		private final String type;
		
		private final String name;
		
		private final String sqlTableName;
		
		private final String hqlPath;
		
		private final StatType statType;
		
		private final String statIdentifyColumn;

		public Mapper(String type, String name, String sqlTableName, String hqlPath, StatType statType, String statIdentifyColumn) {
			super();
			this.type = type;
			this.name = name;
			this.sqlTableName = sqlTableName;
			this.hqlPath = hqlPath;
			this.statType = statType;
			this.statIdentifyColumn = statIdentifyColumn;
		}

		public Mapper(String name, String sqlTableName, String hqlPath,
				StatType statType, String statIdentifyColumn) {
			super();
			this.type=Type.ETL_ENGINE_RUN.getDisplayName();
			this.name = name;
			this.sqlTableName = sqlTableName;
			this.hqlPath = hqlPath;
			this.statType = statType;
			this.statIdentifyColumn = statIdentifyColumn;
		}

		public String getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public String getSqlTableName() {
			return sqlTableName;
		}

		public String getHqlPath() {
			return hqlPath;
		}

		public StatType getStatType() {
			return statType;
		}

		public String getStatIdentifyColumn() {
			return statIdentifyColumn;
		}
	}

}
