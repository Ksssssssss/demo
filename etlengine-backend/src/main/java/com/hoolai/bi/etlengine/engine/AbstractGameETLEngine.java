package com.hoolai.bi.etlengine.engine;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hoolai.bi.etlengine.core.Constant;
import com.hoolai.bi.etlengine.engine.AbstractGameETLEngine.GameETLEngineResult;
import com.hoolai.bi.etlengine.mail.AsyncMailSender;
import com.hoolai.bi.etlengine.util.SpringApplicationUtil;
import com.jian.tools.util.JSONUtils;

public abstract class AbstractGameETLEngine  implements Callable<GameETLEngineResult>{
	
	protected final Logger logger=Logger.getLogger(this.getClass().getSimpleName());
	
	private static final Logger ERROR_LOGGER=Logger.getLogger("exception");
	
	private static final Logger HQL_LOGGER=Logger.getLogger("execHql");

	protected final String snid;
	
	protected final String gameid;
	
	protected final String title;
	
	protected final String hqlTemplate;
	
	protected final Map<String,String> envParams;
	
	protected final SpringApplicationUtil springApplicationUtil;
	
	protected final JdbcTemplate hiveJdbcTemplate;
	
	protected final AsyncMailSender asyncMailSender;
	
	protected final ETLEngineJdbcTemplate etlEngineJdbcTemplate;
	
	public AbstractGameETLEngine(String snid, String gameid,String title,
			String hqlTemplate, Map<String, String> envParams,
			SpringApplicationUtil springApplicationUtil) {
		super();
		this.snid = snid;
		this.gameid = gameid;
		this.title=title;
		this.hqlTemplate = hqlTemplate;
		this.envParams = envParams;
		this.springApplicationUtil=springApplicationUtil;
		this.hiveJdbcTemplate=this.springApplicationUtil.getBean("hiveJdbcTemplate", JdbcTemplate.class);
		this.etlEngineJdbcTemplate=new ETLEngineJdbcTemplate(this.hiveJdbcTemplate.getDataSource());
		this.asyncMailSender=this.springApplicationUtil.getBean(AsyncMailSender.class);
	}

	@Override
	public GameETLEngineResult call() {
		String loggerInfo=this.title+"_"+this.snid+"_"+this.gameid;
		logger.info(loggerInfo+" run begin.");
		long begin=System.currentTimeMillis();
		GameETLEngineResult ret=new GameETLEngineResult(this.getClass().getSimpleName());
		try {
			
			HQLRender hqlRender=new HQLRender(this.hqlTemplate, this.envParams);
			
			String[] hqls=hqlRender.getHqls();
			
			int total=hqls.length;
			
			int index=1;
			for (String hql : hqls) {
				boolean isSucc=this.execHql(hql,index++,total);
				if(!isSucc){
					ret.setSucc(false);
					ret.setMsg("execHql error.");
					return ret;
				}
			}
			ret.setSucc(true);
			long end=System.currentTimeMillis();
			logger.info(loggerInfo+" run end. spendMills:"+(end-begin));
			return ret;
		} catch (Exception e) {
			StringBuilder sb=new StringBuilder(e.getMessage());
			sb.append(loggerInfo);
			sb.append("[");
			sb.append(" title:"+this.title);
			sb.append(" template:").append(this.hqlTemplate);
			sb.append(" envParmas:").append(JSONUtils.toJSON(this.envParams));
			sb.append("]");
			ERROR_LOGGER.error(e.getMessage()+" "+sb.toString(), e);
			this.asyncMailSender.sendMail(Constant.MONITOR_ETL_EMAILS, this.getClass().getSimpleName()+" run method meet error!", sb.toString());
			ret.setMsg(e.getMessage());
			ret.setSucc(false);
			return ret;
		}finally{
			this.destory();
		}
	}
	
	private boolean execHql(String hql,int index,int total){
		if(StringUtils.isEmpty(hql)){
			return true;
		}
		String loggerInfo=this.title+"_"+this.snid+"_"+this.gameid+" precent:"+index+"/"+total;
		HQL_LOGGER.debug(loggerInfo+" run begin.");
		long begin=System.currentTimeMillis();
		try {
			// 此处不能用该方法返回值
			this.etlEngineJdbcTemplate.exec(hql);
			return true;
		} catch (Exception e) {
			StringBuilder sb=new StringBuilder(e.getMessage());
			sb.append(loggerInfo);
			sb.append("[");
			sb.append(" title:"+this.title);
			sb.append(" conn-isdestory:"+this.etlEngineJdbcTemplate.isDestory());
			sb.append(" hql:").append(hql);
			sb.append(" index:").append(index).append("/").append(total);
			sb.append("]");
			ERROR_LOGGER.error(e.getMessage()+" "+sb.toString(), e);
			this.asyncMailSender.sendMail(Constant.MONITOR_ETL_EMAILS, this.getClass().getSimpleName()+" run method meet error!", sb.toString());
		}
		long end=System.currentTimeMillis();
		HQL_LOGGER.debug(loggerInfo+" run end. spendMills:"+(end-begin));
		return false;
	}
	
	/**
	 * 释放该线程占用的资源
	 * 最后关闭数据库连接
	 */
	public void destory(){
		this.etlEngineJdbcTemplate.destory();
	}
	
	public static class GameETLEngineResult{
		
		private final String name;
		
		private boolean isSucc;
		
		private String msg;

		public GameETLEngineResult(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public boolean isSucc() {
			return isSucc;
		}

		public void setSucc(boolean isSucc) {
			this.isSucc = isSucc;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
		
	}
	
}
