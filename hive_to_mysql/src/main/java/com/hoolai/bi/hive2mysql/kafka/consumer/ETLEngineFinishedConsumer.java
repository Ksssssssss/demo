package com.hoolai.bi.hive2mysql.kafka.consumer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hoolai.bi.hive2mysql.sync.SyncAllReportDatas;
import com.hoolai.bi.hive2mysql.sync.SyncConditions.GameBiId;
import com.hoolai.bi.notifyer.mail.MailSenderProxy;
import com.hoolai.bi.report.core.Constant;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;
import com.hoolai.bi.report.job.SystemThreadPoolExecutor;
import com.jian.tools.util.JSONUtils;

@Component
public class ETLEngineFinishedConsumer {
	
	private static final Logger LOGGER=Logger.getLogger("consumer");
	
	private final String topic;
	
	private final ConsumerConnector consumer;
	
	@Autowired
	private SyncAllReportDatas syncAllReportDatas;
	
	private MailSenderProxy maillMailSenderProxy=new MailSenderProxy();
	
	private SystemThreadPoolExecutor systemThreadPoolExecutor=new SystemThreadPoolExecutor(ETLEngineFinishedConsumer.class.getSimpleName(), 3);

	public ETLEngineFinishedConsumer() {
		super();
		this.topic=Constant.GAME_ETL_FINISHED_TOPIC;
		this.consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(Constant.BI_CONSUMER_CONFIGS));
	}
	
	@PostConstruct
	private void init(){
		
		class ETLEngineFinishedConsumerTrigger implements Runnable{
			
			private ETLEngineFinishedConsumer etlEngineFinishedConsumer;

			public ETLEngineFinishedConsumerTrigger(
					ETLEngineFinishedConsumer etlEngineFinishedConsumer) {
				super();
				this.etlEngineFinishedConsumer = etlEngineFinishedConsumer;
			}

			@Override
			public void run() {
				this.etlEngineFinishedConsumer.receive();
			}
		}
		
		this.systemThreadPoolExecutor.submit(new ETLEngineFinishedConsumerTrigger(this));
		
	}
	
	public void receive(){
		try {
			Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
			topicCountMap.put(topic, new Integer(1));
			Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
			KafkaStream<byte[], byte[]> stream =  consumerMap.get(topic).get(0);
			ConsumerIterator<byte[], byte[]> it = stream.iterator();
			while(it.hasNext()){
				String consumerMess=new String(it.next().message());
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug(consumerMess+" [begin]");
				}
				//AsyncSyncDataThread asyncSyncData=new AsyncSyncDataThread(consumerMess);
				this.processMess(consumerMess);
				// 异步方式同步数据
				//this.systemThreadPoolExecutor.submit(asyncSyncData);
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug(consumerMess+" [end]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.maillMailSenderProxy.sendMail(Constant.MONITOR_SYS_EMAILS, "ETLEngineFinishedConsumer receive meet exception!", e.getMessage());
		}
	}
	
	private void processMess(String jsonMess){
		if(StringUtils.isEmpty(jsonMess)){
			return ;
		}
		try {
			ETLEngineGameInfo etlEngineGameInfo=JSONUtils.fromJSON(jsonMess, ETLEngineGameInfo.class);
			List<GameBiId> gameBiIds=new ArrayList<GameBiId>();
			gameBiIds.add(new GameBiId(etlEngineGameInfo.getSnid(), etlEngineGameInfo.getGameid()));
			Date beginDate=DateUtils.parseDate(etlEngineGameInfo.getDs(), "yyyy-MM-dd");
			Date endDate=beginDate;
			if(Type.ETL_ENGINE_RUN.getDisplayName().equals(etlEngineGameInfo.getType())){
				this.syncAllReportDatas.sync(gameBiIds, beginDate, endDate);
			}else if(Type.QUASI_ETL_ENGINE_RUN.getDisplayName().equals(etlEngineGameInfo.getType())){
				this.syncAllReportDatas.syncQuasi(gameBiIds, beginDate, endDate,etlEngineGameInfo.getStep()+"");
			}else if(Type.ADTRACKING_QUASI_ETL_ENGINE_RUN.getDisplayName().equals(etlEngineGameInfo.getType())){
				this.syncAllReportDatas.syncAdtrackingQuasi(gameBiIds, beginDate, endDate,etlEngineGameInfo.getStep()+"");
			}else{
				this.syncAllReportDatas.sync(gameBiIds, beginDate, endDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.maillMailSenderProxy.sendMail(Constant.MONITOR_SYS_EMAILS, "ETLEngineFinishedConsumer procss ret mess meet exception!", e.getMessage()+"["+jsonMess+"]");
		}
	}
	
	private class AsyncSyncDataThread implements Runnable{
		
		private final String jsonMess;

		public AsyncSyncDataThread(String jsonMess) {
			super();
			this.jsonMess=jsonMess;
		}

		@Override
		public void run() {
			processMess(jsonMess);
		}
	}
	
	public static void main(String[] args) {
		ETLEngineFinishedConsumer reportConsumer=new ETLEngineFinishedConsumer();
		reportConsumer.receive();
	}
	
}
