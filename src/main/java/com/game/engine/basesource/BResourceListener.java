package com.game.engine.basesource;

import java.util.List;

/**
 * 基础数据自定义扩展接口
 * 当数据加载完毕以后会调用该接口
 * @author lyh
 */
public interface BResourceListener {

	/**
	 * 基础数据的 名字
	 * @return
	 */
	String getResourceSimpleName();

	/**
	 * 1.数据加载完毕以后回调
	 * 2.数据重新加载完毕以后回调
	 * @param list  相对应类型的数据集合
	 */
	<V> void onLoadAndReLoadComplete(List<V> list);
	
	
}
