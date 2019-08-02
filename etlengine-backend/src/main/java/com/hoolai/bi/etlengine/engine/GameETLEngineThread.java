package com.hoolai.bi.etlengine.engine;

import java.util.Map;

import com.hoolai.bi.etlengine.model.entity.AbstractEngineEtlsAdapter;
import com.hoolai.bi.etlengine.util.SpringApplicationUtil;

public class GameETLEngineThread extends AbstractGameETLEngine{
	
	private final AbstractEngineEtlsAdapter engineEtlsAdapter;

	public GameETLEngineThread(String snid, String gameid,Map<String,String> envParams,
			AbstractEngineEtlsAdapter engineEtlsAdapter,SpringApplicationUtil springApplicationUtil) {
		super(snid, gameid,engineEtlsAdapter.getTitle(), engineEtlsAdapter.getTemplate(), envParams, springApplicationUtil);
		this.engineEtlsAdapter = engineEtlsAdapter;
	}
	
	
}
