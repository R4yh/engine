package com.game.engine.basesource.db;

import java.sql.Connection;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * C3P0 连接池
 * @author lyh
 */
public class C3p0DataSource implements IPooledDataSource{

	private ComboPooledDataSource source = null;
	
	public C3p0DataSource() {
		source = new ComboPooledDataSource("c3p0Game");
	}

	public Connection getConnection() throws Exception {
		return source.getConnection();
	}

	public String getDriverClass() {
		return source.getDriverClass();
	}

	public String getUser() {
		return source.getUser();
	}

	public String getPassword() {
		return source.getPassword();
	}

	public String getJdbcUrl() {
		return source.getJdbcUrl();
	}
}
