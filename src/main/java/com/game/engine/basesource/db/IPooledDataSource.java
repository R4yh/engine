package com.game.engine.basesource.db;

import java.sql.Connection;

/**
 * 数据链接池接口 
 * @author lyh
 */
public interface IPooledDataSource {
	
	/**
	 * 获取 链接
	 * @return
	 */
	Connection getConnection() throws Exception;
	
	/**
	 * 获取 驱动名字
	 * @return
	 */
	String getDriverClass();
	
	/**
	 * 获取 数据库 链接的用户名
	 * @return
	 */
    String getUser();

    /**
     * 获取 数据库 链接的密码
     * @return
     */
    String getPassword();
	
    /**
     * 获取 数据库 URL
     * @return
     */
    String getJdbcUrl();

}
