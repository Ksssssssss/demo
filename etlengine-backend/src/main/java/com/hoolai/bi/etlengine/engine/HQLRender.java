package com.hoolai.bi.etlengine.engine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

public class HQLRender {
	
	private static final String DEFAULT_SPILT=";";
	
	private String hqlTemplate;
	
	private Map<String,String> envParams;

	public HQLRender(String hqlTemplate, Map<String, String> envParams) {
		super();
		this.hqlTemplate = hqlTemplate;
		this.envParams = envParams;
	}

	public String render() throws Exception{
		StringWriter sw = new StringWriter();
		Context cnxt = new VelocityContext();
		
		for(Entry<String, String> entry: this.envParams.entrySet()){
			cnxt.put(entry.getKey(), entry.getValue());
		}
		Velocity.evaluate(cnxt, sw, "velocity", this.hqlTemplate);
		return sw.toString();
	}
	
	public String[] getHqls() throws Exception{
		String rendedHqls=this.render();
		return rendedHqls.split(DEFAULT_SPILT);
	}
	
}
