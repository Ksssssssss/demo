package com.hoolai.bi.etlengine.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoolai.bi.etlengine.engine.ETLEngineProcesser;
import com.hoolai.bi.etlengine.engine.realtime.adtracking.AdTrackingQuasiETLEngineProcesser;
import com.hoolai.bi.etlengine.engine.realtime.quasi.QuasiETLEngineProcesser;
import com.hoolai.bi.etlengine.model.TaskStatusAdapter;
import com.hoolai.bi.etlengine.model.entity.TaskStatus;
import com.hoolai.bi.etlengine.service.TaskStatusService;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;
import com.jian.tools.util.JSONUtils;

@Controller
public class IndexController {
	
	@Autowired
	private ETLEngineProcesser engineProcesser;
	
	@Autowired
	private QuasiETLEngineProcesser quasiETLEngineProcesser;
	
	@Autowired
	private AdTrackingQuasiETLEngineProcesser adTrackingQuasiETLEngineProcesser;
	
	@Autowired
	private TaskStatusService taskStatusService;
	
	@RequestMapping(value = {"/runEtl"}, method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Map<String,Object> runEtl(@RequestParam String snid,@RequestParam String gameid ,@RequestParam String ds ,HttpServletRequest request,HttpSession session)throws Exception {
		
		Map<String,Object> ret=new HashMap<String,Object>();
		
		Date now=new Date();
		
		ETLEngineGameInfo etlEngineGameInfo0=new ETLEngineGameInfo(snid, gameid, ds, 0);
		
		String key=etlEngineGameInfo0.getIdentification();
		
		TaskStatus taskStatus=this.taskStatusService.getTaskStatusByCode(etlEngineGameInfo0.getType(),etlEngineGameInfo0.getIdentification());
		if(!TaskStatusAdapter.canExecute(taskStatus)){
			ret.put("msg", key+" start by "+DateFormatUtils.format(taskStatus.getRuningTime(), "yyyy-MM-dd HH:mm:ss"));
			ret.put("spendMinuts", (now.getTime()-taskStatus.getRuningTime().getTime())/(1000));
			ret.put("taskStatus", taskStatus);
			return ret;
		}
		
		List<ETLEngineGameInfo> etlEngineGameInfos=new ArrayList<ETLEngineGameInfo>();
		etlEngineGameInfos.add(etlEngineGameInfo0);
		this.engineProcesser.asyncProcessGame(etlEngineGameInfos);
		
		ret.put("msg", key+" start by "+DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
		
		return ret;
	}
	
	@RequestMapping(value = {"/runEtls"}, method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public List<Map<String,Object>> runEtls(@RequestParam String beginDate,@RequestParam String endDate,@RequestParam String gameInfosJson,
			HttpServletRequest request,HttpSession session)throws Exception {
		
		try {
			
			List<ETLEngineGameInfo> gameInfos=(List<ETLEngineGameInfo>)JSONUtils.fromJSONToList(gameInfosJson, ArrayList.class,ETLEngineGameInfo.class);
			
			List<Map<String,Object>> ret=new ArrayList<Map<String,Object>>();
			
			Date beginDateIns=DateUtils.parseDate(beginDate, "yyyy-MM-dd");
			Date endDateIns=DateUtils.parseDate(endDate, "yyyy-MM-dd");
			
			List<List<ETLEngineGameInfo>> daysPrepareEtlEngineGameInfos=new ArrayList<List<ETLEngineGameInfo>>();
			
			String etlType=null;
			
			while(DateUtils.isSameDay(beginDateIns, endDateIns)||beginDateIns.before(endDateIns)){
				
				String ds=DateFormatUtils.format(beginDateIns, "yyyy-MM-dd");
				
				List<ETLEngineGameInfo> prepareEtlEngineGameInfos=new ArrayList<ETLEngineGameInfo>();
				
				for (ETLEngineGameInfo etlEngineGameInfo : gameInfos) {
					
					if(etlType==null){
						etlType=etlEngineGameInfo.getType();
					}
					
					Map<String,Object> processMess=new HashMap<String,Object>();
					
					processMess.put("snid", etlEngineGameInfo.getSnid());
					processMess.put("gameid", etlEngineGameInfo.getGameid());
					processMess.put("ds", ds);
					
					ETLEngineGameInfo cloneEngineGameInfo=etlEngineGameInfo.deepClone();
					if(cloneEngineGameInfo==null){
						continue;
					}
					// 在此设定ds才有效果
					cloneEngineGameInfo.setDs(ds);
					
					Date now=new Date();
					
					TaskStatus taskStatus=this.taskStatusService.getTaskStatusByCode(etlEngineGameInfo.getType(),etlEngineGameInfo.getIdentification());
					if(!TaskStatusAdapter.canExecute(taskStatus)){
						processMess.put("msg", " start by "+DateFormatUtils.format(taskStatus.getRuningTime(), "yyyy-MM-dd HH:mm:ss"));
						processMess.put("spendMinuts", (now.getTime()-taskStatus.getRuningTime().getTime())/(60*1000)+"");
						processMess.put("taskStatus", taskStatus.getStatus()+"");
						ret.add(processMess);
						continue;
					}
					
					prepareEtlEngineGameInfos.add(cloneEngineGameInfo);
					
					processMess.put("msg", " start by "+DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
					processMess.put("taskStatus", TaskStatusAdapter.Status.RUNNING.ordinal());
					ret.add(processMess);
				}
				daysPrepareEtlEngineGameInfos.add(prepareEtlEngineGameInfos);
				
				beginDateIns=DateUtils.addDays(beginDateIns, 1);
			}
			
			// 多个执行
			if(Type.ETL_ENGINE_RUN.getDisplayName().equals(etlType)){
				this.engineProcesser.asyncProcessGameDays(daysPrepareEtlEngineGameInfos);	
			}else if(Type.QUASI_ETL_ENGINE_RUN.getDisplayName().equals(etlType)){
				this.quasiETLEngineProcesser.asyncProcessGameDays(daysPrepareEtlEngineGameInfos);
			}else if(Type.ADTRACKING_QUASI_ETL_ENGINE_RUN.getDisplayName().equals(etlType)){
				this.adTrackingQuasiETLEngineProcesser.asyncProcessGameDays(daysPrepareEtlEngineGameInfos);
			}else{
				this.engineProcesser.asyncProcessGameDays(daysPrepareEtlEngineGameInfos);	
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	
	@RequestMapping(value = {"/queryRunEtls"}, method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public List<Map<String,Object>> queryRunEtls(@RequestParam String beginDate,@RequestParam String endDate,@RequestParam String gameInfosJson,HttpServletRequest request,HttpSession session)throws Exception {
		
		try {
			
			Date beginDateIns=DateUtils.parseDate(beginDate, "yyyy-MM-dd");
			Date endDateIns=DateUtils.parseDate(endDate, "yyyy-MM-dd");
			
			List<ETLEngineGameInfo> gameInfos=(List<ETLEngineGameInfo>)JSONUtils.fromJSONToList(gameInfosJson, ArrayList.class,ETLEngineGameInfo.class);
			
			List<Map<String,Object>> ret=new ArrayList<Map<String,Object>>();
			
			while(DateUtils.isSameDay(beginDateIns, endDateIns)||beginDateIns.before(endDateIns)){
				
				String ds=DateFormatUtils.format(beginDateIns, "yyyy-MM-dd");
				
				for (ETLEngineGameInfo etlEngineGameInfo : gameInfos) {
					
					Map<String,Object> processMess=new HashMap<String,Object>();
					
					processMess.put("snid", etlEngineGameInfo.getSnid());
					processMess.put("gameid", etlEngineGameInfo.getGameid());
					processMess.put("ds", etlEngineGameInfo.getDs());
					
					etlEngineGameInfo.setDs(ds);
					
					Date now=new Date();
					
					TaskStatus taskStatus=this.taskStatusService.getTaskStatusByCode(etlEngineGameInfo.getType(),etlEngineGameInfo.getIdentification());
					if(taskStatus==null){
						continue;
					}
					processMess.put("msg", " start by "+DateFormatUtils.format(taskStatus.getRuningTime(), "yyyy-MM-dd HH:mm:ss"));
					long spendMinuts=(now.getTime()-taskStatus.getRuningTime().getTime())/(60*1000);
					if(TaskStatusAdapter.Status.FINISHED.ordinal()==taskStatus.getStatus().intValue()
							&&taskStatus.getEndTime()!=null){
						spendMinuts=(taskStatus.getEndTime().getTime()-taskStatus.getRuningTime().getTime())/(60*1000);
					}
					processMess.put("spendMinuts", spendMinuts+"");
					processMess.put("taskStatus", taskStatus.getStatus()+"");
					ret.add(processMess);
				}
				beginDateIns=DateUtils.addDays(beginDateIns, 1);
			}
			
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	
}
