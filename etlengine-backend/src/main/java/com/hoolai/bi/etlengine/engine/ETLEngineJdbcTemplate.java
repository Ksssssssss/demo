package com.hoolai.bi.etlengine.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;

import com.hoolai.bi.etlengine.core.Constant;

public class ETLEngineJdbcTemplate extends JdbcTemplate {
	
	protected Connection connection;
	
	protected Statement statement;
	
	protected boolean isDestory=false;

	public ETLEngineJdbcTemplate(DataSource dataSource) {
		super(dataSource);
		
		Properties properties=Constant.SETTING_CONFIGS;
		String driveName=properties.getProperty("etlengine.hive.driverClassName","org.apache.hadoop.hive.jdbc.HiveDriver");
		String hiveUrl=properties.getProperty("etlengine.hive.url");
		String username=properties.getProperty("etlengine.hive.username");
		String password=properties.getProperty("etlengine.hive.password");
		try {
			Class.forName(driveName); // change to use hiveserver
			connection = DriverManager.getConnection(hiveUrl, username, password);
			statement=connection.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*
		Connection con = DataSourceUtils.getConnection(getDataSource());
		Statement stmt = null;
		try {
			Connection conToUse = con;
			if (super.getNativeJdbcExtractor() != null &&
					super.getNativeJdbcExtractor().isNativeConnectionNecessaryForNativeStatements()) {
				conToUse = super.getNativeJdbcExtractor().getNativeConnection(con);
			}
			stmt = conToUse.createStatement();
			super.applyStatementSettings(stmt);
			Statement stmtToUse = stmt;
			if (super.getNativeJdbcExtractor()  != null) {
				stmtToUse = super.getNativeJdbcExtractor().getNativeStatement(stmt);
			}
			this.connection=conToUse;
			this.statement=stmtToUse;
		}catch (SQLException ex) {
			JdbcUtils.closeStatement(stmt);
			this.statement = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			this.connection = null;
		}
		*/
	}

	public boolean exec(String hql) throws Exception{
		return this.statement.execute(hql);
	}
	
	public void destory(){
		if(isDestory){
			return ;
		}
		try {
			if(this.statement!=null&&!this.statement.isClosed()){
				JdbcUtils.closeStatement(this.statement);
			}
			if(this.connection!=null&&!this.connection.isClosed()){
				JdbcUtils.closeConnection(this.connection);
				//DataSourceUtils.releaseConnection(this.connection, getDataSource());
			}
			isDestory=true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.destory();
	}

	public boolean isDestory() {
		return isDestory;
	}
	
}
