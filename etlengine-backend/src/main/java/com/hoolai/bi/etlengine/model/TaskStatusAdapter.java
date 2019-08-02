package com.hoolai.bi.etlengine.model;

import com.hoolai.bi.etlengine.model.entity.TaskStatus;

public class TaskStatusAdapter {

	public enum Status{
		RUNNING,
		FINISHED,
		ERROR;
	}
	
	/**
	 * 任务是否可以执行
	 * 第一次执行，或者执行错误、或者正常执行完毕都可以重新执行
	 * 只有在执行中的不能重新执行
	 * @param taskStatus
	 * @return
	 */
	public static boolean canExecute(TaskStatus taskStatus){
		if(taskStatus==null||Status.RUNNING.ordinal()!=taskStatus.getStatus().intValue()){
			return true;
		}
		return false;
	}
	
}
