package com.game.engine.basesource.db;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.StringUtils;

import com.game.engine.basesource.db.annotiation.BResource;

/**
 * 基础数据查询
 * @author lyh
 */
public class BResourceQuery {

	/** 数据源*/
	private BRDataSource source = new BRDataSource(C3p0DataSource.class);
	
	/** 查询*/
	private QueryRunner query = new QueryRunner();
	
	private String querySQL = "select * from ";
	
	/**
	 * 根据 {@link BResoucese} 标识的类,查询所有出对象集合
	 * @param clazz   类
	 * @return        查询类的集合
	 * @throws Exception
	 */
	public <T> List<T> query4BReousrceClass(Class<T> clazz) throws Exception {
		if(clazz == null) {
			throw new RuntimeException("clazz not null!!!"); 
		}
		
		BResource resource = clazz.getAnnotation(BResource.class);
		if(resource == null) {
			throw new RuntimeException("this "+clazz+" is not BResource,Please check!!!");
		}
		
		String tableName = clazz.getSimpleName();
		if(StringUtils.isNoneBlank(resource.tableName())){
			tableName = resource.tableName();
		}
		
		//组装SQL
		StringBuffer buffer = new StringBuffer();
		String sql = buffer.append(querySQL).append(tableName).toString();
		
		//获取链接
		Connection conn = source.getPooledDataSource().getConnection();
		List<T> list =  query.query(conn, sql, new BeanListHandler<T>(clazz));
		//关闭链接
		DbUtils.closeQuietly(conn);  
		return list;
	}
	
	
}
