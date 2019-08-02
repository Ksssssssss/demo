package com.hoolai.bi.etlengine.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hoolai.bi.etlengine.model.entity.TaskStatus;

public class ETLRunStatus {
	
	private static ETLRunStatus instance=null;
	
	public  static synchronized ETLRunStatus getInstance(){
		if(instance==null){
			instance=new ETLRunStatus();
		}
		return instance;
	}
	
	private Map<String,TaskStatus> statusMap=new ConcurrentHashMap<String,TaskStatus>();
	
	private ETLRunStatus() {
		super();
	}

	public synchronized  boolean put(String key,TaskStatus status){
		if(statusMap.containsKey(key)){
			return false;
		}
		statusMap.put(key, status);
		return true;
	}
	
	public boolean isExist(String key){
		return statusMap.containsKey(key);
	}
	
	public void remove(String key){
		statusMap.remove(key);
	}

}
