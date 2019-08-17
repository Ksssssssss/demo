package com.hoolai.bi.hive2mysql.datas;

import java.io.IOException;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hoolai.bi.report.model.Conditions;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class SqlFtlDatas {
	
	@Autowired
	@Qualifier("freemarkerConfiguration")
	private Configuration freemarkerConfiguration;
	
	public synchronized Template getTemplate(String filePath){
		try {
			return this.freemarkerConfiguration.getTemplate(filePath);
		} catch (IOException e) {
		}
		return null;
	}
	
	public synchronized String condition(Conditions conditions,String path){
		Template template=this.getTemplate(path);
		StringWriter sw = new StringWriter();
		try {
			template.process(conditions, sw);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("get sql template error!"+e.getMessage()+" path:"+path);
		}
		return sw.toString();
	}
	
}
