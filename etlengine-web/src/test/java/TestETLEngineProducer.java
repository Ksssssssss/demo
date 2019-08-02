


import com.hoolai.bi.etlengine.kafka.producer.ETLEngineFinishedProducer;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;
import com.jian.tools.util.JSONUtils;

public class TestETLEngineProducer {
	
	public static void main(String[] args) {
		
		ETLEngineFinishedProducer etlProducer=new ETLEngineFinishedProducer();
		
		//QuasiETLEngineFinishedProducer etlProducer=new QuasiETLEngineFinishedProducer();
		
		long events = 1L;

		for (long nEvents = 0; nEvents < events; nEvents++) {
			ETLEngineGameInfo etlEngineGameInfo=new ETLEngineGameInfo(Type.ADTRACKING_QUASI_ETL_ENGINE_RUN.getDisplayName(),"11", "148", "2015-09-08", 18); 
			//ETLEngineGameInfo etlEngineGameInfo=new ETLEngineGameInfo("15", "145", "2015-08-27", 0);
			etlProducer.send(JSONUtils.toJSON(etlEngineGameInfo));
		}
	}
	

}
