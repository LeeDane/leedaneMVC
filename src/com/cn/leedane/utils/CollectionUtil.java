package com.cn.leedane.utils;

import java.util.List;

/**
 * 集合工具类
 * @author LeeDane
 * 2016年11月2日 下午3:10:05
 * Version 1.0
 */
public class CollectionUtil {

	/**
	 * 判断List集合是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List list){
		return list == null || list .size() == 0;
	}
	
	/**
	 * 判断List集合是否不为空
	 * @param list
	 * @return
	 */
	public static boolean isNotEmpty(List list){
		return !isEmpty(list);
	}
}
