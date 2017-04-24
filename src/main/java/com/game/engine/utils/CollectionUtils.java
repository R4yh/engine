package com.game.engine.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合工具类
 * @author lyh
 */
public abstract class CollectionUtils {
	
	/**
	 * 获取列表的子集的拷贝
	 * 
	 * @param <T>
	 * @param source 			源列表
	 * @param start 			开始位置
	 * @param count 			获取记录数量
	 * @return {@link List}		返回结果是另一个 ArrayList 实例
	 */
	public static <T> List<T> subListCopy(List<T> source, int start, int count) {
		if (source == null || source.size() == 0) {
			return new ArrayList<T>(0);
		}
		
		int fromIndex = start <= 0 ? 0 : start;
		if (start > source.size()) {
			fromIndex = source.size();
		}
		
		count = count <= 0 ? 0 : count;	//增加了边界处理
		int endIndex = fromIndex + count;
		if (endIndex > source.size()) {
			endIndex = source.size();
		}
		return new ArrayList<T>(source.subList(fromIndex, endIndex));
	}
	
	/**
	 * 内存列表分页
	 * @param <T>
	 * @param list
	 * @param startIndex
	 * @param fetchCount
	 * @return
	 */
	public static <T> List<T> pageResult(List<T> list, int startIndex, int fetchCount){
		if(list != null && list.size() > 0){
			if(startIndex >= list.size()){
				return null;
			}
			startIndex = startIndex < 0?0:startIndex;
			if(fetchCount <= 0){
				return list.subList(startIndex, list.size());
			}
			int toIndex = Math.min(startIndex + fetchCount, list.size());
			return list.subList(startIndex, toIndex);
		}
		
		return null;
	}
	
	/**
	 * 获取列表的子集的拷贝
	 * 
	 * @param <T>
	 * @param  source 			源列表
	 * @param  startIndex 		开始位置
	 * @param  stopIndex 		结束位置
	 * @return {@link List}		返回结果是另一个 ArrayList 实例
	 */
	public static <T> List<T> subList(List<T> source, int startIndex, int stopIndex) {
		if (source == null || source.size() == 0) {
			return new ArrayList<T>(0);
		}
		
		int fromIndex = startIndex <= 0 ? 0 : startIndex;
		if (startIndex > source.size()) {
			fromIndex = source.size();
		}
		
		stopIndex = stopIndex <= 0 ? 0 : stopIndex;//增加了边界处理
		stopIndex = stopIndex <= startIndex ? startIndex : stopIndex;
		if (stopIndex > source.size()) {
			stopIndex = source.size();
		}
		return new ArrayList<T>(source.subList(fromIndex, stopIndex));
	}
	
	/**
	 * 当列表元素不存在时，进行添加
	 * 
	 * @param list 		列表
	 * @param idx 		插入位置
	 * @param element 	插入的元素
	 */
	public static <T> void addNotExist(List<T> list, int idx, T element) {
		if (list != null && !list.contains(element)) {
			list.add(idx, element);
		}
	}

	/**
	 * 当列表元素不存在时，进行添加
	 * 
	 * @param list 		列表
	 * @param element 	插入的元素
	 */
	public static <T> void addNotExist(List<T> list, T element) {
		if (list != null && !list.contains(element)) {
			list.add(element);
		}
	}

	/**
	 * 判断集合是否为空
	 * 
	 * @param  collections		集合信息
	 * @return {@link Boolean}	true-集合为空, false-集合不为空
	 */
	public static boolean isEmpty(Collection<?> collections) {
		return collections == null || collections.isEmpty();
	}
}
