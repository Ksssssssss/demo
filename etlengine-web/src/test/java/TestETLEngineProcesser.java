


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hoolai.bi.etlengine.engine.ETLEngineProcesser;
import com.hoolai.bi.report.etl.ETLEngineGameInfo;
import com.hoolai.bi.test.BaseTest;

public class TestETLEngineProcesser extends BaseTest {
	
	@Autowired
	private ETLEngineProcesser engineProcesser;
	
	@Test
	public void sync() throws Exception {
		List<ETLEngineGameInfo> etlEngineGameInfos=new ArrayList<ETLEngineGameInfo>();
		ETLEngineGameInfo etlEngineGameInfo0=new ETLEngineGameInfo("1", "123", "2015-09-14", 0);
		etlEngineGameInfos.add(etlEngineGameInfo0);
		this.engineProcesser.processGames(etlEngineGameInfos);
		
		TimeUnit.HOURS.sleep(2);
	}

}
