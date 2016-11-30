package com.cn.leedane.comet4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用全局存储
 * @author LeeDane
 * 2016年11月28日 下午5:26:28
 * Version 1.0
 */
public class AppStore {

	private static Map<String, Object> map;
	
	private static AppStore instance;

	public static AppStore getInstance() {

		if (instance == null) {
			instance = new AppStore();
			map = new HashMap<String, Object>();
		}
		return instance;
	}

	public void put(String key, Object value) {
		map.put(key, value);
	}
	
	public Object get(String key) {
		return map.get(key);
	}
	
	public Map<String, Object> getMap() {
		return map;
	} 
	
	
	public void removeKey(String key){
		try {
			map.remove(key);
		} catch (Exception e) {
		}
	}

	public void destroy() {
		map.clear();
		map = null;
	}
}
