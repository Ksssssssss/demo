package com.hoolai.bi.etlengine.core;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class Constant {

	public static final String DEFAULT_FORMAT_DATE_DAY="yyyy-MM-dd";
	
	public static final String DEFAULT_FORMAT_DATE_MINUTES="yyyy-MM-dd HH:mm";
	
	public static final String DEFAULT_FORMAT_DATE_SECONDS="yyyy-MM-dd HH:mm:ss";
	
	public static final String DEFAULT_CHARSET="UTF-8";
	
	public static final int DEFAULT_INDEX_ZEAO = 0;
	
	public static final int DEFAULT_INDEX_FIRST= 1;
	
	// 默认long
	public static long DEFAULT_LONG_ZEAO = 0;
	
	// 默认long
	public static long DEFAULT_LONG_FIRST = 1;
	
	// 默认分隔符
	public static final String DEFAULT_SPLIT = ",";

	// 默认空格
	public static final String DEFAULT_BLANK = " ";

	// 默认下划线
	public static final String DEFAULT_UNDERLINE = "_";

	// 默认斜线
	public static final String DEFAULT_SPRIT = "/";
	
	public static final String CONSTANT_GLOBLE_FILE_PATH = "constant_globle.properties";

	private static Properties constantProperties=null;
	
	public static String SERVER_URL;
	
	public static String[] MONITOR_ETL_EMAILS;
	public static String[] MONITOR_ETL_PHONES;
	
	public static int ENGINE_THREAD_NUM;
	
	public static int ENGINE_HQL_THREAD_NUM;
	
	public static String GAME_ETL_FINISHED_TOPIC;
	
	public static String QUASI_GAME_ETL_FINISHED_TOPIC;
	
	public static String RUN_QUASI_GAME_ETL_TOPIC;
	
	public static Integer SMS_PRODUCT_ID;
	
	public static String SMS_PRODUCT_KEY;
	
	public static final Properties BI_PRODUCER_CONFIGS=new Properties();
	
	public static final Properties BI_CONSUMER_CONFIGS=new Properties();
	
	public static final Properties SETTING_CONFIGS=new Properties();
	
	static {
		init();
	}
	
	public static void init(){
		try {
			InputStream globleIn = Constant.class.getClassLoader().getResourceAsStream(CONSTANT_GLOBLE_FILE_PATH);
			Properties globleProperties = new Properties();
			if(globleIn==null){
				throw new RuntimeException("constant_globle.properties is not exists!");
			}
			globleProperties.load(globleIn);
			// 项目本事的常量文件
			String constantFilePath=globleProperties.getProperty("constant_file_path");
			if(StringUtils.isEmpty(constantFilePath)){
				throw new RuntimeException("constant_file_path is not contain in the constant_globle.properties!");
			}
			processProjectConstant(constantFilePath);
			
			String producerFilePath=globleProperties.getProperty("bi_producer_configs");
			processProducer(producerFilePath);
			
			String consumerFilePath=globleProperties.getProperty("bi_consumer_configs");
			processConsumer(consumerFilePath);
			
			String settingFilePath=globleProperties.getProperty("setting_configs");
			processSetting(settingFilePath);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void processProjectConstant(String constantFilePath) throws Exception{
		InputStream constantIn = Constant.class.getClassLoader().getResourceAsStream(constantFilePath);
		constantProperties=new Properties();
		constantProperties.load(constantIn);
		
		SERVER_URL=constantProperties.getProperty("server_url");
		
		String etlEmails=constantProperties.getProperty("monitor_etl_emails");
		if(StringUtils.isNotBlank(etlEmails)){
			MONITOR_ETL_EMAILS=etlEmails.split(",");
		}
		
		String etlPhones=constantProperties.getProperty("monitor_etl_phones");
		if(StringUtils.isNotBlank(etlPhones)){
			MONITOR_ETL_PHONES=etlPhones.split(",");
		}
		
		String engineThreadNum=constantProperties.getProperty("engine_thread_num");
		if(StringUtils.isNotBlank(engineThreadNum)){
			ENGINE_THREAD_NUM=Integer.parseInt(engineThreadNum);
		}
		
		String engineHqlThreadNum=constantProperties.getProperty("engine_hql_thread_num");
		if(StringUtils.isNotBlank(engineHqlThreadNum)){
			ENGINE_HQL_THREAD_NUM=Integer.parseInt(engineHqlThreadNum);
		}
		
		GAME_ETL_FINISHED_TOPIC=constantProperties.getProperty("game_etl_finished_topic");
		
		QUASI_GAME_ETL_FINISHED_TOPIC=constantProperties.getProperty("quasi_game_etl_finished_topic");
		
		RUN_QUASI_GAME_ETL_TOPIC=constantProperties.getProperty("run_quasi_game_etl_topic");
		
		String smsProductIdStr=constantProperties.getProperty("sms_product_id");
		if(StringUtils.isNotBlank(smsProductIdStr)){
			SMS_PRODUCT_ID=Integer.parseInt(smsProductIdStr);
		}
		
		SMS_PRODUCT_KEY=constantProperties.getProperty("sms_product_key");
		
	}
	
	private static void processProducer(String filePath) throws Exception{
		
		if(StringUtils.isEmpty(filePath)){
			return ;
		}
		InputStream constantIn = Constant.class.getClassLoader().getResourceAsStream(filePath);
		if(constantIn==null){
			return;
		}
		BI_PRODUCER_CONFIGS.load(constantIn);
		
	}
	
	private static void processConsumer(String filePath) throws Exception{
		
		if(StringUtils.isEmpty(filePath)){
			return ;
		}
		InputStream constantIn = Constant.class.getClassLoader().getResourceAsStream(filePath);
		if(constantIn==null){
			return;
		}
		BI_CONSUMER_CONFIGS.load(constantIn);
		
	}
	
	private static void processSetting(String filePath) throws Exception{
		
		if(StringUtils.isEmpty(filePath)){
			return ;
		}
		InputStream constantIn = Constant.class.getClassLoader().getResourceAsStream(filePath);
		if(constantIn==null){
			return;
		}
		SETTING_CONFIGS.load(constantIn);
		
	}
}
