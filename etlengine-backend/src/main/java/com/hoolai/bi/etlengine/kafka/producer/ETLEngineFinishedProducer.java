package com.hoolai.bi.etlengine.kafka.producer;

import javax.annotation.PreDestroy;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.hoolai.bi.etlengine.core.Constant;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.jian.tools.util.JSONUtils;

@Component
public class ETLEngineFinishedProducer {
	
	private static final Logger LOGGER=Logger.getLogger("producer");
	
	private final String topic;
	
	private final Producer<Integer,String> producer;

	public ETLEngineFinishedProducer() {
		super();
		this.topic=Constant.GAME_ETL_FINISHED_TOPIC;
		this.producer=new Producer<Integer, String>(new ProducerConfig(Constant.BI_PRODUCER_CONFIGS));
	}
	
	public void send(String value){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(value+" [begin]");
		}
		
		producer.send(new KeyedMessage<Integer, String>(topic, value));
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(value+" [end]");
		}
	}
	
	@PreDestroy
	public void close(){
		this.producer.close();
	}
	
	public static void main(String[] args) {
		
		ETLEngineFinishedProducer reportProducer = new ETLEngineFinishedProducer();
		
		long events = 1L;

		for (long nEvents = 0; nEvents < events; nEvents++) {
			ETLEngineGameInfo etlEngineGameInfo=new ETLEngineGameInfo("1", "117", "2015-01-31", 0); 
			reportProducer.send(JSONUtils.toJSON(etlEngineGameInfo));
		}
		
		reportProducer.close();
		
	}
	
}
