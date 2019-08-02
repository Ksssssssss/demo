


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hoolai.bi.etlengine.engine.realtime.quasi.QuasiETLEngineProcesser;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.hoolai.bi.report.etl.ETLEngineGameInfo.Type;
import com.hoolai.bi.test.BaseTest;

public class TestQuasiETLEngineProcesser extends BaseTest {
	
	@Autowired
	private QuasiETLEngineProcesser engineProcesser;
	
	@Test
	public void sync() throws Exception {
		List<ETLEngineGameInfo> etlEngineGameInfos=new ArrayList<ETLEngineGameInfo>();
		ETLEngineGameInfo etlEngineGameInfo0=new ETLEngineGameInfo(Type.QUASI_ETL_ENGINE_RUN.getDisplayName(),"34", "134", "2015-06-09", 13);
		etlEngineGameInfos.add(etlEngineGameInfo0);
		this.engineProcesser.processGames(etlEngineGameInfos);
		
		TimeUnit.HOURS.sleep(2);
	}

}
