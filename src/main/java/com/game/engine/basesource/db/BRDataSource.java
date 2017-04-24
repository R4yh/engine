package com.game.engine.basesource.db;

/**
 * 数据源 
 * @author lyh
 */
public class BRDataSource {
	
	/** 数据源*/
	private IPooledDataSource source;
	
	/**
	 * 构造方法
	 * @param clazz   数据源驱动
	 */
	public BRDataSource(Class<? extends IPooledDataSource> clazz) {
		if(clazz.isInterface()) {
			throw new RuntimeException("传入的参数是接口!");
		}
		
		try {
			source = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 获取链接数据源
	 * @return
	 */
	public IPooledDataSource getPooledDataSource() {
		return source;
	}

}
