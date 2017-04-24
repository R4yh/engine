package com.game.engine.basesource.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.game.engine.basesource.db.annotiation.BId;
import com.game.engine.basesource.db.annotiation.BResource;

/**
 * 存储对象
 * @author lyh
 */
public class Storager<V> {
	
	/** 资源类,标识{@link BResource}的类*/
	private Class<V> clazz;
	
	/** 主存储空间*/
	private Map<Object, V> table = new HashMap<>(0);
	
	/** 排好序的id列表*/
	private List<Object> idList = new CopyOnWriteArrayList<>();
	
	/**
	 * 关闭该构造方法
	 */
	@SuppressWarnings("unused")
	private Storager(){}
	
	/**
	 * 构造方法
	 * @param clazz       类型
	 * @param dataValues  数据集合
	 */
	public Storager(Class<V> clazz, List<V> dataValues) {
		if(clazz == null || dataValues == null) {
			throw new RuntimeException("");
		}
		
		this.clazz = clazz;
		for(V value : dataValues) {
			try {
				Object key = getBIdValue(value);
				if(key != null) {
					this.table.put(key, value);
					this.idList.add(key);
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 销毁
	 */
	public void distroy() {
		this.clazz = null;
		this.table.clear();
		this.idList.clear();
	}
	
	/**
	 * 获取{@link BId}的值, 如果不存在或者有重复这里也不检测了!
	 * @param value
	 * @return
	 */
	private Object getBIdValue(V value) throws Exception{
		for(Field field : value.getClass().getDeclaredFields()) {
			if(field.getAnnotation(BId.class) != null) {
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				Object id = field.get(value);
				field.setAccessible(accessible);
				return id;
			}
		}
		return null;
	}
	
	

	/**
	 * 获取 资源类对象
	 * @return
	 */
	public Class<V> getClazz() {
		return clazz;
	}
	
	/**
	 * 根据 Key值获取 数据对象
	 * @param key
	 * @return
	 */
	public V get(Object key) {
		return table.get(key);
	}
	
	/**
	 * 获取整个 数据对象
	 * @return
	 */
	public Map<Object,V> getTable() {
		return table;
	}
	
	/**
	 * 获取所有的 对象
	 * @return
	 */
	public List<V> listAll() {
		List<V> result = new ArrayList<>(0);
		for(Object id : idList) {
			V value = this.get(id);
			if(value != null) {
				result.add(value);
			}
		}
		return result;
	}
	
	/**
	 * 根据 特定的 {@link BId} 查询对应的数据
	 * @param idList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<V> list(List idList) {
		List<V> result = new ArrayList<>(0);
		if(idList == null || idList.isEmpty()) {
			return result;
		}
		for(Object id : idList) {
			V value = this.get(id);
			if(value != null) {
				result.add(value);
			}
		}
		return result;
	}

}
