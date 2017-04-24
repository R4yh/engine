package com.game.engine.basesource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.engine.basesource.db.BResourceQuery;
import com.game.engine.basesource.db.annotiation.BResource;
import com.game.engine.basesource.model.Storager;
import com.game.engine.utils.ClassFilter;
import com.game.engine.utils.ClassUtils;

/**
 * 基础读取数据
 * @author lyh
 */
public class BResourceManager {
	
	/** 所以扩展对象的实现集合*/
	private final Map<String, BResourceListener> listeners = new HashMap<>(0);
	
	/** 所有存储器的集合*/
	@SuppressWarnings("rawtypes")
	private final Map<Class, Storager> storagers = new ConcurrentHashMap<>(0);
	
	/** 类名 对应 类*/
	@SuppressWarnings("rawtypes")
	private final Map<String, Class> classSimpleNameMapClass = new HashMap<>();
	
	/** 基础数据查询*/
	private BResourceQuery sourceQuery = new BResourceQuery();
	
	/** 扫描包得路劲*/
	private static final String PACKAGE = "com.game";
	
	private static Logger logger = LoggerFactory.getLogger(BResourceManager.class);

	/** 单例*/
	private static BResourceManager instance = null;
	
	/** 关闭构造方法*/
	private BResourceManager(){}
	
	/**
	 * 获取 单例
	 * @return {@link BResourceManager}
	 */
	public static BResourceManager getInstance() {
		if(instance != null) {
			return instance;
		}
		
		synchronized (BResourceManager.class) {
			if(instance != null) {
				return instance;
			}
			instance = new BResourceManager();
			instance.initalize();
			return instance;
		}
	}
	
	
	
	private void initalize() {
		
		List<Class<?>> resClazzs = ClassUtils.scanPackage(PACKAGE, new ClassFilter() {
			@Override
			public boolean actionFilter(Class<?> clazz) {
				boolean finder = false;
				
				if(clazz.getAnnotation(BResource.class) != null) {
					finder = true;
				}
				
				if(ClassUtils.hasClassInterface(clazz, BResourceListener.class)) {
					finder = true;
				}
				
				return finder;
			}
		});
		
		for(Class<?> listenerClz : resClazzs) {
			if(ClassUtils.hasClassInterface(listenerClz, BResourceListener.class)) {
				try {
					BResourceListener listener = (BResourceListener) listenerClz.newInstance();
					listeners.put(listener.getResourceSimpleName(), listener);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} 
			}
		}
		
		for(Class<?> dataClz : resClazzs) {
			if(dataClz.getAnnotation(BResource.class) != null) {
				classSimpleNameMapClass.put(dataClz.getSimpleName(), dataClz);
				this.reload(dataClz);
			}
		}
	}
	
	

	/**
	 * 根据 ID 和 类型 获取数据对象 
	 * @param id   ID
	 * @param clz  类
	 * @return 数据对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public <T> T get(Object id, Class<T> clz) {
		if(id == null || clz == null) {
			return null;
		}
		Storager storager = storagers.get(clz);
		return storager != null ? (T)storager.get(id) : null;
	}
	
	
	/**
	 * 根据ID列表 获取 数据对象集合
	 * @param ids   ID 列表
	 * @param clz   类
	 * @return {@link List} 数据集合(每次都返回一个新的集合)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public <T> List<T> get(List<Object> ids , Class<T> clz) {
		if(ids == null || ids.isEmpty()) {
			return new ArrayList<>(0);
		}
		
		if(clz == null) {
			return new ArrayList<>(0);
		}
		
		List<T> arrays = new ArrayList<>();
		Storager storager = storagers.get(clz);
		for(Object id : ids) {
			T entity = (T) storager.get(id);
			if(entity != null) {
				arrays.add(entity);
			}
		}
		return arrays;
	}
	
	
	/**
	 * 通过 简单类名 重新加载 类配置
	 * @param classSimpleName   类名
	 */
	public void reload(String classSimpleName) {
		if(StringUtils.isBlank(classSimpleName)) {
			return;
		}
		
		Class<?> clazz = classSimpleNameMapClass.get(classSimpleName);
		if(clazz != null) {
			this.reload(clazz);
		}
	}
	
	
	/**
	 * 通过 类 重新加载 类配置
	 * @param clz  类
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reload(Class<?> clz) {
		if(clz == null) {
			return;
		}
		
		if(clz.getAnnotation(BResource.class) == null) {
			logger.error("{}不是基础数据类,无法加载!!!",clz);
			return;
		}
		
		try {
			List<?> results = sourceQuery.query4BReousrceClass(clz);
			if(results != null) {
				Storager oldStorager = this.getStorager(clz);
				if(oldStorager != null) {
					oldStorager.distroy();
				}
				
				storagers.put(clz, new Storager(clz, results));
				BResourceListener listener = this.getRresourceListener(clz);
				if(listener != null) {
					listener.onLoadAndReLoadComplete(results);
				}
				logger.error("重新加载基础数据{}成功!!!",clz);
			}
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}
	
	
	/**
	 * 重新加载所有配置
	 */
	public void reloadAll() {
		
		List<Class<?>> resClazzs = ClassUtils.scanPackage(PACKAGE, new ClassFilter() {
			@Override
			public boolean actionFilter(Class<?> clazz) {
				return clazz.getAnnotation(BResource.class) != null;
			}
		});
		
		for(Class<?> clazz : resClazzs) {
			this.reload(clazz);
		}
		
		resClazzs.clear();
	}
	
	
	/**
	 * 根据 类型 获取所有数据
	 * @param clz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> List<T> listAll(Class<T> clz) {
		Storager storager = storagers.get(clz);
		if(storager == null) {
			return new ArrayList<>(0);
		}
		return storager.listAll();
	}
	
	
	/**
	 * 根据 类型 获取扩展对象
	 * @param clz
	 * @return
	 */
	public <T> BResourceListener getRresourceListener(Class<T> clz) {
		if(clz == null) {
			return null;
		}
		return listeners.get(clz.getSimpleName());
	}
	
	/**
	 * 获取 类 对应的存储对象
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Storager getStorager(Class<?> clazz) {
		return storagers.get(clazz);
	}
}
