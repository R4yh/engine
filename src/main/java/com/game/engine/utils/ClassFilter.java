package com.game.engine.utils;

/**
 * 类型过滤器 
 * @author lyh
 */
public interface ClassFilter {
	
	/**
	 * 过滤
	 * @param clazz  类型
	 * @return {@link Boolean} true 符合过滤条件 ;  false 不符合过滤条件
	 */
    boolean actionFilter(Class<?> clazz);
}
