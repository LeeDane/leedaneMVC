package com.cn.leedane.utils;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

/**
 * sql工具类
 * @author leedane
 *
 */
public class SqlUtil {
	public static boolean getBooleanByList(List<Map<String, Object>> list) {
		boolean result = false;
		if(!CollectionUtils.isEmpty(list)){
			result = true;
		}
		return result;
	}
	
	public static int getCreateUserIdByList(List<Map<String, Object>> list) {
		int createUserId = 0;
		if(!CollectionUtils.isEmpty(list)){
			createUserId = StringUtil.changeObjectToInt(list.get(0).get("create_user_id"));
		}
		return createUserId;
	}

	public static int getTotalByList(List<Map<String, Object>> list) {
		int total = 0;
		if(!CollectionUtils.isEmpty(list)){
			total = StringUtil.changeObjectToInt(list.get(0).get("st"));
		}
		return total;
	}
}
