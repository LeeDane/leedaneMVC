package com.cn.leedane.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring相关工具类
 * @author LeeDane
 * 2016年7月12日 上午10:31:30
 * Version 1.0
 */
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static Object getBean(String beanName) {	
		return applicationContext.getBean(beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SpringUtil.applicationContext = context;
	}
	
}
